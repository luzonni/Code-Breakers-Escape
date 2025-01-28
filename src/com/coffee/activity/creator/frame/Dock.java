package com.coffee.activity.creator.frame;

import com.coffee.graphics.SpriteSheet;
import com.coffee.inputs.Mouse;
import com.coffee.inputs.Mouse_Button;
import com.coffee.main.Engine;
import com.coffee.main.Theme;
import com.coffee.main.tools.Responsive;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

class Dock {

    private final Rectangle dockBounds;
    private Color colorSelected;
    private Shapes selected;
    private Map<Shapes, Painter> items;
    private final SpriteSheet sheet;

    private int sizeDraw;
    private final Rectangle sizeDrawBounds;

    private final int pageSize = 6;
    private int page;

    public Dock() {
        Responsive pointDock = Responsive.createPoint(null, 3, 50);
        selected = Shapes.Circle;
        colorSelected = Theme.Secondary;
        this.sizeDraw = 16;
        sheet = new SpriteSheet(Engine.ResPath + "/icons/shapes.png", Engine.SCALE + 1);
        buildItens();
        dockBounds = new Rectangle(getImage(0).getWidth(), getImage(0).getHeight()*pageSize);
        dockBounds.setLocation(pointDock.getPosition().x - dockBounds.width/2, pointDock.getPosition().y - dockBounds.height/2);
        sizeDrawBounds = new Rectangle(getImage(0).getWidth(), getImage(0).getHeight());
        sizeDrawBounds.setLocation(dockBounds.x, dockBounds.y - sizeDrawBounds.height - Engine.SCALE*3);
    }

    private void buildItens() {
        items = new HashMap<>();
        sheet.replaceColor(Theme.PRIMARY, Theme.Primary.getRGB());
        sheet.replaceColor(Theme.SECONDARY, Theme.Secondary.getRGB());
        sheet.replaceColor(Theme.TERTIARY, Theme.Tertiary.getRGB());
        items.put(Shapes.Eraser, (int x, int y, Graphics2D g) -> {
            g.setColor(Theme.Tertiary);
            g.fillOval(x - sizeDraw /2, y - sizeDraw /2, sizeDraw, sizeDraw);
        });
        items.put(Shapes.Circle, (int x, int y, Graphics2D g) -> {
            g.setColor(colorSelected);
            g.fillOval(x - sizeDraw /2, y - sizeDraw /2, sizeDraw, sizeDraw);
        });
        items.put(Shapes.Rectangle, (int x, int y, Graphics2D g) -> {
            g.setColor(colorSelected);
            g.fillRect(x - sizeDraw /2, y - sizeDraw /2, sizeDraw, sizeDraw);
        });
        items.put(Shapes.Spray, (int x, int y, Graphics2D g) -> {
            g.setColor(colorSelected);
            double distance = Math.sqrt(Engine.RAND.nextDouble(0f, 1f)) * sizeDraw / 2;
            double angle = Engine.RAND.nextDouble(Math.PI * 2);
            int xx = x + (int)(distance * Math.cos(angle));
            int yy = y + (int)(distance * Math.sin(angle));
            g.fillRect(xx , yy, 1, 1);
        });
        items.put(Shapes.Spray_Circle, (int x, int y, Graphics2D g) -> {
            g.setColor(colorSelected);
            double distance = Math.sqrt(Engine.RAND.nextDouble(0f, 1f)) + (double) sizeDraw /2;
            double angle = Engine.RAND.nextDouble(Math.PI*2);
            int xx = x + (int)(distance * Math.cos(angle));
            int yy = y + (int)(distance * Math.sin(angle));
            g.fillRect(xx , yy, 1, 1);
        });
        Shapes[] shapes = Shapes.values();
        for(int i = 5; i < shapes.length; i++) {
            Shapes shape = shapes[i];
            items.put(shape, (int x, int y, Graphics2D g) -> {
                g.drawImage(getImage(shape.ordinal()), x - sizeDraw, y - sizeDraw, sizeDraw *2, sizeDraw *2, null);
            });
        }
    }

    public boolean over() {
        return Mouse.On_Mouse(dockBounds) || Mouse.On_Mouse(sizeDrawBounds);
    }

    private BufferedImage getImage(int index) {
        return sheet.getSprite(index * 16, 0);
    }

    public void tick() {
        if(Mouse.On_Mouse(dockBounds)) {
            int scroll = Mouse.Scrool();
            int p = page + scroll;
            if(p >= 0 && p + pageSize <= items.size())
                page = p;
        }
        Shapes[] shapes = Shapes.values();
        if(Mouse.On_Mouse(sizeDrawBounds)) {
            int scrool = Mouse.Scrool();
            if(scrool > 0 && this.sizeDraw > 2) {
                this.sizeDraw -= 2;
            }else if(scrool < 0 && this.sizeDraw < 64) {
                this.sizeDraw += 2;
            }
            if(Mouse.clickOn(Mouse_Button.LEFT, sizeDrawBounds)) {
                this.colorSelected = this.colorSelected == Theme.Primary ? Theme.Secondary : Theme.Primary;
            }
        }
    }

    public Painter getPainter() {
        return this.items.get(selected);
    }

    public void render(Graphics2D g) {
        //renderIcon(g);
        renderDock(g);
    }

    private void renderIcon(Graphics2D g) {
        if(!Engine.UI.overButtons() && !over()) {
            g.setColor(colorSelected);
            BufferedImage sprite = getImage(selected.ordinal());
            int size = (int)((double)sizeDraw * ((double)Engine.SCALE+1.2d));
            int x = Mouse.getX() - size/2;
            int y = Mouse.getY() - size/2;
            g.drawImage(sprite, x, y, size, size, null);
        }
    }

    private void renderDock(Graphics2D g) {
        Shapes[] shapes = Shapes.values();
        g.setColor(new Color(Theme.Secondary.getRed(), Theme.Secondary.getGreen(), Theme.Secondary.getBlue(), 100));
        g.fillRect(dockBounds.x, dockBounds.y, dockBounds.width, dockBounds.height);
        for(int i = 0; i < pageSize; i++) {
            Shapes shape = shapes[page + i];
            BufferedImage sprite = getImage(shape.ordinal());
            int x = dockBounds.x;
            int y = dockBounds.y + sprite.getHeight() * i;
            int w = sprite.getWidth();
            int h = sprite.getHeight();
            g.drawImage(sprite, x, y, null);
            if(Mouse.clickOn(Mouse_Button.LEFT, new Rectangle(x, y, w, h))) {
                this.selected = shape;
            }
        }
        g.fillOval(sizeDrawBounds.x, sizeDrawBounds.y, sizeDrawBounds.width, sizeDrawBounds.height);
        g.setColor(colorSelected);
        g.setStroke(new BasicStroke(Engine.SCALE));
        g.drawOval(sizeDrawBounds.x + Engine.SCALE, sizeDrawBounds.y + Engine.SCALE, sizeDrawBounds.width - Engine.SCALE*2, sizeDrawBounds.height - Engine.SCALE*2);
        int sizing = (int)(((double)sizeDraw / 64d) * 360);
        g.setStroke(new BasicStroke(Engine.SCALE*2));
        g.drawArc(sizeDrawBounds.x, sizeDrawBounds.y, sizeDrawBounds.width, sizeDrawBounds.height, 90, -sizing);

        g.drawImage(getImage(selected.ordinal()), sizeDrawBounds.x, sizeDrawBounds.y, null);
        int barSize = (int)(((double)pageSize / (double)items.size()) * dockBounds.getHeight());
        double a = ((double)barSize * (double)page) / (double)pageSize;
        g.setColor(Theme.Primary);
        g.fillRect(dockBounds.x + dockBounds.width - Engine.SCALE, (int)(dockBounds.y + a), Engine.SCALE, barSize);
    }

}
