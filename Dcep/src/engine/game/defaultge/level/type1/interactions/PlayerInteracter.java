package engine.game.defaultge.level.type1.interactions;

import engine.game.defaultge.level.type1.entity.PlayerEntityV3;
import engine.physic.basic2DInteractionV3.IInteractable;
import engine.physic.basic2DInteractionV3.IInteracter;
import engine.physic.basic2DInteractionV3.RelativeZone;
import my.util.Keys;
import my.util.geometry.IRectangle;

public class PlayerInteracter implements IInteracter {

	// TODO implements les points relatifs
	// ou du moins un fix temporaire

	PlayerEntityV3 plr;
	RelativeZone zone;

	public PlayerInteracter(PlayerEntityV3 nplr) {
		plr = nplr;
		zone = new RelativeZone(0, -10, 24, 19, () -> {
			return nplr.getHitbox().getCenter().toInt();
		}, () -> {
			return nplr.getFacing();
		});
	}

	@Override
	public IRectangle getZone() {
		return zone;
	}

	public boolean isTryingToInteract() {
		return this.plr.scontext.getInputE().isPressed(Keys.enter.value);
	}

	protected long doorcd = 0;

	@Override
	public void tryInteract(IInteractable fed) {
		if (!this.plr.scontext.getInputE().isPressed(Keys.enter.value)) {
			return;
		}
		if (fed instanceof DoorInteractable) {
			if (doorcd + 1000 > System.currentTimeMillis()) {
				return;
			}
			doorcd = System.currentTimeMillis();
			// TODO deplacer dans un event
			this.plr.scontext.getStageE().moveRoom(((DoorInteractable) fed).getDoor().side.toCardinal());
		}
	}
}
