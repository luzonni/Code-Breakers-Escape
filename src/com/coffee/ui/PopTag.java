package com.coffee.ui;

import com.coffee.Inputs.Mouse;
import com.coffee.graphics.FontG;
import com.coffee.items.Item;
import com.coffee.main.Engine;

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
        Font f = FontG.font(12* Engine.GameScale);
        int wF = FontG.getWidth(name, f);
        int hF = FontG.getHeight(name, f);
        g.setFont(f);
        g.setColor(Engine.Color_Tertiary);
        g.fillRect(x + 8*Engine.GameScale, y + 17*Engine.GameScale - hF, wF + 2*Engine.GameScale, hF);
        g.setColor(Engine.Color_Secondary);
        g.fillRect(x + 7*Engine.GameScale, y + 16*Engine.GameScale - hF, wF + 2*Engine.GameScale, hF);
        g.setColor(Engine.Color_Tertiary);
        g.drawString(name, x + 9*Engine.GameScale, y + 15*Engine.GameScale);
        g.setColor(Engine.Color_Primary);
        g.drawString(name, x + 8*Engine.GameScale, y + 14*Engine.GameScale);
        clear();
    }

}
