package engine.physic.basic2DInteractionV2;

import my.util.geometry.IRectangle;

public interface IFeedback {

	IRectangle getZone();

	void acceptInteracter(IInteracter ini);

	default boolean IsInteracting(IInteracter ini) {
		return this.getZone().isContaining(ini.getXY());
	}

}
