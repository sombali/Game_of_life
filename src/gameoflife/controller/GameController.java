package gameoflife.controller;

import java.util.Map;

import gameoflife.model.Cell;
import gameoflife.model.Point;

/**
 * A játék menetéért felelõs interface.
 * Itt vannak azok a metódusok, melyekkel hozzá lehet adni és törölni lehet sejteket.
 * Továbbá itt van az a metódus is, melyben meg vannak határozva a játék szabályai.
 *
 * @author sombali
 */
public interface GameController {

	/**
	 * Sejt hozzáadása.
	 *
	 * @param cell
	 * @param point
	 */
	void addCell(Cell cell, Point point);

	/**
	 * Sejt törlése.
	 *
	 * @param point
	 */
	void removeCell(Point point);

	/**
	 * Itt vannak megfogalmazva a játék szabályai.
	 * 1. lépésben halottnak jelöli az összes olyan sejtet, aminek 3nál több vagy 2nél kevesebb szomszédja van.
	 * 2. lépésben hozzáad egy új sejtet, ha annak pontosan 3 szomszédja van.
	 * 3. lépésben törli a halott sejteket.
	 * 4. lépésben a születõ sejteket átállítja élõre.
	 *
	 */
	void step();
	
	public Map<Point, Cell> getStepCellMap();
	public void setCellMap(Map<Point, Cell> cellMap);
}
