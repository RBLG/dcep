package engine.save.room.type1;

import java.io.Serializable;

import engine.physic.basic2DvectorialV2.ISegment;

public class WallSlice implements Serializable {
	private static final long serialVersionUID = 6713305338311582291L;
	public final ISegment top;
	public final ISegment bottom;
	public final int start;
	public final int end;
	public final int color;

	public WallSlice(ISegment ntop, ISegment nbot, int nstart, int nend, int ncolor) {
		top = ntop;
		bottom = nbot;
		start = nstart;
		end = nend;
		color = ncolor;
	}
}