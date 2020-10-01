var isSetup = true;
var placedShips = 0;
var game;
var shipType;
var vertical;
var submerge;
//******Sonar*******
var numSonars = 2; //number of Sonars
var sonarStatus = false; //allowed to use sonar or not
var sonarInUse = false; //sonar is currently being used
//***** Move Fleet *****
var numMoveFleet = 2; // times you can move
var theDirection;


function makeGrid(table, isPlayer) {
    for (i=0; i<10; i++) {
        let row = document.createElement('tr');
        for (j=0; j<10; j++) {
            let column = document.createElement('td');
            column.addEventListener("click", cellClick);
            row.appendChild(column);
        }
        table.appendChild(row);
    }
}

function markHits(board, elementId, surrenderText) {
    board.attacks.forEach((attack) => {
        let className;
        if (attack.result === "MISS"){
            className = "miss";
        }

        else if (attack.result === "HIT"){
            className = "hit";
        }

        else if (attack.result === "HITCQ"){
            className = "hitCQ";
        }

        else if (attack.result === "SUNK"){
            className = "sink"
            if(elementId == "player"){
                opponentShipsSunk++;
            }
            else if(elementId == "opponent"){
                playerShipsSunk++;
            }
        }

        else if (attack.result === "SURRENDER"){
            className = "sink"
            if(elementId == "player"){
                opponentShipsSunk++;
                }
            else if(elementId == "opponent"){
                playerShipsSunk++;
            }
            alert(surrenderText);
            }
        document.getElementById(elementId).rows[attack.location.row-1].cells[attack.location.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add(className);
    });
}

//Serg mod: added three new parameters which are used in displaySonar()
function redrawGrid(theRow, theCol, sonarCondition) {
    Array.from(document.getElementById("opponent").childNodes).forEach((row) => row.remove());
    Array.from(document.getElementById("player").childNodes).forEach((row) => row.remove());
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    if (game === undefined) {
        return;
    }

    game.playersBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => {
        document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("occupied");

    }));

    markHits(game.opponentsBoard, "opponent", "Congrats, You won the game!");
    markHits(game.playersBoard, "player", "Sorry, You lost the game.");

    displaySonar(game.opponentsBoard, "opponent", theRow, theCol, sonarCondition);

}

var oldListener;
function registerCellListener(f) {
    let el = document.getElementById("player");
    for (i=0; i<10; i++) {
        for (j=0; j<10; j++) {
            let cell = el.rows[i].cells[j];
            cell.removeEventListener("mouseover", oldListener);
            cell.removeEventListener("mouseout", oldListener);
            cell.addEventListener("mouseover", f);
            cell.addEventListener("mouseout", f);
        }
    }
    oldListener = f;
}

//----------Team11Code----------
//Reset everything when you refresh the page
window.onload = function(){
    document.getElementById('score').innerHTML = 0;
    document.getElementById('playerSunkShip').innerHTML = 0;
    document.getElementById('opponentSunkShip').innerHTML = 0;
    document.getElementById("useSonarPulseButton").style.display = "none";

    document.getElementById('subword').style.display = "none";

    document.getElementById("moveFleetNorthButton").style.display = "none";
    document.getElementById("moveFleetEastButton").style.display = "none";      // Hide ship moves to start
    document.getElementById("moveFleetWestButton").style.display = "none";
    document.getElementById("moveFleetSouthButton").style.display = "none";

}

//----------Team11Code-----------
var shipClicks = 0;
var clicks = 0;
// Carson's Ship Sunk counter
var playerShipsSunk = 0;
var opponentShipsSunk = 0;
// Tells us if we should display message
var spaceLaserAlert = 0;
// Tells us if we should display message
var moveFleetAlert = 0;

// Hide the Ship Placement buttons after you placed them
function hideShipPlacers() {
    document.getElementById("shipMenu2").style.display = "none";
}


//hide sonar pulse button
function hideSonarPulse() {
    document.getElementById("useSonarPulseButton").style.display = "none";
}

// Show the Sonar Pulse Weapon once you have sunk a ship
function showSonarPulse() {
    document.getElementById("useSonarPulseButton").style.display = "block";
}

    //if sonar button clicked the sonarStatus becomes true(used in cellClick())
    document.getElementById("useSonarPulseButton").addEventListener("click", enableSonar);

function enableSonar(){
    sonarStatus = true;
}


//Carson's hide move fleet
function hideMoveFleet() {
    document.getElementById("moveFleetNorthButton").style.display = "none";
    document.getElementById("moveFleetEastButton").style.display = "none";
    document.getElementById("moveFleetWestButton").style.display = "none";
    document.getElementById("moveFleetSouthButton").style.display = "none";
}


// Carson's show the Move Fleet Button once you have sunk 2 ships
function showMoveFleet() {
    document.getElementById("moveFleetNorthButton").style.display = "block";
    document.getElementById("moveFleetEastButton").style.display = "block";
    document.getElementById("moveFleetWestButton").style.display = "block";
    document.getElementById("moveFleetSouthButton").style.display = "block";
}

// Arin's hide the submerge options
function hideSubmeregeButton(){
    document.getElementById('subcheck').style.display = "none";
    document.getElementById('subword').style.display = "none";
}

// Arin's show the submerge  options when the sub is selected
function showSubmeregeButton(){
    document.getElementById('subcheck').style.display = "block";
    document.getElementById('subword').style.display = "block";
}

     document.getElementById("moveFleetNorthButton").onclick = function() {shiftFleet("north")};
     document.getElementById("moveFleetEastButton").onclick = function() {shiftFleet("east")};
     document.getElementById("moveFleetWestButton").onclick = function() {shiftFleet("west")};
     document.getElementById("moveFleetSouthButton").onclick = function() {shiftFleet("south")};

//serg function: shifts all ships by 1 block in a particular direction
function shiftFleet(theDirection){

     game.playersBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => {

//          if(square.getRow() !=)

          if(document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.contains("hit") == true){
               document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.remove("hit");
          }else if(document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.contains("hitCQ") == true){
               document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.remove("hitCQ");
          }else if(document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.contains("sink") == true){
               document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.remove("sink");
          }else{
               document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.remove("occupied");
          }

          }));

        sendXhr("POST", "/moveFleet", {game: game, direction: theDirection}, function(data) {
             game = data;


             redrawGrid();

        });

}



function cellClick() {
//----------Team11Code----------

    //Serg code: used in redRawGrid() in displaySonar()
    let aRow = this.parentNode.rowIndex;
    let aCol = this.cellIndex;


    // -- Carson changed this from Serg's previous counter
    if(placedShips < 4){    // Adjusted for 4 ships - Carson P
        shipClicks++
    }
    else{
        clicks++;
        document.getElementById('score').innerHTML = clicks;
    }
// Change to space laser
if(playerShipsSunk == 1){
    if (spaceLaserAlert == 0){
        spaceLaserAlert = 1;
        alert("Captain, we have received activation codes for the Space Laser.");
    }
}


//-------- Carson's Display # of ships sunk

if(playerShipsSunk > 1){
    playerShipsSunk--;
}
if(playerShipsSunk > 2){
    playerShipsSunk--;
}
if(playerShipsSunk > 3){
    playerShipsSunk--;
}
if(playerShipsSunk > 4){
playerShipsSunk = 4;
}
document.getElementById('playerSunkShip').innerHTML = playerShipsSunk;


if(opponentShipsSunk > 1){
    opponentShipsSunk--;
}
if(opponentShipsSunk > 2){
    opponentShipsSunk--;
}
if(opponentShipsSunk > 3){
    opponentShipsSunk--;
}
if(opponentShipsSunk > 4){
opponentShipsSunk = 4;
}
document.getElementById('opponentSunkShip').innerHTML = opponentShipsSunk;

// Remove the ship buttons
if(placedShips > 3){        // Adjusted for 4 ships - Carson P
    hideShipPlacers();
}

      //hide sonarPulse button
    if(numSonars <= 0){
        hideSonarPulse();
    }
    //display sonarPulse button
    if(playerShipsSunk > 0 && numSonars >= 1){
        showSonarPulse();
    }

    // Button appearance and removal of Move Fleet
    if(playerShipsSunk > 1 && numMoveFleet >= 1){
        showMoveFleet();
    }
    if(numMoveFleet <= 0){
        hideMoveFleet();
    }


  // Explain that you can move the ships now
  if(playerShipsSunk == 2){
      if (moveFleetAlert == 0){
          moveFleetAlert = 1;
          alert("Captain, the fleet is ready to move at your command.");
      }
  }

//----------Team11Code----------
    let row = this.parentNode.rowIndex + 1;
    let col = String.fromCharCode(this.cellIndex + 65);
    //Serg mod: added new redrawGrid()
    if (isSetup) {
        sendXhr("POST", "/place", {game: game, shipType: shipType, x: row, y: col, isVertical: vertical}, function(data) {
            game = data;
//            redrawGrid();
             redrawGrid(aRow, aCol, sonarInUse);
            placedShips++;
            if (placedShips == 4) {     // Adjusted for 4 ships - Carson P
                isSetup = false;
                registerCellListener((e) => {});
            }
        });
    } else {
             //Serg mod
             if(sonarStatus == true){

             sendXhr("POST", "/sonarPulseAttack", {game: game, x: row, y: col}, function(data){
                 game = data;
                 sonarInUse = true;

                 redrawGrid(aRow, aCol, sonarInUse);

                 sonarInUse = false;
                 sonarStatus = false;
                 CLDeleteActive = true;
                 numSonars--;

             });

             }else{

             sendXhr("POST", "/attack", {game: game, x: row, y: col}, function(data) {
             game = data;
             redrawGrid(aRow, aCol, sonarInUse);
             //redrawGrid();
         })
         }
    }
}

//Serg code
function displaySonar(board, elementId, theRow, theCol, sonarCondition){

    let className1 = "spotted";
    let className2 = "empty";


    //used in "sonar empty loop"
    var startLocationI = theRow - 1;
    var startLocationJ = theCol - 1;

    var numSonarHits = JSON.parse(board.numSonarHits);

    //Sonar Hits Loop: Displays all locations in the sonar which contain a ship location. The locations are
    //displayed every time redRawGrid() is called
    if(numSonarHits != 0){
        for(i = 0; i < numSonarHits; i++){
             if(document.getElementById(elementId).rows[board.sonarHits[i].row-1].cells[board.sonarHits[i].column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.contains("hit") != true &&
              document.getElementById(elementId).rows[board.sonarHits[i].row-1].cells[board.sonarHits[i].column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.contains("hitCQ") != true &&
              document.getElementById(elementId).rows[board.sonarHits[i].row-1].cells[board.sonarHits[i].column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.contains("sink") != true){

             document.getElementById(elementId).rows[board.sonarHits[i].row-1].cells[board.sonarHits[i].column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add(className1);

             }
        }
    }

    //used in "sonar empty loop"
    endLocationI = startLocationI + 3;
    endLocationJ = startLocationJ + 3;

    //for edge case
    if(startLocationI == 8){
        endLocationI--;
    }
    //for edge case
    if(startLocationJ == 8){
        endLocationJ--;
    }


    //Sonar Empty Loop: this loop displays all of the locations in the sonar range which were not "spotted"
    //by assigning them className2("empty"). The locations are only displayed when the sonar is being used
    if(sonarCondition == true){
    for(i = startLocationI; i < endLocationI; i++){
          //fore edge case
          if(i == -1){
            i++;
          }

        for(j = startLocationJ; j < endLocationJ; j++){
            //for edge case
            if(j == -1){
                j++;
            }

            if(document.getElementById(elementId).rows[i].cells[j].classList.contains(className1) != true &&
               document.getElementById(elementId).rows[i].cells[j].classList.contains("hit") != true &&
               document.getElementById(elementId).rows[i].cells[j].classList.contains("hitCQ") != true &&
               document.getElementById(elementId).rows[i].cells[j].classList.contains("sink") != true){

                document.getElementById(elementId).rows[i].cells[j].classList.add(className2);

            }


        }
    }
    }

}

function sendXhr(method, url, data, handler) {
    var req = new XMLHttpRequest();
    req.addEventListener("load", function(event) {
        if (req.status != 200) {
            if(playerShipsSunk>0)
                playerShipsSunk++;
            alert("Cannot complete the action");
            return;
        }
        handler(JSON.parse(req.responseText));
    });
    req.open(method, url);
    req.setRequestHeader("Content-Type", "application/json");
    req.send(JSON.stringify(data));
}

function place(size) {
    return function() {
        let row = this.parentNode.rowIndex;
        let col = this.cellIndex;
        vertical = document.getElementById("is_vertical").checked;
        let table = document.getElementById("player");
        for (let i=0; i<size; i++) {
            let cell;
            if(vertical) {
                let tableRow = table.rows[row+i];
                if (tableRow === undefined) {
                    // ship is over the edge; let the back end deal with it
                    break;
                }
                cell = tableRow.cells[col];
            } else {
                cell = table.rows[row].cells[col+i];
            }
            if (cell === undefined) {
                // ship is over the edge; let the back end deal with it
                break;
            }
            cell.classList.toggle("placed");
        }
    }
}

function initGame() {
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);

    document.getElementById("place_minesweeper").addEventListener("click", function(e) {
        shipType = "MINESWEEPER";
       registerCellListener(place(2));
       hideSubmeregeButton();
    });
    document.getElementById("place_destroyer").addEventListener("click", function(e) {
        shipType = "DESTROYER";
       registerCellListener(place(3));
       hideSubmeregeButton();
    });
    document.getElementById("place_battleship").addEventListener("click", function(e) {
        shipType = "BATTLESHIP";
       registerCellListener(place(4));
       hideSubmeregeButton();
    });
    // WE NEED TO FIGURE THIS PART OUT - Carson
    document.getElementById("place_submarine").addEventListener("click", function(e) {
            shipType = "SUBMARINE";
           registerCellListener(place(5));
          showSubmeregeButton();
    });

    sendXhr("GET", "/game", {}, function(data) {
        game = data;
    });
};


