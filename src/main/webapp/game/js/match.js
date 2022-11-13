const canvas = document.getElementById('matchCanvas');
const ctx = canvas.getContext('2d');
canvas.width = 1050;
canvas.height = 680;


// PLAYING FIELD
const LINE_WIDTH = 5;
const LINE_COLOR = 'white'
const GOAL_LINE_WIDTH = LINE_WIDTH * 2;
const GOAL_COLOR = 'black';
const PENALTY_AREA_WIDTH = 165;
const PENALTY_AREA_HEIGHT = 400;
const SMALL_PENALTY_AREA_WIDTH = 55;
const SMALL_PENALTY_AREA_HEIGHT = 183;
const GOAL_HEIGHT = 73;
const PENALTY_SPOT_WIDTH = 110;
const CENTRE_CIRCLE_RADIUS = 91;


// PLAYERS
const LEFT_TEAM_ID = "LEFT_TEAM";
const RIGHT_TEAM_ID = "RIGHT_TEAM";
const PLAYER_SIZE = 12;
const FONT_SIZE = 15;

// BALL
const BALL_SIZE = 6;
const BALL_COLOR = 'black';

// GAME
let ENABLE_DRAWING_PLAYERS = false;
let MOVE_ENABLED = false;

// MATCH STATE
// let PLAYERS_STATE = {};
let INITIAL_MATCH_INFO = {};
let MATCH_STATE = {};

let leftTeamScore = 0;
let rightTeamScore = 0;


canvas.addEventListener('click', function(event) {
	MOVE_ENABLED = !MOVE_ENABLED;
})

function drawMiddleLine() {
	ctx.beginPath();
	ctx.strokeStyle = LINE_COLOR;
	ctx.lineWidth = LINE_WIDTH
	ctx.moveTo(canvas.width / 2, 0);
	ctx.lineTo(canvas.width / 2, canvas.height);
	ctx.stroke();
	
}

function drawMiddleCircle() {
	ctx.beginPath();
	ctx.strokeStyle = LINE_COLOR;
	ctx.lineWidth = LINE_WIDTH
	ctx.arc(canvas.width / 2, canvas.height / 2, CENTRE_CIRCLE_RADIUS, 0, Math.PI * 2);
	ctx.stroke();
}

function drawPenaltyBoxes() {

	// LEFT BOX
	ctx.beginPath();
	ctx.strokeStyle = LINE_COLOR;
	ctx.lineWidth = LINE_WIDTH
	ctx.rect(0, canvas.height / 2 - PENALTY_AREA_HEIGHT / 2, PENALTY_AREA_WIDTH, PENALTY_AREA_HEIGHT);
	ctx.stroke();

	// RIGHT BOX
	ctx.beginPath();
	ctx.strokeStyle = LINE_COLOR;
	ctx.lineWidth = LINE_WIDTH
	ctx.rect(canvas.width - PENALTY_AREA_WIDTH, canvas.height / 2 - PENALTY_AREA_HEIGHT / 2, PENALTY_AREA_WIDTH, PENALTY_AREA_HEIGHT);
	ctx.stroke();

	// LEFT SMALL BOX
	ctx.beginPath();
	ctx.strokeStyle = LINE_COLOR;
	ctx.lineWidth = LINE_WIDTH
	ctx.rect(0, canvas.height / 2 - SMALL_PENALTY_AREA_HEIGHT / 2, SMALL_PENALTY_AREA_WIDTH, SMALL_PENALTY_AREA_HEIGHT);
	ctx.stroke();

	// RIGHT SMALL BOX
	ctx.beginPath();
	ctx.strokeStyle = LINE_COLOR;
	ctx.lineWidth = LINE_WIDTH
	ctx.rect(canvas.width - SMALL_PENALTY_AREA_WIDTH, canvas.height / 2 - SMALL_PENALTY_AREA_HEIGHT / 2, SMALL_PENALTY_AREA_WIDTH, SMALL_PENALTY_AREA_HEIGHT);
	ctx.stroke();
}

function drawPenaltyDots() {
	//let dist_from_left = PENALTY_BOX_WIDTH / 2 + PENALTY_BOX_WIDTH / 6;

	ctx.beginPath();
	ctx.fillStyle = LINE_COLOR;
	ctx.lineWidth = LINE_WIDTH;

	ctx.arc(PENALTY_SPOT_WIDTH, canvas.height / 2, 3, 0, Math.PI * 2);
	ctx.fill();

	ctx.beginPath();
	ctx.fillStyle = LINE_COLOR;
	ctx.lineWidth = LINE_WIDTH
	ctx.arc(canvas.width - PENALTY_SPOT_WIDTH, canvas.height / 2, 3, 0, Math.PI * 2);
	ctx.fill();
}

function drawBorder() {
	ctx.beginPath();
	ctx.strokeStyle = LINE_COLOR;
	ctx.lineWidth = LINE_WIDTH
	ctx.rect(0, 0, canvas.width, canvas.height);
	ctx.stroke();
}

function drawGoals() {
	ctx.beginPath();
	ctx.strokeStyle = GOAL_COLOR;
	ctx.lineWidth = GOAL_LINE_WIDTH;
	ctx.moveTo(0, canvas.height / 2 - GOAL_HEIGHT / 2);
	ctx.lineTo(0, canvas.height / 2 + GOAL_HEIGHT / 2);
	ctx.stroke();
	
	ctx.beginPath();
	ctx.strokeStyle = GOAL_COLOR;
	ctx.lineWidth = GOAL_LINE_WIDTH;
	ctx.moveTo(canvas.width, canvas.height / 2 - GOAL_HEIGHT / 2);
	ctx.lineTo(canvas.width, canvas.height / 2 + GOAL_HEIGHT / 2);
	ctx.stroke();
}

function drawPlayingField() {
	drawMiddleLine();
	drawMiddleCircle();
	drawPenaltyBoxes();
	drawPenaltyDots();
	drawBorder();
	drawGoals();
}


function startMatch() {
	ENABLE_DRAWING_PLAYERS = true;
	console.log("Initial match state", INITIAL_MATCH_INFO);
	console.log("Match State", MATCH_STATE)

	for (site of [LEFT_TEAM_ID, RIGHT_TEAM_ID]) {
		for (const player of INITIAL_MATCH_INFO["teams"][site]["players"]) {
		    player.fieldSite = site;
		    player.isPlaying = true;
	        createPlayerCard(player);
		}
	}
}

function drawPlayers() {

	for (site of [LEFT_TEAM_ID, RIGHT_TEAM_ID]) {
		for (const player of MATCH_STATE["teams"][site]["players"]) {

			const x = player.x;
			const y = player.y
			
			// Shirt Color
			ctx.fillStyle = INITIAL_MATCH_INFO["teams"][site]["shirtColor"];
			if (player.position == "GK") {
				ctx.fillStyle = INITIAL_MATCH_INFO["teams"][site]["goalkeeperShirtColor"];
			}
			
			// Draw Player
			ctx.beginPath();
			ctx.arc(x, y, PLAYER_SIZE, 0, Math.PI * 2);
			ctx.fill();
			
			// Number Color
			ctx.font = FONT_SIZE + "px Arial";
			ctx.fillStyle = INITIAL_MATCH_INFO["teams"][site]["numberColor"];
			if (player.position == "GK") {
				ctx.fillStyle = INITIAL_MATCH_INFO["teams"][site]["goalkeeperNumberColor"]
			}
			
			// Draw Number
			const txt = player.number.toString();
			const txtSize = ctx.measureText(txt);
			ctx.fillText(txt, x - txtSize.width / 2, y + PLAYER_SIZE / 2);
		}
	}
}

function drawBall() {
	
	ctx.fillStyle = BALL_COLOR;
	ctx.beginPath();
	ctx.arc(MATCH_STATE["ball"].x, MATCH_STATE["ball"].y, BALL_SIZE, 0, Math.PI * 2);
	ctx.fill();
}

function updateScore() {
	
	if (leftTeamScore != MATCH_STATE["scores"][LEFT_TEAM_ID]) {
		document.getElementById("leftScore").textContent = MATCH_STATE["scores"][LEFT_TEAM_ID];
	}
	
	if (rightTeamScore != MATCH_STATE["scores"][RIGHT_TEAM_ID]) {
		document.getElementById("rightScore").textContent = MATCH_STATE["scores"][RIGHT_TEAM_ID];
	}
}

function updateTime() {
	document.getElementById("matchTime").textContent = MATCH_STATE["matchDuration"];
}


function handleGame() {
	drawPlayingField();
	if (!ENABLE_DRAWING_PLAYERS) {
		return;
	}
	drawPlayers();
	drawBall();
	updateScore();
	updateTime();
}

function animate() {
	ctx.clearRect(0, 0, canvas.width, canvas.height);
	handleGame();
	requestAnimationFrame(animate);
}

animate();




function createPlayerCard(playerData) {
	const tile = document.createElement("div");
	tile.classList.add("playerTile")
	tile.classList.add("playingPlayer")
	if (!playerData.isPlaying) {
		tile.classList.add("reserve")
	}
	
	// Left Element
	const playerPositionDiv = document.createElement("div");
	playerPositionDiv.classList.add("playerPosition");
	if (playerData.position != null) {
	    playerPositionDiv.innerText = playerData.position;
	}

	// Midle Element
	const playerDescriptionDiv = document.createElement("div");
	playerDescriptionDiv.classList.add("playerDescription")
	
	// Right Element
	const playerNumberDiv = document.createElement("div");
	playerNumberDiv.classList.add("PlayerNumber")
	playerNumberDiv.innerText = playerData.number;
	
	// Player Description
	
	// Description header
	const playerDescriptionHeader = document.createElement("div");
	playerDescriptionHeader.classList.add("PlayerDescriptionHeader")
	
	// Player Name
	const playerName = document.createElement("div");
	playerName.classList.add("PlayerName")
	playerName.innerText = playerData.name;
	
	// Compoe description header
	playerDescriptionHeader.appendChild(playerName);
	
	// Player Stats
	const playerStatsContainer = document.createElement("div");
	playerStatsContainer.classList.add("PlayerStatsContainer")
	
	// Player Stat
	playerData.playerStats.overall = playerData.overall;
	for (const [key,value] of Object.entries(playerData.playerStats)) {
		const playerStat = document.createElement("div");
		playerStat.classList.add("PlayerStat")
		const playerStatKey = document.createElement("div");
		playerStatKey.classList.add("PlayerStatKey")
		playerStatKey.innerText = key
		const playerStatValue = document.createElement("div");
		playerStatValue.classList.add("PlayerStatValue")
		playerStatValue.innerText = value
		
		// Compose player stat
		playerStat.appendChild(playerStatKey);
		playerStat.appendChild(playerStatValue);
		
		// Compose player description
		playerStatsContainer.append(playerStat)
	}

	
	// Compose Middle Div
	playerDescriptionDiv.appendChild(playerDescriptionHeader);
	playerDescriptionDiv.appendChild(playerStatsContainer);
	
	// Compose Tile
	tile.appendChild(playerPositionDiv);
	tile.appendChild(playerDescriptionDiv);
	tile.appendChild(playerNumberDiv);
	
	
	
	if (playerData.fieldSite == LEFT_TEAM_ID) {
		document.getElementById("LeftContainer").appendChild(tile);
	} else {
		document.getElementById("RightContainer").appendChild(tile);
	}
}

