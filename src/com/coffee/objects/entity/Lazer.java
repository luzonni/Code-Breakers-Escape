package com.coffee.objects.entity;

import com.coffee.main.Engine;
import com.coffee.main.Geometry;
import com.coffee.main.activity.Game;
import com.coffee.objects.Directions;
import com.coffee.objects.particles.Dust;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Lazer extends Entity {

    private final Point pi;

    public Lazer(int x, int y, Directions direction) {
        super(-1, x, y);
        pi = new Point(x + direction.getDir()[0]*Engine.GameScale*8, y + direction.getDir()[1]*Engine.GameScale*8);
        getOE().setDirection(direction);
        setSize(Engine.GameScale, Engine.GameScale);
    }

    @Override
    public BufferedImage getSprite() {
        return null;
    }

    @Override
    public void tick() {
        setX(pi.x);
        setY(pi.y);
        while(getOE().sliding(Engine.GameScale*4)) {}
        List<Entity> entities = Game.getLevel().getEntities();
        for(int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if(e == this)
                continue;
            if(Geometry.isRectangleBetweenPoints(e.getBounds(), pi, getMiddle())) {
                e.kill();
            }
        }
        if(Engine.RAND.nextInt(100) < 20)
            Game.getLevel().addParticle(new Dust((int)getX() + Engine.GameScale, (int)getY() + Engine.GameScale, getOE().getReverse().getRadians()));
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Engine.Color_Primary);
        g.setStroke(new BasicStroke(Engine.RAND.nextInt(Engine.GameScale*2) + 1));
        g.drawLine(pi.x - Game.getCam().getX(), pi.y - Game.getCam().getY(), (int)getX() - Game.getCam().getX(), (int)getY() - Game.getCam().getY());
    }

    @Override
    public void dispose() {

    }

}
