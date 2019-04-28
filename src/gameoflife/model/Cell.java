package gameoflife.model;

import java.io.Serializable;

import gameoflife.common.CellState;

/**
 * Sejt osztály, amely tartalmaz egy sejtállapot változót.
 * Le lehet kérni, illetve be lehet állítani a sejt állapotát.
 *
 * @author sombali
 */
@SuppressWarnings("serial")
public class Cell implements Serializable{
	private CellState cellState = CellState.ALIVE;

	public CellState getCellState() {
		return cellState;
	}

	public void setCellState(CellState cellState) {
		this.cellState = cellState;
	}

}
