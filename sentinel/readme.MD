# Redis Sentinel - Dockerized Deployment 🛡️

## Overview 📝

Redis Sentinel provides high availability for Redis by monitoring master and replica instances and performing automatic
failover if the master node becomes unavailable.

---

## Pros & Cons ⚖️

### ✅ Pros:

- **High Availability** – Automatically promotes a replica to master if the master fails.
- **Monitoring & Notifications** – Constantly checks the health of Redis instances and provides alerts.
- **Automatic Failover** – Ensures minimal downtime in case of failure.
- **Scalability** – Allows multiple Sentinel instances for improved reliability.
- **Decentralized Architecture** – No single point of failure in the monitoring system.

### ❌ Cons:

- **Complex Configuration** – Requires multiple instances and careful network setup.
- **Eventual Consistency** – Failover time depends on the Sentinel quorum agreement.
- **Requires Proper Quorum** – If not enough Sentinels are available, failover will not occur.
- **No Data Sharding** – Sentinel does not support horizontal scaling like Redis Cluster.

---

## Deployment Instructions 🚀

To deploy the Redis Sentinel setup, use the following commands:

```sh
cd sentinel
docker-compose up -d
```

This will start the Redis master, replicas, and three Sentinel instances based on the configurations in `conf/`.

---

## Configuration ⚙️

Each Redis instance and Sentinel uses its respective `redis.conf` file located within the `conf/` directories. Key
configurations include:

- **Sentinel Quorum** – Defines the number of Sentinel nodes required for failover.
- **Replication Settings** – Ensures replicas sync correctly with the master.
- **Failover Parameters** – Controls how fast the failover process is triggered.

---

## Data Persistence 💾

Redis Sentinel itself does not store data but monitors Redis instances, which can be configured for persistence using:

- **RDB Snapshots** – Saves the dataset at specific intervals.
- **AOF (Append-Only File)** – Logs every write operation for durability.

These settings can be configured in `redis.conf` for each instance.

---

## License 📝

This project is licensed under The Unlicense. See the [LICENSE](../LICENSE) file for details.

