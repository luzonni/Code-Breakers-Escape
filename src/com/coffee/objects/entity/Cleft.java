package com.coffee.objects.entity;

import com.coffee.main.Engine;
import com.coffee.main.Theme;
import com.coffee.activity.game.Game;
import com.coffee.objects.Variables;
import com.coffee.objects.particles.Speck;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Cleft extends Entity {

    private double r;
    private static BufferedImage sprite;

    public Cleft(int id, int x, int y) {
        super(id, x, y);
        setEffect(Variables.Selectable);
        setEffect(Variables.Movable);
        setEffect(Variables.Removable);
        setEffect(Variables.Reanimable);
    }

    @Override
    public BufferedImage getSprite() {
        if(Cleft.sprite == null)
            Cleft.sprite = getSprite("teleporter")[0];
        return Cleft.sprite;
    }

    @Override
    public void tick() {
        r += 0.02f;
        teleport();
    }

    private void teleport() {
        List<Entity> entities = Game.getLevel().getEntities();
        for(int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if(e == this)
                continue;
            if(e.collidingWith(this)) {
                List<Cleft> clefts = getListClefts();
                Cleft next = getNext(clefts);
                e.setX(next.getX());
                e.setY(next.getY());
                next.disappear();
                clefts.remove(next);
                disappear();
                for(int j = 0; j < 25; j++) {
                    Game.getLevel().addParticle(new Speck(getMiddle().x, getMiddle().y));
                    Game.getLevel().addParticle(new Speck(next.getMiddle().x, next.getMiddle().y));
                }
            }
        }
    }

    private Cleft getNext(List<Cleft> clefts) {
        Cleft next = clefts.get(Engine.RAND.nextInt(clefts.size()));
        if(next == this && clefts.size() > 1)
            return getNext(clefts);
        return next;
    }

    private List<Cleft> getListClefts() {
        List<Cleft> list = new ArrayList<>();
        List<Entity> entities = Game.getLevel().getEntities();
        for(int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            if(entity instanceof Cleft t) {
                list.add(t);
            }
        }
        return list;
    }


    @Override
    public void render(Graphics2D g) {
        renderRect(Engine.SCALE * 8, true, g);
        renderRect(Engine.SCALE * 4, false, g);
        renderRect(Engine.SCALE * 12, false, g);
    }

    private void renderRect(int size, boolean fill, Graphics2D g) {
        double rr = r;
        int x = getMiddle().x - size/2 - Game.getCam().getX();
        int y = getMiddle().y - size/2 - Game.getCam().getY();
        if(fill)
            rr *= -1;
        g.rotate(rr, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
        if(fill) {
            g.setColor(Theme.Primary);
            g.fillRect(x, y, size, size);
        }else {
            g.setColor(Theme.Secondary);
            g.setStroke(new BasicStroke(Engine.SCALE));
            g.drawRect(x, y, size, size);
        }
        g.rotate(-rr, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
    }

    @Override
    public void dispose() {

    }
}
