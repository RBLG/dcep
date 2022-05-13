package engine.game.defaultge.level.type1.entity;

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
import engine.physic.basic2Dvectorial.motionprovider.BasicV2PlayerInput;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.renderable.Animation;
import engine.render.engine2d.renderable.I2DRenderable;
import engine.render.engine2d.renderable.LoopingAnimation;
import engine.render.engine2d.renderable.MapGraphicEntity;
import engine.render.misc.HitBoxBasedModifier;
import my.util.Cardinal;
import my.util.Log;
import my.util.geometry.IPoint;
import my.util.geometry.IPoint.Point;
import my.util.geometry.IRectangle;
import my.util.geometry.floats.IFloatVector;
import res.visual.FolderVideos;

public class PlayerEntityV3 implements IRoomTraverserEntity, IHasCollidable, IHasVisuals, //
	IHasInteracters, IHasAttackers, IHasAttackables, //
	IOnCollisionComputedListener {
	public static final int visualsizex = 50;
	public static final int visualsizey = 90;

	protected MapGraphicEntity<PlayerVState> visual1;
	protected HitBoxBasedModifier mod;
	public StageContext scontext;
	protected BasicV2PlayerInput motprov;
	protected MovingBox hitbox;
	public PlayerInteracter interacter = new PlayerInteracter(this);
	////////////////
	// protected Rectangle visualhitbox;
	// protected DevHollowRectangle rztest = new DevHollowRectangle(0, 0, 0, 0,
	//////////////// Color.pink);

	public PlayerEntityV3(StageContext nscontext) {
		this.scontext = nscontext;

		this.motprov = new BasicV2PlayerInput(scontext.getInputE());
		this.hitbox = new MovingBox(0, 0, 20, 17, this.motprov, null, this);
		this.mod = new HitBoxBasedModifier(this.hitbox, new IPoint.Point(0, 0), 0);

		// TODO a externaliser en fichier de conf
		EnumMap<PlayerVState, I2DRenderable> e = new EnumMap<>(PlayerVState.class);
		DrawLayer layer = DrawLayer.Room_Entities;
		e.put(PlayerVState.up_move, new LoopingAnimation(FolderVideos.player_redbox_move_up.get(), layer));
		e.put(PlayerVState.down_move, new LoopingAnimation(FolderVideos.player_redbox_move_down.get(), layer));
		e.put(PlayerVState.left_move, new LoopingAnimation(FolderVideos.player_redbox_move_left.get(), layer));
		e.put(PlayerVState.right_move, new LoopingAnimation(FolderVideos.player_redbox_move_right.get(), layer));
		e.put(PlayerVState.up_stand, new LoopingAnimation(FolderVideos.player_redbox_stand_up.get(), layer));
		e.put(PlayerVState.down_stand, new LoopingAnimation(FolderVideos.player_redbox_stand_down.get(), layer));
		e.put(PlayerVState.left_stand, new LoopingAnimation(FolderVideos.player_redbox_stand_left.get(), layer));
		e.put(PlayerVState.right_stand, new LoopingAnimation(FolderVideos.player_redbox_stand_right.get(), layer));
		this.visual1 = new MapGraphicEntity<>(new Point(0, -23), PlayerVState.down_stand, e, layer);
		this.visual1.setModifier(mod);
		// visualhitbox = new Rectangle(0, 0, 20, 17, java.awt.Color.GRAY);
		// this.visualhitbox.getPos().setModifier(mod);
	}

	@Override
	public void enter(Room room, Cardinal dir) {
		Point newco = room.state.getEntryPointFromDir(dir, this.hitbox.toInt().getWH());
		this.hitbox.applyMotion();
		this.hitbox.setXY(newco);
		// TODO deplacer pour re generaliser (a toutes les entités) la gestion du point
		// d'entree
	}

	@Override
	public void leave(Room room) {
		// TODO Auto-generated method stub

	}

	protected Cardinal lastdir = Cardinal.south;

	public void onCollisionComputed(MovingBox box) {
		PlayerVState last = visual1.getKey();
		this.mod.resetBeginning();
		IFloatVector vec = this.hitbox.getVec();
		Cardinal ndir = Cardinal.getVectorGeneralDirection(vec);
		if (ndir != null) {
			lastdir = ndir;
		}
		PlayerVState state = PlayerVState.concat(ndir != null, this.lastdir);
		this.visual1.set(state);
		if (last != state) {
			((Animation) this.visual1.get(state)).setBeginning(System.currentTimeMillis());
		}
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
	public void forEachAttacker(Consumer<IAttacker> task) {
		task.accept(attacker);
	}

	@Override
	public void forEachAttackables(Consumer<IAttackable> task) {
		task.accept(atthitbox);
	}

	@Override
	public void forEachVisuals(Consumer<I2DRenderable> task) {
		task.accept(this.visual1);
		// IRectangle rec = this.interacter.getZone();
		// rztest.set(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
		// task.accept(rztest);
		// task.accept(visualhitbox);
	}

	protected PlayerTestAttack attacker = new PlayerTestAttack(this);

	protected IAttackable atthitbox = new IAttackable() {

		@Override
		public IRectangle getZone() {
			return hitbox.toInt();
		}

		public void receiveAttack(Enum<?> group, int dmg, Object effect) { // ameliorer la gestion des groupes d'attaque
			if (group.equals(AttackGroup.any) || group.equals(AttackGroup.player)) {
				Log.e("aie");
			}
		}

	};

	public Cardinal getFacing() {
		return this.lastdir;
	}

}
