package studio.retrozoni.activities.game.objects.tiles;

public enum TileTag {

    Air,
    Wall,
    Floor,
    Door,
    Gate,
    Box,
    Crate,
    Thron,
    Repellent,
    Fake_Wall,
    Portalizer;

    public int getId() {
        return this.ordinal();
    }
}
