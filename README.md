# jEnt
![build](https://img.shields.io/travis/prgpascal/jEnt)


## Overview
This library is essentially a Java porting of the [ENT randomness test tool][ent], created by John Walker and written in C.  

You can run it in your Java programs and get the tests results in a standard JavaBean object. You can then interpret it to establish if a particular test (or all of them) passed.

Given an input file (and few optional parameters), JEnt will calculate the following values:
- Entropy
- Chi-square Test
- Arithmetic Mean
- Monte Carlo Value for Pi
- Serial Correlation Coefficient

For more detail about the tests, please refer to the [ENT project page][ent].


## Sample
Just create a JEntSettings object and pass it to JEnt.
As a result, you will get a JavaBean object, containing all the tests results.
```java
JEntSettings settings = new JEntSettings.Builder()
    .setInputFile(inputFile)
    .build();

JEntReport report = JEnt.executeTests(settings);
```
You  need to implement the whole logic to define if a test passed or not, based on the result this library will return.  
For example, you can check if the tested input file contains a sufficient amount of entropy, with something like this:
```java
if (report.getEntropy() > 7.5) {
    // OK, it may be random...
} else {
    // mmmh, too little entropy...
}
```

Calling toString() you will get a String similar to the original ENT output message.

```
Entropy = 7,999775630955 bits per byte.

Optimum compression would reduce the size
of this 860760 byte file by 0.0 percent.

Chi square distribution for 860760 samples is 267,720916399461, and randomly
would exceed this value 27,974687250646 percent of the times.

Arithmetic mean value of data bytes is 127,675932896510 (127.5 = random).
Monte Carlo value for Pi is 3,140025094103 (error 0.04989696818637273 percent).
Serial correlation coefficient is -0,001768443360 (totally uncorrelated = 0.0).
```

## Settings
*JEnt.executeTests()* method requires a JEntSettings parameter object.
The input file field is mandatory, while the other fields are optional (because a default value is already set).
```java
JEntSettings settings = new JEntSettings.Builder()
      .setInputFile(inputFile)
      .setBinary(true)
      .setCharset(StandardCharsets.ISO_8859_1)
      .build();
```

## How to import
#### Maven
You can use JitPack to easily import this library into your Maven project.  
In your pom.xml add this:
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```
and this:
```xml
<dependency>
  <groupId>com.github.prgpascal.jEnt</groupId>
  <artifactId>core</artifactId>
  <version>2.0.0</version>
</dependency>
```

#### Gradle
You can use JitPack to easily import this library into your Gradle project.  
In your build.gradle add this:
```gradle
repositories {
  maven { url "https://jitpack.io" }
}
```
and this:
```gradle
dependencies {
  compile 'com.github.prgpascal:jEnt:core:2.0.0'
}
```

## License
	Copyright 2017 Riccardo Leschiutta

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.

The original [ENT project][ent] was developed by John Walker, and it's in the public domain.


[ent]: http://www.fourmilab.ch/random/
