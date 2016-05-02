# kollection
Yet Another Kotlin Collection Library and Additional Utility

[![Build Status](https://travis-ci.org/kmizu/kollection.png?branch=master)](https://travis-ci.org/kmizu/kollection)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.kmizu/kollection/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.kmizu/kollection)
[![Dokka](http://javadoc-badge.appspot.com/com.github.kmizu/kollection.svg?label=javadoc)](http://javadoc-badge.appspot.com/com.github.kmizu/kollection/index.html)
[![Reference Status](https://www.versioneye.com/java/com.github.kmizu:kollection/reference_badge.svg?style=flat)](https://www.versioneye.com/java/com.github.kmizu:kollection/references)

# Usage

## For gradle user

Add the following line to build.gradle:

```groovy
dependencies {
  compile "com.github.kmizu:kollection:0.1"
}

```

## Examples

* [KList](https://github.com/kmizu/kollection/blob/releases/0.1/src/test/kotlin/com/github/kmizu/kollection/KListSpec.kt)
* [KStream](https://github.com/kmizu/kollection/blob/releases/0.1/src/test/kotlin/com/github/kmizu/kollection/KStreamSpec.kt)
* [KOption](https://github.com/kmizu/kollection/blob/releases/0.1/src/test/kotlin/com/github/kmizu/kollection/KOptionSpec.kt)
* [KStack](https://github.com/kmizu/kollection/blob/releases/0.1/src/test/kotlin/com/github/kmizu/kollection/KStackSpec.kt)

## Collections

### KList

KList is a cons-based List.  It provides access to head by `hd` and access to tail by `hd`.  It takes only constant time
to insert a new element to front of the KList.  Note that it takes linear time to append a new element to last of the KList

### KStream

KStream is like a KList seemingly.  But it differs from KList in that KStream's tail is lazily evaluated.
Therefore, KStream can represent **an infinite stream*.  For example, `ones` is an infinite stream that consists
of only 1 such as the following:

```kotlin
val ones: KStream<Int> = 1 cons { ones }
```

### KOption

### KStack
