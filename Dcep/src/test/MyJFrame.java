package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MyJFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public MyJFrame() {
		// default panel for visual feedback
		JPanel pan = new JPanel();
		pan.setBackground(Color.green);
		this.setContentPane(pan);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // TODO faire que ça save jsp
		this.setSize(400, 400);
		// other unrelated configuration stuff
		// ...

		this.setVisible(true);

	}

	public void switchPanel(JPanel pan) {
		this.setContentPane(pan);
		// pan.setVisible(true); doesnt work
		Dimension dim = this.getSize();
		int y = dim.height;
		int x = dim.width;
		this.setSize(x+1,y);
		this.setSize(x, y);
	}

	public static void main(String[] args) {

		MyJFrame frame = new MyJFrame();

		// code that make my custom jpanel

		JPanel mypan = new MyJPanel();
		
		// there i have an IO operation to get an image, without this delay this code work just fine
		// so i put this thread.sleep to simulate this delay
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
		// end of the replacement

		frame.switchPanel(mypan);

		while (true) {
			// this repaint wont ever work until i change the window size manually
			mypan.repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}

	// nested class for the sake of the example
	public static class MyJPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		public Point pos = new Point(0, 0);

		public synchronized void paintComponent(Graphics g) {
			super.paintComponent(g);
			pos.x += 1;
			g.fillRect(pos.x, pos.y, 10, 10);
		}
	}

}