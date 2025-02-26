# Redis Deployment Examples

This repository includes various examples for deploying Redis in different configurations, ranging from simple
standalone setups to more advanced configurations with Sentinel and Cluster.

## Supported Deployment Modes

At the moment, this repository includes examples for the following Redis deployment modes:

- **Standalone**
- **Master-Replica**
- **Sentinel**
- **Cluster** (Planned for future releases)

## Deployment Modes Overview

### 1. **Standalone Redis**

- **Description**: This is the simplest setup where a single Redis instance is deployed as both the master and the only
  node in the system.
- **Pros**:
    - Easy to set up and manage.
    - Ideal for small projects or low-traffic applications.
- **Cons**:
    - No redundancy: If the Redis instance fails, the service is down.
    - No scalability: All reads and writes are handled by a single node, which can become a bottleneck.

### 2. **Master-Replica Redis**

- **Description**: This configuration includes one master Redis instance and one or more replica nodes. The replicas
  mirror the master data and can serve read requests to offload traffic from the master.
- **Pros**:
    - Improved availability: If the master fails, one of the replicas can be promoted (though manually in this setup).
    - Better read scalability: Replicas can handle read queries, reducing the load on the master.
- **Cons**:
    - No automatic failover: If the master node goes down, manual intervention is required to promote a replica to
      master.
    - Writes are still limited to the master, potentially creating a bottleneck.

### 3. **Redis with Sentinel**

- **Description**: This setup includes Redis with Sentinel, a system for monitoring Redis instances, performing
  automatic failover, and promoting a replica to master if the current master fails.
- **Pros**:
    - High availability: Sentinel ensures that if the master fails, one of the replicas is automatically promoted.
    - Automated failover: No manual intervention is required to recover from failures.
    - Simple to scale for small to medium-sized applications.
- **Cons**:
    - Not horizontally scalable: Redis Sentinel doesn't support data sharding across multiple Redis nodes. If you need
      more capacity, you'll need to add more replicas.
    - Sentinel adds complexity: You need to configure and manage at least 3 Sentinel nodes for quorum and proper
      failover.

### 4. **Redis Cluster**

- **Description**: Redis Cluster supports data sharding across multiple nodes, allowing you to distribute your Redis
  data across several machines for horizontal scaling.
- **Pros**:
    - Horizontal scalability: Redis Cluster allows you to scale out your Redis instance by partitioning data across
      multiple nodes.
    - Built-in high availability: Redis Cluster supports replication and automatic failover.
- **Cons**:
    - More complex to set up and manage compared to simpler setups like Standalone or Sentinel.
    - Limited cross-shard transactions: Operations that span multiple shards may be more complex or slower.

## Current Status

At the moment, this repository **only** supports the **Sentinel** deployment mode. Future updates will include examples
and configurations for other modes, including Redis Cluster.

If you're looking for high availability and automatic failover, Redis with Sentinel is a reliable choice. For now, refer
to the **Sentinel** example in this repository for guidance on setting up and managing your Redis deployment.

## How to Get Started

1. Clone the repository:
   ```bash
   git clone <repository-url>
    ```

2. Navigate to the desired example folder:
   ```bash
   cd redis-sentinel-example
   ```

3. Follow the instructions in the README of that example folder for deployment details.

