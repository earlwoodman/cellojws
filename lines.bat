@echo off
PATH=C:\cygwin64\bin;%PATH%
cd E:\Dropbox\Earl\Projects\CelloJWS
C:\cygwin64\bin\find . | grep '\.java' | xargs wc -l | grep total
pause