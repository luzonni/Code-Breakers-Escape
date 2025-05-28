package studio.retrozoni.activities.game.items;

import studio.retrozoni.engine.ui.command.Commands;

public class CMD extends Item {

	private final Commands command;

	public CMD(Commands command) {
		super("Command: " + command.getName());
		this.command = command;
		setSprite("commands");
	}
	
	public Commands getCMD() {
		return this.command;
	}

}
