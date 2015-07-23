//#include btnHandler.js::save

(function($) {
	$(document).ready( function() {	
		//функция обработки нажатия "+" и добавления заметки на рабочее поле
		$('.addNote').click(function() {

			if ($('#emptyList') != null) {
				$('#emptyList').remove();
				$('.workDiv').css('min-height', '260px');
			}

			var divCell = document.createElement("div");
			divCell.setAttribute("class", "cell");
			
			divCell.setAttribute("id", "-1");

			var divContent = document.createElement("div");
			divContent.setAttribute("class", "content");

			var textarea = document.createElement("textarea");
			textarea.setAttribute("name", "text");
			textarea.setAttribute("maxlength", "20000");
			textarea.setAttribute('style', 'padding: 0px');

			divContent.appendChild(textarea);
				
			var btnDel = document.createElement("button");
			btnDel.classList.add("delBtn");
			btnDel.setAttribute('style', 'visibility: hidden;');

			var dropdownDiv = document.createElement("div");			
			dropdownDiv.classList.add("dropdown", "dropup");
			dropdownDiv.setAttribute('style', 'visibility: hidden;');

			var dropdownDivHTML = '<button class="dropdown-toggle shaBtn" data-toggle="dropdown"></button>'; 
			dropdownDivHTML += '<ul class="dropdown-menu">' + '<li class="dropdown-submenu"><label>Владельцы:</label></li>';
			dropdownDivHTML += '<li class="divider"></li>';
			dropdownDivHTML += '<li><a href="#"><input type="text" name="textbox" id="email_textbox" value="" placeholder="Введите E-mail адресата"></a></ul>';

			dropdownDiv.innerHTML += dropdownDivHTML;

			divCell.appendChild(divContent);
			divCell.appendChild(btnDel);
			divCell.appendChild(dropdownDiv);
			divCell.setAttribute('style', 'min-width: 0px; width: 0px; margin-top: 123px; height: 2px;');

			$(".noteDiv").prepend(divCell);

			divCell = $('#-1');
			btnDel = $(divCell).children('.delBtn');
			dropdownDiv = $(divCell).children('.dropdown');

			divCell.animate({width: '31%'}, 150, 'swing');

			divCell.animate({
					height: '250px',
					marginTop: '0px'},
				150, 'swing', function() {
					divCell.css('min-width', '270px');
					btnDel.css('visibility', 'inherit');
					dropdownDiv.css('visibility', 'inherit');
					textarea.removeAttribute('style', 'padding: 0px');
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