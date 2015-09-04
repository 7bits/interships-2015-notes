function autosaver($textarea) {

	var text = $textarea.val();

	var data = {
        id: $textarea.closest('.js-note').attr('id'),
        text: text
    }

    $(".js-note[id=" + data.id + "] .js-content").text(text);
		
	data.text = htmlspecialchars(data.text);
	
	//отправить другим пользователям
	var cmd = {
        id: data.id,
        text: data.text,
        command: "block,text"
    };

    sendCommand(cmd);

    data.text = nl2br(data.text);

	App.Note.save(data, function() {

		if ($(window).width() > 800) {
							
			$('#js-status').text("Все заметки сохранены");
						
		} else {
							
			$('#js-minStatus').text('');
			$('#js-minStatus').removeClass('minStatusTyping');
						
		};
	});
}