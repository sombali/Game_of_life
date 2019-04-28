package gameoflife.model;

import java.util.Iterator;
import java.util.Map;

import gameoflife.common.CellState;
import gameoflife.model.Point;

/**
 * Mez� oszt�ly.
 * T�rol egy sz�less�get illetve magass�got, melyek nem megv�ltoztathat�ak.
 * Tartalmaz egy Map-et, melyben a sejtek vannak t�rolva bizonyos Pont kulcsokhoz.
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
	 * Sejt hozz�ad�sa a Map-hez.
	 *
	 * @param cell
	 * @param point
	 */
	public void addCell(Cell cell, Point point) {
		this.cellMap.put(point, cell);
	}

	/**
	 * Sejt elt�vol�t�sa a Map-b�l.
	 *
	 * @param point
	 */
	public void removeCell(Point point) {
		if(this.cellMap.containsKey(point)) {
			this.cellMap.remove(point);
		}
	}

	/**
	 * Ez a met�dus megn�zi, hogy egy bizonyos sejtnek mennyi szomsz�dja van.
	 *
	 * @param point
	 * @return a sejt szomsz�dainak sz�ma
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
	 * A sejtet halott-ra �ll�tja, ha annak t�bb mint 3 vagy kevesebb mint 2 szomsz�dja van.
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
	 * Hozz�ad egy �j, sz�let� sejtet a Map-hez, ha annak szomsz�djainak sz�ma pontosan 3.
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
	 * Elt�nteti a halott sejteket a Map-b�l.
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
	 * A sz�let� sejtek(BORN) �llapot�t �t�ll�tja �l�re(ALIVE), hogy a k�vetkez� k�rben m�r �k is
	 * figyelembe legyenek v�ve.
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
