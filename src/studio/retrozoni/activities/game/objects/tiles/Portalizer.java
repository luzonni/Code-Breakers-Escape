package studio.retrozoni.activities.game.objects.tiles;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.engine.tools.Timer;
import studio.retrozoni.activities.game.objects.Variables;
import studio.retrozoni.activities.game.objects.entity.Cleft;
import studio.retrozoni.activities.game.objects.entity.Entity;
import studio.retrozoni.activities.game.objects.entity.EntityTag;
import studio.retrozoni.activities.game.objects.particles.Kaboom;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Portalizer extends Tile {

    private static int index = -1;
    private final Timer timer;

    public Portalizer(int id, int x, int y) {
        super(id, x, y);
        loadSprite("portalizer");
        if(index == -1) {
            index = Engine.RAND.nextInt(getSheet().size());
            getSheet().setIndex(index);
        }
        timer = new Timer(5);
        setVar(Variables.Selectable, true);
        setVar(Variables.Movable, true);
    }

    public void tick() {
        if(!collidingWithCleft() && timer.tiktak()) {
            for(int i = 0; i < 25; i++) {
                Game.getLevel().addParticle(new Kaboom(getMiddle().x, getMiddle().y));
            }
            Game.getLevel().addEntity(Entity.Factory(EntityTag.Cleft, (int)getX(), (int)getY()));
        }
    }

    private boolean collidingWithCleft() {
        List<Entity> entities = Game.getLevel().getEntities();
        for(int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            if(entity instanceof Cleft && entity.collidingWith(this)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void render(Graphics2D g) {
        BufferedImage spriteFloor = Engine.sheetHolder.getSheet("tiles", "floot").getSprite();
        renderTile(spriteFloor, g);
        super.render(g);
    }

    @Override
    public void dispose() {

    }
}
