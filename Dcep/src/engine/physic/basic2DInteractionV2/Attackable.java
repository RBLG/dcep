package engine.physic.basic2DInteractionV2;

import my.util.geometry.IRectangle;

import java.util.function.Function;
import java.util.function.Supplier;

public class Attackable implements IFeedback {

	protected Supplier<IRectangle> zone;
	protected Function<Attack,Boolean> oninteraction;

	public Attackable(Supplier<IRectangle> nzone) {
		this.zone = nzone;
	}

	@Override
	public IRectangle getZone() {
		return zone.get();
	}

	@Override
	public void acceptInteracter(IInteracter ini) {
		if (ini instanceof Attack) {
			if(oninteraction.apply((Attack) ini)) {
				((Attack)ini).confirmHit();
			}
		}
	}

}
