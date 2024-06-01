package app.router;

import java.util.Scanner;
import java.util.ArrayList;

public class NetworkClass {
    
    public void start() {
        Scanner in = new Scanner(System.in);
        ArrayList<DeviceClass> devices = new ArrayList<>();
        final int N;  // max numbers of connections a router can accepts
        final int TD; // total number of devices that wish to connect
        
        System.out.println("What is the maximum number of connections the router can accepts?");
        N = in.nextInt();
        
        System.out.println("How many number of devices wish to connect?");
        TD = in.nextInt();
        in.nextLine();
        
        RouterClass router = new RouterClass(N);
        for (int i = 0; i < TD; ++i) {
            String tdLine = in.nextLine();
            String[] tokens = tdLine.split("\\s+");
            String deviceName = tokens[0];
            String deviceType = tokens[1];
            devices.add(new DeviceClass(deviceName, deviceType, router));
        }
        
        in.close();
        
        for (int i = 0; i < TD; ++i) {
            Thread device = new Thread(devices.get(i));
            device.start();
        }
    }
}
