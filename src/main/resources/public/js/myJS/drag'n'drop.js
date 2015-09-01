(function($) {
	$(document).ready(function() {
		//		$( ".noteSection" ).sortable({
        //        	cancel: "p, span, br"
        //        });

		$( ".js-noteSection" ).sortable();
		$( ".js-noteSection" ).disableSelection();

    	var checkPosition = {
    		idCur: -1,
    		idPrev: -1,
    		idNext: -1
    	};

    	$('body').on('mousedown', '.js-note, .js-content, .js-control', function() {
    		var thisNoteSection = $(this).closest('.js-noteSection');
    		var thisNote = $(this).closest('.js-note');
    		thisNote.css('cursor', 'move');
    		thisNote.children('.js-control').css('cursor', 'move');

			var prevIndex = thisNote.index() - 1;
			var nextIndex = thisNote.index() + 1;

			var idCur = thisNote.attr('id');
			var idPrev = (prevIndex == -1) ? 0 : thisNoteSection.find('.js-note:eq(' + prevIndex + ')').attr('id');
			var idNext = (nextIndex >= thisNoteSection.children().length) ? 0 : thisNoteSection.find('.js-note:eq(' + nextIndex + ')').attr('id');

			checkPosition = {
				idCur: idCur,
				idPrev: idPrev,
				idNext: idNext
			}
  		})


    	$('body').on('mouseup', '.js-note, .js-content, .js-control', function() {
    		var thisNoteSection = $(this).closest('.js-noteSection');
    		var thisNote = $(this).closest('.js-note');
    		thisNote.css('cursor', '-webkit-grab');
    		thisNote.children('.js-control').css('cursor', '-webkit-grab');

			setTimeout(function() {
				var prevIndex = thisNote.index() - 1;
				var nextIndex = thisNote.index() + 1;

				var idCur = thisNote.attr('id');
				var idPrev = (prevIndex == -1) ? 0 : thisNoteSection.find('.cell:eq(' + prevIndex + ')').attr('id');
				var idNext = (nextIndex >= thisNoteSection.children().length) ? 0 : thisNoteSection.find('.cell:eq(' + nextIndex + ')').attr('id');

				var sendData = {
					idCur: idCur,
					idPrev: idPrev,
					idNext: idNext
				}

				if (checkPosition.idPrev != sendData.idPrev || checkPosition.idNext != sendData.idNext) {
					$.ajax({
				    	type: "POST",
						url: "/telenote/order",
						dataType: "json",
						headers: {'X-CSRF-TOKEN': $("meta[name = _csrf]").attr("content") },
						data: sendData
					})
				};
			}, 50);
		})
	})		
})(jQuery);