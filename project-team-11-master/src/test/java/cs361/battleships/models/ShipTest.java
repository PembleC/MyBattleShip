package cs361.battleships.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ShipTest {

    @Test
    public void testPlaceMinesweeperHorizontaly() {
        Ship minesweeper = new Ship("MINESWEEPER");
        minesweeper.place('A', 1, false);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(1, 'B'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testPlaceMinesweeperVertically() {
        Ship minesweeper = new Ship("MINESWEEPER");
        minesweeper.place('A', 1, true);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(2, 'A'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testPlaceDestroyerHorizontaly() {
        Ship minesweeper = new Ship("DESTROYER");
        minesweeper.place('A', 1, false);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(1, 'B'));
        expected.add(new Square(1, 'C'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testPlaceDestroyerVertically() {
        Ship minesweeper = new Ship("DESTROYER");
        minesweeper.place('A', 1, true);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(2, 'A'));
        expected.add(new Square(3, 'A'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testPlaceBattleshipHorizontaly() {
        Ship minesweeper = new Ship("BATTLESHIP");
        minesweeper.place('A', 1, false);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(1, 'B'));
        expected.add(new Square(1, 'C'));
        expected.add(new Square(1, 'D'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testPlaceBattleshipVertically() {
        Ship minesweeper = new Ship("BATTLESHIP");
        minesweeper.place('A', 1, true);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(2, 'A'));
        expected.add(new Square(3, 'A'));
        expected.add(new Square(4, 'A'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testShipOverlaps() {
        Ship minesweeper1 = new Ship("MINESWEEPER");
        minesweeper1.place('A', 1, true);

        Ship minesweeper2 = new Ship("MINESWEEPER");
        minesweeper2.place('A', 1, true);

        assertTrue(minesweeper1.overlaps(minesweeper2));
    }

    @Test
    public void testShipsDontOverlap() {
        Ship minesweeper1 = new Ship("MINESWEEPER");
        minesweeper1.place('A', 1, true);

        Ship minesweeper2 = new Ship("MINESWEEPER");
        minesweeper2.place('C', 2, true);

        assertFalse(minesweeper1.overlaps(minesweeper2));
    }

    @Test
    public void testIsAtLocation() {
        Ship minesweeper = new Ship("BATTLESHIP");
        minesweeper.place('A', 1, true);

        assertTrue(minesweeper.isAtLocation(new Square(1, 'A')));
        assertTrue(minesweeper.isAtLocation(new Square(2, 'A')));
    }

    @Test
    public void testHit() {
        Ship minesweeper = new Ship("BATTLESHIP");
        minesweeper.place('A', 1, true);

        Result result = minesweeper.attack(1, 'A');
        assertEquals(AtackStatus.HIT, result.getResult());
        assertEquals(minesweeper, result.getShip());
        assertEquals(new Square(1, 'A'), result.getLocation());
    }

    @Test
    public void testSink() {
        Ship minesweeper = new Ship("MINESWEEPER");
        minesweeper.place('A', 1, true);

        minesweeper.attack(1, 'A');
        Result result = minesweeper.attack(2, 'A');

        assertEquals(AtackStatus.HIT, result.getResult());
        assertEquals(minesweeper, result.getShip());
        assertEquals(new Square(2, 'A'), result.getLocation());
    }

    @Test
    public void testOverlapsBug() {
        Ship minesweeper = new Ship("MINESWEEPER");
        Ship destroyer = new Ship("DESTROYER");
        minesweeper.place('C', 5, false);
        destroyer.place('C', 5, false);
        assertTrue(minesweeper.overlaps(destroyer));
    }

    @Test
    public void testAttackSameSquareTwice() {
        Ship minesweeper = new Ship("MINESWEEPER");
        minesweeper.place('A', 1, true);
        var result = minesweeper.attack(1, 'A');
        assertEquals(AtackStatus.SUNK, result.getResult());
    }

    @Test
    public void testEquals() {
        Ship minesweeper1 = new Ship("MINESWEEPER");
        minesweeper1.place('A', 1, true);
        Ship minesweeper2 = new Ship("MINESWEEPER");
        minesweeper2.place('A', 1, true);
        assertTrue(minesweeper1.equals(minesweeper2));
        assertEquals(minesweeper1.hashCode(), minesweeper2.hashCode());
    }

    @Test
    public void testCQisSet(){
        Ship minesweeper = new Ship("MINESWEEPER");
        minesweeper.place('A', 1, false);
        var result = minesweeper.attack('1', 'B');
        assertEquals(AtackStatus.MISS, result.getResult());
    }
    @Test
    public void testCQHitIsSetMINESWEEPER(){
        Ship minesweeper = new Ship("MINESWEEPER");
        Square square = new Square(1, 'A');
        minesweeper.place('A', 1, false);
        assertFalse(square.isHitCQ());

        var result = minesweeper.attack(1,'A');
        assertEquals(AtackStatus.SUNK, result.getResult());
    }
    @Test
    public void testCQHitISSetDESTROYER() {
        Ship destroyer = new Ship("DESTROYER");
        Square square = new Square(1, 'A');
        destroyer.place('A', 1, true);
        assertFalse(square.isHitCQ());

        var result = destroyer.attack(2, 'A');
        assertEquals(AtackStatus.HITCQ, result.getResult());
    }
    @Test
    public void testCQHitISSetBATTLESHIP() {
        Ship battleship = new Ship("BATTLESHIP");
        Square square = new Square(1, 'A');
        battleship.place('A', 1, true);
        assertFalse(square.isHitCQ());

        var result = battleship.attack(3, 'A');
        assertEquals(AtackStatus.HITCQ, result.getResult());
    }

    @Test
    public void testPlaceSubmarineHorizontaly() {
        Ship minesweeper = new Ship("SUBMARINE");
        minesweeper.place('A', 2, false);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(2, 'A'));
        expected.add(new Square(2, 'B'));
        expected.add(new Square(1, 'C'));
        expected.add(new Square(2, 'C'));
        expected.add(new Square(2, 'D'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testPlaceSubmarineVertically() {
        Ship minesweeper = new Ship("SUBMARINE");
        minesweeper.place('A', 1, true);
        List<Square> occupiedSquares = minesweeper.getOccupiedSquares();
        ArrayList<Object> expected = new ArrayList<>();
        expected.add(new Square(1, 'A'));
        expected.add(new Square(2, 'A'));
        expected.add(new Square(3, 'B'));
        expected.add(new Square(3, 'A'));
        expected.add(new Square(4, 'A'));
        assertEquals(expected, occupiedSquares);
    }

    @Test
    public void testCQHitISSetSUBMARINE() {
        Ship submarine = new Ship("SUBMARINE");
        Square square = new Square(1, 'A');
        submarine.place('A', 1, true);
        assertFalse(square.isHitCQ());

        var result = submarine.attack(4, 'A');
        assertEquals(AtackStatus.HITCQ, result.getResult());
    }


    @Test
    public void testShipSunk(){
        Ship minesweeper = new Ship("MINESWEEPER");
        Square square = new Square(1, 'A');
        minesweeper.place('A', 1, false);
        assertFalse(square.isHitCQ());

        var result = minesweeper.attack(1,'A');
        var test = minesweeper.getShipSunk();
        assertEquals(true, test);
    }

    @Test
    public void testLaserReady(){
        Ship minesweeper = new Ship("MINESWEEPER");
        Square square = new Square(1, 'A');
        minesweeper.place('A', 1, false);
        assertFalse(square.isHitCQ());

        Board board = new Board();

        board.getShips().add(0, minesweeper);

        var result = minesweeper.attack(1,'A');
        var test = minesweeper.getShipSunk();
        var test2 = board.getLaserReady();

        assertEquals(true, test2);
    }





}
