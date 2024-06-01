package app.router;

public class SemaphoreClass {
    private int val;
    
    public SemaphoreClass() {
        this.val = 0;
    }
    
    public SemaphoreClass(int val) {
        this.val = val;
    }
    
    public synchronized void reserve(String name, String type) throws InterruptedException {
        --this.val;
        if (this.val < 0) {
            System.out.println(name + " (" + type + ") arrived and waiting\n");
            wait();
        } else {
            System.out.println(name + " (" + type + ") arrived\n");
        }
    }
    
    public synchronized void release(String name, int deviceTurn) {
        ++this.val;
        if (this.val <= 0) {
            notify();
        }
        System.out.println("Connection " + deviceTurn + ": " + name + " logged out\n");
    }
}
