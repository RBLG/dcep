package engine.render.misc;

import java.awt.Dimension;

import javax.swing.JFrame;
import engine.render.NotAPanel;
import engine.render.PanelAdapter;
import engine.render.engine2d.Basic2DSub;

public class DcepJFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	static public final int wdx = Basic2DSub.LDxmax * 2 + 2;
	static public final int wdy = Basic2DSub.LDymax * 2 + 2;

	public static final PanelAdapter staticadapter = new PanelAdapter();
	public final PanelAdapter adapter;

	public DcepJFrame() {
		this.adapter = staticadapter;
		this.setContentPane(this.adapter);
		//System.setProperty("sun.java2d.opengl", "true");
		// TODO taille d'ouverture en fonction de la taille d'écran
		this.getContentPane().setPreferredSize(new Dimension(wdx, wdy));
		this.pack();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		this.setTitle("Deathception");
		// try{this.setIconImage(ImageIO.read(new File("default/other/icon_2.png")));}
		// catch (IOException e) {}
		this.setVisible(true);

	}

	public void changePanel(NotAPanel pan) {
		this.adapter.linkPanel(pan);

	}

}
