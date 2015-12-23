package cubic;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

/**
 * The Controller class interacts with the operating system, providing methods for
 * accessing the game state and the left/right keys. It handles the basic computer
 * vision aspects of the game and provides a standard set of methods for Agents to
 * interact with CubeRunner.
 *
 * @author kevz
 * @version 0.2
 */
public class Controller {
	final int BLOCK_RGB = 150;
	final int GAME_WIDTH = 120;
	final int GAME_HEIGHT = 60;

	private Robot robot;
	private Rectangle region;
	private BufferedImage image;
	private Graphics graphics2d;

	/**
	 * @throws AWTException if java.awt.Robot is unavailable
	 */
	public Controller() throws AWTException {
		this.robot = new Robot();
		region = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		image = new BufferedImage(GAME_WIDTH, GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
		graphics2d = image.createGraphics();
	}

	/**
	 * This method moves the mouse and clicks on the New Game button. The class
	 * should be calibrated before this method is called.
	 */
	public void click() {
		robot.mouseMove(region.x+region.width/3, region.y+5*region.height/12);
		for (int _ = 0; _ < 2; _++) {
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.delay(100);
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		}
	}

	/**
	 * Returns a 2D integer array representing the state of the game. Locations
	 * that are occupied by blocks are indicated by the number 1; the remaining
	 * spaces are filled with the number 0.
	 *
	 * @return the state of the game as a 2D integer array
	 */
	public int[][] state() {
		BufferedImage screen = robot.createScreenCapture(region);
		graphics2d.drawImage(screen, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
		int[][] data = new int[GAME_WIDTH][GAME_HEIGHT];
		for (int y = 0; y < GAME_HEIGHT; y++) {
			for (int x = 0; x < GAME_WIDTH; x++) {
				int value = 0;
				Color c = new Color(image.getRGB(x, y));
				value += (c.getRed() > BLOCK_RGB) ? 1 : 0;
				value += (c.getBlue() > BLOCK_RGB) ? 1 : 0;
				value += (c.getGreen() > BLOCK_RGB) ? 1 : 0;
				if (value == 1 || value == 2)
					data[x][y] = 1;
				if (y == GAME_HEIGHT*5/12)
					if (x == GAME_WIDTH*2/3)
						if (value == 0) {
							System.out.println("Game over...");
							System.out.println("Restarting...");
							click();
						}
			}
		}
		return data;
	}

	/**
	 * Release left/right key.
	 */
	public void stop() {
		robot.keyRelease(KeyEvent.VK_LEFT);
		robot.keyRelease(KeyEvent.VK_RIGHT);
	}
	
	/**
	 * Switch to holding left key.
	 */
	public void left() {
		robot.keyRelease(KeyEvent.VK_RIGHT);
		robot.keyPress(KeyEvent.VK_LEFT);
	}
	
	/**
	 * Switch to holding right key.
	 */
	public void right() {
		robot.keyRelease(KeyEvent.VK_LEFT);
		robot.keyPress(KeyEvent.VK_RIGHT);
	}
	
	/**
	 * @param duration of time to hold the left key
	 */
	public void left(int duration) {
		robot.keyPress(KeyEvent.VK_LEFT);
		robot.delay(1 + duration);
		robot.keyRelease(KeyEvent.VK_LEFT);
	}

	/**
	 * @param duration of time to hold the right key
	 */
	public void right(int duration) {
		robot.keyPress(KeyEvent.VK_RIGHT);
		robot.delay(1 + duration);
		robot.keyRelease(KeyEvent.VK_RIGHT);
	}

	/**
	 * This method uses a hand-coded HAAR-feature to detect the location of the
	 * CubeRunner game window on the screen. It works surprisingly well, and
	 * successfully isolates the game across a variety of screen resolutions.
	 */
	public void calibrate() {
		region = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage screen = robot.createScreenCapture(region);

		int[][] data = new int[region.width][region.height];
		for (int y = 0; y < region.height; y++)
			for (int x = 0; x < region.width; x++) {
				Color c = new Color(screen.getRGB(x, y));
				if (Math.abs(c.getRed() - c.getBlue()) > 10)
					data[x][y] = -1;
				else if (Math.abs(c.getRed() - c.getGreen()) > 10)
					data[x][y] = -1;
				else
					data[x][y] = c.getRed();
			}

		int mid_y = region.height/2;
		int min_x = region.width, max_x = 0;
		for (int y = 0; y < region.height - 50; y++) {
			for (int x = 0; x < region.width; x += 10) {
				boolean haar;
				haar = data[x][y] > 250 &&
						(data[x][y+0] == data[x][y+5]) &&
						(data[x][y+5] == data[x][y+10]) &&
						(data[x][y+10] == data[x][y+15]) &&
						(data[x][y+15] == data[x][y+20]) &&
						(data[x][y+20] == data[x][y+25]);
				haar = haar && (data[x][y+25] - data[x][y+30] > 20);
				haar = haar &&
						(data[x][y+30] > data[x][y+35]) &&
						(data[x][y+35] > data[x][y+40]) &&
						(data[x][y+40] > data[x][y+45]) &&
						(data[x][y+45] > data[x][y+50]);

				if (haar) {
					mid_y = y + 25;
					if (x < min_x)
						min_x = x;
					if (x > max_x)
						max_x = x;
				}
			}
		}
		if (max_x <= min_x)
			throw new IllegalStateException("Could not find CubeField... is it open?");

		int min_y = mid_y - 30;
		while (
				min_y >= 0 && (
					data[min_x][min_y] == data[min_x][min_y+2] ||
					data[max_x][min_y] == data[max_x][min_y+2]
				)
			  )
			min_y -= 2;
		int max_y = mid_y + 30;
		while (
				max_y < region.height && (
					data[min_x][max_y] <= data[min_x][max_y-2] ||
					data[max_x][max_y] <= data[max_x][max_y-2]
				)
			  ) {
			max_y += 2;
		}

		region.x = min_x;
		region.width = max_x - min_x;
		region.y = min_y;
		region.height = max_y - min_y;
	}
}
