# properties

## Maven dependency
```xml
<dependency>
    <groupId>com.github.oyvindbjerke</groupId>
    <artifactId>properties</artifactId>
    <version>0.3</version>
</dependency>
```

## The purpose of this library
Create a thin and simple library to facilitate use of environment variables and system properties. Why is this useful? If you want to get some sort of property from your code, but you want to support both environment variables and system properties without having to write that code yourself.

An example could be that you have an application that will connect to a database. You want to provide the username and password for the database connection through either environment variables or system properties.

A valid scenario for this is for instance if you want to use system properties for local development, but use docker with environment variables for actual deployment of the application.

## Features
* Support for fetching a "property" from either an environment variable or system property given a key, where system property is given precedence

## Restrictions

Strict validation of allowed property keys is implemented, basically restricted to what is recommended for bash variable names.

Other libraries commonly uses dot separated keys as a syntax to semantically "group" properties for instance.

```
database.host
database.username
database.password
```

Dots are not allowed in bash variable names, meaning that allowing this format would not be possible to pass as an environment variable to for example docker.

For simplicity this library will only allow uppercased letters and underscore as a delimiter, numbers is also allowed except as a first character as this is also not a valid bash variable.

## Code example

The following example will fetch a string value for the system property "DB_HOST" if it exists, if it does not it will look for an environment variable for the same key. If neither exists it will return an empty result.

```java
import no.bjerke.properties.Properties

Optional<String> value = Properties.getProperty("DB_HOST");
```

## Implementation details
* JUnit5
* Java 10
