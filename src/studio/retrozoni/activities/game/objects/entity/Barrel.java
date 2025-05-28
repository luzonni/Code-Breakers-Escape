package studio.retrozoni.activities.game.objects.entity;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.activities.game.objects.Directions;
import studio.retrozoni.activities.game.objects.Variables;
import studio.retrozoni.activities.game.objects.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Barrel extends Entity {

    private static BufferedImage[] sprite;
    private int indexAnim;

    public Barrel(int id, int x, int y) {
        super(id, x, y);
        if(sprite == null) {
            sprite = getSprite("barrel");
        }
        getValues().addInt("speed", Engine.SCALE * 2);
        setVar(Variables.Selectable, true);
        setVar(Variables.Movable, true);
        setVar(Variables.Removable, true);
        setDepth(1);
    }

    @Override
    public BufferedImage getSprite() {
        return sprite[0];
    }

    private int getSpeed() {
        return this.getValues().getInt("speed");
    }

    private boolean rolling() {
        return !getVar(Variables.Breakable);
    }

    @Override
    public void tick() {
        //TODO verificando logica!
        boolean overEntity = false;
        setVar(Variables.Breakable, getOE().getDirection() == Directions.Idle);
        List<Entity> entities = Game.getLevel().getEntities();
        for(int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if(e == this || !e.getVar(Variables.Alive)) // Mesma entidade
                continue;
            if(e.collidingWith(this))  // Caso o barril esteja sobre alguma entidade
                overEntity = true;
            if(e.getOE().getDirection() == Directions.Idle) { //Entidades sem direção são ignoradas!
                continue;
            }
            if(rolling() && e.collidingWith(this)) { //caso alguma entidade bata enquanto esta rolando
                getOE().setDirection(getOE().getReverse());
                return;
            }
            Tile tile = getOE().nextTile(e.getOE().getDirection());
            if(tile != null && tile.isSolid()) // caso o barrel não tenha para onde rolar!
                continue;
            if(e.getOE().overTile() == Game.getLevel().getTile(getMiddle().x / Tile.getSize(), getMiddle().y / Tile.getSize())) {
                getOE().setDirection(e.getOE().getDirection());
                e.getOE().setDirection(getOE().getReverse());
            }
        }
        indexAnim = rolling() ? ((getOE().getDirection() == Directions.Up || getOE().getDirection() == Directions.Down) ? 1 : 2) : 0;
        if(!getOE().slide(getSpeed()) && !overEntity) {
            stop();
        }
    }


    private void stop() {
        Tile tile = Game.getLevel().getTile(getMiddle().x / Tile.getSize(), getMiddle().y / Tile.getSize());
        setX(tile.getX());
        setY(tile.getY());
        setDirection(Directions.Idle);
    }

    @Override
    public void render(Graphics2D g) {
        renderEntity(sprite[indexAnim], g);
    }

    @Override
    public void dispose() {
        sprite = null;
    }
}
