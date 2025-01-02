package com.coffee.items;

import com.coffee.ui.command.Commands;

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
