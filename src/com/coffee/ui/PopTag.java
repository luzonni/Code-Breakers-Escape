package com.coffee.ui;

import com.coffee.Inputs.Mouse;
import com.coffee.graphics.FontG;
import com.coffee.main.Engine;
import com.coffee.main.Theme;

import java.awt.*;

public class PopTag {

    private String text;

    public void setText(String text) {
        this.text = text;
    }

    private void clear() {
        this.text = null;
    }

    public void renderName(Graphics2D g) {
        if(this.text == null)
            return;
        String name = this.text;
        int x = Mouse.getX();
        int y = Mouse.getY();
        Font f = FontG.font(12* Engine.SCALE);
        int wF = FontG.getWidth(name, f);
        int hF = FontG.getHeight(name, f);
        g.setFont(f);
        g.setColor(Theme.Tertiary);
        g.fillRect(x + 8*Engine.SCALE, y + 17*Engine.SCALE - hF, wF + 2*Engine.SCALE, hF);
        g.setColor(Theme.Secondary);
        g.fillRect(x + 7*Engine.SCALE, y + 16*Engine.SCALE - hF, wF + 2*Engine.SCALE, hF);
        g.setColor(Theme.Tertiary);
        g.drawString(name, x + 9*Engine.SCALE, y + 15*Engine.SCALE);
        g.setColor(Theme.Primary);
        g.drawString(name, x + 8*Engine.SCALE, y + 14*Engine.SCALE);
        clear();
    }

}
