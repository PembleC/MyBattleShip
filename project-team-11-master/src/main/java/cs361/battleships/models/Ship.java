package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import com.mchange.v1.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Ship {


	@JsonProperty private String kind;
	@JsonProperty private List<Square> occupiedSquares;
	@JsonProperty private int occupiedSquaresLength;
	@JsonProperty private int size;
	@JsonProperty private int submerged;
	@JsonProperty private boolean isVertical;
	@JsonProperty List<Result> sunkShip;
	boolean shipSunk = false;

	int sunkCounter = 0;



	public Ship() {
		occupiedSquares = new ArrayList<>();
	}

	public Ship(String kind) {
		this();
		this.occupiedSquaresLength = 0;
		this.kind = kind;
		switch (kind) {
			case "MINESWEEPER":
				size = 2;
				submerged = 0;
				break;
			case "DESTROYER":
				size = 3;
				submerged = 0;
				break;
			case "BATTLESHIP":
				size = 4;
				submerged = 0;
				break;
            case "SUBMARINE":
                size = 4;
				submerged = 1;
                break;
		}
	}

	public List<Square> getOccupiedSquares() {
		return occupiedSquares;
	}

	public void place(char col, int row, boolean isVertical) {
		for (int i = 0; i < size; i++) {
			if (isVertical) {
                if(kind.equals("SUBMARINE") && i == 2){
                    Square mySquareExtra = new Square(row+i, (char) (col+1));
                    occupiedSquares.add(mySquareExtra);
                    occupiedSquaresLength++;
                }

				Square mySquare = new Square(row+i, col);

				if(size == 2 && i == 0){
					mySquare.setCQ(true);
					mySquare.setHitCQ(true);
				}
				if(size == 3 && i == 1){
					mySquare.setCQ(true);
					mySquare.setHitCQ(false);
				}
				if(size == 4 && i == 2){
					if(kind.equals("BATTLESHIP")) {
						mySquare.setCQ(true);
						mySquare.setHitCQ(false);
					}
				}
				else if(size == 4 && i == 3){
					if(kind.equals("SUBMARINE")){
						mySquare.setCQ(true);
						mySquare.setHitCQ(false);
					}
				}
				occupiedSquares.add(mySquare);
				occupiedSquaresLength++;
			}

			else {
			    if(kind.equals("SUBMARINE") && i == 2){
                    Square mySquareExtra = new Square(row-1, (char) (col + i));
                    occupiedSquares.add(mySquareExtra);
                    occupiedSquaresLength++;
                }

				Square mySquare = new Square(row, (char) (col + i));

				if(size == 2 && i == 0){
					mySquare.setCQ(true);
					mySquare.setHitCQ(true);
				}
				else if(size == 3 && i == 1){
					mySquare.setCQ(true);
					mySquare.setHitCQ(false);
				}
                else if(size == 4 && i == 2){
                	if(kind.equals("BATTLESHIP")) {
						mySquare.setCQ(true);
						mySquare.setHitCQ(false);
					}
                }
                else if(size == 4 && i == 3){
                	if(kind.equals("SUBMARINE")){
						mySquare.setCQ(true);
						mySquare.setHitCQ(false);
					}
                }
				occupiedSquares.add(mySquare);
				occupiedSquaresLength++;

			}
		}
	}

	public void placeSubmerged(char col, int row, boolean isVertical) {
		for (int i = 0; i < size; i++) {
			if (isVertical) {
				if(i == 2){
					Square mySquareExtra = new Square(row+i, (char) (col+1));
					occupiedSquares.add(mySquareExtra);
					occupiedSquaresLength++;
				}

				Square mySquare = new Square(row+i, col);

				if(i == 3){
					mySquare.setCQ(true);
					mySquare.setHitCQ(false);
				}

				occupiedSquares.add(mySquare);
				occupiedSquaresLength++;
			}

			else {
				if(i == 2){
					Square mySquareExtra = new Square(row-1, (char) (col + i));
					occupiedSquares.add(mySquareExtra);
					occupiedSquaresLength++;
				}

				Square mySquare = new Square(row, (char) (col + i));

				if(i == 3){
					mySquare.setCQ(true);
					mySquare.setHitCQ(false);
				}

				occupiedSquares.add(mySquare);
				occupiedSquaresLength++;
			}
		}
	}


	public boolean overlaps(Ship other) {
		Set<Square> thisSquares = Set.copyOf(getOccupiedSquares());
		Set<Square> otherSquares = Set.copyOf(other.getOccupiedSquares());
		Sets.SetView<Square> intersection = Sets.intersection(thisSquares, otherSquares);
		return intersection.size() != 0;
	}

	public boolean isAtLocation(Square location) {
		return getOccupiedSquares().stream().anyMatch(s -> s.equals(location));
	}

	public String getKind() {
		return kind;
	}


	public Result attack(int x, char y) {
		var attackedLocation = new Square(x, y);
		var square = getOccupiedSquares().stream().filter(s -> s.equals(attackedLocation)).findFirst();
		if (!square.isPresent()) {
			return new Result(attackedLocation);
		}


		var attackedSquare = square.get();

		if (attackedSquare.isHit()) {

			var result = new Result(attackedLocation);
			result.setResult(AtackStatus.INVALID);
			return result;
		}

		if(attackedSquare.isCQ() && !attackedSquare.isHitCQ()){
			attackedSquare.setHitCQ(true);
			var result = new Result(attackedLocation);
			result.setResult(AtackStatus.HITCQ);
			return result;
		}
		else if(attackedSquare.isCQ() && attackedSquare.isHitCQ()){
			var result = new Result(attackedLocation);
			attackedSquare.hit();

			setShipSunk(true);//added so a ship has been sunk and the counter is now true

			result.setResult(AtackStatus.SUNK);
			sunkCounter = sunkCounter + 1;
			return result;
		}

		attackedSquare.hit();
		var result = new Result(attackedLocation);
		result.setShip(this);

		result.setResult(AtackStatus.HIT);


		return result;
	}

	@JsonIgnore
	public boolean isSunk() {
		return (getOccupiedSquares().stream().allMatch(s -> s.isHit())) || (getOccupiedSquares().stream().anyMatch(s -> s.isHit() && s.isCQ()));
	}


	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Ship)) {
			return false;
		}
		var otherShip = (Ship) other;

		return this.kind.equals(otherShip.kind)
				&& this.size == otherShip.size
				&& this.occupiedSquares.equals(otherShip.occupiedSquares);
	}

	@Override
	public int hashCode() {
		return 33 * kind.hashCode() + 23 * size + 17 * occupiedSquares.hashCode();
	}

	@Override
	public String toString() {
		return kind + occupiedSquares.toString();
	}

	public int getSubmerged() {
		return submerged;
	}

	public void setSubmerged(int submerged) {
		this.submerged = submerged;
	}
  
	public int getSize() {return size;}

	public void setOrientation(boolean vertical){
		if(vertical == true){
			this.isVertical = true;
		} else{
			this.isVertical = false;
		}
	}

	public boolean getOrientation() {return isVertical;}
	public void setShipSunk(boolean i){ shipSunk = i;}
	public boolean getShipSunk(){return shipSunk;}

}
