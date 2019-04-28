package gameoflife.controller;

import java.util.Map;

import gameoflife.common.CellState;
import gameoflife.model.Cell;
import gameoflife.model.Field;
import gameoflife.model.Point;

/**
 * A játék vezérlésének implementációja.
 *
 * @author sombali
 */
public class GameControllerImpl implements GameController {
	private Field field;

	public GameControllerImpl(Integer width, Integer height, Map<Point, Cell> map) {
		this.field = new Field(width, height);
		this.field.setCellMap(map);
	}

	@Override
	public void addCell(Cell cell, Point point) {
		this.field.addCell(cell, point);
	}

	@Override
	public void removeCell(Point point) {
		this.field.removeCell(point);
	}

	@Override
	public void step() {

		for (Map.Entry<Point, Cell> entry : this.field.getCellMap().entrySet()) {
			Point currentPoint = entry.getKey();
			Cell currentCell = entry.getValue();
			if(CellState.ALIVE.equals(currentCell.getCellState())) {
				this.field.markDeadCell(currentPoint);
			}
		}
		for(int i = 0; i < this.field.getWidth(); i++) {
			for(int j = 0; j < this.field.getHeight(); j++) {
				Point point = new Point(i, j);
				this.field.addBornCell(point);
			}
		}
		this.field.removeDeadCells();
		this.field.changeBornToAlive();

	}

	@Override
	public Map<Point, Cell> getStepCellMap(){
		return this.field.getCellMap();
	}

	@Override
	public void setCellMap(Map<Point, Cell> cellMap){
		this.field.setCellMap(cellMap);
	}

}
