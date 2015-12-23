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
				if (data[x][y] > 0)
					h_sum[x] = y;
		
		if (h_sum[WIDTH/2] <= 35 && h_sum[WIDTH/2 - 1] <= 35 && h_sum[WIDTH/2 + 1] <= 35)
			controller.stop();
		else {
			for (int dx = 2; dx < 4; dx++) {
				int left_val = h_sum[WIDTH/2 - dx] + h_sum[WIDTH/2 - dx - 1] + h_sum[WIDTH/2 - dx + 1];
				if (left_val < 35*3) {
					controller.left();
					return;
				}
				int right_val = h_sum[WIDTH/2 + dx] + h_sum[WIDTH/2 + dx - 1] + h_sum[WIDTH/2 + dx + 1];
				if (right_val < 35*3) {
					controller.right();
					return;
				}
			}
		}
	}
}
