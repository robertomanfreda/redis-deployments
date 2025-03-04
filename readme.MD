# Redis Deployments 🚀

## Overview 📝

This project provides a fully dockerized setup for Redis, including different deployment configurations:

- 🏗️ [Standalone](./standalone/readme.MD)
- 🏛️ [Master-Replica](./master_replica/readme.MD)
- 🛡️ [Sentinel](./sentinel/readme.MD)
- 📡 [Cluster (planned for future release)](./cluster/readme.MD)

---

## Deployment Options ⚙️

### 1. Standalone Redis 🏗️

This is a single-instance Redis deployment.

```sh
cd standalone
docker-compose up -d
```

### 2. Master-Replica Setup 🏛️

This setup includes one Redis master and one or more replicas.

```sh
cd master_replica
docker-compose up -d
```

### 3. Sentinel High Availability 🛡️

This setup includes a Redis master, replicas, and three Sentinel instances to monitor failover.

```sh
cd sentinel
docker-compose up -d
```

---

## Redis Key Features ✨

- ⚡ **In-memory storage:** Redis keeps data in memory, ensuring high-speed read/write operations.
- 📂 **Data structures:** Supports strings, hashes, lists, sets, sorted sets, bitmaps, and more.
- 🛠️ **Persistence:** Supports RDB snapshots and AOF logging for durability.
- 🔁 **Replication:** Allows master-replica replication for data redundancy.
- 🏗️ **High availability:** Sentinel provides automatic failover for a resilient setup.
- 🌍 **Clustering:** Enables horizontal scaling via sharding.

---

## How Redis Works ⚙️

Redis is a high-performance, in-memory key-value store designed for speed and efficiency. Here are some key aspects of
its architecture:

- 🚀 **Single-threaded core:** Redis processes all commands using a single-threaded event loop, avoiding context
  switching overhead and ensuring predictable performance.
- 🌋 **Blazing-fast operations:** Due to its in-memory nature, Redis achieves sub-millisecond response times for most
  operations.
- 🛑 **Atomic commands:** All Redis commands are atomic, ensuring that operations are completed in a single step.
- 📝 **Data durability options:** Redis supports both RDB snapshots (point-in-time persistence) and AOF (Append-Only
  File) logging for durability.
- 🔄 **Replication & failover:** Redis supports master-replica replication and Sentinel-based automatic failover for high
  availability.
- 🔀 **Sharding for scalability:** Redis Cluster enables horizontal scaling by partitioning data across multiple nodes.

### Single-thread Nature

> ... Another contributing factor is its predominantly single-threaded nature: single-threading avoids race conditions
> and CPU-heavy context switching associated with threads
> @https://redis.io/learn/operate/redis-at-scale/talking-to-redis/redis-server-overview

Redis is primarily **single-threaded**, meaning it handles all operations using a single core. This design choice avoids
race conditions and the overhead caused by context switching between threads, leading to **predictable and fast
performance**.

However, since **Redis 6.0**, **multithreading** has been introduced **only for I/O operations** (e.g., accepting
connections, reading requests, and sending responses). The number of threads for I/O can be configured using the
`io-threads` directive in `redis.conf`, but **the core execution of commands remains single-threaded**.

---

### **Why Avoid Using Multiple Databases in Redis?**

In Redis, multiple logical databases can be accessed using the `SELECT` command (e.g., `SELECT 1`, `SELECT 2`). However,
using multiple databases is **discouraged** except in very limited cases.

🔹 **Why?**

- Redis still operates with a **single thread** for all databases, meaning that all DBs compete for the same CPU time.
- There is **no real isolation** between databases beyond logical separation.
- Most Redis commands, including `FLUSHDB` and `FLUSHALL`, affect all databases, leading to potential issues in shared
  environments.

Salvatore Sanfilippo, the creator of Redis, has discussed this topic in this conversation:  
👉 [Google Groups Discussion](https://groups.google.com/g/redis-db/c/vS5wX8X4Cjg)

**Alternative?** ✅ **Use key prefixes instead of multiple databases** (e.g., `user:1001:name`, `order:5001:status`).
This approach is more scalable and recommended for applications needing separation within Redis. 🚀

For more details, refer to the official Redis documentation: [Redis.io](https://redis.io/docs/)

---

## Caching Patterns 🔥

### Cache-aside 🏃‍♂️

- The application checks Redis for the data first.
- If not present, it queries the database, updates Redis, and returns the data.
- Used for read-heavy workloads with occasional writes.

### Write-through ✍️

- The application writes data to Redis and the database simultaneously.
- Ensures consistency between Redis and the database.
- Suitable for scenarios where data consistency is critical.

### Write-back ⏳

- The application writes to Redis first, deferring database updates.
- Redis periodically flushes updates to the database.
- Improves write performance but may cause temporary inconsistencies.

### Least Recently Used (LRU) Eviction ♻️

- Redis automatically removes the least recently used keys when memory is full.
- Helps manage cache size efficiently.
- Can be configured with policies like `volatile-lru`, `allkeys-lru`, etc.

### Time-to-Live (TTL) Expiry ⏱️

- Keys can have an expiration time set.
- Ensures stale data is automatically removed.
- Useful for session storage and temporary caches.

### Distributed Caching 🌐

- Multiple Redis instances can be used to distribute cache load.
- Commonly used in large-scale applications with high concurrency.

---

## Configuration ⚙️

Each Redis instance uses its respective `redis.conf` file, located within the `conf` directories.

---

## Data Persistence 💾

Each Redis instance stores data in the `data/` directory, ensuring persistence across restarts.

### RDB VS AOF

| Feature             | RDB (Redis Database File)           | AOF (Append-Only File)                          |
|---------------------|-------------------------------------|-------------------------------------------------|
| **Persistence**     | Periodic snapshot of data           | Continuous log of write operations              |
| **Performance**     | Faster, uses fewer resources        | Slower, ensures more data safety                |
| **Durability**      | Risk of data loss between snapshots | Higher reliability with fsync                   |
| **Memory Usage**    | Lower, only stores snapshots        | Higher, logs all operations                     |
| **Recovery Speed**  | Faster (loads a compact file)       | Slower (replays all operations)                 |
| **File Corruption** | More resilient to corruption        | Higher risk if the file is damaged              |
| **Configurability** | Snapshot intervals (`save 900 1`)   | Various sync modes (`fsync always/everysec/no`) |
| **Best Use Case**   | Periodic backup, high speed         | Maximum data reliability                        |

---

## License 📜

This project is licensed under The Unlicense. See the [LICENSE](./LICENSE) file for details.

