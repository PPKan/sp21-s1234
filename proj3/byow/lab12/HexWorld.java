package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final int WIDTH = 80;
    private static final int HEIGHT = 80;
    private static final Random RANDOM = new Random();


    private static void addHexagon(TETile[][] world, int size, int x_start, int y_start) {
        // fills in a block 14 tiles wide by 4 tiles tall
        int total = size * 2;
        int hex;
        int odd;
        TETile texture = randomTile();
        hex = size / 2; //integer division
        if (size % 2 == 0) {
            odd = 0;
        } else {
            odd = 1;
        }
        for (int y = y_start; y <= y_start + total; y += 1) {
            for (int x = x_start + size - hex; x < x_start + size + hex + odd; x += 1) {
                world[x][y] = texture;
            }
            if (y >= y_start + size) {
                if (hex <= size / 2) break;
                hex -= 1;
            } else if (y == y_start + size - 1) {
            } else {
                hex += 1;
            }
        }
    }

    private static void fillEmptyTile(TETile[][] world) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    /** soooooo ugly oh my eyes */
    private static void hexWorld(TETile[][] world, int size, int x_start, int y_start) {
        /* first y */
        for (int y1 = y_start; y1 < y_start + size * 6; y1 += size * 2) {
            addHexagon(world, size, x_start, y1);
        }
        /* second y */
        for (int y2 = y_start - size; y2 < y_start - size + size * 6 + 2 * size; y2 += size * 2 ) {
            addHexagon(world, size, x_start+size*2-1, y2);
        }
        /* third y */
        for (int y3 = y_start - size * 2; y3 < y_start - size + size * 6 + 3 * size; y3 += size * 2 ) {
            addHexagon(world, size, x_start+size*4-2, y3);
        }
        /* fourth y */
        for (int y4 = y_start - size; y4 < y_start - size + size * 6 + 2 * size; y4 += size * 2 ) {
            addHexagon(world, size, x_start+size*6-3, y4);
        }
        /* fifth y */
        for (int y5 = y_start; y5 < y_start + size * 6; y5 += size * 2) {
            addHexagon(world, size, x_start+size*8-4, y5);
        }
    }


    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(7);
        switch (tileNum) {
            case 0: return Tileset.TREE;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.WALL;
            case 4: return Tileset.FLOOR;
            case 5: return Tileset.MOUNTAIN;
            case 6: return Tileset.SAND;
            default: return Tileset.WALL;
        }
    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        fillEmptyTile(world);
        hexWorld(world, 3, 10, 10);

        // draws the world to the screen
        ter.renderFrame(world);

    }


}
