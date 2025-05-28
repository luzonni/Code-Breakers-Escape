package studio.retrozoni.engine.inputs.buttons;

import studio.retrozoni.engine.tools.ActionBack;
import studio.retrozoni.engine.tools.Responsive;

public class ActionButton extends Button {

	private ActionBack actionBack;

	public ActionButton(String Name, int x_per, int y_per, Responsive parent, int size, ActionBack actionBack) {
		super(Name, x_per, y_per, parent, size);
		this.actionBack = actionBack;
	}
	
	public void action() {
		actionBack.function();
	}


}
