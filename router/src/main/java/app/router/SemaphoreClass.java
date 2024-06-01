package app.router;

public class SemaphoreClass {
    private int val;
    private final int maxConnections;
    
    public SemaphoreClass(int maxConnections) {
        this.val = maxConnections;
        this.maxConnections = maxConnections;
    }
    
    public synchronized int reserve(String deviceName, String deviceType) throws InterruptedException {
        --this.val;
        if (this.val < 0) {
            System.out.println(deviceName + " (" + deviceType + ") arrived and waiting");
            wait();
        } else {
            System.out.println(deviceName + " (" + deviceType + ") arrived");
        }
        return this.maxConnections - Math.abs(this.val);
    }
    
    public synchronized void release(String deviceName) {
        ++this.val;
        if (this.val <= 0) {
            notify();
        }
    }
}
