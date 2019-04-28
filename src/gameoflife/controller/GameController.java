package gameoflife.controller;

import java.util.Map;

import gameoflife.model.Cell;
import gameoflife.model.Point;

/**
 * A j�t�k menet��rt felel�s interface.
 * Itt vannak azok a met�dusok, melyekkel hozz� lehet adni �s t�r�lni lehet sejteket.
 * Tov�bb� itt van az a met�dus is, melyben meg vannak hat�rozva a j�t�k szab�lyai.
 *
 * @author sombali
 */
public interface GameController {

	/**
	 * Sejt hozz�ad�sa.
	 *
	 * @param cell
	 * @param point
	 */
	void addCell(Cell cell, Point point);

	/**
	 * Sejt t�rl�se.
	 *
	 * @param point
	 */
	void removeCell(Point point);

	/**
	 * Itt vannak megfogalmazva a j�t�k szab�lyai.
	 * 1. l�p�sben halottnak jel�li az �sszes olyan sejtet, aminek 3n�l t�bb vagy 2n�l kevesebb szomsz�dja van.
	 * 2. l�p�sben hozz�ad egy �j sejtet, ha annak pontosan 3 szomsz�dja van.
	 * 3. l�p�sben t�rli a halott sejteket.
	 * 4. l�p�sben a sz�let� sejteket �t�ll�tja �l�re.
	 *
	 */
	void step();
	
	public Map<Point, Cell> getStepCellMap();
	public void setCellMap(Map<Point, Cell> cellMap);
}
