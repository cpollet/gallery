#!/usr/bin/env bash

for i in `docker images --format "{{.ID}} {{.Repository}}" | grep cpollet/net.cpollet.gallery- | cut -d' ' -f1`; do
    docker rmi -f $i
done

echo "Remove postgres data? [yN]"
read answer
if [ "$answer" = "y" ]; then
    docker volume rm gallery_ws_pg_data
fi

# warning: this can be harmful ;)
# docker stop `docker ps | grep postgres- | cut -d' ' -f1`
# docker rm `docker ps -a | grep postgres- | cut -d' ' -f1`
