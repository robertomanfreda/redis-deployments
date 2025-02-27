# Standalone Redis Deployment

This guide provides instructions for deploying a **Standalone Redis** instance using Docker.

## Overview

In a **Standalone Redis** setup, a single Redis instance handles all read and write operations. This is the simplest
deployment mode and is suitable for small projects or environments where high availability is not a requirement.

## Folder Structure

```
standalone/
├── conf
│   └── redis.conf      # Redis configuration file
├── data                # Directory for persistent Redis data
└── docker-compose.yml  # Docker Compose file for deployment
```

## Pros and Cons

### ✅ Pros:

- **Simple setup**: Easy to configure and deploy.
- **Lightweight**: Requires minimal resources.
- **Ideal for development**: Useful for testing and small-scale applications.

### ❌ Cons:

- **No redundancy**: If the Redis instance fails, all data is lost unless persistent storage is used.
- **No scalability**: All operations are handled by a single node, which can become a performance bottleneck.

## Deployment Instructions

### 1. Start the Redis Container

Navigate to the `standalone` directory and start the Redis instance using Docker Compose:

```bash
cd standalone
docker-compose up -d
```

This will start a Redis container with the configuration specified in `redis.conf`.

### 2. Verify the Redis Instance

To check if Redis is running correctly, use the following command:

```bash
docker ps
```

You should see a running Redis container.

Alternatively, you can connect to the Redis CLI inside the container:

```bash
docker exec -it redis redis-cli
```

Once inside the Redis CLI, test connectivity:

```redis
PING
```

If Redis is working correctly, it should return:

```
PONG
```

### 3. Stopping and Removing the Container

To stop the Redis instance:

```bash
docker-compose down
```

To remove all data stored in the `data` directory:

```bash
rm -rf data/*
```

## Configuration

You can modify the Redis configuration in `conf/redis.conf`. Some useful parameters include:

- **`bind 0.0.0.0`** – Allows Redis to be accessible from any IP (use with caution in production).
- **`appendonly yes`** – Enables data persistence using Append-Only File (AOF).
- **`maxmemory 100mb`** – Limits Redis memory usage.

## Conclusion

This standalone Redis deployment is ideal for small-scale applications, testing, and development environments. For
production environments requiring high availability and scalability, consider using **Master-Replica** or **Sentinel**
configurations.

---

For further details on Redis configurations, visit the [official Redis documentation](https://redis.io/docs/).