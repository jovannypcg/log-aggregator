# log-aggregator

Given a set of huge files containing logs, `log-aggregator` displays all the
entries from the different files sorted by timestamp.

- [Prerequisites](#prerequisites)
  * [SDKMan](#sdkman)
  * [Installing Java 11 Using SDKMan](#installing-java-11-using-sdkman)
  * [Installing Gradle Using SDKMan](#installing-gradle-using-sdkman)
- [Building](#building)
- [Usage](#usage)
- [Testing](#testing)
- [Design](#design)
  * [Approach to Tackle](#approach-to-tackle)
    + [RandomAccessFile](#randomaccessfile)
    + [Comparing Logs](#comparing-logs)
    + [Printing Logs](#printing-logs)

## Prerequisites

* Java 11
* Gradle 6.7 (Optional)

### SDKMan

Java 11 can be installed via [SDKMan](https://sdkman.io/install), which is a
version manager for technologies related to the Java Virtual Machine (similar to
`rbenv` for Ruby or `pyenv` for Python).

### Installing Java 11 Using SDKMan

```shell
$ sdk install java 11.0.6.hs-adpt
$ export JAVA_HOME=~/.sdkman/candidates/java/current
```

### Installing Gradle Using SDKMan

Notice that this step is optional, as the repo contains a wrapper (`gradlew`)
that installs Gradle for us without the need to do so explicitly.

```shell
$ sdk install gradle 6.7
```

## Building

Since the application is written in Java, it is necessary to generate the binary
package in order to execute it through the Java Virtual Machine. This task is
readily achieved using Gradle. The following commands are meant to be executed
in the root path of the `log-aggregator` codebase.

```shell
$ ./gradlew clean build
```

## Usage

Once built, the generated Java artifact is located inside the `build/libs/`
directory and can be executed by running:

```shell
$ java -jar build/libs/log-aggregator.jar <log-files-dir>
```

Where `<log-files-dir>` is the directory containing the huge log files.

Example:

```shell
$ java -jar build/libs/log-aggregator.jar src/test/resources/fixtures

______                        _______                                                  _____
___  / ______ _______ _       ___    |_______ ________ ______________ _______ _______ ___  /_______ ________
__  /  _  __ \__  __ `/       __  /| |__  __ `/__  __ `/__  ___/_  _ \__  __ `/_  __ `/_  __/_  __ \__  ___/
_  /___/ /_/ /_  /_/ /        _  ___ |_  /_/ / _  /_/ / _  /    /  __/_  /_/ / / /_/ / / /_  / /_/ /_  /
/_____/\____/ _\__, /         /_/  |_|_\__, /  _\__, /  /_/     \___/ _\__, /  \__,_/  \__/  \____/ /_/
              /____/                  /____/   /____/                 /____/
2016-12-20T18:51:16Z, Server C started.
2016-12-20T19:00:45Z, Server A started.
2016-12-20T19:01:16Z, Server B started.
2016-12-20T19:01:25Z, Server A completed job.
2016-12-20T19:02:02Z, Server C completed job.
2016-12-20T19:02:48Z, Server A terminated.
2016-12-20T19:03:25Z, Server B completed job.
2016-12-20T19:04:50Z, Server B terminated.
2016-12-20T19:05:00Z, Server C terminated.
2016-12-20T19:59:15Z, Server D started.
2016-12-20T20:22:40Z, Server E started.
2016-12-20T20:25:11Z, Server D completed job.
2016-12-20T20:25:11Z, Server D terminated.
2016-12-20T20:26:02Z, Server E completed job.
2016-12-20T20:26:03Z, Server E terminated.
```

## Testing

```shell
$ ./gradlew clean test
```

## Design

`log-aggregator` solves a problem related to a myriad of files containing tons
of logs.

Imagine you have any number of servers (1 to 1000+) that generate log files for
your distributed app. Each log file can range from 100MB - 512GB in size. They
are copied to your machine which contains only 16GB of RAM.

The local directory would look like this:

```shell script
$ ls /temp
server-ac329xbv.log
server-buyew12x.log
server-cnw293z2.log
```

Our goal is to print the individual lines out to the standard output,
sorted by timestamp.

### Approach to Tackle

`log-aggregator` receives the path of the directory containing log files.
Internally, it grabs all the files and converts them into instances of
[RandomAccessFile](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/RandomAccessFile.html),
which provides a handy way to read the files **without loading them
into memory**.

#### RandomAccessFile

Given that only 16GB of memory are available and that a single file can range
from 100MB - 512GB in size, avoiding loading the whole files into memory is
mandatory.

[RandomAccessFile](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/RandomAccessFile.html),
allows to read the file using a pointer that is stored within the instance. This
pointer is a number that represents the index of the byte to read in the file.
In other words, each line can be read and a pointer to the next line is stored
inside the `RandomAccessFile` instance.

#### Comparing Logs

In addition to storing the pointer of each file, a special `Set` is also in the
game. This data structure holds instances of `LogFileHolder`, which contains two
things: the current log of each file and the reference to the `RandomAccessFile`
instance that owns the log.

The `Set` is a `TreeSet` that sorts its elements by the log using the timestamp.
A comparator for the `TreeSet` structure is provided to ensure the items are
sorted as expected.

Given that the timestamps are provided in the ISO 8601 format, there is no need
to convert them into `Instant`s or any other kind of date objects for
comparison, as this format provides lexicographical order, which means that they
can be compared as `String` instances.

#### Printing Logs

Since the `TreeSet` structure contains the logs sorted by timestamp, its very
first element is the log to be printed out, due to each item contains a
reference to the `RandomAccessFile` instance, `log-aggregator` prints the log,
reads the next one and decides whether insert it into the `TreeSet` or whether
it is time to close the file (no more entries).
