package engine.game.defaultge.level.type1.attacks;

import engine.game.defaultge.level.type1.entity.PlayerEntityV3;
import engine.physic.basic2Dattacks.IAttacker;
import my.util.Keys;
import my.util.geometry.IRectangle;

public class PlayerTestAttack implements IAttacker {

	PlayerEntityV3 plr;

	public PlayerTestAttack(PlayerEntityV3 nplr) {
		plr = nplr;
	}

	@Override
	public IRectangle getZone() {
		return plr.getHitbox().toOutInt();
	}

	@Override
	public boolean isActive() {
		return plr.scontext.getGcontext().getInputE().isPressed(Keys.e.value);
	}

	@Override
	public Enum<?> getGroup() {
		return AttackGroup.anybutplayer;
	}

	@Override
	public int getDmg() {
		return 40;
	}

	@Override
	public Object getEffect() {
		return null;
	}

}
