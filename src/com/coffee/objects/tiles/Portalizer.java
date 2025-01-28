package com.coffee.objects.tiles;

import com.coffee.main.Engine;
import com.coffee.main.Theme;
import com.coffee.activity.game.Game;
import com.coffee.main.tools.Timer;
import com.coffee.objects.Variables;
import com.coffee.objects.entity.Cleft;
import com.coffee.objects.entity.Entity;
import com.coffee.objects.entity.EntityTag;
import com.coffee.objects.particles.Kaboom;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Portalizer extends Tile {

    private static BufferedImage[] sprites;
    private static int index;
    private final Timer timer;

    public Portalizer(int id, int x, int y) {
        super(id, x, y);
        if(sprites == null) {
            sprites = getSprite("portalizer", Theme.Primary);
            index = Engine.RAND.nextInt(sprites.length);
        }
        timer = new Timer(5);
        setVar(Variables.Selectable, true);
        setVar(Variables.Movable, true);
    }

    @Override
    public BufferedImage getSprite() {
        return sprites[index];
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
        renderTile(Floor.sprite[Floor.index], g);
        super.render(g);
    }

    @Override
    public void dispose() {
        sprites = null;
    }
}
