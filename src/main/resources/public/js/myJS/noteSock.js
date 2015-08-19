var stompClient = null;

function connect() {
    var socket = new SockJS('/updatenote');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        //setConnected(true);
        //var email = $("meta[name=current_user]").attr("content");
        stompClient.subscribe('/user/queue/notes', subscribeEvent);
    });
}

function subscribeEvent(noteForm) {
    var quote = JSON.parse(noteForm.body);
    $("#" + quote.id + " .content").text(quote.text);
}

function disconnect() {
    stompClient.disconnect();
    //setConnected(false);
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