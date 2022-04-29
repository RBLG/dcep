package engine.game.defaultge.level.type1;

import engine.entityfw.EntityWackSystem;
import engine.entityfw.subsystems.EntitySubSystem;
import engine.game.defaultge.level.type1.interactions.IStageWalker;

public class StageEntityHolder {

	protected EntityWackSystem ews = new EntityWackSystem();

	public StageEntityHolder() {
		ews.add(new RoomTraverserESS());
	}

	public void enter(IStageWalker entity) {
		this.ews.add(entity);
	}

	public static class RoomTraverserESS extends EntitySubSystem<IStageWalker> {

		public RoomTraverserESS() {
			super(IStageWalker.class);
		}

		@Override
		public void update() {
			this.components.forEach((comp) -> {
				comp.outsideThink();
			});
		}
	}
}
