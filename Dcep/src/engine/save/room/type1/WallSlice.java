package engine.save.room.type1;

import java.io.Serializable;

import engine.physic.basic2Dvectorial.ISegment;
import my.util.geometry.IRectangle;

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

	public static WallSlice newScaled(WallSlice sli, float sc) {
		return new WallSlice(//
				scale(sli.top, sc), //
				scale(sli.bottom, sc), //
				scale(sli.start, sc), //
				scale(sli.end + 1, sc) - 1, //
				sli.color);
	}

	private static int scale(int val, float scale) {
		return (int) (val * scale);
	}

	private static ISegment scale(ISegment seg, float scale) {
		return seg.getScaledCopy(scale);
	}

	public int getLength() {
		return end - start;
	}

	public int getHeight() {
		return bottom.getY() - top.getY();
	}

	public IRectangle getZone() {
		return new IRectangle.Rectangle(start, end, getLength(), getHeight());
	}
}
