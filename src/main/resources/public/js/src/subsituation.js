function divToTextarea($content) {

	//блокируем заметку
    var cmd = {
      id: $content
        .parent('.js-note')
          .attr('id'),
      command: 'block'
    };
    
    sendCommand(cmd);

	$content.removeClass('displayBlock').addClass('displayNone');
	var $note = $content.parent('.js-note');

	var textarea = '<textarea id="js-textarea" ' +
    'class="textarea" name="text" maxlength="20000">';

	$note.prepend(textarea);

	var text = $content.html();

	text = br2nl(text);
	text = rhtmlspecialchars(text);

	var $textarea = $('#js-textarea').text(text);
	$content.text('');

	$textarea.focus();
	$textarea.scrollTop(0);

}


function textareaToDiv($textarea, timeoutId) {

	clearTimeout(timeoutId);

	var cmd = {
    id: $textarea
      .parent('.js-note')
        .attr('id'),
    command : 'unblock'
  };
           
  sendCommand(cmd);

	var data = {
    id: cmd.id,
    text: $textarea.val()
  }; 

	data.text = htmlspecialchars(data.text);
	data.text = nl2br(data.text);

	var $content = $textarea.siblings('.js-content').html(data.text);

	$textarea.remove();
	$content.removeClass('displayNone').addClass('displayBlock');

	App.Note.save(data, function() {

		if ($(window).width() > 800) {
							
			$('#js-status').text('Все заметки сохранены');
						
		} else {
							
			$('#js-minStatus').text('');
			$('#js-minStatus').removeClass('minStatusTyping');
						
		}

		return timeoutId;
	
	});
}
