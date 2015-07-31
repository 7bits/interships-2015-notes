(function($) {
	$(document).ready( function() {	
		//функция обработки нажатия "+" и добавления заметки на рабочее поле
		$('.addNote').click(function() {

			if ($('#emptyList') != null) {
				$('#emptyList').remove();
			};

			var divCell = document.createElement("div");
			divCell.setAttribute("class", "cell");
			
			divCell.setAttribute("id", "-1");

			var divContent = document.createElement("div");
			divContent.setAttribute("class", "content");

			// var textarea = document.createElement("textarea");
			// textarea.setAttribute("name", "text");
			// textarea.setAttribute("maxlength", "20000");
			// textarea.setAttribute('style', 'padding: 0px');

			//divContent.appendChild(textarea);
				
			var btnDel = document.createElement("button");
			btnDel.classList.add("delBtn");
			btnDel.setAttribute('style', 'visibility: hidden;');

			var shaBtn = document.createElement("button");			
			shaBtn.classList.add("shaBtn");
			shaBtn.setAttribute('style', 'visibility: hidden;');


			var control = document.createElement("div");
			control.classList.add("control");

			control.appendChild(btnDel);
			control.appendChild(shaBtn);

			divCell.appendChild(divContent);
			divCell.appendChild(control);
			divCell.setAttribute('style', 'width: 0px; margin-top: 123px; height: 2px;');

			$(".noteDiv").prepend(divCell);

			divCell = $('#-1');

			divCell.animate({width: '350'}, 150, 'swing');

			divCell.animate({
					height: '200px',
					marginTop: '0px'},
				150, 'swing', function() {
					$(divCell).children('.control').children('.delBtn').css('visibility', 'inherit');
					$(divCell).children('.control').children('.shaBtn').css('visibility', 'inherit');
					//textarea.removeAttribute('style', 'padding: 0px');
				}
			)

			$(function() {
				var data = {
					id: '-1',
					text: ''
				};

				App.Note.save(data, function(id) {
					document.getElementById('-1').setAttribute('id', id);
				});				
			})

		})
	})
})(jQuery);