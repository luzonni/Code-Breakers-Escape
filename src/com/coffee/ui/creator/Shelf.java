package com.coffee.ui.creator;

import java.awt.*;

import com.coffee.Inputs.Mouse;
import com.coffee.Inputs.Mouse_Button;
import com.coffee.graphics.FontG;
import com.coffee.items.Item;
import com.coffee.main.Engine;
import com.coffee.main.activity.Creator;
import com.coffee.main.activity.Game;
import com.coffee.main.tools.Responsive;
import com.coffee.objects.Objects;
import com.coffee.objects.entity.EntityItem;
import com.coffee.objects.tiles.Tile;

public class Shelf {

	private final String name;
	private final Objects[] default_inventory;
	private Objects[] inventory;
	private int page;
	private final Responsive bounds;
	private final int size;
	
	public Shelf(String name, Objects[] inventory, Responsive ref, int x, int y, int size) {
		this.name = name;
		this.default_inventory = inventory;
		this.size = size;
		this.bounds = Responsive.createRectangle(ref, new Rectangle(Tile.getSize(), Tile.getSize()), x, y);
	}
	
	private void setList(int page_size) {
		this.getResponsive().setSize(Tile.getSize(), Tile.getSize()*page_size);
		if(Mouse.On_Mouse(getResponsive().getBounds())) {
			int scroll = Mouse.Scrool();
			int p = page + scroll;
			if(p >= 0 && p + page_size <= default_inventory.length)
				page = p;
		}
		inventory = new Objects[page_size];
		int index = 0;
		for(int i = page; i < page + page_size; i++) {
			Objects o = default_inventory[i];
			o.setX(getResponsive().getBounds().x);
			o.setY(getResponsive().getBounds().y + index * Tile.getSize());
			inventory[index] = o;
			index++;
		}
	}

	public boolean itemOnSlots(Objects objects) {
		if(this.inventory != null)
			for(Objects o : this.inventory)
				if(o.equals(objects))
					return true;
		return false;
	}
	
	public Objects getItem() {
		setList(size);
		for(int i = 0; i < inventory.length; i++) {
			Objects o = inventory[i];
			if(Mouse.clickOn(Mouse_Button.LEFT, o.getBounds())) {
				return o;
			}
		}
		return null;
	}

	public Responsive getResponsive() {
		return this.bounds;
	}
	
	public void render(Graphics2D g) {
		if(inventory == null)
			return;
		int X = getResponsive().getBounds().x - Engine.GameScale;
		int Y = getResponsive().getBounds().y - Engine.GameScale;
		int width = getResponsive().getBounds().width + Engine.GameScale*3;
		int height = getResponsive().getBounds().height + Engine.GameScale*2;
		g.setColor(new Color(Engine.Color_Primary.getRed(), Engine.Color_Primary.getGreen(), Engine.Color_Primary.getBlue(), 60));
		g.fillRect(X, Y, width, height);
		for(int i = 0; i < inventory.length; i++) {
			Objects t = inventory[i];
			int x = (int)t.getX();
			int y = (int)t.getY();
			int w = Tile.getSize() - 4*Engine.GameScale;
			int h = Tile.getSize() - 4*Engine.GameScale;
			if(Mouse.On_Mouse(t.getBounds()) && t instanceof EntityItem ei) {
				Item item = ei.getItem();
				Engine.UI.getPopTag().setText(item.getName());
			}
			g.setColor(new Color(Engine.Color_Secondary.getRed(), Engine.Color_Secondary.getGreen(), Engine.Color_Secondary.getBlue(), 60));
			g.fillRect(x + 2*Engine.GameScale, y + 2*Engine.GameScale, w, h);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, t == Creator.getSelected() ? 1f : 0.5f));
			g.drawImage(t.getSprite(), x , y, Tile.getSize(), Tile.getSize(), null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}
		g.setColor(Engine.Color_Tertiary);
		int size = (int)(((double)inventory.length / (double)default_inventory.length)*getResponsive().getBounds().height);
		double a = ((double)size * ((double)(page) / (double)(inventory.length)));
		g.fillRect(getResponsive().getBounds().x + getResponsive().getBounds().width, (int)(getResponsive().getBounds().y + a), Engine.GameScale, size);

		renderTitle(X, Y, width, height, g);
	}

	public void renderTitle(int x, int y, int w, int h, Graphics2D g) {
		Font f = FontG.font(8*Engine.GameScale);
		String value = this.name;
		int wF = FontG.getWidth(value, f);
		int xx = x + w/2 - wF/2;
		int yy = y - Engine.GameScale*2;
		g.setFont(f);
		g.setColor(Engine.Color_Secondary);
		g.drawString(value, xx + Engine.GameScale, yy + Engine.GameScale);
		g.setColor(Engine.Color_Primary);
		g.drawString(value, xx , yy);
	}


}
