package engine.physic.basic2Dvectorial.motionprovider;

import engine.physic.basic2Dvectorial.FinalMotionVector;
import engine.physic.basic2Dvectorial.MovingBox;

public interface IMotionProvider {
	
	FinalMotionVector getNextMove();
	
	void onMotionComputed(MovingBox movingBox);
}
