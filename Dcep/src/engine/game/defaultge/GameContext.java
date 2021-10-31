package engine.game.defaultge;

import engine.game.defaultge.StageTree.IStageTree;
import engine.game.defaultge.StageTree.StaticStageTree;
import engine.game.defaultge.level.IStageEngine;
import engine.input.IInputEngine;
import engine.render.PanelAdapter;
import engine.save.ISaveEngine;

public class GameContext {

	// contexte pour les stages
	protected PanelAdapter adapter;
	protected IInputEngine inputE;
	protected ISaveEngine saveE;
	protected DefaultGameEngine gameE;

	// niveau actuel
	protected IStageEngine curstage;

	// arbre des niveaux
	protected IStageTree tree = new StaticStageTree(this); // TODO

	// statistiques générale de la partie
	protected StatTracker stats = new StatTracker(); // TODO;

	// "feuille personnage" du joueur
	protected PlayerSoul playerstats = new PlayerSoul(); // TODO;

	// autre
	// protected IRenderEngine DefaultRenderEngine;

	public GameContext(PanelAdapter nadapter, IInputEngine ninputE, ISaveEngine nsaveE, DefaultGameEngine ngameE) {
		this.adapter = nadapter;
		this.inputE = ninputE;
		this.saveE = nsaveE;
		this.gameE = ngameE;

	}

	public PanelAdapter getAdapter() {
		return adapter;
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
