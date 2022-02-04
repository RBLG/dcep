package engine.game.defaultge.level.type1.entity;

import static my.util.ImageCache.getImages2;

import java.util.EnumMap;
import java.util.function.Consumer;

import engine.entityfw.components.IHasCollidable;
import engine.entityfw.components.IHasInteracters;
import engine.entityfw.components.IHasVisuals;
import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.StageContext;
import engine.game.defaultge.level.type1.attacks.AttackGroup;
import engine.game.defaultge.level.type1.attacks.PlayerTestAttack;
import engine.game.defaultge.level.type1.interactions.PlayerInteracter;
import engine.physic.basic2DInteractionV3.IInteracter;
import engine.physic.basic2Dattacks.IAttackable;
import engine.physic.basic2Dattacks.IAttacker;
import engine.physic.basic2Dattacks.IHasAttackables;
import engine.physic.basic2Dattacks.IHasAttackers;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.physic.basic2Dvectorial.MovingBox.IOnCollisionComputedListener;
import engine.physic.basic2Dvectorial.MovingBox.IOnCollisionListener;
import engine.physic.basic2Dvectorial.motionprovider.BasicV2PlayerInput;
import engine.render.engine2d.I2DRenderable;
import engine.render.engine2d.renderable.Animation;
import engine.render.engine2d.renderable.LoopingAnimation;
import engine.render.engine2d.renderable.MapGraphicEntity;
import engine.render.misc.HitBoxBasedModifier;
import my.util.Cardinal;
import my.util.Log;
import my.util.geometry.IPoint;
import my.util.geometry.IPoint.Point;
import my.util.geometry.IRectangle;
import my.util.geometry.floats.IFloatVector;

public class PlayerEntityV3 implements IRoomTraverserEntity, IHasCollidable, IHasVisuals, //
		IHasInteracters, IHasAttackers, IHasAttackables, //
		IAttackable, //
		IOnCollisionComputedListener, IOnCollisionListener {
	public static final int visualsizex = 50;
	public static final int visualsizey = 90;

	protected MapGraphicEntity<PlayerVisualState> visual1;
	protected HitBoxBasedModifier mod;
	public StageContext scontext;
	protected BasicV2PlayerInput motprov;
	protected MovingBox hitbox;
	protected PlayerInteracter interacter = new PlayerInteracter(this);
	////////////////
	// protected Rectangle visualhitbox;

	public PlayerEntityV3(StageContext nscontext) {
		this.scontext = nscontext;

		this.motprov = new BasicV2PlayerInput(scontext.getInputE());
		this.hitbox = new MovingBox(0, 0, 20, 17, this.motprov, this, this);
		this.mod = new HitBoxBasedModifier(this.hitbox, new IPoint.Point(0, 0), 0);

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
		// visualhitbox = new Rectangle(0, 0, 20, 17, java.awt.Color.GRAY);
		// this.visualhitbox.getPos().setModifier(mod);
	}

	@Override
	public void enter(Room room, Cardinal dir) {
		Point newco = room.state.getDoorFront(dir, this.hitbox.toInt().getWH());
		this.hitbox.applyMotion();
		this.hitbox.setX(newco.getX());
		this.hitbox.setY(newco.getY());
		// TODO deplacer pour re generaliser (a toutes les entit�s) la gestion du point
		// d'entree
	}

	@Override
	public void leave(Room room) {
		// TODO Auto-generated method stub

	}

	protected Cardinal lastdir = Cardinal.south;

	public void onCollisionComputed(MovingBox box) {
		PlayerVisualState last = visual1.getKey();
		this.mod.resetBeginning();
		IFloatVector vec = this.hitbox.getVec();
		boolean mov = false;
		if (Math.abs(vec.getY()) > Math.abs(vec.getX())) {
			if (vec.getY() != 0) {
				mov = true;
				this.lastdir = (vec.getY() > 0) ? Cardinal.south : Cardinal.north;
			}
		} else {
			if (vec.getX() != 0) {
				mov = true;
				this.lastdir = (vec.getX() > 0) ? Cardinal.east : Cardinal.west;
			}
		}
		PlayerVisualState state = PlayerVisualState.concat(mov, this.lastdir);
		this.visual1.set(state);
		if (last != state) {
			((Animation) this.visual1.get(state)).setBeginning(System.currentTimeMillis());
		}
	}

	@Override
	public void onCollision(MovingBox box) {
		return;
	}

	public MovingBox getHitbox() {
		return this.hitbox;
	}

	@Override
	public void forEachInteracters(Consumer<IInteracter> task) {
		task.accept(interacter);
	}

	@Override
	public void forEachCollidables(Consumer<MovingBox> task) {
		task.accept(this.hitbox);

	}

	@Override
	public void forEachVisuals(Consumer<I2DRenderable> task) {
		task.accept(this.visual1);
		// task.accept(visualhitbox);
	}

	protected PlayerTestAttack attacker = new PlayerTestAttack(this);

	@Override
	public void forEachAttacker(Consumer<IAttacker> task) {
		task.accept(attacker);
	}

	@Override
	public void forEachAttackables(Consumer<IAttackable> task) {
		task.accept(this);
	}

	public void receiveAttack(Enum<?> group, int dmg, Object effect) { // ameliorer la gestion des group d'attaque
		if (group.equals(AttackGroup.any) || group.equals(AttackGroup.player)) {
			Log.e("aie");
		}
	}

	@Override
	public IRectangle getZone() {
		return this.hitbox.toInt();
	}

}
