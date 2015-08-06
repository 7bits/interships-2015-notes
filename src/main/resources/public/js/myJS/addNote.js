(function($) {
	$(document).ready( function() {	
		//функция обработки нажатия "+" и добавления заметки на рабочее поле
		$('.addNote').click(function() {

			if ($('#emptyList') != null) {
				$('#emptyList').remove();
			};

			var cell = "<div id='new' class='cell' style='width: 0px; margin-top: 123px; height: 2px;'>"+
				"<div class='content'></div>"+
				"<div class='control'>"+
					"<button class='delBtn' style='visibility: hidden;'></button>"+
					"<button class='shaBtn' style='visibility: hidden;'></button>"+
				"</div>"+
			"</div>"
			
			$(".noteDiv").prepend(cell);

			cell = $('#new');

			cell.animate({width: '350'}, 150, 'swing');

			cell.animate({
					height: '200px',
					marginTop: '0px'},
				150, 'swing', function() {
					$(cell).children().find('.delBtn').css('visibility', 'inherit');
					$(cell).children().find('.shaBtn').css('visibility', 'inherit');
				}
			)

			$(function() {
				var data = {
					id: '-1',
					text: ''
				};

				App.Note.save(data, function(id) {
					$('#new').attr('id', id);
				});				
			})

		})
	})
})(jQuery);