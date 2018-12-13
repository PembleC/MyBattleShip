package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Board {

	@JsonProperty private List<Ship> ships;
	@JsonProperty private List<Result> attacks;

	//@JsonProperty private List<Result> attacks2;

	@JsonProperty private List<Square> sonarHits; //for Sonar: holds the Square(s) which have been located by Sonar
	@JsonProperty private int numSonarHits; //for Sonar: number of squares located by Sonar (used in JS)


	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		ships = new ArrayList<>();
		attacks = new ArrayList<>();


		sonarHits = new ArrayList<>();
		numSonarHits = 0;

	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		if (ships.size() >= 4) { // Need to make this 4 some how -Carson
			return false;
		}
		if (ships.stream().anyMatch(s -> s.getKind().equals(ship.getKind()))) {
			return false;
		}
		final var placedShip = new Ship(ship.getKind());
		placedShip.place(y, x, isVertical);
		if (ships.stream().anyMatch(s -> s.overlaps(placedShip))) {
			return false;
		}
		if (placedShip.getOccupiedSquares().stream().anyMatch(s -> s.isOutOfBounds())) {
			return false;
		}
		placedShip.setOrientation(isVertical);
		ships.add(placedShip);
		return true;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		Result attackResult = attack(new Square(x, y));
		attacks.add(attackResult);



		return attackResult;
	}



	//added

	private Result attack(Square s) {
		if (attacks.stream().anyMatch(r -> r.getLocation().equals(s))) {


			if(s.isHitCQ()) {

				var attackResult = new Result(s);
				attackResult.setResult(AtackStatus.SUNK);
				return attackResult;
			}
		}

		var shipsAtLocation = ships.stream().filter(ship -> ship.isAtLocation(s)).collect(Collectors.toList());
		if (shipsAtLocation.size() == 0) {
			var attackResult = new Result(s);
			return attackResult;
		}
		var hitShip = shipsAtLocation.get(0);
		var attackResult = hitShip.attack(s.getRow(), s.getColumn());

		if (attackResult.getResult() == AtackStatus.SUNK) {
			if (ships.stream().allMatch(ship -> ship.isSunk())) {
				attackResult.setResult(AtackStatus.SURRENDER);
			}
		}
		return attackResult;
	}

	List<Ship> getShips() {
		return ships;
	}


	//Sonar Pulse written by Serg
	public void sonarPulse(int x, char y){

		for(int i = 0; i < ships.size(); i++){
			Ship currentShip = ships.get(i);
			List<Square> currentOccupiedSquares = currentShip.getOccupiedSquares();

			for(int j = 0; j < currentShip.getSize(); j++){

				char columnLoc = currentOccupiedSquares.get(j).getColumn();
				int rowLoc = currentOccupiedSquares.get(j).getRow();

				char lowerBoundCol = (char)(y-1);
				char upperBoundCol = (char)(y+1);

				int lowerBoundRow = x - 1;
				int upperBoundRow = x + 1;

				if((lowerBoundCol <= columnLoc && columnLoc <= upperBoundCol) && (lowerBoundRow <= rowLoc && rowLoc <= upperBoundRow)){
					sonarHits.add(currentOccupiedSquares.get(j));
				}
			}
		}

		if(sonarHits != null){
			numSonarHits = sonarHits.size();
		}


	}


	//Move Fleet written by Serg
	public void moveFleet(String direction) {


		for (int i = 0; i < ships.size(); i++) {
			Ship currentShip = ships.get(i);
			List<Square> currentOccupiedSquares = currentShip.getOccupiedSquares();


			for (int j = 0; j < currentShip.getSize(); j++) {

				int row = currentOccupiedSquares.get(j).getRow();
				char col = currentOccupiedSquares.get(j).getColumn();


				if (direction.equals("north")) {

					//check if vertical (getOrientation returns true if vertical)
					if (currentShip.getOrientation() == true) {
						if ((row - j) != 1) {
							row--;

							ships.get(i).getOccupiedSquares().get(j).updateRow(row);

						}
					} else { //horizontal
						if (row != 1) {
							row--;

							ships.get(i).getOccupiedSquares().get(j).updateRow(row);
						}
					}

				} else if (direction.equals("east")) {

					//check if vertical
					if (currentShip.getOrientation() == true) {
						if (col != 74) {
							col = (char) (col + 1);

							ships.get(i).getOccupiedSquares().get(j).updateCol(col);
						}

					} else { //horizontal
						if (((col - 1) + (currentShip.getSize() - j)) != 74) {
							col = (char) (col + 1);

							ships.get(i).getOccupiedSquares().get(j).updateCol(col);
						}
					}

				} else if (direction.equals("south")) {

					//check if vertical
					if (currentShip.getOrientation() == true) {
						if ((row - 1 + (currentShip.getSize() - j)) != 10) {
							row++;
							ships.get(i).getOccupiedSquares().get(j).updateRow(row);
						}
					} else { //horizontal
						if (row != 10) {
							row++;
							ships.get(i).getOccupiedSquares().get(j).updateRow(row);
						}
					}
				} else if (direction.equals("west")) {

					//check if vertical
					if (currentShip.getOrientation() == true) {
						if (col != 65) {
							col = (char) (col - 1);
							ships.get(i).getOccupiedSquares().get(j).updateCol(col);
						}
					} else { //horizontal
						if ((col - j) != 65) {
							col = (char) (col - 1);
							ships.get(i).getOccupiedSquares().get(j).updateCol(col);
						}
					}
				}


			}
		}
	}

	@JsonIgnore
	public boolean getLaserReady() {
		if(ships.stream().anyMatch(ship -> ship.getShipSunk())){
			return true;
		}
		else
			return false;
	}
}



