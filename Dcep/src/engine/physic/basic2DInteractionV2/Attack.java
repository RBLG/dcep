package engine.physic.basic2DInteractionV2;

import my.util.geometry.IPoint;

//TODO finir après entityV2
public class Attack implements IInteracter { // ,IEntityV2

	protected IPoint pt;
	protected boolean active = true;

	public Attack(IPoint npt) {
		pt = npt;
	}

	@Override
	public IPoint getXY() {
		return pt;
	}

	public void confirmHit() {
		this.active = false;
	}

}
