function divToTextarea($content) {

	//блокируем заметку
    var cmd = {
        id: $content.parent('.js-note').attr("id"),
        command : "block"
    }
    
    sendCommand(cmd);

	$content.removeClass('displayBlock').addClass('displayNone');
	var $note = $content.parent('.js-note');

	var textarea = "<textarea id='js-textarea' class='textarea' name='text' maxlength='20000'>"

	$note.prepend(textarea);

	var text = $content.html();

	text = br2nl(text);
	text = rhtmlspecialchars(text);

	var $textarea = $('#js-textarea').text(text);
	$content.text('');

	$textarea.focus();
	$textarea.scrollTop(0);

}


function textareaToDiv($textarea) {

	var cmd = {
        id: $(this).parent('.js-note').attr("id"),
        command : "unblock"
    }
           
    sendCommand(cmd);

	var text = $textarea.val();
	text = htmlspecialchars(text);
	text = nl2br(text);

	var $content = $textarea.siblings('.js-content').html(text);

	$textarea.remove();
	$content.removeClass('displayNone').addClass('displayBlock');

}