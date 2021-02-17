# miKrate

miKrate aims at providing an easy, typesafe, pluggable DSL and API to allow you to quickly develop migrations, apply them automatically and all of that database agnostic (dialects are pluggable).

## Installing

To add the packages using the gradle build tool, you fist have to add the corresponding maven repo to your `repositories`:

```kotlin
maven("https://gitlab.com/api/v4/projects/23743161/packages/maven")
```

_Maven can also be use, for more info on using maven refer to the [GitLab packages](https://gitlab.com/factory-org/tools/mikrate/-/packages)_

!!! note
    A JAR distribution is currently not available

!!! note
    miKrate in its current state is only compatible to JVM version 11 and upwards

To see which packages are available, you can look at the [GitLab packages](https://gitlab.com/factory-org/tools/mikrate/-/packages) or at the individual packages.
