package com.coffee.objects;

public enum Variables {
	
	Selectable, Movable, Removeble, Duplicable, Pullable, Ollarable, Generable, Modifiable, Breakable, Freeze, Alive;
	
	public String getName() {
		return this.name().toLowerCase();
	}
	
}
