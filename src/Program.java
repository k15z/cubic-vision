import cubic.*;
import java.awt.*;

public class Program {
	public static void main(String[] args) {
		System.out.println("Welcome to CubicVision v0.2!");
		System.out.println("--------------------------------------------------");
		try {
			for (int t = 5; t > 0; t--) {
				System.out.println("Launching program in " + t + "...");
				Thread.sleep(1000);
			}

			System.out.println("Initializing controller...");
			Controller c = new Controller();

			System.out.println("Calibrating controller...");
			c.calibrate();

			System.out.println("Creating agent...");
			Agent a = new BasicAgent(c);

			System.out.println("Let the games begin...");
			c.click();
			while (true)
				a.move();
		} catch (AWTException e) {
			System.out.println("ERROR: java.awt.Robot is not enabled.");
		} catch (IllegalStateException e) {
			System.out.println("ERROR: CubeRunner window not found.");
		} catch (InterruptedException e) {
			System.out.println("ERROR: This is a bizarre error.");
		}
	}
}
