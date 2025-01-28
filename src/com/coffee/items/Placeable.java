package com.coffee.items;

import com.coffee.activity.game.Game;
import com.coffee.objects.entity.Entity;
import com.coffee.objects.entity.EntityTag;
import com.coffee.objects.tiles.Tile;

public class Placeable extends Item {

	private final EntityTag tag_entity;

	public Placeable(EntityTag tag_entity) {
		super(Entity.Factory(tag_entity, 0, 0).getClass().getSimpleName());
		setSprite(Entity.Factory(tag_entity, 0, 0).getClass().getSimpleName());
		this.tag_entity = tag_entity;
	}
	
	public boolean place(String[] values) {
		String name = values[1];
		int x = (int)Game.getPlayer().getX() + Integer.parseInt(values[2])*Tile.getSize();
		int y = (int)Game.getPlayer().getY() + Integer.parseInt(values[3])*Tile.getSize();
		Entity entity = Entity.Factory(tag_entity, x, y);
		if(entity == null || !entity.getClass().getSimpleName().equalsIgnoreCase(name))
			return false;
		Game.getLevel().addEntity(entity);
		Game.getInventory().remove(this);
		return true;
	}

}
