package res.visual;

import my.util.ImageCache;

public enum Images {
	stage_bunker1_ground("stages/type1/togen/bunker1/bg1.png"), //
	stage_bunker1_wall1("stages/type1/togen/bunker1/wall1.png"), //
	stage_bunker1_lamp1("stages/type1/togen/bunker1/lamp1.png"), //
	stage_bunker1_wallsupport1("stages/type1/togen/bunker1/wall_support1.png"), //
	stage_bunker1_walltop1("stages/type1/togen/bunker1/wall1_top.png"), //
	stage_bunker1_pipe_corner_se("stages/type1/togen/bunker1/pipe/corner_se.png"), //
	stage_bunker1_pipe_detail_s("stages/type1/togen/bunker1/pipe/.png"), //
	stage_bunker1_pipe_detail2_s("stages/type1/togen/bunker1"), //
	stage_bunker1_pipe_line_e("stages/type1/togen/bunker1"), //
	stage_bunker1_pipe_line_s("stages/type1/togen/bunker1"), //
	//
	stage_default_bg("stages/type1/togen/default/bg.png"), //
	stage_default_wall1("stages/type1/togen/default/wall1.png"),//
	//

	;

	public final String path;

	private Images(String np) {
		this.path = np;
	}

	public java.awt.Image get() {
		return ImageCache.getImage(path);
	}
}
