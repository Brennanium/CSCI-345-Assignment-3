#!/bin/bash
java \
    --enable-preview \
    -Dprism.verbose=true \
    -Djava.library.path="./lib/javafx-sdk-15.0.1/lib/" \
    -jar deadwood.jar