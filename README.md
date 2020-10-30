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
* [Docker Integration](#docker-integration)
  * [Building](#build-with-docker)
  * [Usage](#usage-with-docker)
  * [Testing](#testing-with-docker)

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
$ java -jar build/libs/log-aggregator.jar /temp/logs
```

## Testing

```shell
$ ./gradlew clean test
```

## Docker Integration

`log-aggregator` has Docker integration, which means that it can be also built and executed within a Docker container.

### Build With Docker

```shell
$ docker build -t log-aggregator -f src/main/docker/Dockerfile .
```

Where:

* `log-aggregator` is describing a tag for the Docker image
* `src/main/docker/Dockerfile` is the path where the `Dockerfile` is located, in order to build the image

### Usage With Docker

```shell
$ docker run --rm log-aggregator
```

### Testing With Docker

```shell
$ docker run --rm log-aggregator gradle -p /app/ clean test
```
