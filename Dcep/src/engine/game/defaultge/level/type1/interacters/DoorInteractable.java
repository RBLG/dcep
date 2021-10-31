package engine.game.defaultge.level.type1.interacters;

import engine.physic.basic2DInteractionV2.IFeedback;
import engine.physic.basic2DInteractionV2.IInteracter;
import my.util.geometry.IRectangle;

public class DoorInteractable implements IFeedback {

	protected IRectangle rec;

	public DoorInteractable(IRectangle nrec) {
		this.rec = nrec;
	}

	@Override
	public IRectangle getZone() {
		return rec;
	}

	@Override
	public void acceptInteracter(IInteracter ini) {
		if (ini instanceof PlayerActionInteracter) {
			// TODO quand event fait:
			// ajouter un event de fin de tour pour initier le changement de salle
		}
	}

}
