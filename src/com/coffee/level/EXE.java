package com.coffee.level;

import com.coffee.Inputs.Mouse;
import com.coffee.Inputs.Mouse_Button;
import com.coffee.ui.command.Commands;
import com.coffee.ui.command.Receiver;
import com.coffee.exceptions.ConsoleError;
import com.coffee.items.Bow;
import com.coffee.items.Item;
import com.coffee.items.Placeable;
import com.coffee.main.activity.Game;
import com.coffee.objects.Directions;
import com.coffee.objects.Variables;
import com.coffee.objects.entity.Entity;
import com.coffee.objects.entity.Player;
import com.coffee.objects.tiles.Tile;

public class EXE {

    protected static void selector(Receiver receiver) {
        new Thread(() -> {
            Level level = Game.getLevel();
            while(level.getSelected() == null) {
                for(int i = 0; i < level.getMap().length; i++) {
                    Tile T = level.getMap()[i];
                    if(Mouse.pressingOnMap(Mouse_Button.LEFT, T.getBounds(), Game.getCam()) && T.getVar(Variables.Selectable)) {
                        level.selected = T;
                        receiver.used(Commands.select);
                    }
                }
                for(int i = 0; i < level.getEntities().size(); i++) {
                    Entity E = level.getEntities().get(i);
                    if(Mouse.pressingOnMap(Mouse_Button.LEFT, E.getBounds(), Game.getCam()) && E.getVar(Variables.Selectable)) {
                        level.selected = E;
                        receiver.used(Commands.select);
                    }
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignore) {}
            }
        }).start();
    }

    protected static void shot(String[] keys, Receiver receiver) {
        String dir_name = (keys[1].substring(0, 1).toUpperCase() + keys[1].substring(1).toLowerCase());
        Directions dir = Directions.valueOf(dir_name);
        Player player = Game.getPlayer();
        Item[] items = Game.getInventory().getList();
        for(Item item : items)
            if(item instanceof Bow) {
                ((Bow)item).shot(player, dir);
                receiver.used(Commands.shot);
                return;
            }
        throw new ConsoleError("You don't have a bow to shoot");
    }

    protected static void put(String[] keys, Receiver receiver) {
        Item[] items = Game.getInventory().getList();
        for(int i = 0; i < items.length; i++) {
            if(items[i] instanceof Placeable) {
                if(((Placeable)items[i]).place(keys)) {
                    receiver.used(Commands.put);
                }else
                    throw new ConsoleError("Item not found");
            }else {
                throw new ConsoleError("Item not found");
            }
        }
    }
}
