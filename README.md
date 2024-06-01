## CPU Scheduling Simulator
Scheduling is a fundamental operating system function. Almost all computer resources are scheduled before use. The CPU is, of course, one of the primary computer resources.
Thus, its scheduling is central to operating-system design. CPU scheduling determins which processes run when there are multiple run-able processes. CPU scheduling is important
becuase it can have a big effect on resource utilization and the overall performance of the system.

#### The following scheduling algorithms are implemented
1. Shortest-Job First (SJF): non-preemptive, context switching
2. Shortest-Remaining Time First (SRTF): preemptive, context switching
3. Priority Scheduling: non-preemptive, context switching
4. AG Scheduling:
   - AG-Factor = (Priority or 10 or random_function(0, 20)) + arrival time + burst time
     - if random_function < 10 -> AG-Factor = RF() + arrival time + burst time
     - if random_function > 10 -> AG-Factor = 10 + arrival time + burst time
     - if random_function = 10 -> AG-Factor = Priority + arrival time + burst time
   - The process is non-preemptive till it finish 50% of its quantum time, then it becomes preemptive
   - The 3 scenarios of the running process
     i. The running process used all its quantum time and it still have job to do (add this process to the end of the queue, then increases its quantum time by (ceil(10% of the (mean of quantum))))
     ii. The running process didn't use all its quantum time based on another process converted from ready to running (add this process to the end of the queue, and then increase its quantum time by the remaining unused quantum time of the process
     iii. The running process finished its job (set its quantum time to zero and remove it from ready queue and add it to the die list).

### Test Case
(pr stands for process)
the input format for processes is like this: pr_name pr_color pr_arrival_time pr_burst_time pr_priority



## Synchronization
It is required to simulate a limited number of devices connected to a router's Wi-Fi using Java threading and semaphore. Routers can be designed to limit the number of open connections.
For example, aa router may wish to have only N connections at any point in time. As soon as N connections are made, the Router will not accept other incoming connections until an existing
connection is released.

### Following rules should be applied
- The Wi-Fi number of connected devices is initially empty.
- If a client is logged in (print a message that the client is logged in) and if it can be served (means that it can reach the internet) then the client should perform online activity (these actions will be represented by printed messages)
- If a client arrives and all connections are occupied, it must wait until one of the currently available client finish his service and leave.
- After a client finishes his service, he leave and one the waiting client (if exist) will connect to the internet.

### Test Case
program output is =
- =What is the maximum number of connections the router can accepts?
- 2
- =How many number of devices wish to connect?
- 4
- C1 mobile
- C2 tablet
- C3 pc
- C4 laptop
