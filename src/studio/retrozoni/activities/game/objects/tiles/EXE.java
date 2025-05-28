package studio.retrozoni.activities.game.objects.tiles;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.exceptions.ConsoleError;
import studio.retrozoni.activities.game.objects.Variables;
import studio.retrozoni.activities.game.objects.entity.Entity;

class EXE {

    protected static void move(String[] keys, Tile tile) {
        Tile[] map = Game.getLevel().getMap();
        int w = Game.getLevel().getBounds().width / Tile.getSize();
        int h = Game.getLevel().getBounds().height / Tile.getSize();
        int x_next = Integer.parseInt(keys[1]);
        int y_next = Integer.parseInt(keys[2])*-1;
        int xIndex = (int)tile.getX() / Tile.getSize();
        int yIndex = (int)tile.getY() / Tile.getSize();
        if(xIndex+x_next < 0 || xIndex+x_next > w || yIndex+y_next < 0 || yIndex+y_next > h)
            throw new ConsoleError("Out position");
        Tile nextTile = map[(xIndex+x_next)+(yIndex+y_next)*w];
        if(!tile.getVar(Variables.Movable) || !nextTile.getVar(Variables.Movable)) {
            throw new ConsoleError("The box could not be moved to this position");
        }
        for(Entity e : Game.getLevel().getEntities())
            if(nextTile.centralizedWith(e)) {
                throw new ConsoleError("The position has an entity");
            }
        tile.setX(tile.getX()+x_next*Tile.getSize());
        tile.setY(tile.getY()+y_next*Tile.getSize());
        nextTile.setX(nextTile.getX()-x_next*Tile.getSize());
        nextTile.setY(nextTile.getY()-y_next*Tile.getSize());
        Game.getLevel().getMap()[xIndex+yIndex*w] = nextTile;
        Game.getLevel().getMap()[(xIndex+x_next)+(yIndex+y_next)*w] = tile;
    }

    protected static void remove(Tile tile) {
        int w = Game.getLevel().getBounds().width / Tile.getSize();
        int xIndex = (int)tile.getX() / Tile.getSize();
        int yIndex = (int)tile.getY() / Tile.getSize();
        Game.getLevel().getMap()[xIndex+yIndex*w] = Tile.Factory(TileTag.Floor, (int)tile.getX(), (int)tile.getY());
        Game.getLevel().clearSelect();
    }

}
