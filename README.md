# JII - Java Image Info

There are a lot of possibilities to read out basic properties of image files like the width and height. Each way has its pro and cons relating to specific project requirements. JII provides a simple and clear interface to these different ways which are represented by various libraries and source codes. This significantly simplifies the validation of these methods with respect to a particular project.

The focus is on the following image types which are relevant for the web:

- BMP
- GIF
- JPEG
- PNG

This project licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) (the "License").

[![Build Status](https://travis-ci.com/th-schwarz/JII.svg?branch=master)](https://travis-ci.com/th-schwarz/JII)

For completeness, here is the [changelog](changelog.md).

Last release      [![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/th-schwarz/JII?include_prereleases)](https://github.com/th-schwarz/JII/releases)

## Disclaimer

I'm not responsible for any data loss, hardware damage or broken keyboards. This guide comes without any warranty!

## Supported libraries / source codes:

- *[iText](http://sourceforge.net/projects/itext/)*: A library for pdf generation. <br>
  There is an internal class to read out some image properties.  
- *ImageInfo*: A small and robust Java class by Marco Schmidt. It seems that he doesn't support this project any longer.<br>
  The corresponding website hasn't been accessible for years.
- *[SimpleImageInfo](http://jaimonmathew.wordpress.com/2011/01/29/simpleimageinfo)*: A Java class by Jaimon Mathew
  to get the image size without loading the whole data. It's very fast.
- *Wrapper to the command line tool (identify)[http://www.imagemagick.org/script/identify.php] of ImageMagick*: This wrapper is
  written by the author and analyzes the output of the tool.
- *Wrapper to Java's javax.imageio.ImageIO*: A relatively slow pure JRE solution. 
- *Wrapper to Apache's [commons-imaging](https://commons.apache.org/proper/commons-imaging/}commons-imaging)*: A pure-Java image library.

## Usage

Here is the [current api doc](https://th-schwarz.github.io/JII/apidocs/index.html)!

JII provides currently two interfaces:

- [codes.thischwa.jii.IDimensionProvider](https://th-schwarz.github.io/JII/apidocs/codes/thischwa/jii/IDimensionProvider.html)<br>
  It just provides the Dimension of an image.
- [codes.thischwa.jii.IResolutionProvider](https://th-schwarz.github.io/JII/apidocs/codes/thischwa/jii/IResolutionProvider.html)<br>
  It just provides the Resolution of an image.

All available implementations (wrappers) can be found in the core package. Each wrapper implements one or both interfaces above.

Example:
```java
  IDimensionProvider dp = new SimpleImageInfoWrapper();
  dp.set(new File("/dir/file.jpg"));
  Dimension dim = dp.getDimension();
  System.out.println(String.format("Dimension: %dx%d", dim.height, dim.width));
```
The codes.thischwa.jii.IResolutionProvider works in the same way.
