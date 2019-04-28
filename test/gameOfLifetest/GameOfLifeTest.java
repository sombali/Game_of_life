package gameOfLifetest;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gameoflife.common.CellState;
import gameoflife.controller.GameControllerImpl;
import gameoflife.model.Cell;
import gameoflife.model.Field;
import gameoflife.model.Point;

public class GameOfLifeTest {
	Map<Point, Cell> map;
	GameControllerImpl gameController;

	@Before
	public void setUp() {
		map = new HashMap<Point, Cell>();
		gameController = new GameControllerImpl(50, 50, map);
	}

	@Test
	public void addCellTest() {
		Cell cell = new Cell();
		cell.setCellState(CellState.ALIVE);
		Point point = new Point(5,6);
		gameController.addCell(cell, point);
		Assert.assertTrue(map.containsKey(point));
	}

	@Test
	public void addCellFailureTest() {
		Point point2 = new Point(6,6);
		Assert.assertFalse(map.containsKey(point2));
	}

	@Test
	public void removeCellTest() {
		Cell cell = new Cell();
		cell.setCellState(CellState.ALIVE);

		Point point = new Point(5,6);

		gameController.addCell(cell, point);
		gameController.removeCell(point);

		Assert.assertTrue(!map.containsKey(point));
	}

	@Test
	public void underPopulationTest() {
		Cell cell = new Cell();
		cell.setCellState(CellState.ALIVE);

		Point point = new Point(5,6);

		gameController.addCell(cell, point);
		gameController.step();

		Assert.assertFalse(map.containsKey(point));
	}

	@Test
	public void cellRemainsTest() {
		Cell cell = new Cell();
		cell.setCellState(CellState.ALIVE);
		Cell cell2 = new Cell();
		cell2.setCellState(CellState.ALIVE);
		Cell cell3 = new Cell();
		cell3.setCellState(CellState.ALIVE);

		Point point = new Point(5,6);
		Point point2 = new Point(4,6);
		Point point3 = new Point(6,6);

		gameController.addCell(cell, point);
		gameController.addCell(cell2, point2);
		gameController.addCell(cell3, point3);
		gameController.step();
		Assert.assertTrue(map.containsKey(point));
	}

	@Test
	public void overPopulationTest() {
		Cell cell = new Cell();
		cell.setCellState(CellState.ALIVE);
		Cell cell2 = new Cell();
		cell2.setCellState(CellState.ALIVE);
		Cell cell3 = new Cell();
		cell3.setCellState(CellState.ALIVE);
		Cell cell4 = new Cell();
		cell4.setCellState(CellState.ALIVE);
		Cell cell5 = new Cell();
		cell5.setCellState(CellState.ALIVE);

		Point point1 = new Point(5,6);
		Point point2 = new Point(5,5);
		Point point3 = new Point(4,6);
		Point point4 = new Point(6,6);
		Point point5 = new Point(5,7);

		gameController.addCell(cell, point1);
		gameController.addCell(cell2, point2);
		gameController.addCell(cell3, point3);
		gameController.addCell(cell4, point4);
		gameController.addCell(cell5, point5);
		gameController.step();
		Assert.assertFalse(map.containsKey(point1));
	}

	@Test
	public void afterOneStepTest() {
		Cell cell = new Cell();
		cell.setCellState(CellState.ALIVE);
		Cell cell2 = new Cell();
		cell2.setCellState(CellState.ALIVE);
		Cell cell3 = new Cell();
		cell3.setCellState(CellState.ALIVE);
		Cell cell4 = new Cell();
		cell4.setCellState(CellState.ALIVE);
		Cell cell5 = new Cell();
		cell5.setCellState(CellState.ALIVE);

		Point point1 = new Point(5,6);
		Point point2 = new Point(5,5);
		Point point3 = new Point(4,6);
		Point point4 = new Point(6,6);
		Point point5 = new Point(5,7);

		gameController.addCell(cell, point1);
		gameController.addCell(cell2, point2);
		gameController.addCell(cell3, point3);
		gameController.addCell(cell4, point4);
		gameController.addCell(cell5, point5);
		gameController.step();

		Point bornPoint1 = new Point(6,7);
		Point bornPoint2 = new Point(6,5);
		Point bornPoint3 = new Point(4,5);
		Point bornPoint4 = new Point(4,7);

		Assert.assertTrue(map.containsKey(bornPoint1));
		Assert.assertTrue(map.containsKey(bornPoint2));
		Assert.assertTrue(map.containsKey(bornPoint3));
		Assert.assertTrue(map.containsKey(bornPoint4));

		Assert.assertTrue(map.containsKey(point4));
		Assert.assertTrue(map.containsKey(point2));
		Assert.assertTrue(map.containsKey(point3));
		Assert.assertTrue(map.containsKey(point5));

		Assert.assertFalse(map.containsKey(point1));
	}

	@Test
	public void fieldNullTest() {
		Field field = new Field(50,50);
		Assert.assertNull(field.getCellMap());
	}

	@Test(expected = NullPointerException.class)
	public void notInitializedMapTest() throws Exception {
		Field field = new Field(50,50);
		Cell cell = new Cell();
		cell.setCellState(CellState.ALIVE);
		Point point = new Point(5,5);
		field.addCell(cell, point);
		Assert.assertEquals(0, field.numberOfGenerations(point));
	}

	@Test
	public void nullNeighboursTest() {
		Field field = new Field(50,50);
		field.setCellMap(map);
		Cell cell = new Cell();
		cell.setCellState(CellState.ALIVE);
		Point point = new Point(5,5);
		field.addCell(cell, point);
		Assert.assertEquals(0, field.numberOfGenerations(point));
	}

	@Test
	public void markDeadCellTest() {
		Field field = new Field(50,50);
		field.setCellMap(map);
		Cell cell = new Cell();
		Point point = new Point(5,5);
		field.addCell(cell, point);
		field.markDeadCell(point);
		Assert.assertEquals(CellState.DEAD, map.get(point).getCellState());
	}

	@Test
	public void changeBornToAliveTest() {
		Field field = new Field(50,50);
		field.setCellMap(map);
		Cell cell = new Cell();
		cell.setCellState(CellState.BORN);
		Point point = new Point(5,5);
		field.addCell(cell, point);
		field.changeBornToAlive();
		Assert.assertEquals(CellState.ALIVE, map.get(point).getCellState());
	}
}
