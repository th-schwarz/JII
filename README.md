# JII (Java Image Info) 

It provides a clean interface to a collection of Java 
libraries and source code to read basic properties of images.

This project licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) (the "License").

[![Build Status](https://travis-ci.org/th-schwarz/JII.svg?branch=master)](https://travis-ci.org/th-schwarz/JII)

## Changelog

* 1.1.0
  * issue #5 Include Apache-Commons-Imaging 
  * issue #8 Update platform to Java 8
  * updated slf4j to 1.7.25
  * changed to maven 3.5

* 1.0.1
  * issue #5: ImageMagick: path to the library must be respected 
  * issue #6: Some wrappers doesn't close InputStreams correctly

* 1.0
  * changed the base package to codes.thischwa.jii
  * updated slf4j to 1.7.7

* 0.6
  * updated slf4j to 1.7.5
  * ImageIOWrapper: add type 'bmp'

* 0.5
  * updated slf4j to 1.7.2
  * issue#2: ImageMagickWrapper: change identify call to work with the format parameter
