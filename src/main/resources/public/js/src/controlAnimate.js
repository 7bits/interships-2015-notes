function mouseHover($note) {
	
	var $control = $note.find('.js-control');
	var $delBtn = $note.find('.js-delBtn');
	var $shareBtn = $note.find('.js-shaBtn');

	$delBtn.removeClass('btnHeightZero');
	$shareBtn.removeClass('btnHeightZero');
			

	$control.css('visibility', 'inherit');

	$control.stop();

	$control.animate({
		height: '40px',
		marginTop: '0px'
	}, 200, 'swing', function() {
		$delBtn.css('height', '40px');
		$shareBtn.css('height', '40px');
	});

}


function mouseLeave($note) {

	var $control = $note.find('.js-control');
	var $delBtn = $note.find('.js-delBtn');
	var $shareBtn = $note.find('.js-shaBtn');

	$delBtn.addClass('btnHeightZero');
	$shareBtn.addClass('btnHeightZero');

	$control.animate({
		height: '0px',
		marginTop: '40px'
	}, 200, 'swing', function() {
		$control.css('visibility', 'hidden');
	});

}