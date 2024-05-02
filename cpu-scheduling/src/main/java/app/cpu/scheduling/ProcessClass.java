package app.cpu.scheduling;

import java.util.Random;

public class ProcessClass {
    private String name;
     private String color;
     private int arrivalTime;
     private int burstTime;
     private int priority;
     private int quantum;
     private int waitingTime;
     private int startTime;
     private int compelationTime;
     private int remainingTime;
     private int turnAroundTime;
     private int AGFactor;
     private int order;
     
    ProcessClass(String name, String color, int arrivalTime, int burstTime, int priority, int quantum, int order) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = priority;
        this.quantum = quantum;
        this.order = order;
        this.AGFactor = arrivalTime + burstTime;
        int randomNum = new Random().nextInt(21);
        if (randomNum < 10) {
            this.AGFactor += randomNum;
        } else if (randomNum > 10) {
            this.AGFactor += 10;
        } else {
            this.AGFactor += priority;
        }
    }
    
    public ProcessClass(ProcessClass process) {
        this.name = process.getName();
        this.color = process.getColor();
        this.arrivalTime = process.getArrivalTime();
        this.burstTime = process.getBurstTime();
        this.priority = process.getPriority();
        this.waitingTime = process.getWaitingTime();
        this.turnAroundTime = process.getTurnAroundTime();
        this.compelationTime = process.getCompelationTime();
        this.remainingTime = process.getRemainingTime();
        this.startTime = process.getStartTime();
        this.order = process.getOrder();
        this.quantum = process.getQuantum();
        this.AGFactor = process.getAGFactor();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public int getAGFactor() {
        return AGFactor;
    }

    public void setAGFactor(int AGFactor) {
        this.AGFactor = AGFactor;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    public int getCompelationTime() {
        return compelationTime;
    }

    public void setCompelationTime(int compelationTime) {
        this.compelationTime = compelationTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
