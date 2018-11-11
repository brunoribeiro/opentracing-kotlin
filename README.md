
# OpenTracing Kotlin

## Why

This library was built to make the kotlin/opentracing integration more fluid, avoiding boilerplate code.

### Modules

- `core: The basic opentracing kotlin integration`
- `springboot-support: Additional utilities to integrate with spring-boot 1.5, configuration and method annotation` 

### How to

1 - Initialize the tracer 

```kotlin
    OpenTracing.configure(
        
        OpenTracingConfiguration(
            "your token",
            "collector host",
            "collector port",
            "service name"
        )
    )

```

2 - Import the `OpenTracing.trace` and trace your code

```kotlin
    trace("some_operation"){
    
    // some very special piece of code here
    
    }
```

3 - Spring support with the `@Treceable` annotation

```kotlin

    @GetMapping("/stuff")
    @Traceable("list_stuff")
    List<Stuff> list() {
        return repository.findAll()
    }
	
```


### Artifacts

Gradle repository

```groovy
    maven {
        url  "https://dl.bintray.com/ribeiropt/maven"
    }
 ```
 
 dependencies
 
 ```groovy
     compile "io.github.brunoribeiro:opentracing-kotlin-core:${latestVersion}"
```