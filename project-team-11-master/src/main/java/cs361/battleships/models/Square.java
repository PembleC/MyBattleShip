package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class Square {

	@JsonProperty private int row;
	@JsonProperty private char column;
	@JsonProperty private boolean hit = false;
	@JsonProperty private boolean isCQ = false;
	@JsonProperty private boolean hitCQ = false;

	public Square() {
	}

	public Square(int row, char column) {
		this.row = row;
		this.column = column;
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Square) {
			return ((Square) other).row == this.row && ((Square) other).column == this.column;
		}
		return false;
	}

	public void updateRow(int newRow){
		this.row = newRow;
	}

	public void updateCol(char newCol){
		this.column = newCol;
	}

	@Override
	public int hashCode() {
		return 31 * row + column;
	}

	@JsonIgnore
	public boolean isOutOfBounds() {
		return row > 10 || row < 1 || column > 'J' || column < 'A';
	}

	public boolean isHit() {
		return hit;
	}

	public void hit() {
		hit = true;
	}

	@Override
	public String toString() {
		return "(" + row + ", " + column + ')';
	}

	public void setCQ(boolean CQ) {
		isCQ = CQ;
	}

	public boolean isCQ() {
		return isCQ;
	}

	public boolean isHitCQ() {
		return hitCQ;
	}

	public void setHitCQ(boolean hitCQ) {
		this.hitCQ = hitCQ;
	}


}
