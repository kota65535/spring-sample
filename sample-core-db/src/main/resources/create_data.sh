#!/usr/bin/env bash

echo "name,tag"
cat /dev/urandom | base64 | fold -w 9 | sed 's/./,/5' | head -n 100000
