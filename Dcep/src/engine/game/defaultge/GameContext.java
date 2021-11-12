package engine.game.defaultge;

import engine.game.defaultge.StageTree.IStageTree;
import engine.game.defaultge.StageTree.StaticStageTree;
import engine.game.defaultge.level.IStageEngine;
import engine.input.IInputEngine;
import engine.render.engine2d.Basic2DEngine;
import engine.save.ISaveEngine;
import main.EventCore;

public class GameContext {

	// contexte pour les stages
	protected Basic2DEngine renderE;
	protected IInputEngine inputE;
	protected ISaveEngine saveE;
	protected DefaultGameEngine gameE;
	
	public EventCore EventE =new EventCore();

	// niveau actuel
	public IStageEngine curstage;

	// arbre des niveaux
	protected IStageTree tree = new StaticStageTree(this); // TODO

	// statistiques générale de la partie
	protected StatTracker stats = new StatTracker(); // TODO;

	// "feuille personnage" du joueur
	protected PlayerSoul playerstats = new PlayerSoul(); // TODO;

	public GameContext(Basic2DEngine nrenderE, IInputEngine ninputE, ISaveEngine nsaveE, DefaultGameEngine ngameE) {
		this.renderE = nrenderE;
		this.inputE = ninputE;
		this.saveE = nsaveE;
		this.gameE = ngameE;

	}

	public Basic2DEngine getRenderE() {
		return renderE;
	}

	public IInputEngine getInputE() {
		return inputE;
	}

	public ISaveEngine getSaveE() {
		return saveE;
	}

	public IStageEngine getCurstage() {
		return curstage;
	}

	public IStageTree getTree() {
		return tree;
	}

	public StatTracker getStats() {
		return stats;
	}

	public PlayerSoul getPlayerstats() {
		return playerstats;
	}

}
