package engine.game.defaultge;

public class StatTracker {

	protected int stagesCleared;

	public StatTracker() {

	}

	//////////////////////////////////////////////// EVENTS/////////////////////////////////////////

	public void OnStageClear() {
		this.stagesCleared += 1;
	}

	/////////////////////////////////////////////// GETTER/SETTER///////////////////////////////////

	public int getStagesCleared() {
		return stagesCleared;
	}

	public void setStagesCleared(int stagesCleared) {
		this.stagesCleared = stagesCleared;
	}

}
