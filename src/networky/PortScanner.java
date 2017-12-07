package networky;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PortScanner {
	
	private String ip;
	private ObservableList<String> portList;
	private Button scanBtn;
	private Label scanResult;
	
	public PortScanner(String ip, ObservableList<String> portList, Button scanBtn, Label scanResult) {
		this.ip = ip;
		this.portList = portList;
		this.scanBtn = scanBtn;
		this.scanResult = scanResult;
	}
	
	public void go(int startPort, int endPort, int timeout) {
		System.out.println("range scan on " + ip + ", ports" + startPort + "-" + endPort + ", " + timeout + "ms");
		new Thread( new Runnable() {
			public void run() {
				for (int i = startPort; i <= endPort; i++) {
					try {
						Socket sock = new Socket();
						InetSocketAddress ipAddress = new InetSocketAddress(ip, i);
						
						System.out.print(".");
						sock.connect(ipAddress, timeout);
						
						if (sock.isConnected()) {
							System.out.println("\nO: " + i);
							portList.add(Integer.toString(i)); // TODO: throws error
						}
						
						sock.close();
					}
					catch (IOException e) {
						// socket failed to connect, oh well
					}
				}
				System.out.println("\ndone.");
				scanBtn.setDisable(false);
				scanResult.setText("range scan of ports " + startPort + "-" + endPort + " finished."); // TODO: throws error
			}
		}).start();
		
	// JAVAFX DOES NOT PERMIT MODIFYING THE MAIN THREAD FROM ANOTHER THREAD. 
	// I DO NOT KNOW HOW TO STOP IT THROWING ERRORS BECAUSE OF THIS.
	}
	
	public void go(int port, int timeout) {
		System.out.println("pinpoint scan on " + ip + ", port" + port + ", " + timeout + "ms");
		try {
			Socket sock = new Socket();
			InetSocketAddress ipAddress = new InetSocketAddress(ip, port);
			
			sock.connect(ipAddress, timeout);
			
			if (sock.isConnected()) {
				System.out.println("\nO: " + port);
				portList.add(Integer.toString(port));
				scanResult.setText("port " + port + " is OPEN.");
			}
			
			sock.close();
		}
		catch (IOException e) {
			// socket failed to connect, oh well
			scanResult.setText("port " + port + " is CLOSED.");
		}
		System.out.println("\ndone.");
		scanBtn.setDisable(false);
	}
}
