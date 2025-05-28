package studio.retrozoni.activities.game.objects.entity;

public enum EntityTag {
    Player,
    Flag,
    Cannon,
    CrossBow,
    Ant,
    Button,
    Spine,
    Computer,
    Skull,
    Vase,
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
    Cleft,
    Worm;

    public int getId() {
        return this.ordinal();
    }

}
