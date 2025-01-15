package com.coffee.objects.entity;

import com.coffee.level.Level;
import com.coffee.main.Theme;
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

    public Blaster(int id, int x, int y, Directions direction) {
        super(id, x, y);
        if(sprite == null) {
            sprite = getSprite("blaster", Theme.Primary);
        }
        getOE().setDirection(direction);
        t = new Timer(1);
        setVar(Variables.Breakable, false);
        lazer = new Lazer(getMiddle().x, getMiddle().y, getOE().getDirection());
    }

    @Override
    public BufferedImage getSprite() {
        switch(getOE().getDirection()) {
            case Up -> {
                return sprite[0];
            }
            case Right -> {
                return sprite[1];
            }
            case Down -> {
                return sprite[2];
            }
            case Left -> {
                return sprite[3];
            }
            default -> throw new RuntimeException("Sprite not found for direction");
        }
    }


    public void tick() {
        if(t.tiktak()) {
            Level level = Game.getLevel();
            if(!level.getEntities().contains(lazer))
                Game.getLevel().addEntity(lazer);
        }
    }

    @Override
    public void kill() {
        super.kill();
        lazer.disappear();
    }

    @Override
    public void render(Graphics2D g) {
        renderEntity(getSprite(), g);
    }

    @Override
    public void dispose() {
        sprite = null;
    }
}
