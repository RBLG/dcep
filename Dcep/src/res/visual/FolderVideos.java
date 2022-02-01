package res.visual;

import java.util.ArrayList;

import my.util.ImageCache;

public enum FolderVideos {
	player_redbox_move_down("stages/type1/player_redbox/down_move"), //
	player_redbox_move_up("stages/type1/player_redbox/up_move"), //
	player_redbox_move_right("stages/type1/player_redbox/right_move"), //
	player_redbox_move_left("stages/type1/player_redbox/left_move"),//
	player_redbox_stand_down("stages/type1/player_redbox/down_stand"), //
	player_redbox_stand_up("stages/type1/player_redbox/up_stand"), //
	player_redbox_stand_right("stages/type1/player_redbox/right_stand"), //
	player_redbox_stand_left("stages/type1/player_redbox/left_stand"),//

	;

	public final String path;

	private FolderVideos(String np) {
		this.path = np;
	}

	public ArrayList<java.awt.Image> get() {
		return ImageCache.getImages(path);
	}
}
