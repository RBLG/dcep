package main;

import engine.game.IGameEngine;
import engine.game.defaultge.DefaultGameEngine;
import engine.input.BasicInputEngine;
import engine.input.IInputEngine;
import engine.render.engine2d.Basic2DEngine;
import engine.render.misc.DcepJFrame;

public class DcepMain {

	public static void main(String[] args) {

		DcepJFrame frame = new DcepJFrame();

		Basic2DEngine rne = new Basic2DEngine(DcepJFrame.staticadapter);
		rne.load(frame.adapter);
		IInputEngine ine = new BasicInputEngine(frame);

		EventExecuter vis = new EventExecuter((time) -> {
			rne.updateScreen();
			return false;
		});
		vis.start(1000 / 100);

		IGameEngine gme = new DefaultGameEngine(rne, ine, null);
		gme.start();
	}

}
