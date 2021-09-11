# Gradle JS Import Fix Plugin
[ ![artifactory](https://img.shields.io/badge/Artifactory-v0.2.0-green) ](https://artfable.jfrog.io/ui/packages/gav:%2F%2Fcom.artfable.gradle:gradle-js-import-fix-plugin)

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
        classpath "com.artfable.gradle:gradle-js-import-fix-plugin:0.2.0"
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
        classpath("com.artfable.gradle:gradle-js-import-fix-plugin:0.2.0")
    }
}
apply(plugin = "artfable.js.import.fix")
```

It'll add a task `jsImportFix`

For use in `plugins {}` see [Gradle resolution strategy](https://docs.gradle.org/current/userguide/custom_plugins.html#note_for_plugins_published_without_java_gradle_plugin)

## Usage

```groovy
jsImport {
    directory = "$projectDir/src/libs/"
}
```

or

```kotlin
configure<GradleJsImportFixExtension> { // jsImport {  // if plugin was added in plugins {} block 
    directory = "$projectDir/src/libs/"
}
```

where `directory` is a directory that should be scanned for the files.

The plugin will also create `index.js` files for each directory if not provided and if js file with the same name as the directory is exists. 