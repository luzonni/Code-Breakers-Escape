package studio.retrozoni.activities.game.objects.entity;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.graphics.SpriteHandler;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.tools.Geometry;
import studio.retrozoni.activities.game.objects.Directions;
import studio.retrozoni.activities.game.objects.Variables;
import studio.retrozoni.activities.game.objects.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Worm extends Entity {

    private static BufferedImage[] sprites;
    private final List<Entity> pieces;
    private double radians;
    private final Geometry.AngleFollower angleFollower = new Geometry.AngleFollower(0);

    public Worm(int id, int x, int y) {
        super(id, x, y);
        if(sprites == null)
            sprites = getSprite("worm");
        pieces = new ArrayList<>();
        pieces.add(this);
        for(int i = 1; i < 4; i++) {
            pieces.add(new Body(pieces.getLast(), sprites[1], getOE().getDirection()));
        }
        pieces.add(new Body(pieces.getLast(), sprites[2], getOE().getDirection()));
        setEffect(Variables.Alive);
        setEffect(Variables.Selectable);
    }

    @Override
    public boolean collidingWith(Entity o) {
        boolean colliding = super.collidingWith(o);
        for(int i = 0; i < pieces.size(); i++) {
            if(o.getBounds().intersects(pieces.get(i).getBounds())) {
                colliding = true;
            }
        }
        return colliding;
    }

    @Override
    public BufferedImage getSprite() {
        return SpriteHandler.Rotate(SpriteHandler.Vertical(sprites[0]), (int)Math.toDegrees(this.radians));
    }

    @Override
    public void tick() {
        moving();
        for(int i = 1; i < pieces.size(); i++) {
            Entity worm = pieces.get(i);
            worm.tick();
        }
        killer();
    }

    private void moving() {
        Player player = Game.getPlayer();
        double pr = Math.atan2(player.getMiddle().y - getMiddle().y, player.getMiddle().x - getMiddle().x);
        angleFollower.followAngle(Math.toDegrees(pr), 1);
        this.radians = Math.toRadians(angleFollower.getAngle());
        this.setX(getX() + Math.cos(this.radians) * Engine.SCALE);
        this.setY(getY() + Math.sin(this.radians) * Engine.SCALE);
    }

    private void killer() {
        List<Entity> entities = Game.getLevel().getEntities();
        for(int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if(this.collidingWith(e) && e != this && e.getVar(Variables.Alive)) {
                e.kill();
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        renderEntity(getSprite(), g);
        for(int i = 1; i < pieces.size(); i++) {
            Entity worm = pieces.get(i);
            worm.render(g);
        }
    }

    @Override
    public void dispose() {
        sprites = null;
    }

    private static class Body extends Entity {

        private final Entity parent;
        private final BufferedImage sprite;
        private double radians;

        public Body(Entity parent, BufferedImage sprite, Directions direction) {
            super(-1, (int)parent.getX(), (int)parent.getY());
            this.parent = parent;
            this.sprite = sprite;
            setDirection(direction);
            setEffect(Variables.Alive);
        }

        @Override
        public BufferedImage getSprite() {
            return sprite;
        }

        @Override
        public void tick() {
            double mx = parent.getMiddle().getX();
            double my = parent.getMiddle().getY();
            this.radians = Math.atan2(getMiddle().getY() - my, getMiddle().getX() - mx);
            setX(parent.getX() + Math.cos(this.radians) * (double) (Tile.getSize() - Engine.SCALE));
            setY(parent.getY() + Math.sin(this.radians) * (double) (Tile.getSize() - Engine.SCALE));
        }

        @Override
        public void render(Graphics2D g) {
            double r = radians;
            g.rotate(r, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
            renderEntity(sprite, g);
            g.rotate(-r, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
        }

        @Override
        public void dispose() {

        }
    }

}
