package cubic;

public class BasicAgent extends Agent {
	int WIDTH, HEIGHT;
	Controller controller;

	public BasicAgent(Controller controller) {
		this.controller = controller;
		WIDTH = controller.GAME_WIDTH;
		HEIGHT = controller.GAME_HEIGHT;
	}

	public void move() {
		int[][] data = controller.state();
		int[] h_sum = new int[WIDTH];
		for (int x = 0; x < WIDTH; x++)
			for (int y = 0; y < HEIGHT; y++)
				h_sum[x] += data[x][y] * y * y;
		for (int dx = -3; dx <= 3; dx++)
			if (h_sum[WIDTH/2 + dx] > 50) {
				makeMove(h_sum);
				return;
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
			controller.left(0);
		if (best_dx > 0)
			controller.right(0);
	}
}
