package com.coffee.activity.creator.frame;

import com.coffee.graphics.FontG;
import com.coffee.graphics.SpriteSheet;
import com.coffee.inputs.Keyboard;
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
    private final StringBuilder drawText;
    private final Rectangle sizeDrawBounds;

    private final int pageSize = 6;
    private int page;

    private static final char[] Caracteres = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            '1','2','3','4','5','6','7','8','9','0','-','+','=','.','[',']','(',')','{','}',';','!','\'','@','_','\"','?','/','\\','^','<','>','#','*'};

    public Dock() {
        Responsive pointDock = Responsive.createPoint(null, 3, 50);
        selected = Shapes.Circle;
        colorSelected = Theme.Primary;
        this.sizeDraw = 16;
        this.drawText = new StringBuilder("Text");
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
        items.put(Shapes.InputText, (int x, int y, Graphics2D g) -> {
            Font font = FontG.font(sizeDraw);
            g.setFont(font);
            g.setColor(colorSelected);
            g.drawString(this.drawText.toString(), x, y);
            this.writeInputDrawText();
        });
        Shapes[] shapes = Shapes.values();
        for(int i = 6; i < shapes.length; i++) {
            Shapes shape = shapes[i];
            items.put(shape, (int x, int y, Graphics2D g) -> {
                g.drawImage(getImage(shape.ordinal()), x - sizeDraw, y - sizeDraw, sizeDraw *2, sizeDraw *2, null);
            });
        }
    }

    private void writeInputDrawText() {
        char key = Keyboard.getKeyChar(Caracteres);
        System.out.println(key);
        if(key != Keyboard.NONE) {
            drawText.append(key);
        }
        if(Keyboard.KeyPressed("Space")) {
            drawText.append(" ");
        }
        if(Keyboard.KeyPressed("Back_Space") && !drawText.isEmpty()) {
            drawText.delete(drawText.length()-1, drawText.length());
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
        }else {
            int scrool = Mouse.Scrool();
            if(scrool > 0 && this.sizeDraw > 2) {
                this.sizeDraw -= 2;
            }else if(scrool < 0 && this.sizeDraw < 64) {
                this.sizeDraw += 2;
            }
        }
        if(Mouse.On_Mouse(sizeDrawBounds)) {
            if(Mouse.clickOn(Mouse_Button.LEFT, sizeDrawBounds)) {
                this.colorSelected = this.colorSelected == Theme.Primary ? Theme.Secondary : Theme.Primary;
                if(this.colorSelected == Theme.Primary) {
                    sheet.replaceColor(Theme.Secondary.getRGB(), Theme.Primary.getRGB());
                }else {
                    sheet.replaceColor(Theme.Primary.getRGB(), Theme.Secondary.getRGB());
                }
            }
        }
    }

    public Painter getPainter() {
        return this.items.get(selected);
    }

    public void render(Graphics2D g) {
        renderDock(g);
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
