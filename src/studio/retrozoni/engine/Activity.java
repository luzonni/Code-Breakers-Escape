package studio.retrozoni.engine;

import studio.retrozoni.engine.ui.command.Receiver;

import java.awt.*;

public interface Activity extends Receiver {
	
	public void enter();
	
	public void tick();
	
	public void render(Graphics2D g);
	
	public void dispose();

}
