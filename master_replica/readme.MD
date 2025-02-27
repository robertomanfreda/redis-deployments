# Master-Replica Redis Deployment

This guide provides instructions for deploying a **Master-Replica Redis** setup using Docker.

## Overview

In a **Master-Replica Redis** setup, one Redis instance acts as the **master**, handling all write operations, while one
or more **replicas** synchronize data from the master and can serve read requests. This improves read scalability and
provides data redundancy.

## Folder Structure

```
master_replica/
├── docker-compose.yml  # Docker Compose file to start Redis master and replicas
├── master
│   ├── conf
│   │   └── redis.conf  # Configuration file for the master instance
│   └── data            # Directory for persistent master data
└── replica
    ├── conf
    │   └── redis.conf  # Configuration file for the replica instance
    └── data            # Directory for persistent replica data
```  

## Pros and Cons

### ✅ Pros:

- **Improved availability**: If the master fails, a replica contains a copy of the data.
- **Read scalability**: Read operations can be distributed across replicas.
- **Simple setup**: Easy to configure compared to more complex Redis cluster solutions.

### ❌ Cons:

- **No automatic failover**: If the master goes down, replicas do not automatically become the new master.
- **Write operations are limited to the master**: This can create a bottleneck in write-heavy workloads.

## Deployment Instructions

### 1. Start the Redis Master and Replica Containers

Navigate to the `master_replica` directory and run:

```bash
cd master_replica
docker-compose up -d
```  

This will start:

- **A Redis master instance**
- **A Redis replica instance**, which synchronizes data from the master

### 2. Verify the Deployment

To check if both instances are running, execute:

```bash
docker ps
```  

You should see two running Redis containers, one for the master and one for the replica.

To connect to the Redis master:

```bash
docker exec -it redis-master redis-cli
```  

To connect to the Redis replica:

```bash
docker exec -it redis-replica redis-cli
```  

Inside the Redis CLI, check if the replica is properly syncing with the master:

```redis
INFO replication
```  

You should see output indicating that this instance is a **replica** and is connected to the master.

### 3. Stopping and Removing Containers

To stop the Redis instances:

```bash
docker-compose down
```  

To remove all stored data:

```bash
rm -rf master/data/* replica/data/*
```  

## Configuration

### **Master Configuration (`master/conf/redis.conf`)**

Key settings in the master configuration file:

```conf
bind 0.0.0.0
protected-mode yes
save 3600 1 300 100 60 10000
stop-writes-on-bgsave-error yes
dbfilename dump.rdb
dir /data
requirepass foobar
maxmemory 1gb
maxmemory-policy noeviction
appendonly no
```  

### **Replica Configuration (`replica/conf/redis.conf`)**

Key settings in the replica configuration file:

```conf
bind 0.0.0.0
dir /data
replicaof 127.0.0.1 6379
replica-read-only yes
requirepass foobar
masterauth foobar
```  

## Conclusion

This **Master-Replica Redis** setup improves read scalability and provides data redundancy, making it a better option
than a standalone deployment for applications with high read loads. However, it does not provide automatic failover. For
high availability with failover, consider using **Redis Sentinel**.

---

For further details, check the [official Redis documentation](https://redis.io/docs/).