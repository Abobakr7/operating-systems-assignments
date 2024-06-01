package app.router;

public class RouterClass {
    private final SemaphoreClass semaphore;
    private final int maxConnections;
    
    public RouterClass(int maxConnections) {
        this.maxConnections = maxConnections;
        this.semaphore = new SemaphoreClass(maxConnections);
    }
    
    public void occupyConn(DeviceClass device) throws InterruptedException {
        String name = device.getName(), type = device.getType();
        int turn = this.semaphore.reserve(name, type);
        device.setTurn(turn);
        System.out.println("Connection " + turn + ": " + name + " Occupied");
    }
    
    public void disconnect(String deviceName) {
        this.semaphore.release(deviceName);
    }
}
