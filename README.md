# Gradle JS Import Fix Plugin
(version: 0.0.1)

## Overview
Simple plugin that was written on [kotlin](https://kotlinlang.org). Some js libraries aren't adopted for es6 standard regarding import modules from file.
It could start from module name that won't work in browsers directly, as the standard require that path should start from `/`, `./` or `../`. 
Current plugin finds all js files in a provided directory for witch imports doesn't start with the required symbols, assumes that in that case it starts with the module name and change path for a proper relative path. 
 

## Install
```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath "com.github.artfable.gradle:gradle-js-import-fix-plugin:0.0.1"
    }
}

apply plugin: 'artfable.js.import.fix'
```

or

```kotlin
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("com.github.artfable.gradle:gradle-js-import-fix-plugin:0.0.1")
    }
}
apply(plugin = "artfable.js.import.fix")
```

It'll add a task `jsImportFix`

## Usage

```groovy
jsImport {
    directory = "$projectDir/src/libs/"
}
```

or

```kotlin
configure<GradleJsImportFixExtension> {
    directory = "$projectDir/src/libs/"
}
```

where `directory` is a directory that should be scanned for the files.