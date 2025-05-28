package studio.retrozoni.engine.ui;

import studio.retrozoni.activities.menu.Menu;
import studio.retrozoni.engine.inputs.buttons.MenuButton;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.tools.ActionBack;
import studio.retrozoni.engine.tools.Responsive;
import studio.retrozoni.engine.ui.command.Console;
import studio.retrozoni.engine.ui.command.Receiver;
import studio.retrozoni.engine.ui.win.View;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UserInterface {

	private final PopTag tag;

	private Receiver RECEIVER;
	private final Console console;


	private final MenuButton menuButton;
	
	public List<View> views;

	public UserInterface() {
		console = new Console();
		tag = new PopTag();
		menuButton = new MenuButton(1, 1, Responsive.createPoint(null, 2, 2) , Engine.SCALE);
		views = new ArrayList<View>();
	}
	
	//TODO melhorar sistema de views
	public void addView(View newView) {
		if(views.contains(newView))
			return;
		newView.init();
		int x = Engine.getWidth() - newView.bounds().width - 4*Engine.SCALE;
		int y = 4*Engine.SCALE;
		newView.setLocation(x, y + (newView.bounds().height + 4*Engine.SCALE) * (views.size()));
		views.add(newView);
		
	}
	
	public void addOption(String name, ActionBack action) {
		menuButton.addOption(name, action);
	}
	
	public boolean overButtons() {
		return this.menuButton.overMenu();
	}

	public void clearOptions() {
		this.menuButton.clearOption();
	}
	
	public void clearViews() {
		views.clear();
	}
	
	public void setReceiver(Receiver receiver) {
		RECEIVER = receiver;
	}

	public Receiver getReceiver() {
		return RECEIVER;
	}
	
	public Responsive getMenuPosition() {
		return this.menuButton.getResponsive();
	}
	
	public Console getConsole() {
		return console;
	}

	public PopTag getPopTag() {
		return this.tag;
	}
	
	public synchronized void tick() {
		console.tick();
		if(!(Engine.ACTIVITY instanceof Menu))
			menuButton.function();
		for(int i = 0; i < views.size(); i++)
			views.get(i).tick();
	}
	
	public void render(Graphics2D g) {
		console.render(g);
		if(!(Engine.ACTIVITY instanceof Menu))
			menuButton.render(g);
		for(int i = 0; i < views.size(); i++) {
			views.get(i).render(g);
		}
		tag.renderName(g);
	}
	
}
