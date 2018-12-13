package cs361.battleships.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    private Board board;

    @Before
    public void setUp() {
        board = new Board();
    }

    @Test
    public void testInvalidPlacement() {
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
    }

    @Test
    public void testPlaceMinesweeper() {
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 1, 'A', true));
    }

    @Test
    public void testAttackEmptySquare() {
        board.placeShip(new Ship("MINESWEEPER"), 1, 'A', true);
        Result result = board.attack(2, 'E');
        assertEquals(AtackStatus.MISS, result.getResult());
    }

    @Test
    public void testAttackShip() {
        Ship minesweeper = new Ship("MINESWEEPER");
        board.placeShip(minesweeper, 1, 'A', true);
        minesweeper = board.getShips().get(0);
        Result result = board.attack(1, 'A');
        assertEquals(AtackStatus.SURRENDER, result.getResult());
    }

    @Test
    public void testMoveFleet(){
        Ship minesweeper = new Ship("MINESWEEPER");
        board.placeShip(minesweeper, 2, 'B', false);
        minesweeper = board.getShips().get(0);
//        Result result = board.moveFleet("north");
        board.moveFleet("north");
        assertEquals(1, minesweeper.getOccupiedSquares().get(0).getRow());
    }


    @Test
    public void testAttackSameSquareMultipleTimes() {
        Ship minesweeper = new Ship("MINESWEEPER");
        board.placeShip(minesweeper, 1, 'A', true);
        board.attack(1, 'A');
        Result result = board.attack(1, 'A');
        assertEquals(AtackStatus.INVALID, result.getResult());
    }

    @Test
    public void testAttackSameEmptySquareMultipleTimes() {
        Result initialResult = board.attack(1, 'A');
        assertEquals(AtackStatus.MISS, initialResult.getResult());
        Result result = board.attack(1, 'A');
        assertEquals(AtackStatus.MISS, result.getResult());
    }

    @Test
    public void testSurrender() {
        board.placeShip(new Ship("MINESWEEPER"), 1, 'A', true);
        board.attack(1, 'A');
        var result = board.attack(2, 'A');
        assertEquals(AtackStatus.HIT, result.getResult());
    }

    @Test
    public void testPlaceMultipleShipsOfSameType() {
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 1, 'A', true));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 5, 'D', true));

    }

    @Test   // Changed this up for 4 ships
    public void testCantPlaceMoreThan4Ships() {
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 1, 'A', true));
        assertTrue(board.placeShip(new Ship("BATTLESHIP"), 5, 'D', true));
        assertTrue(board.placeShip(new Ship("DESTROYER"), 6, 'A', false));
        assertTrue(board.placeShip(new Ship("SUBMARINE"), 3, 'B', false));
        assertFalse(board.placeShip(new Ship(""), 6, 'F', false));

    }

    @Test
    public void isCQHit() {
        Square square1 = new Square(1, 'A');
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 1, 'A', false);
        board.attack(1, 'A');
        assertFalse(square1.isHitCQ());
    }

    @Test
    public void isCQHit2() {
        Square square1 = new Square(3, 'C');
        Board board = new Board();
        board.placeShip(new Ship("BATTLESHIP"), 3, 'C', false);
        board.attack(3, 'C');
        assertFalse(square1.isHitCQ());
    }

    @Test
    public void testAttackLaserReady(){
        Ship minesweeper = new Ship("MINESWEEPER");
        Square square = new Square(1, 'A');
        minesweeper.place('A', 1, false);
        assertFalse(square.isHitCQ());

        Ship battleship = new Ship("BATTLESHIP");
        Square square2 = new Square(5, 'A');
        battleship.place('A', 5, false);
        assertFalse(square2.isHitCQ());

        Board board = new Board();

        board.getShips().add(0, minesweeper);
        board.getShips().add(1, battleship);

        var result = minesweeper.attack(1,'A');
        var test = minesweeper.getShipSunk();
        var test2 = board.getLaserReady();
        var test3 = board.attack(square.getRow(), square.getColumn());

        assertEquals(true, test2);
    }
}
