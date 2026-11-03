#!/bin/sh
set -e

java -jar /app/app.jar &

exec nginx -g 'daemon off;'
