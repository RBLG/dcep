package engine.game.defaultge.level.type1.entity;

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
import engine.render.engine2d.Scene;
import engine.render.engine2d.renderable.MapGraphicEntity;
import engine.render.engine2d.renderable.I2DRenderable;
import engine.render.engine2d.renderable.LoopingAnimation;
import engine.render.misc.HitBoxBasedModifier;
import engine.save.room.type1.Side;
import static my.util.ImageCache.*;
import my.util.CardinalDirection;
import my.util.Geometry;
import my.util.Keys;

//@Deprecated
public class PlayerEntity extends EntityV1 implements PlayerInputV2Feedbackable {
	public static final int visualsizex = 50;
	public static final int visualsizey = 90;
	protected Scene parentscene;
	protected MapGraphicEntity<PlayerVisualState> visual1;
	protected HitBoxBasedModifier mod;
	protected StageContext scontext;
	protected RelativeInteractionObserver interobs;
	protected BasicV2PlayerInput motprov;

	public PlayerEntity(StageContext nscontext) {
		this.scontext = nscontext;
		this.parentscene = scontext.getStageE().getCurrentScene();

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
		e.put(PlayerVisualState.right_stand, new LoopingAnimation(getImages2("stages/type1/player_redbox/right_stand")));
		this.visual1 = new MapGraphicEntity<>(new java.awt.Point(0, -23), PlayerVisualState.down_stand, e);
		this.visual1.getPos().setModifier(mod);

		String[] infos = new String[] { "player" };
		this.interobs = new RelativeInteractionObserver(this::interobsTriger, 0, 0, 20, 17, hitbox, infos);
	}

	@Override
	public MotionVector getVector() {
		return this.hitbox.getVec();
	}

	@Override
	public MovingBox getHitbox() {
		return this.hitbox;
	}

	public RelativeInteractionObserver getActionBox() {
		return interobs;
	}

	public void setPos(int nx, int ny) {
		this.hitbox.setX(nx);
		this.hitbox.setY(ny);
	}

	@Override
	public void loadVisual() {
		// this.parentscene.addRenderable(this.interactionVisualhelp,
		// DrawLayer.Game_Entities);
		this.parentscene.addRenderable(this.visual1, DrawLayer.Room_Entities);

	}

	public void setScene(Scene scene) {
		this.parentscene.remove(this.visual1, DrawLayer.Room_Entities);
		this.parentscene = scene;
	}

	protected long doorcd = 0;

	// TODO remplacer
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

	// protected Rectangle interactionVisualhelp = new Rectangle(0, 0, 30, 30,
	// Color.green);

	@Override
	public void think() {
		// TODO gerer les attaques et tout (faire un type de classe Brain?)
		// interactionVisualhelp.getPos().setPos(new
		// java.awt.Point(this.interobs.getX(), this.interobs.getY()));
	}

//	public void doGuidedMove(long duration, int npx, int npy) {
//		this.hitbox.setMotionProvider(new TimedProvider(duration, npx, npy));
//
//	}

//	public void setGuided(boolean guided) {
//		if (guided) {
//			this.hitbox.setMotionProvider(new TimedProvider(0, 0, 0));
//		} else {
//			this.hitbox.setMotionProvider(this.motprov);
//		}
//		this.hitbox.setChaotic(!guided);
//	}

	@Override
	public void load(Room room) {
		// TODO Auto-generated method stub
	
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

}
