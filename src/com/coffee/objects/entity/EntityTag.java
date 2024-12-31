package com.coffee.objects.entity;

public enum EntityTag {
    Player,
    Flag,
    Cannon,
    CrossBow,
    Ant,
    Button,
    Computer,
    Skull,
    Pluuter,
    Karto,
    Saw,
    Bomb,
    Fish,
    Barrel,
    Trampoline_UpRight,
    Trampoline_RightDown,
    Trampoline_DownLeft,
    Trampoline_LeftUp,
    Blaster_Up,
    Blaster_Right,
    Blaster_Down,
    Blaster_Left,
    Cleft;

    public int getId() {
        return this.ordinal();
    }

}
