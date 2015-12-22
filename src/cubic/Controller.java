package cubic;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Controller {
	final int SCALED_WIDTH = 120;
	final int SCALED_HEIGHT = 90;
	
	private Robot robot;
	private Rectangle region;
	private BufferedImage image;
	private Graphics graphics;
	
	public Controller(Robot robot) {
		this.robot = robot;
		region = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		image = new BufferedImage(SCALED_WIDTH, SCALED_HEIGHT, BufferedImage.TYPE_INT_RGB);
		graphics = image.createGraphics();
	}
	
	public int[][] capture() {
		BufferedImage screen = robot.createScreenCapture(region);
		graphics.drawImage(screen, 0, 0, SCALED_WIDTH, SCALED_HEIGHT, null);
		
		int[][] data = new int[SCALED_WIDTH][SCALED_HEIGHT];
		for (int y = 40; y < SCALED_HEIGHT - 8; y++)
			for (int x = 0; x < SCALED_WIDTH; x++) {
				Color c = new Color(image.getRGB(x, y));
				if (c.getRed() > 150 && (c.getGreen() < 150 || c.getBlue() < 150))
					data[x][y] = 1;
				if (c.getGreen() > 150 && (c.getGreen() < 150 || c.getBlue() < 150))
					data[x][y] = 1;
				if (c.getBlue() > 150 && (c.getGreen() < 150 || c.getBlue() < 150))
					data[x][y] = 1;
			}
		return data;
	}
	
	public void keyLeft(int duration) {
		robot.keyPress(KeyEvent.VK_LEFT);
		robot.delay(5 + duration);
		robot.keyRelease(KeyEvent.VK_LEFT);
	}
	
	public void keyRight(int duration) {
		robot.keyPress(KeyEvent.VK_RIGHT);
		robot.delay(5 + duration);
		robot.keyRelease(KeyEvent.VK_RIGHT);
	}
}
