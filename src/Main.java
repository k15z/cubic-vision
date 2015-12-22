import java.awt.Robot;

import cubic.*;

import java.awt.AWTException;

public class Main {
	public static void main(String[] args) throws AWTException, InterruptedException {
		startCountDown(5);
		
		Thread thread1 = new Thread() {
			public void run() {
				Controller c;
				try {
					c = new Controller(new Robot());
					Agent agent = new Agent(c);

					while (true) {
						agent.step();
					}
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}
		};
		
		Thread thread2 = new Thread() {
			public void run() {
				Controller c;
				try {
					c = new Controller(new Robot());
					Agent agent = new Agent(c);

					while (true) {
						agent.step();
					}
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}
		};
		
		thread1.start();
		thread2.start();
	}

	static void startCountDown(int i) throws InterruptedException {
		while (i-- > 0) {
			System.out.println("Starting in " + i + "...");
			Thread.sleep(1000);
		}
	}
}
