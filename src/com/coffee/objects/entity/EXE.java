package com.coffee.objects.entity;

import com.coffee.exceptions.ConsoleError;
import com.coffee.main.activity.game.Game;
import com.coffee.objects.Variables;
import com.coffee.objects.tiles.Tile;

import java.util.List;

class EXE {


    protected static void remove(Entity entity) {
        if(!entity.getVar(Variables.Removable)) {
            throw new ConsoleError("This object don't be removed");
        }
        entity.kill();
        Game.clearSelect();
    }

    protected static void move(String[] keys, Entity entity) {
        Entity nextEntity = null;
        int x = (int)entity.getX() / Tile.getSize();
        int y = (int)entity.getY() / Tile.getSize();
        int x_next = Integer.parseInt(keys[1]);
        int y_next = Integer.parseInt(keys[2]);
        int map_width = Game.getLevel().getBounds().width / Tile.getSize();
        int map_height = Game.getLevel().getBounds().height / Tile.getSize();
        if(x + x_next < 0 || x + x_next > map_width || y + y_next < 0 || y + y_next > map_height) {
            throw new ConsoleError("The next position outside the map");
        }
        Tile nextTile = Game.getLevel().getTile(x+x_next, y+y_next);
        if(nextTile.getVar(Variables.Breakable)) {
            throw new ConsoleError("The next tile is solid");
        }
        List<Entity> list = Game.getLevel().getEntities();
        for(int i = 0; i < list.size(); i++) {
            if(nextTile.centralizedWith(list.get(i))) {
                nextEntity = list.get(i);
            }
        }
        if((nextEntity != null && !nextEntity.getVar(Variables.Movable)) || !entity.getVar(Variables.Movable)) {
            throw new ConsoleError("Some thing don't be movable");
        }
        if(nextEntity != null) {
            nextEntity.setX(x * Tile.getSize());
            nextEntity.setY(y * Tile.getSize());
        }
        entity.setX((x + x_next) * Tile.getSize());
        entity.setY((y + y_next) * Tile.getSize());
    }

    protected static void freeze(Entity entity) {
        entity.setEffect(Variables.Freeze);
    }

    protected static void revive(Entity entity) {
        List<Entity> entities = Game.getLevel().getEntities();
        if(!entities.contains(entity)) {
            if(entity.getVar(Variables.Reanimable))
                Game.getLevel().addEntity(entity);
        }else {
            throw new ConsoleError("The entity selected don't be died");
        }
    }

}
