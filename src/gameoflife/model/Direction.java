package gameoflife.model;

/**
 * Ir�ny enumer�ci�.
 * Egy adott pontt�l megn�zi a t�le a 8 �gt�j fel� l�v� pontokat.
 *
 * @author Sombali
 */
public enum Direction {
    NORTHWEST(-1, -1),
    NORTH(0, -1),
    NORTHEAST(1, -1),
    EAST(1, 0),
    SOUTHEAST(1, 1),
    SOUTH(0, 1),
    SOUTHWEST(-1, 1),
    WEST(-1, 0),
    ;

    final int dx;
    final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}