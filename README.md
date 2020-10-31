# log-aggregator

Given a set of huge files containing logs, `log-aggregator` displays all the
entries from the different files sorted by timestamp.

* [Prerequisites](#prerequisites)
  * [SDKMan](#sdkman)
  * [Installing Java 11 Using SDKMan](#installing-java-11-using-sdkman)
  * [Installing Gradle Using SDKMan](#installing-gradle-using-sdkman)
* [Building](#building)
* [Usage](#usage)
* [Testing](#testing)

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
