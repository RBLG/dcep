package engine.game.defaultge.level.type1.entity;

import static my.util.ImageCache.getImages2;

import java.util.ArrayList;
import java.util.EnumMap;

import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.StageContext;
import engine.physic.basic2DInteraction.InteractionObserver;
import engine.physic.basic2DInteraction.RelativeInteractionObserver;
import engine.physic.basic2Dvectorial.MotionVector;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.physic.basic2Dvectorial.Point;
import engine.physic.basic2Dvectorial.motionprovider.BasicV2PlayerInput;
import engine.physic.basic2Dvectorial.motionprovider.BasicV2PlayerInput.PlayerInputV2Feedbackable;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.renderable.I2DRenderable;
import engine.render.engine2d.renderable.LoopingAnimation;
import engine.render.engine2d.renderable.MapGraphicEntity;
import engine.render.misc.HitBoxBasedModifier;
import engine.save.room.type1.Side;
import my.util.CardinalDirection;
import my.util.Geometry;
import my.util.Keys;

public class PlayerV2 implements IEntityV2, PlayerInputV2Feedbackable {

	public static final int visualsizex = 50;
	public static final int visualsizey = 90;

	protected MapGraphicEntity<PlayerVisualState> visual1;
	protected HitBoxBasedModifier mod;
	protected StageContext scontext;
	protected BasicV2PlayerInput motprov;
	protected MovingBox hitbox;

	@Deprecated
	protected RelativeInteractionObserver interobs;

	public PlayerV2(StageContext nscontext) {
		this.scontext = nscontext;

		this.motprov = new BasicV2PlayerInput(scontext.getInputE(), this);
		MotionVector vec = new MotionVector(0, 0);
		this.hitbox = new MovingBox(0, 0, 20, 17, vec, this.motprov);
		this.mod = new HitBoxBasedModifier(this.hitbox, new Point(0, 0), 0);
		// TODO a externaliser en fichier de conf
		EnumMap<PlayerVisualState, I2DRenderable> e = new EnumMap<>(PlayerVisualState.class);
		e.put(PlayerVisualState.up_move, new LoopingAnimation(getImages2("stages/type1/player_redbox/up_move")));
		e.put(PlayerVisualState.down_move, new LoopingAnimation(getImages2("stages/type1/player_redbox/down_move")));
		e.put(PlayerVisualState.left_move, new LoopingAnimation(getImages2("stages/type1/player_redbox/left_move")));
		e.put(PlayerVisualState.right_move, new LoopingAnimation(getImages2("stages/type1/player_redbox/right_move")));
		e.put(PlayerVisualState.up_stand, new LoopingAnimation(getImages2("stages/type1/player_redbox/up_stand")));
		e.put(PlayerVisualState.down_stand, new LoopingAnimation(getImages2("stages/type1/player_redbox/down_stand")));
		e.put(PlayerVisualState.left_stand, new LoopingAnimation(getImages2("stages/type1/player_redbox/left_stand")));
		e.put(PlayerVisualState.right_stand,
				new LoopingAnimation(getImages2("stages/type1/player_redbox/right_stand")));
		this.visual1 = new MapGraphicEntity<>(new java.awt.Point(0, -23), PlayerVisualState.down_stand, e);
		this.visual1.getPos().setModifier(mod);

		String[] infos = new String[] { "player" };
		this.interobs = new RelativeInteractionObserver(this::interobsTriger, 0, 0, 20, 17, hitbox, infos);
	}

	public MotionVector getVector() {
		return this.hitbox.getVec();
	}

	public MovingBox getHitbox() {
		return this.hitbox;
	}

	@Override
	public ArrayList<MovingBox> getHitboxes() {
		ArrayList<MovingBox> rtn = new ArrayList<>();
		rtn.add(hitbox);
		return rtn;
	}

	@Override
	public void load(Room room) {
		room.getScene().addRenderable(this.visual1, DrawLayer.Room_Entities);

	}

	@Override
	public void unload(Room room) {
		room.getScene().remove(this.visual1, DrawLayer.Room_Entities);

	}

	@Override
	public void enterRoom(Room room, int nx, int ny) {
		this.load(room);
		this.hitbox.setX(nx);
		this.hitbox.setY(ny);
	}

	@Override
	public void leaveRoom(Room room) {
		this.unload(room);

	}

	@Override
	public void think() {

	}

	protected CardinalDirection lastdir = CardinalDirection.south;

	public void onMotionComputed() {
		this.mod.resetBeginning();
		MotionVector vec = this.hitbox.getVec();
		boolean mov = false;
		if (Geometry.abs(vec.getY()) > Geometry.abs(vec.getX())) {
			if (vec.getY() != 0) {
				mov = true;
				this.lastdir = (vec.getY() > 0) ? CardinalDirection.south : CardinalDirection.north;
			}
		} else {
			if (vec.getX() != 0) {
				mov = true;
				this.lastdir = (vec.getX() > 0) ? CardinalDirection.east : CardinalDirection.west;
			}
		}
		this.visual1.set(PlayerVisualState.concat(mov, this.lastdir));
	}

	@Deprecated
	protected long doorcd = 0;

	@Deprecated
	public void interobsTriger(InteractionObserver e) {
		if (!this.scontext.getInputE().isPressed(Keys.enter.value)) {
			return;
		}

		if (doorcd + 1000 > System.currentTimeMillis()) {
			return;
		}
		doorcd = System.currentTimeMillis();

		String[] infos = e.getInfos();
		// Log.log(this, "plrActTrigger:" + infos[0]);
		if (infos[0] == "door" && infos.length == 2) {
			Side nside = null;
			for (Side side : Side.values()) {
				if (side.toString() == infos[1]) {
					nside = side;
				}
			}
			this.scontext.getStageE().moveRoom(nside.toCardinal());
		}

	}

	public CardinalDirection getDirection() {
		// TODO Auto-generated method stub
		return null;
	}

}
