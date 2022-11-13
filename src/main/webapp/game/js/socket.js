const matchId = new URL(window.location.href).searchParams.get("id")
const uri = "ws://127.0.0.1:8080/soccer/ws/game/match/" + matchId;

if (matchId == null) {
    location.replace("error.html");
}

const websocket = new WebSocket(uri);

websocket.onopen = function (evt) {
    openEvent(evt)
};

websocket.onmessage = function (evt) {
    onMessage(evt)
};

websocket.onerror = function (evt) {
    onError(evt)
};

websocket.onclose = function (evt) {
    onClose(evt)
};

let initialMatchInfoPresent = false;
let firstMatchInfoPresent = false;
let matchStarted = false;

function openEvent(e) {
    console.log('connection established');
}

function onClose(e) {
    console.log(e);
    //location.replace("error.html");
}



function onMessage(e) {
	let msg = JSON.parse(e.data);
	if (msg.hasOwnProperty("header") && msg["header"] == "MATCH_STATE_INITIAL_HEADER") {
		INITIAL_MATCH_INFO = msg["data"];
		initialMatchInfoPresent = true;
	}
	else if (msg.hasOwnProperty("header") && msg["header"] == "MATCH_STATE_HEADER") {
		MATCH_STATE = msg["data"];
		firstMatchInfoPresent = true;
		calculate_pps();
	} else {
	    console.log("Unrecognized msg header: " + msg["header"])
	}
	
	if (!matchStarted && initialMatchInfoPresent && firstMatchInfoPresent) {
		startMatch();
		matchStarted = true;
	}
}

function onError(e) {
	console.log(uri);
    console.log('an error occurred')
    location.replace("error.html");
}

let startedTime = 0;
let messagesInCurrentSecCount = 0;
const oneSec = 1000;

// How many messages from server per second
function calculate_pps() {
    let currentTime = new Date().getTime();
    if (currentTime > startedTime + oneSec) {
        startedTime = currentTime;
        document.getElementById("fps_value").textContent = messagesInCurrentSecCount;
        messagesInCurrentSecCount = 0;
    }
    messagesInCurrentSecCount += 1;
}