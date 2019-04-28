package gameoflife.model;

import java.io.Serializable;

/**
 * Pont osztály.
 * Tartalmaz egy x és egy y koordinátát, mely alapján a sejtek beazonosíthatóak.
 *
 * @author sombali
 */
@SuppressWarnings("serial")
public class Point implements Serializable{
	private Integer x;
	private Integer y;

	public Point() {
	}

	public Point(int x, int y) {
		this.x = (Integer)x;
		this.y = (Integer)y;
	}

	public Point(Integer x, Integer y) {
		this.x = x;
		this.y = y;
	}

	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}

	/**
	 * Ez a metódus lekérdezi az összes szomszédos pontot.
	 *
	 * @param direction
	 * @return új szomszédos pont
	 */
	Point getNeighbours(Direction direction) {
		return new Point(this.x + direction.dx, this.y + direction.dy);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		return true;
	}

}
