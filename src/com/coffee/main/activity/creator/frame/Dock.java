package com.coffee.main.activity.creator.frame;

import com.coffee.graphics.SpriteSheet;
import com.coffee.main.Engine;
import com.coffee.main.Theme;
import com.coffee.main.tools.Responsive;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

class Dock {

    private Responsive pointDock;
    private Color colorSelected;
    private Shapes selected;
    private Map<Shapes, Painter> itens;
    private SpriteSheet sheet;

    public Dock() {
        this.pointDock = Responsive.createPoint(null, 3, 50);
        selected = Shapes.Arrow_Right;
        sheet = new SpriteSheet(Engine.ResPath + "/icons/shapes.png");
        buildItens();
    }

    private void buildItens() {
        sheet.replaceColor(Theme.PRIMARY, Theme.Primary.getRGB());
        sheet.replaceColor(Theme.SECONDARY, Theme.Secondary.getRGB());
        sheet.replaceColor(Theme.TERTIARY, Theme.Tertiary.getRGB());
        itens = new HashMap<>();
        itens.put(Shapes.Eraser, (int x, int y, int size, Graphics2D g) -> {
            g.setColor(Theme.Tertiary);
            g.fillOval(x - size/2, y - size/2, size, size);
        });
        itens.put(Shapes.Circle, (int x, int y, int size, Graphics2D g) -> {
            g.setColor(colorSelected);
            g.fillOval(x - size/2, y - size/2, size, size);
        });
        itens.put(Shapes.Rectangle, (int x, int y, int size, Graphics2D g) -> {
            g.setColor(colorSelected);
            g.fillRect(x - size/2, y - size/2, size, size);
        });
        itens.put(Shapes.Spray, (int x, int y, int size, Graphics2D g) -> {
            g.setColor(colorSelected);
            double distance = Math.sqrt(Engine.RAND.nextDouble(0f, 1f)) * size/2;
            double angle = Engine.RAND.nextDouble(Math.PI*2);
            int xx = x + (int)(distance * Math.cos(angle));
            int yy = y + (int)(distance * Math.sin(angle));
            g.fillRect(xx , yy, 1, 1);
        });
        itens.put(Shapes.Spray_Circle, (int x, int y, int size, Graphics2D g) -> {
            g.setColor(colorSelected);
            double distance = Math.sqrt(Engine.RAND.nextDouble(0f, 1f)) + size/2;
            double angle = Engine.RAND.nextDouble(Math.PI*2);
            int xx = x + (int)(distance * Math.cos(angle));
            int yy = y + (int)(distance * Math.sin(angle));
            g.fillRect(xx , yy, 1, 1);
        });
        itens.put(Shapes.Arrow_Right, (int x, int y, int size, Graphics2D g) -> {
            g.setColor(colorSelected);
            g.drawImage(sheet.getSprite(Shapes.Arrow_Right.ordinal()*16, 0), x - size, y - size, size*2, size*2, null);
        });
        //TODO colocar todos os shaptes!
    }

    public void tick() {

    }

    public Painter getPainter() {
        return this.itens.get(selected);
    }

    public void render(Graphics2D g) {
        Rectangle rec = pointDock.getBounds();
        g.setColor(Theme.Primary);
        g.fillRect(rec.x - 8, rec.y - 8, 16, 16);
        renderDock(g);
    }

    private void renderDock(Graphics2D g) {

    }

}
