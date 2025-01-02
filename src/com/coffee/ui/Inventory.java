package com.coffee.ui;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.coffee.Inputs.Mouse;
import com.coffee.items.Item;
import com.coffee.main.Engine;
import com.coffee.main.Theme;

public class Inventory {
	
	private final Item[] inventory;
	
	public Inventory(int size) {
		inventory = new Item[size];
	}
	
	public boolean put(Item item) {
		if(haveSpace()) {
			for(int i = 0; i < inventory.length; i++) {
				if(inventory[i] == null) {
					inventory[i] = item;
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean remove(Item item) {
		for(int i = inventory.length-1; i >= 0; i--) {
			if(inventory[i] == item) {
				inventory[i] = null;
				return true;
			}
		}
		return false;
	}
	
	public boolean haveSpace() {
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i] == null) {
				return true;
			}
		}
		return false;
	}
	
	public Item[] getList() {
		List<Item> list = new ArrayList<Item>();
		for(Item item : this.inventory)
			if(item != null)
				list.add(item);
		return list.toArray(new Item[0]);
	}
	
	public boolean isEmpty() {
		boolean isEmpty = true;
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i] != null) {
				isEmpty = false;
			}
		}
		return isEmpty;
	}
	
	public void tick() {
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i] != null)
				inventory[i].tick();
		}
	}
	
	public void render(Graphics2D g) {
		if(isEmpty())
			return;
		int size = inventory.length;
		int width = 16*Engine.SCALE *size;
		int x = Engine.getWidth()/2 - width/2;
		int y = Engine.getHeight()/24;
		for(int i = 0; i < size; i++) {
			g.setColor(Theme.Color_Secondary);
			g.fillRect(x + i*16*Engine.SCALE, y, 16*Engine.SCALE, 16*Engine.SCALE);
			g.setStroke(new BasicStroke(Engine.SCALE));
			g.setColor(Theme.Color_Primary);
			g.drawRect(x + i*16*Engine.SCALE, y, 16*Engine.SCALE, 16*Engine.SCALE);
			if(inventory[i] != null)
				g.drawImage(inventory[i].getSprite(), x + i*16*Engine.SCALE, y, 16*Engine.SCALE, 16*Engine.SCALE, null);
			
		}
		for(int i = 0; i < size; i++) 
			if(inventory[i] != null)
				if(Mouse.On_Mouse(new Rectangle(x + i*16*Engine.SCALE, y, 16*Engine.SCALE, 16*Engine.SCALE)))
					Engine.UI.getPopTag().setText(inventory[i].getName());
	}
	
}
