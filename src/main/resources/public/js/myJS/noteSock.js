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

    //да да, свитч плохо все дела, но пока у меня нет времени на полиморфизм)
    if (quote.command != null) {
        var commands = quote.command.split(',');
        for (var i = 0; i < commands.length; ++i) {
            switch (commands[i]) {
                case 'block':
                    var note = $("#" + quote.id + " .content");
                    if (note.hasClass('clickable')) {
                        note.removeClass('clickable');
                        note.trigger('blur');

                        note.addClass('grayBack');
                    }
                    break;
                case 'text':
                    $("#" + quote.id + " .content").text(quote.text);
                    break;
                case 'unblock':
                    var note = $("#" + quote.id + " .content");
                    if (!note.hasClass('clickable')) {
                        note.addClass('clickable');

                        note.removeClass('grayBack');
                    }

                    break;
            }
        }
    }
}

function disconnect() {
    stompClient.disconnect();
    //setConnected(false);
}

function sendCommand(data) {
    stompClient.send("/app/updatenote", {}, JSON.stringify(data));
}