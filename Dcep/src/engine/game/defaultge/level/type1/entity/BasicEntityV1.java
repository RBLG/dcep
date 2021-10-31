package engine.game.defaultge.level.type1.entity;

import java.util.function.Consumer;

import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.StageType1;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.physic.basic2Dvectorial.Point;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.renderable.MapGraphicEntity;
import engine.render.misc.HitBoxBasedModifier;
import my.util.Timing;

public class BasicEntityV1<VISUALSTATE extends Enum<VISUALSTATE>> extends EntityV1 {

	protected Room proom;
	protected HitBoxBasedModifier mod;
	protected StageType1 stage;
	protected IBrain brain;
	protected MapGraphicEntity<VISUALSTATE> visual;

	protected BasicEntityV1() {

	}

	public BasicEntityV1(StageType1 nstage, IBrain nbrain, MapGraphicEntity<VISUALSTATE> nvisual, MovingBox nbox) {
		onCreate(nstage, nbrain, nvisual, nbox);
	}

	public void onCreate(StageType1 nstage, IBrain nbrain, MapGraphicEntity<VISUALSTATE> nvisual, MovingBox nbox) {
		stage = nstage;
		visual = nvisual;
		brain = nbrain;
		hitbox = nbox;

		Consumer<Timing> c = (Timing t) -> {
			if (t.equals(Timing.after)) {
				this.mod.resetBeginning();
			}
		};
		nbox.setOBs(c);
		mod = new HitBoxBasedModifier(this.hitbox, new Point(0, 0), 0);
		this.visual.getPos().setModifier(mod);
	}

	@Override
	public void loadVisual() {
		if (proom != null) {
			this.proom.getScene().addRenderable(visual, DrawLayer.Room_Entities);
		}
	}

	public void changeRoom(Room nroom) {
		if (proom != null) {
			proom.getScene().remove(visual, DrawLayer.Room_Entities);
		}
		proom = nroom;
	}

	@Override
	public void setPos(int nx, int ny) {
		this.hitbox.setX(nx);
		this.hitbox.setY(ny);
	}

	@Override
	public void think() {
		this.brain.think();
	}

	@Override
	public void load(Room nroom) {
		this.changeRoom(nroom);
		this.loadVisual();
	}
}
