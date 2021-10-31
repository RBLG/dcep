package engine.input.basic;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

import engine.input.IInputEngine;
import engine.loop.ILoopable;

public class BasicInputEngine extends InputListener implements IInputEngine, ILoopable {
	protected ArrayList<Integer> keyspress = new ArrayList<Integer>(5);
	// protected ArrayList<Integer> keysactive = new ArrayList<Integer>(5);
	protected boolean[] isdown = new boolean[600];

	public BasicInputEngine(JFrame frame) {
		frame.addKeyListener(this);
		frame.addMouseListener(this);
	}

	@Override
	public boolean isPressed(int key) {
		return (this.keyspress.contains(key));
	}

	@Override
	public boolean isActive(int key) {
		return (this.isdown[key]);
	}

	@Override
	public boolean areComboPressed(int key1, int key2) {
		return false; // juste il sait pas faire
	}

	@Override
	public boolean isComboActive(int key1, int key2) {
		return false; // pas fait
	}

	@Override
	public void keyPressed(KeyEvent e) {
		this.keyspress.add(e.getKeyCode());
		// this.keysactive.add(e.getKeyCode());
		this.isdown[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// this.keysactive.remove((Integer) e.getKeyCode());
		this.isdown[e.getKeyCode()] = false;

	}

	@Override
	public void go(long time) {
		this.keyspress.clear();
	}

}
