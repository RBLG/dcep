package engine.game.defaultge.level.type1.interacters;

import engine.game.defaultge.level.type1.entity.PlayerV2;
import engine.physic.basic2DInteractionV2.IInteracter;
import my.util.CardinalDirection;
import my.util.geometry.IPoint;
import my.util.geometry.IVector;

public class PlayerActionInteracter implements IInteracter {

	protected PlayerV2 plr;

	public PlayerActionInteracter(PlayerV2 nplr) {
		plr = nplr;
	}

	@Override
	public IPoint getXY() {
		IPoint pt = plr.getHitbox().getCenter();
		// TODO dégager vers une fonction pour recup les multis sous forme de vecteur
		CardinalDirection dir = plr.getDirection();
		int len = 4; //TODO changer par une vraie value
		IVector vec = new IVector.Vector(dir.toXMultiplier() * len, dir.toYMultiplier() * len);
		pt = pt.getTranslated(vec);
		return pt;
	}
}
