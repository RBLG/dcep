package engine.save;

import java.io.Serializable;

public class Settings implements Serializable {

	public static Settings settings = new Settings();

	/**
	 * 
	 */
	private static final long serialVersionUID = 9048116185405777204L;

	public final int frame_per_second = 60;

}
