#!/bin/bash

IMG=$1;
convert -size 96x96 $IMG -resize 96x96 +profile '*' res/drawable-xhdpi/`basename $IMG .jpg`.png
convert -size 72x72 $IMG -resize 72x72 +profile '*' res/drawable-hdpi/`basename $IMG .jpg`.png
convert -size 48x48 $IMG -resize 48x48 +profile '*' res/drawable-mdpi/`basename $IMG .jpg`.png
convert -size 36x36 $IMG -resize 36x36 +profile '*' res/drawable-ldpi/`basename $IMG .jpg`.png
