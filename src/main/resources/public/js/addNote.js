(function($) {
	$(document).ready( function() {
		$('.addNote').click(function() {
			var divCell = document.createElement("div");
			divCell.setAttribute("class", "cell");
			
			divCell.setAttribute("id", "-1");

			var divContent = document.createElement("div");
			divContent.setAttribute("class", "content");

			var textarea = document.createElement("textarea");
			textarea.setAttribute("name", "text");

			divContent.appendChild(textarea);
				
			var btnDel = document.createElement("button");
			btnDel.classList.add("delBtn");

			// dropdown share button
			var dropdownDiv = document.createElement("div");			
			dropdownDiv.classList.add("dropdown", "dropup");

			dropdownDiv.innerHTML += '<button class="dropdown-toggle shaBtn" data-toggle="dropdown">';
      		dropdownDiv.innerHTML += '<ul class="dropdown-menu"><li><a href="#"><input type="text" name="textbox" id="email_textbox" value="" placeholder="Введите E-mail адресата"></a>';
			//

			var btnEdit = document.createElement("button"); 
			btnEdit.classList.add("edBtn");

			divCell.appendChild(divContent);
			divCell.appendChild(btnDel);
			divCell.appendChild(dropdownDiv);
			divCell.appendChild(btnEdit);

			$(".noteDiv").append(divCell);
			divCell.scrollIntoView(false);
		})
	})
})(jQuery);