package tk.valoeghese.rwg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class Main extends JPanel {
	public static void main(String[] args) throws Exception {
		// gui
		Main panel = new Main(new WorldGen(new Random().nextLong()));

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JFrame frame = new JFrame("Regional World Gen");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					panel.mode = (panel.mode + 1) % 3;
					panel.repaint();
				}
			}
		});
				frame.setSize(600, 600);
		frame.setVisible(true);
	}

	Main(WorldGen worldGen) {
		this.worldGen = worldGen;
	}

	private final WorldGen worldGen;
	private int mode = 0;

	@Override
	public void paint(Graphics g) {
		int mtn = rgb(170, 170, 170);
		int land = rgb(0, 200, 20);
		int coast = rgb(230, 230, 120);
		int sea = rgb(0, 0, 130);

		BufferedImage image = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < 600; x++) {
			for (int y = 0; y < 600; y++) {
				if (this.mode == 2) {
					double[] sample = this.worldGen.sampleRaw(x * 30, y * 30); // zoomed out on 1:30 scale

					int greyscale = (int) Maths.clampMap(sample[0], 0, 1, 0, 255);
					int rmul = sample[1] > 0 ? 1 : 0;
					image.setRGB(x, y, rgb(greyscale * rmul, greyscale * (1 - rmul), greyscale * (1 - rmul)));
				}
				else if (this.mode == 1) {
					double sample = this.worldGen.sample(x * 30, y * 30); // zoomed out on 1:30 scale
					int greyscale = (int) Maths.clampMap(sample, -3, 3, 0, 255);
					image.setRGB(x, y, rgb(greyscale, greyscale, greyscale));
				}
				else {
					double sample = this.worldGen.sample(x * 30, y * 30); // zoomed out on 1:30 scale

					if (sample >= 3.5) image.setRGB(x, y, mtn);
					else if (sample >= 1) image.setRGB(x, y, land);
					else if (sample > 0) image.setRGB(x, y, coast);
					else image.setRGB(x, y, sea);
				}
			}
		}

		g.drawImage(image, 0, 0, this);
	}

	private int rgb(int r, int g, int b) {
		return (0xFF << 24) | (r << 16) | (g << 8) | b;
	}
}
