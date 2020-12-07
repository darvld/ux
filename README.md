# ux
Kotlin/Native multiplatform UI framework based on GTK+ bindings

### Note
This project contains more than just GTK Cinterop bindings: it adds some Kotlin-style functionality to some tedious parts of GTK (like doing some work in the background while updating the UI or using ListStore and TreeStore). If you're only looking for basic bindigs with type-safe builders, take a look at [these](https://github.com/kropp/kotlin-native-gtk), they were developed by a member of the JetBrains team, and are pretty complete.

## Current status
This repository represents a work in progress, releases are still a little ways down the road since most of the code is uncommented and untested. Collaboration in these regards will be highly appreciated.

## Project goal
The purpose of this project is to generate a fully functional library allowing Kotlin/Native developers to use a multi-platform UI framework that is simple to use. Through the use of Kotlin constructs like type-safe builders, UI structures can be designed from code, similar to what [Kotlin Compose](https://github.com/JetBrains/compose-jb) does for Kotlin-JVM users.

In addition to currently exposed bindings, support GtkBuilder will be added in the near future, as well as several demo applications demonstrating this library's most simple use-cases.  

### A simple example
```kotlin
 fun main() {
    // Create the Application using the DSL
    val returnCode = application(id="com.github.example") {
        applicationWindow(title = "Hello World!"){
              box {
                  label("Hey!")
                  button("Press me"){
                      onClick {
                          println("Button pressed")
                      }
                  }
              }
              fileChooserButton("Select a file"){
                  onFileSet {
                      println("File $it selected")
                  }
              }
          }
    }.run() // The `run` method blocks until the application finishes
    
    // Check for errors, etc...
    if(returnCode != 0)
      throw Exception()
}

```
