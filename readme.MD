# Redis Deployments 🚀

---

## Overview 📝

This project provides a fully dockerized setup for Redis, including different deployment configurations:

- 🏗️ [Standalone](./standalone/readme.MD)
- 🏛️ [Master-Replica](./master-replica/readme.MD)
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

---

## License 📜

This project is licensed under The Unlicense. See the [LICENSE](./LICENSE) file for details.

