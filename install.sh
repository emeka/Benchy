#!/bin/sh

gradle clean install zip
rm -dfr /java/tools/benchy-0.1-SNAPSHOT/
unzip -o build/benchy-0.1-SNAPSHOT.zip -d /java/tools/
chmod +x /java/tools/benchy-0.1-SNAPSHOT/bin/*
cp benchy-driver/build/libs/* /java/projects/multiverse/multiverse-beta/lib/