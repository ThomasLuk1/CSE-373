package huskymaps.rastering;

    import huskymaps.graph.Coordinate;
    import static huskymaps.utils.Constants.ROOT_ULLON;
    import static huskymaps.utils.Constants.ROOT_ULLAT;
    import static huskymaps.utils.Constants.LON_PER_TILE;
    import static huskymaps.utils.Constants.LAT_PER_TILE;
    import static huskymaps.utils.Constants.NUM_X_TILES_AT_DEPTH;
    import static huskymaps.utils.Constants.NUM_Y_TILES_AT_DEPTH;

/**
 * @see Rasterer
 */
public class DefaultRasterer implements Rasterer {
    public TileGrid rasterizeMap(Coordinate ul, Coordinate lr, int depth) {
        if (ul.equals(lr)) {
            Tile blankTile = new Tile(depth, 0, 0);
            Tile[][] grid = new Tile[1][1];
            grid[1][1] = blankTile;
            return new TileGrid(grid);
        }
        int ulX = (int) Math.floor((ul.lon() - ROOT_ULLON) / (LON_PER_TILE[depth]));
        int ulY = (int) Math.floor((ul.lat() - ROOT_ULLAT) / -LAT_PER_TILE[depth]);
        int lrX = (int) Math.floor((lr.lon() - ROOT_ULLON) / (LON_PER_TILE[depth]));
        int lrY = (int) Math.floor((lr.lat() - ROOT_ULLAT) / -LAT_PER_TILE[depth]);
        System.out.println("ulX: " + ulX);
        System.out.println("ulY: " + ulY);
        System.out.println("lrX: " + lrX);
        System.out.println("lrY: " + lrY);
        System.out.println(NUM_X_TILES_AT_DEPTH[depth]);
        System.out.println(NUM_Y_TILES_AT_DEPTH[depth]);
        if (lrX > NUM_X_TILES_AT_DEPTH[depth] - 1) {
            lrX = NUM_X_TILES_AT_DEPTH[depth] - 1;
        }
        if (lrY > NUM_Y_TILES_AT_DEPTH[depth] - 1) {
            lrY = NUM_Y_TILES_AT_DEPTH[depth] - 1;
        }
        if (ulX < 0) {
            ulX = 0;
        }
        if (ulY < 0) {
            ulY = 0;
        }
        int gridXSize = lrX - ulX + 1;
        int gridYSize = lrY - ulY + 1;
        Tile[][] grid = new Tile[gridYSize][gridXSize];
        for (int x = 0; x < gridXSize; x++) {
            for (int y = 0; y < gridYSize; y++) {
                grid[y][x] = new Tile(depth, x + ulX, y + ulY);
                System.out.println(grid[y][x].toString());
            }
        }
        return new TileGrid(grid);
    }
}
