# Redis Master-Replica - Dockerized Deployment 🏦

## Overview 📜

This section covers the **Master-Replica** Redis deployment, which consists of a primary Redis instance (master) and one
or more replicas. The replicas synchronize with the master, ensuring data redundancy and read scalability.

---

## Pros & Cons ⚖️

### ✅ Pros:

- **High Availability** – Provides data redundancy by replicating the master’s data to replicas.
- **Read Scalability** – Read queries can be distributed across multiple replicas, reducing the load on the master.
- **Easy Setup** – Simple to configure and deploy compared to Redis Cluster.
- **Failover Read Support** – Even if the master goes down, replicas can still serve read requests.

### ❌ Cons:

- **No Automatic Failover** – If the master fails, a manual intervention or Sentinel is required to promote a replica.
- **Eventual Consistency** – Replicas may lag behind the master, leading to temporary inconsistencies.
- **Write Scalability Limitations** – All writes must go through the master; replicas are read-only.
- **Data Loss Risk** – If the master crashes before replication is completed, data may be lost.

---

## Deployment Instructions 🚀

To deploy the Redis Master-Replica setup, use the following commands:

```sh
cd master_replica
docker-compose up -d
```

This will start a Redis **master** and a **replica** container using the configurations found in
`master/conf/redis.conf` and `replica/conf/redis.conf`.

---

## Configuration ⚙️

Each instance uses its respective `redis.conf` file, located within:

- **Master Configuration:** `master/conf/redis.conf`
- **Replica Configuration:** `replica/conf/redis.conf`

The replica is configured to synchronize with the master using the `replicaof` directive.

Example configuration in `replica.conf`:

```
replicaof redis-master 6379
```

---

## Data Persistence 💾

By default, Redis stores data in memory, but persistence can be enabled through:

- **RDB Snapshots** – Saves the dataset at specific intervals.
- **AOF (Append-Only File)** – Logs every write operation for durability.

These settings can be configured in `redis.conf`.

---

## License 📞

This project is licensed under The Unlicense. See the [LICENSE](../LICENSE) file for details.

