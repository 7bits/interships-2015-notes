//#include btnHandler.js::save

(function($) {
	$(document).ready( function() {	
		//функция обработки нажатия "+" и добавления заметки на рабочее поле
		$('.addNote').click(function() {
			var divCell = document.createElement("div");
			divCell.setAttribute("class", "cell");
			
			divCell.setAttribute("id", "-1");

			var divContent = document.createElement("div");
			divContent.setAttribute("class", "content");

			var textarea = document.createElement("textarea");
			textarea.setAttribute("name", "text");
			textarea.setAttribute("maxlength", "20000");

			divContent.appendChild(textarea);
				
			var btnDel = document.createElement("button");
			btnDel.classList.add("delBtn");

			var dropdownDiv = document.createElement("div");			
			dropdownDiv.classList.add("dropdown", "dropup");

			dropdownDiv.innerHTML += '<button class="dropdown-toggle shaBtn" data-toggle="dropdown">';
      		dropdownDiv.innerHTML += '<ul class="dropdown-menu"><li><a href="#"><input type="text" name="textbox" id="email_textbox" value="" placeholder="Введите E-mail адресата"></a>';

			divCell.appendChild(divContent);
			divCell.appendChild(btnDel);
			divCell.appendChild(dropdownDiv);

			$(".noteDiv").prepend(divCell);

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