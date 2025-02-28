# Redis Standalone - Dockerized Deployment 🏗️

## Overview 📝

This section covers the standalone Redis deployment, which consists of a single Redis instance running in a Docker
container.

---

## Pros & Cons ⚖️

### ✅ Pros:

- **Simplicity** – Easy to deploy and configure.
- **Performance** – Provides high-speed read and write operations since everything runs in memory.
- **Lightweight** – Consumes minimal system resources compared to distributed setups.
- **Great for Caching** – Ideal for applications requiring fast, temporary data storage.
- **Quick Startup** – No additional coordination or failover mechanisms required.

### ❌ Cons:

- **No High Availability** – If the instance crashes, all data is lost unless persistence mechanisms (RDB/AOF) are
  enabled.
- **No Scalability** – A single instance cannot handle large-scale distributed workloads.
- **No Automatic Failover** – Unlike Sentinel or Cluster modes, no built-in failover mechanism exists.
- **Single Point of Failure** – If the server goes down, Redis becomes unavailable.

---

## Deployment Instructions 🚀

To deploy the standalone Redis instance, use the following commands:

```sh
cd standalone
docker-compose up -d
```

This will start a Redis container using the configuration found in `conf/redis.conf` and persist data in the `data/`
directory.

---

## Configuration ⚙️

The standalone Redis instance uses its respective `redis.conf` file, located within the `conf/` directory. You can
modify this file to change settings like persistence, eviction policies, and memory limits.

---

## Data Persistence 💾

By default, Redis stores data in memory, but persistence can be enabled through:

- **RDB Snapshots** – Saves the dataset at specific intervals.
- **AOF (Append-Only File)** – Logs every write operation for durability.

These settings can be configured in `redis.conf`.

---

## License 📜

This project is licensed under The Unlicense. See the [LICENSE](./LICENSE) file for details.

