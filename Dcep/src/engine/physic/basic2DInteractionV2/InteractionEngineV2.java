package engine.physic.basic2DInteractionV2;

import java.util.EnumMap;

public class InteractionEngineV2 {

	protected EnumMap<Type, InteractionDualList> inters = new EnumMap<>(Type.class);

	public InteractionEngineV2() {
		for (Type type : Type.values()) {
			inters.put(type, new InteractionDualList());
		}
	}

	public void add(IInteracter nini, Type type) {
		inters.get(type).add(nini);
	}

	public void add(IFeedback nfed, Type type) {
		inters.get(type).add(nfed);
	}

	public void remove(IInteracter nini, Type type) {
		inters.get(type).remove(nini);
	}

	public void remove(IFeedback nfed, Type type) {
		inters.get(type).remove(nfed);
	}

	public static enum Type {
		attacks, others;
	}
}
