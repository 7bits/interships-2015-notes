var stompClient = null;

function connect() {
    var socket = new SockJS('/updatenote');
    stompClient = Stomp.over(socket);
    var headerName = $('meta[name=_csrf_header]').attr('content');
    var token = $('meta[name=_csrf]').attr('content');
    var headers = {};
    headers[headerName] = token;
    stompClient.connect(headers, function(frame) {
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
      var note;

      switch (commands[i]) {
        case 'block':
                    
          note = $('.js-note[id=' + quote.id + '] .js-content');
          if (note.hasClass('clickable')) {
          
            note.removeClass('clickable');
            note.blur();

            note.addClass('grayBack');
          
          }

          break;

        case 'text':
                    
          $('.js-note[id=' + quote.id + '] .js-content').text(quote.text);
          //$("#" + quote.id + " .content").text(quote.text);
                    
          break;

        case 'unblock':
                    
          note = $('.js-note[id=' + quote.id + '] .js-content');
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
    stompClient.send('/app/updatenote', {}, JSON.stringify(data));
}
