package engine.physic.basic2DvectorialV2;

import java.io.Serializable;
import engine.physic.basic2Dvectorial.FinalPoint;
import engine.physic.basic2Dvectorial.MotionVector;
import my.util.CardinalDirection;

public interface ISegment extends Serializable {
	public boolean isIntersecting(FinalPoint pt, FinalPoint pt2);

	public boolean impact(FinalPoint pt, MotionVector vec);

	public boolean impact(FinalPoint pt, FinalPoint pt2, MotionVector vec, CardinalDirection dir);

	public int getX();

	public int getY();

	public int getX2();

	public int getY2();

	public int getColor();

}
