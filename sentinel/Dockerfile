FROM redis:7.4.2

COPY conf/redis.conf /usr/local/etc/redis/redis.conf

ENTRYPOINT ["redis-server", "/usr/local/etc/redis/redis.conf", "--sentinel"]