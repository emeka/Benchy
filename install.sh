#!/bin/sh

gradle clean install zip
rm -dfr ~/work/tools/benchy-0.1-SNAPSHOT/
unzip -o build/benchy-0.1-SNAPSHOT.zip -d ~/work/tools/
chmod +x ~/work/tools/benchy-0.1-SNAPSHOT/bin/*
#cp benchy-driver/build/libs/* /java/projects/multiverse/multiverse-beta/lib/