#!/bin/sh

pnpm run build
tar -czf archive.tar.gz dist Dockerfile docker-compose.yml nginx.conf
curl -i -X POST -H "Authorization: Bearer $SEELF_TOKEN" -F environment=$SEELF_ENVIRONMENT -F archive=@archive.tar.gz $SEELF_APPLICATION_URL