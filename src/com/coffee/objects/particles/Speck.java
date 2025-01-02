package com.coffee.objects.particles;

import com.coffee.main.Engine;
import com.coffee.main.Theme;

import java.awt.*;

public class Speck extends Particle {

    private final double radians;
    private final double speed;
    private final Color c;

    public Speck(int x, int y) {
        super(x, y);
        this.radians = Engine.RAND.nextDouble()*(Math.PI*2);
        this.speed = Engine.RAND.nextDouble() + 1;
        c = Engine.RAND.nextBoolean() ? Theme.Color_Primary : Theme.Color_Secondary;
        setSize(Engine.SCALE, Engine.SCALE);
    }

    @Override
    public void spawn(int x, int y) {

    }

    @Override
    public void tick() {
        dead();
        double dirX = this.getX() + Math.cos(this.radians)*speed;
        double dirY = this.getY() + Math.sin(this.radians)*speed;
        this.setX(dirX);
        this.setY(dirY);
    }

    @Override
    public void render(Graphics2D g) {
        renderParticle(c, g);
    }
}
