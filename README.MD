##启动server
`D:\zookeeper-3.4.12\bin\zkServer.cmd`






## 分布式共识算法 raft
单机 `client -->data--> server`<br/>
集群 `client -->data--> [server1,server2,server3]` <br>
如何使得 `[server1,server2,server3]` 都接受并确认data <br>
server1 has 3 state : fellow candidate leader