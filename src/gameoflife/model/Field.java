package gameoflife.model;

import java.util.Iterator;
import java.util.Map;

import gameoflife.common.CellState;
import gameoflife.model.Point;

/**
 * Mezõ osztály.
 * Tárol egy szélességet illetve magasságot, melyek nem megváltoztathatóak.
 * Tartalmaz egy Map-et, melyben a sejtek vannak tárolva bizonyos Pont kulcsokhoz.
 *
 * @author sombali
 */
public class Field {
	private final Integer width;
	private final Integer height;
	private Map<Point, Cell> cellMap;

	public Field(Integer width, Integer height) {
		this.width = width;
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public Integer getHeight() {
		return height;
	}

	public Map<Point, Cell> getCellMap() {
		return cellMap;
	}

	public void setCellMap(Map<Point, Cell> cellMap) {
		this.cellMap = cellMap;
	}

	/**
	 * Sejt hozzáadása a Map-hez.
	 *
	 * @param cell
	 * @param point
	 */
	public void addCell(Cell cell, Point point) {
		this.cellMap.put(point, cell);
	}

	/**
	 * Sejt eltávolítása a Map-bõl.
	 *
	 * @param point
	 */
	public void removeCell(Point point) {
		if(this.cellMap.containsKey(point)) {
			this.cellMap.remove(point);
		}
	}

	/**
	 * Ez a metódus megnézi, hogy egy bizonyos sejtnek mennyi szomszédja van.
	 *
	 * @param point
	 * @return a sejt szomszédainak száma
	 */
	public int numberOfGenerations(Point point) {
		int numbOfNeigbours = 0;
		for(Direction direction : Direction.values()) {
			Point currentPoint = point.getNeighbours(direction);
			if(this.cellMap.containsKey(currentPoint)) {
				Cell currentCell = this.cellMap.get(currentPoint);
				if(!CellState.BORN.equals(currentCell.getCellState())) {
					numbOfNeigbours++;
				}
			}
		}
		return numbOfNeigbours;
	}


	/**
	 * A sejtet halott-ra állítja, ha annak több mint 3 vagy kevesebb mint 2 szomszédja van.
	 *
	 * @param point
	 */
	public void markDeadCell(Point point) {
		int numberOfNeighbours  = numberOfGenerations(point);
		if(numberOfNeighbours > 3 || numberOfNeighbours < 2) {
			this.cellMap.get(point).setCellState(CellState.DEAD);
		}
	}

	/**
	 * Hozzáad egy új, születõ sejtet a Map-hez, ha annak szomszédjainak száma pontosan 3.
	 *
	 * @param point
	 */
	public void addBornCell(Point point) {
		int numberOfNeighbours = numberOfGenerations(point);
		if(numberOfNeighbours == 3 && !this.cellMap.containsKey(point)) {
			Cell newCell = new Cell();
			newCell.setCellState(CellState.BORN);
			addCell(newCell, point);
		}

	}

	/**
	 * Eltûnteti a halott sejteket a Map-bõl.
	 *
	 */
	public void removeDeadCells() {
		for(Iterator<Map.Entry<Point, Cell>> it = cellMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Point, Cell> entry = it.next();
			Cell currentCell = entry.getValue();
			if(CellState.DEAD.equals(currentCell.getCellState())) {
				it.remove();
			}
		}
	}

	/**
	 * A születõ sejtek(BORN) állapotát átállítja élõre(ALIVE), hogy a következõ körben már õk is
	 * figyelembe legyenek véve.
	 *
	 */
	public void changeBornToAlive() {
		for(Iterator<Map.Entry<Point, Cell>> it = cellMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Point, Cell> entry = it.next();
			Cell currentCell = entry.getValue();
			if(CellState.BORN.equals(currentCell.getCellState())) {
				currentCell.setCellState(CellState.ALIVE);
			}
		}
	}

}
