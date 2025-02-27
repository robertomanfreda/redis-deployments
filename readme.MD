# 🚀 Redis Deployment Examples

This repository provides **hands-on examples** for deploying Redis in different configurations, from a **basic
standalone setup** to **highly available architectures** with **Sentinel** and **Cluster**. Whether you're a beginner or
an advanced user, you'll find a setup that fits your needs!

## 🎯 Supported Deployment Modes

At the moment, this repository includes the following **Redis deployment modes**:

✅ **Standalone** – The simplest setup for small projects.  
✅ **Master-Replica** – Adds read scalability with replicas.  
✅ **Sentinel** – Provides **automatic failover** for high availability.  
⏳ **Cluster (Planned)** – Full **horizontal scaling** and **sharding** across nodes.

---  

## ⚡ Deployment Modes Overview

### 1️⃣ **Standalone Redis** 🏗️

- **Description**: A **single Redis instance** acts as both the master and the only node in the system.
- **Pros**:  
  ✅ Super easy to set up & manage.  
  ✅ Perfect for small projects or low-traffic apps.
- **Cons**:  
  ❌ No redundancy – if Redis goes down, your service is down.  
  ❌ No scalability – all operations run on one node, which may become a bottleneck.

👉 **[Show More](./standalone/readme.MD)**

---  

### 2️⃣ **Master-Replica Redis** 🔄

- **Description**: A **master node** handles writes, while **one or more replicas** sync data and serve read requests.
- **Pros**:  
  ✅ **Better availability** – a replica holds a copy of the data.  
  ✅ **Improved read scalability** – replicas handle read traffic.
- **Cons**:  
  ❌ **No automatic failover** – if the master fails, promotion must be done manually.  
  ❌ **Writes are still limited to the master**, which may create a bottleneck.

👉 **[Show More](./master_replica/readme.MD)**

---  

### 3️⃣ **Redis with Sentinel** 🛡️

- **Description**: Sentinel **monitors** Redis instances, detects failures, and **automatically promotes** a replica to
  master if the current master goes down.
- **Pros**:  
  ✅ **High availability** – no single point of failure.  
  ✅ **Automated failover** – no manual intervention needed.  
  ✅ **Scalable for small to medium-sized applications.**
- **Cons**:  
  ❌ **Not horizontally scalable** – Redis Sentinel doesn’t support data sharding.  
  ❌ **More complex setup** – at least **3 Sentinel nodes** are required for proper failover.

👉 **[Show More](./sentinel/readme.MD)**

---  

### 4️⃣ **Redis Cluster** 🌍 (Planned)

- **Description**: Redis Cluster **shards data across multiple nodes**, enabling **horizontal scaling** and **built-in
  high availability**.
- **Pros**:  
  ✅ **Scales out** by distributing data across nodes.  
  ✅ **Automatic failover** – built-in replication and self-healing.
- **Cons**:  
  ❌ **More complex setup** – requires multiple nodes.  
  ❌ **Limited cross-shard transactions** – multi-key operations across different nodes can be tricky.

🚧 **Coming soon! Stay tuned for updates.** 🚧

---

## 📌 Current Status

🚀 **Master-Replica & Sentinel setups are fully available!**  
🔜 **Redis Cluster deployment is under development.**

🔎 If you need **high availability and failover**, **Redis Sentinel** is a great choice! Check out
the [Sentinel example](./sentinel/readme.MD) for a step-by-step guide.

---

## 🛠️ How to Get Started

1️⃣ Clone the repository:

```bash
git clone https://github.com/robertomanfreda/redis-deployments.git
```  

2️⃣ Navigate to the example you want to try:

```bash
cd master_replica
```  

3️⃣ Follow the instructions in the specific **README** file for deployment details.

📢 **Have feedback or suggestions? Feel free to contribute!** 🚀