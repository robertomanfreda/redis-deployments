# Redis Sentinel Deployment

This guide provides instructions for deploying **Redis with Sentinel** using Docker.

## Overview

Redis Sentinel is a monitoring and failover system that provides high availability for Redis deployments. It ensures
that if the **master** node fails, a **replica** is automatically promoted to master, minimizing downtime.

⚠️ Note: In this project, we have included a single Sentinel deployment for simplicity. However, in a real-world
production environment, Redis Sentinel requires at least three Sentinel instances to ensure proper failover. This setup
relies on the master-replica configuration from the master_replica folder, where the Sentinel monitors the master and
promotes a replica if needed.

## Folder Structure

```
sentinel/
├── docker-compose.yml  # Docker Compose file to start Sentinel
├── Dockerfile          # Dockerfile for building the Sentinel container
├── conf
│   └── redis.conf      # Configuration file for Sentinel
```  

## How Sentinel Works

- Monitors the **master** instance.
- Detects failures and promotes a **replica** to master when needed.
- Updates clients with the new master’s address after failover.

## Pros and Cons

### ✅ Pros:

- **Automatic failover**: If the master fails, Sentinel promotes a replica automatically.
- **High availability**: Ensures minimal downtime.
- **No manual intervention required**: Unlike the simple master-replica setup, failover is automated.

### ❌ Cons:

- **Requires at least three Sentinel nodes** for proper failover decision-making.
- **Adds complexity**: More configuration and management compared to a basic master-replica setup.
- **No horizontal scaling**: Sentinel does not support data sharding.

## Deployment Instructions

### 1. Start the Redis Master, Replica, and Sentinel Containers

Navigate to the `sentinel` directory and run:

```bash
cd sentinel
docker-compose up -d
```  

This will start:

- **A Redis master instance**
- **One or more Redis replicas**
- **Three Sentinel instances** for monitoring and failover

### 2. Verify Sentinel Status

Check the running containers:

```bash
docker ps
```  

Connect to the Redis Sentinel container:

```bash
docker exec -it sentinel-1 redis-cli -p 26379
```  

Inside the Redis CLI, check the Sentinel status:

```redis
SENTINEL MASTER mymaster
```  

This should display information about the monitored Redis master.

To see the list of replicas:

```redis
SENTINEL REPLICAS mymaster
```  

### 3. Simulating a Failover

To test automatic failover, stop the Redis master:

```bash
docker stop redis
```  

After a few seconds, check the new master using:

```bash
docker exec -it sentinel redis-cli -p 26379
SENTINEL MASTER mymaster
```  

You should see that one of the replicas has been promoted to **master**.

### 4. Stopping and Removing Containers

To stop all instances:

```bash
docker-compose down
```  

To remove all stored data:

```bash
rm -rf data/*
```  

## Configuration

### **Sentinel Configuration (`sentinel/conf/redis.conf`)**

```conf
port 26379

sentinel monitor mymaster 192.168.1.146 6379 2
sentinel down-after-milliseconds mymaster 10000
sentinel failover-timeout mymaster 60000
sentinel parallel-syncs mymaster 1

requirepass "foobarbaz"
sentinel auth-pass mymaster foobar
```  

## Conclusion

Redis Sentinel provides **automatic failover and high availability**, making it a great option for production
environments that require reliability. However, it **does not support horizontal scaling**. For large-scale
applications, **Redis Cluster** is recommended.

---

For more details, check the [official Redis Sentinel documentation](https://redis.io/docs/management/sentinel/).