package app.cpu.scheduling;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.LinkedList;

public class Scheduler {
    private final int rrTime;
    private final int contextSwitchTime;
    private final int algoType;
    private final String algoName;
    private final int nProcess;
    private double averageWT;   // average waiting time
    private double averageTAT;  // average turn around time
    private ArrayList<ProcessClass> processes;
    private final LinkedList<ProcessClass> result = new LinkedList<>();
    
    public Scheduler(int nProcess, int rrTime, int contextSwitchTime, int algoType, String algoName, ArrayList<ProcessClass> processes) {
        this.nProcess = nProcess;
        this.rrTime = rrTime;
        this.contextSwitchTime = contextSwitchTime;
        this.algoType = algoType;
        this.algoName = algoName;
        this.processes = new ArrayList<>(processes);
        Collections.sort(this.processes, Comparator.comparingInt(ProcessClass::getArrivalTime));
    }
    
    public void simulate() {
        boolean isPreemptive = false;
        switch (this.algoType) {
            case 0:
                this.shortestJobFirst();
                break;
            case 1:
                this.shortestRemainingTimeFirst();
                isPreemptive = true;
                break;
            case 2:
                this.priorityScheduling();
                break;
            case 3:
                this.AGScheduling();
                isPreemptive = true;
                break;
        }
        // gantt chart
    }
    
    private void calcAverageWTandTAT() {
        int waitingTime = 0, turnAroundTime = 0;
        for (ProcessClass pr : this.result) {
            waitingTime += pr.getWaitingTime();
            turnAroundTime += pr.getTurnAroundTime();
        }
        this.averageWT = (double)waitingTime / this.nProcess;
        this.averageTAT = (double)turnAroundTime / this.nProcess;
    }

    private void shortestJobFirst() {
       LinkedList<ProcessClass> prcs = new LinkedList<>(this.processes);
       LinkedList<ProcessClass> readyQueue = new LinkedList<>();
       
       int lastTime = prcs.get(0).getArrivalTime();
       readyQueue.addLast(prcs.pollFirst());
       while (!readyQueue.isEmpty()) {
           ProcessClass pr = readyQueue.pollFirst();
           
           int startTime = lastTime;
           pr.setStartTime(startTime);
           
           int waitingTime = startTime - pr.getArrivalTime();
           pr.setWaitingTime(waitingTime);
           
           int compelationTime = startTime + pr.getBurstTime();
           pr.setCompelationTime(compelationTime);
           
           int turnAroundTime = compelationTime - pr.getArrivalTime();
           pr.setTurnAroundTime(turnAroundTime);
           
           lastTime = compelationTime + this.contextSwitchTime;
           this.result.add(pr);
           while (!prcs.isEmpty()) {
               if (compelationTime >= prcs.getFirst().getArrivalTime()) {
                   readyQueue.addLast(prcs.pollFirst());
               } else {
                   break;
               }
           }
           if (!readyQueue.isEmpty()) {
                Collections.sort(readyQueue, Comparator.comparingInt(ProcessClass::getBurstTime));
           }
        }
       this.calcAverageWTandTAT();
       this.print();
    }
    
    private void print() {
        System.out.println("Name\t Color\t BurstTime\t ArrivalTime\t WaitingTime\t StartTime\t CompelationTime\t TurnTime\t");
        for (ProcessClass pr : this.result) {
            System.out.print(pr.getName() + "\t" + pr.getColor() + "\t\t" + pr.getBurstTime() + "\t\t");
            System.out.print(pr.getArrivalTime() + "\t\t" + pr.getWaitingTime() + "\t\t" + pr.getStartTime() + "\t\t");
            System.out.println(pr.getCompelationTime() + "\t\t" + pr.getTurnAroundTime());
        }
    }
    
    private void shortestRemainingTimeFirst() {
        LinkedList<ProcessClass> prcs = new LinkedList<>(this.processes);
        LinkedList<ProcessClass> readyQueue = new LinkedList<>();
        
        int lastTime = prcs.get(0).getArrivalTime();
        readyQueue.add(prcs.pollFirst());
        while (!readyQueue.isEmpty()) {
            ProcessClass pr = readyQueue.pollFirst();
            
            int startTime = lastTime;
            pr.setStartTime(startTime);
            
            int waitingTime = startTime - pr.getArrivalTime();
            pr.setWaitingTime(waitingTime);
            
            int compelationTime = startTime + pr.getRemainingTime();
            ProcessClass newShortPr = null;
            if (!prcs.isEmpty() && prcs.getFirst().getArrivalTime() < compelationTime) {
                if (prcs.getFirst().getRemainingTime() < pr.getRemainingTime()) {
                    newShortPr = prcs.pollFirst();
                    readyQueue.addLast(newShortPr);

                    compelationTime = newShortPr.getArrivalTime();
                    pr.setCompelationTime(compelationTime);

                    int remainingTime = pr.getRemainingTime() - (compelationTime - startTime);
                    pr.setRemainingTime(remainingTime);
                    
                    int turnAroundTime = compelationTime - pr.getArrivalTime();
                    pr.setTurnAroundTime(turnAroundTime);

                    this.result.add(new ProcessClass(pr));
                    pr.setArrivalTime(compelationTime);
                    readyQueue.addLast(pr);
                } else {
                    pr.setCompelationTime(compelationTime);
                    pr.setRemainingTime(0);
                    pr.setTurnAroundTime(compelationTime - pr.getArrivalTime());
                    this.result.add(new ProcessClass(pr));
                    
                    readyQueue.addLast(prcs.pollFirst());
                    Collections.sort(readyQueue, Comparator.comparingInt(ProcessClass::getRemainingTime));
                }
            }
            else {
                pr.setCompelationTime(compelationTime);
                pr.setRemainingTime(0);
                pr.setTurnAroundTime(compelationTime - pr.getArrivalTime());
                this.result.add(new ProcessClass(pr));
            }
            lastTime = compelationTime;
        }
        this.calcAverageWTandTAT();
        this.print();
    }
    
    private void priorityScheduling() {
        LinkedList<ProcessClass> prcs = new LinkedList<>(this.processes);
        LinkedList<ProcessClass> readyQueue = new LinkedList<>();
        
        int lastTime = prcs.get(0).getArrivalTime();
        readyQueue.add(prcs.pollFirst());
        while (!readyQueue.isEmpty()) {
            ProcessClass pr = readyQueue.pollFirst();
            
            int startTime = lastTime;
            pr.setStartTime(startTime);
           
            int waitingTime = startTime - pr.getArrivalTime();
            pr.setWaitingTime(waitingTime);

            int compelationTime = startTime + pr.getBurstTime();
            pr.setCompelationTime(compelationTime);

            int turnAroundTime = compelationTime - pr.getArrivalTime();
            pr.setTurnAroundTime(turnAroundTime);

            lastTime = compelationTime + this.contextSwitchTime;
            this.result.add(pr);
            while (!prcs.isEmpty()) {
                if (compelationTime >= prcs.getFirst().getArrivalTime()) {
                    readyQueue.addLast(prcs.pollFirst());
                } else {
                    break;
                }
            }
            Collections.sort(readyQueue, Comparator.comparingInt(ProcessClass::getPriority));
        }
        this.calcAverageWTandTAT();
        this.print();
    }
    
    private int quantumMean(LinkedList<ProcessClass> prcs, LinkedList<ProcessClass> readyQueue) {
        double sum = 0.0;
        for (ProcessClass p : prcs) {
            sum += p.getQuantum();
        }
        for (ProcessClass p : readyQueue) {
            sum += p.getQuantum();
        }
        return (int)Math.ceil((sum/this.nProcess) * 0.1);
    }
    
    private ProcessClass getNextProcess(int currTime, LinkedList<ProcessClass> prcs, LinkedList<ProcessClass> readyQueue) {
        ProcessClass pr = null;
        if (!readyQueue.isEmpty()) {
            pr = readyQueue.pollFirst();
        } else {
            for (int i = 0; i < prcs.size() && currTime >= prcs.get(i).getArrivalTime(); ++i) {
                pr = prcs.remove(i);
                break;
            }
        }
        return pr;
    }
    
    private boolean minAGReadyQueue(ProcessClass pr, LinkedList<ProcessClass> readyQueue) {
        LinkedList<ProcessClass> temp = new LinkedList<>(readyQueue);
        Collections.sort(temp, Comparator.comparingInt(ProcessClass::getAGFactor));
        if (temp.getFirst().getAGFactor() < pr.getAGFactor()) {
            for (int i = 0; i < readyQueue.size(); ++i) {
                if (temp.getFirst().getName().equals(readyQueue.get(i).getName())) {
                    readyQueue.remove(i);
                    break;
                }
            }
            readyQueue.addFirst(temp.pollFirst());
            return true;
        }
        return false;
    }
    
    private void getNewProcesses(int currTime, ProcessClass pr, LinkedList<ProcessClass> readyQueue, LinkedList<ProcessClass> prcs) {
        for (int i = 0; i < prcs.size() && currTime >= prcs.get(i).getArrivalTime(); ++i) {
            readyQueue.add(prcs.remove(i));
            --i;
        }
    }
    
    private void AGScheduling() {
        LinkedList<ProcessClass> prcs = new LinkedList<>(this.processes);
        LinkedList<ProcessClass> readyQueue = new LinkedList<>();
        ArrayList<ProcessClass> deadList = new ArrayList<>();
        int currTime = 0;
        
        while (deadList.size() < this.nProcess) {
            ProcessClass pr = getNextProcess(currTime, prcs, readyQueue);
            if (pr == null) {
                ++currTime;
                continue;
            }
            int quantum = pr.getQuantum();
            pr.setStartTime(currTime);
            
            // non-preemptive part
            int quantumHalfTime = (int)Math.ceil(0.5 * (double)pr.getQuantum());
            int mn = Math.min(quantumHalfTime, pr.getRemainingTime());
            pr.setRemainingTime(pr.getRemainingTime() - mn);
            quantum -= mn;
            currTime += mn;
            
            // preemptive part
            if (pr.getRemainingTime() > 0) {
                while (pr.getRemainingTime() > 0) {
                    getNewProcesses(currTime, pr, readyQueue, prcs);
                    if (!readyQueue.isEmpty() && minAGReadyQueue(pr, readyQueue)) {
                        break;
                    }
                    ++currTime;
                    --quantum;
                    pr.setRemainingTime(pr.getRemainingTime() - 1);
                    if (quantum == 0) { break; }
                }
            }
            
            if (pr.getRemainingTime() == 0) {
                pr.setQuantum(0);
                deadList.add(pr);
            } else if (quantum == 0) {
                int newQuantum = pr.getQuantum() + (int)Math.ceil(0.1 * this.quantumMean(prcs, readyQueue));
                pr.setQuantum(newQuantum);
                readyQueue.addLast(pr);
            } else {
                pr.setQuantum(pr.getQuantum() + quantum);
                // sort ready queue
                readyQueue.addLast(pr);
            }
            pr.setCompelationTime(currTime);
            int turnAroundTime = currTime - pr.getArrivalTime();
            pr.setTurnAroundTime(turnAroundTime);
            pr.setWaitingTime(pr.getStartTime() - pr.getArrivalTime());
            this.result.add(new ProcessClass(pr));
            pr.setArrivalTime(currTime);
        }
        this.calcAverageWTandTAT();
        this.print();
    }
}
