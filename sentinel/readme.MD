# Redis Master + Replica + Sentinel

This setup ensures high availability and automatic failover by using **Redis Master**, **Replica** nodes, and
**Sentinel** for monitoring and automatic promotion of a new master node in case of failure.  
Here's a standard approach for deploying Redis with the following components:

- **1 Master Node**: The primary node where writes and reads happen (unless replicas are used for read scaling).
- **N Replica Nodes**: These nodes replicate the master to provide redundancy and help with read scaling.
- **N Sentinel Nodes**: These nodes monitor the health of Redis nodes, detect failures, and handle the failover process.

## Configuration Steps

1. **Master Node**:
    - Set up the master node by installing Redis and configuring it to accept both read and write requests. This node
      will serve as the source for replication.

2. **Replica Nodes**:
    - Configure replica nodes to replicate the master node. Each replica will have a read-only copy of the data from the
      master. In Redis, replicas can be configured with the `replicaof` directive in the Redis configuration file.

3. **Sentinel Nodes**:
    - Sentinel monitors the health of the Redis cluster and ensures that a new master is promoted if the current master
      fails.
    - **Minimum Number of Sentinels**: At least **3 Sentinel nodes** are recommended to prevent split-brain scenarios
      and ensure a majority for leader election.
    - **Why 3 Sentinels?**: With 3 Sentinels, you ensure that a majority can be achieved in the event of a failure. In
      case one Sentinel goes down, the remaining two Sentinels can still make decisions.

4. **Sentinel Configuration**:
    - Set the `sentinel monitor` configuration to tell Sentinels which Redis master to monitor.
    - Example:
      ```bash
      sentinel monitor mymaster <master-ip> 6379 2
      ```
      In this case, the Sentinel monitors the `mymaster` at the specified IP and port, with a quorum of 2 Sentinels to
      agree on failover.

## Important Considerations

- **Quorum**: The quorum is the minimum number of Sentinels that need to agree to perform a failover. The higher the
  quorum, the more resilient the setup will be, but it may increase failover latency.

- **Network Stability**: Ensure that all Redis and Sentinel nodes are located in a stable network environment. Network
  partitions can lead to split-brain scenarios, even with a quorum-based approach.

- **Failover Process**: In case the master node becomes unavailable, Sentinels will:
    1. Elect a new master among the replicas.
    2. Update the remaining nodes to point to the new master.
    3. Redis clients need to be configured to work with Sentinels for automatic discovery of the new master.

## Why Use Redis with Sentinel?

1. **High Availability**: Redis with Sentinel ensures that even if the master node goes down, one of the replicas will
   be promoted to master without manual intervention.
2. **Failover Automation**: Sentinel automatically handles the failover process, reducing the need for human
   intervention.
3. **Scalability**: Using replica nodes allows for scaling reads and maintaining performance even during master
   failures.

## Recommendations

- **Start with 3 Sentinels**: A setup with at least three Sentinels is critical for preventing split-brain scenarios.
- **Add More Replicas for High Availability**: Adding more replica nodes will increase redundancy and allow for a more
  reliable failover process in case of failure.
- **Monitoring**: Always monitor your Redis and Sentinel nodes for performance and health, using Redis’s built-in
  monitoring tools or third-party solutions.

This setup provides a basic, robust, and reliable Redis deployment for small to medium-scale applications, ensuring both
high availability and disaster recovery.
