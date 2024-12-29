package com.coffee.objects.entity;

import com.coffee.level.Level;
import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.main.tools.Timer;
import com.coffee.objects.Directions;
import com.coffee.objects.Variables;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Blaster extends Entity {

    private static BufferedImage[] sprite;
    private final Timer t;
    private final Lazer lazer;

    public Blaster(int id, int x, int y) {
        super(id, x, y);
        if(sprite == null) {
            sprite = getSprite("blaster", Engine.Color_Primary);
        }
        setDirection(getOrientation());
        t = new Timer(1);
        setVar(Variables.Breakable, true);
        lazer = new Lazer(getMiddle().x, getMiddle().y, getOE().getDirection());
    }

    @Override
    public BufferedImage getSprite() {
        return sprite[0];
    }

    private Directions getOrientation() {
        //TODO logica para o lado.
        return Directions.Right;
    }

    public void tick() {
        if(t.tiktak()) {
            Level level = Game.getLevel();
            if(!level.getEntities().contains(lazer))
                Game.getLevel().addEntity(lazer);
        }
    }

    @Override
    public void render(Graphics2D g) {
        int index = 1;
        renderEntity(sprite[index], g);

    }

    @Override
    public void dispose() {
        sprite = null;
    }
}
