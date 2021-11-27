#!/bin/bash

# Change into the right directory
cd sempre

# Downloading all the dependencies needed:
./pull-dependencies core
./pull-dependencies corenlp
./pull-dependencies freebase
./pull-dependencies tables