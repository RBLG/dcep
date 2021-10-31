package engine.physic.basic2DInteractionV2;

import java.util.ArrayList;

public class InteractionDualList {

	protected ArrayList<IInteracter> inits = new ArrayList<>();
	protected ArrayList<IFeedback> feds = new ArrayList<>();

	/***
	 * déclenche les interactions entre inits et feds
	 */
	public void go() {
		for (IInteracter ini : inits) {
			this.interact(ini);
		}
	}

	public void interact(IInteracter ini) {
		for (IFeedback fed : feds) {
			if (fed.IsInteracting(ini)) {
				fed.acceptInteracter(ini);
			}
		}
	}

	public void add(IFeedback nfed) {
		this.feds.add(nfed);
	}

	public void add(IInteracter nini) {
		this.inits.add(nini);
	}
	
	public void remove(IFeedback nfed) {
		this.feds.remove(nfed);
	}

	public void remove(IInteracter nini) {
		this.inits.remove(nini);
	}

}
