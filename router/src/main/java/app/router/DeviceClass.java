package app.router;

public class DeviceClass implements Runnable {
    private final String name;
    private final String type;
    private int turn;
    private final RouterClass router;
    
    public DeviceClass(String name, String type, RouterClass router) {
        this.name = name;
        this.type = type;
        this.router = router;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setTurn(int turn) {
        this.turn = turn;
    }
    
    private void preformActivity() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("Connection " + this.turn + ": " + this.name + " performing online activity");
        Thread.sleep(2000);
    }
    
    @Override
    public void run() {
        try {
            this.router.occupyConn(this);
            System.out.println("Connection " + this.turn + ": " + this.name + " logged in");
            this.preformActivity();
            System.out.println("Connection " + this.turn + ": " + this.name + " logged out");
            this.router.disconnect(name);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    
}
