package engine.game.defaultge.level.type1.entity.actions;

import java.util.Random;
import java.util.function.Supplier;

import engine.entityfwp2.ai.action.ParamAction.ParamSupplier;
import engine.game.defaultge.level.type1.StageMap;
import engine.game.defaultge.level.type1.StagePath;
import my.util.geometry.IPoint;

public class FindRandomRoom implements ParamSupplier<StagePath> {

	protected StageMap smap;
	protected Supplier<IPoint> pos;

	public FindRandomRoom(StageMap nsmap, Supplier<IPoint> npos) {
		smap = nsmap;
		pos = npos;
	}

	protected static final Random rand = new Random();

	@Override
	public StagePath get() {
		return get(smap, pos);
	}

	public static StagePath get(StageMap smap, Supplier<IPoint> pos) {
		IPoint goal = smap.rooms.get(rand.nextInt(smap.rooms.size()));
		return smap.getPath(pos.get(), goal);
	}

}
