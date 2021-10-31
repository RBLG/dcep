package main;

import engine.game.IGameEngine;
import engine.game.defaultge.DefaultGameEngine;
import engine.input.IInputEngine;
import engine.input.basic.BasicInputEngine;
import engine.render.misc.DcepJFrame;

public class DcepMain {

	public static void main(String[] args) {

		DcepJFrame frame = new DcepJFrame();

		// Basic2DSub sub = new Basic2DSub(DcepJFrame.staticadapter);
		// frame.changePanel(sub);
		// Scene scene = new Scene();
		// sub.setScene(scene);
		// scene.addRenderable(new LoopingAnimation(ImageCache.getImages2("animtest"),
		// new java.awt.Point(30, 100), 10),
		// DrawLayer.Game_Entities);
		// Log.log("main", "test");
		//frame.repaint();

		IInputEngine ine = new BasicInputEngine(frame);

		IGameEngine gme = new DefaultGameEngine(frame.adapter, ine, null);
		gme.start();
	}

}
