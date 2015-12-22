package cubic;

public class Agent {
	int WIDTH, HEIGHT;
	Controller controller;
	
	public Agent(Controller controller) {
		this.controller = controller;
		int[][] data = controller.capture();
		WIDTH = data.length;
		HEIGHT = data[0].length;
	}
	
	public void step() {
		int[][] data = controller.capture();
/*		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++)
				System.out.print(data[x][y]);
			System.out.println();
		}
		for (int x = 0; x < data.length; x++)
			System.out.print("-");
		System.out.println();*/
		
		int[] h_sum = new int[WIDTH];
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++)
				h_sum[x] += data[x][y] * y * y;
/*			if (x == WIDTH/2)
				System.out.print("*");
			else
				System.out.print(Math.min(9, h_sum[x]));*/
		}
//		System.out.println();
		
		for (int dx = -3; dx <= 3; dx++) {
			if (h_sum[WIDTH/2 + dx] > 50) {
				makeMove(h_sum);
				return;
			}
		}
	}
	
	private void makeMove(int[] h_sum) {
		int best_dx = 0;
		int best_sum = 99999;
		for (int dx = -15; dx <= 15; dx++) {
			int sum = 0;
			for (int ax = -3; ax <= 3; ax++)
				sum += h_sum[WIDTH/2+dx+ax];
			if (sum < best_sum) {
				best_dx = dx;
				best_sum = sum;
			}
		}
		
		if (best_dx < 0)
			controller.keyLeft(0);
		if (best_dx > 0)
			controller.keyRight(0);
	}
}
