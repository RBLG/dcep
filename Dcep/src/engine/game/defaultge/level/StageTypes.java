package engine.game.defaultge.level;

import java.util.function.Supplier;

public enum StageTypes {
//	Side2DStageType1(() -> {
//		return new Side2DStageType1();
//	}), //
	TestStageEngine(() -> {
		return new TestStageEngine();
	}), //
	;

	public final Supplier<IStageEngine> value;

	private StageTypes(Supplier<IStageEngine> nvalue) {
		this.value = nvalue;
	}
}
