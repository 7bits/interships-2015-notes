var stompClient = null;

function connect() {
    var socket = new SockJS('/updatenote');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        //setConnected(true);
        console.log('Connected: ' + frame);
        /*stompClient.subscribe('/topic/greetings', function(greeting){
            showGreeting(JSON.parse(greeting.body).content);
        });*/
    });
}

function disconnect() {
    stompClient.disconnect();
    //setConnected(false);
    console.log("Disconnected");
}

function sendNote(data) {
    stompClient.send("/app/updatenote", {}, JSON.stringify(data));
}

function showGreeting(message) {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(message));
    response.appendChild(p);
}