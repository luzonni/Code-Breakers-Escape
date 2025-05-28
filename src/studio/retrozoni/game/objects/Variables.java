package studio.retrozoni.game.objects;

public enum Variables {
	
	Selectable, Movable, Removable, Duplicable, Breakable, Freeze, Alive, Reanimable;
	
	public String getName() {
		return this.name().toLowerCase();
	}
	
}
