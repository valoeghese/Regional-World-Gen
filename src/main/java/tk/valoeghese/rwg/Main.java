package tk.valoeghese.rwg;

import javax.swing.*;
import java.awt.*;

public class Main extends JPanel {
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JFrame frame = new JFrame("Regional World Gen");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(new Main());
		frame.setSize(600, 600);
		frame.setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		for (int x = 0; x < 600; x++) {
			for (int y = 0; y < 600; y++) {

			}
		}
	}
}
