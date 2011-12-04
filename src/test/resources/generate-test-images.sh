#!/bin/sh
convert -size 240x130 canvas:none -font Arial-Black-Standard -pointsize 144 -draw "text 15,120 'JII'" -channel RGBA -blur 0x6 -fill silver -stroke darkred -strokewidth 5 -draw "text 10,110 'JII'" JII.png
convert JII.png -units PixelsPerInch -density 300x300 JII_240x130-300x300.jpg
convert JII.png -resize 50% JII_120x65-72x72.bmp
convert JII.png -resize 50% -units PixelsPerInch -density 72x72 JII_120x65-72x72.gif
convert JII.png -resize 50% -units PixelsPerInch -density 140x72 JII_120x65-140x72.png