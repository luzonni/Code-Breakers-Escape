package com.coffee.objects.tiles;

public enum TileTag {

    Air,
    Wall,
    Floor,
    Door,
    Gate,
    Box,
    Crate,
    Spine,
    Vase,
    Thron,
    Repellent,
    Fake_Wall,
    Portalizer;

    public int getId() {
        return this.ordinal();
    }
}
