package engine.physic.basic2DInteraction;

import java.util.ArrayList;
import java.util.EnumMap;

public class InteractionEngine {

	public static enum InteractionLayer {
		entattacks, playerattacks, action
	}

	protected EnumMap<InteractionLayer, ArrayList<RelativeInteractionObserver>> movings;
	protected EnumMap<InteractionLayer, ArrayList<InteractionObserver>> stills;

	public InteractionEngine() {
		this.movings = new EnumMap<>(InteractionLayer.class);
		this.stills = new EnumMap<>(InteractionLayer.class);
		for (InteractionLayer lay : InteractionLayer.values()) {
			movings.put(lay, new ArrayList<>());
			stills.put(lay, new ArrayList<>());
		}

	}

	public void go() {
		for (InteractionLayer lay : InteractionLayer.values()) {
			for (RelativeInteractionObserver mov : movings.get(lay)) {
				for (InteractionObserver sti : stills.get(lay)) {
					sti.checkOverlap(mov);
				}
			}
		}
	}

	public void add(InteractionLayer lay, InteractionObserver obs) {
		this.stills.get(lay).add(obs);
	}

	public void add(InteractionLayer lay, RelativeInteractionObserver obs) {
		this.movings.get(lay).add(obs);
	}

}
