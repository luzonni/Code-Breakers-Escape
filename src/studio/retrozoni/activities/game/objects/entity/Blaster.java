package studio.retrozoni.activities.game.objects.entity;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.activities.game.Level;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.engine.tools.Timer;
import studio.retrozoni.activities.game.objects.Directions;
import studio.retrozoni.activities.game.objects.Variables;

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
        setVar(Variables.Alive, true);
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
