package connectfour.game.agent;

public class NoOpAction extends DynamicAction {

	public static final NoOpAction NO_OP = new NoOpAction();

	//
	// START-Action
	public boolean isNoOp() {
		return true;
	}

	// END-Action
	//

	private NoOpAction() {
		super("NoOp");
	}
}
