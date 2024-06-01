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
    
    private void login() {
        System.out.println("Connection " + this.turn + ": " + this.name + " logged in\n");
    }
    
    private void preformActivity() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("Connection " + this.turn + ": " + this.name + " performing online activity\n");
        Thread.sleep(2000);
    }
    
    private void logout() {
        System.out.println("Connection: " + this.turn + ": " + this.name + " logged out\n");
//        this.router.disconnect(name);
    }
    
    @Override
    public void run() {
        try {
            this.login();
            this.preformActivity();
            this.logout();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    
}
