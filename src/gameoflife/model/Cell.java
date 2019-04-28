package gameoflife.model;

import java.io.Serializable;

import gameoflife.common.CellState;

/**
 * Sejt oszt�ly, amely tartalmaz egy sejt�llapot v�ltoz�t.
 * Le lehet k�rni, illetve be lehet �ll�tani a sejt �llapot�t.
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
