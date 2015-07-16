(function($) {
	$(document).ready( function() {
		$('.addNote').click(function() {
			var divCell = document.createElement("div");
			divCell.setAttribute("class", "cell");
			
			if ($(".cell").length % 3 == 2) {
				divCell.classList.add("lastcell");
			};
			
			divCell.setAttribute("id", "-1");

			var divContent = document.createElement("div");
			divContent.setAttribute("class", "content");

			var textarea = document.createElement("textarea");
			textarea.setAttribute("name", "text");

			divContent.appendChild(textarea);
				
			var btnDel = document.createElement("button");
			btnDel.classList.add("btn-default", "delBtn");
			$(btnDel).text("Удалить");

			var btnShare = document.createElement("button");
			btnShare.classList.add("btn-default", "shaBtn");
			$(btnShare).text("Поделиться");

			var btnEdit = document.createElement("button"); 
			btnEdit.classList.add("btn-default", "edBtn")
			$(btnEdit).text("Изменить");

			divCell.appendChild(divContent);
			divCell.appendChild(btnDel);
			divCell.appendChild(btnShare);
			divCell.appendChild(btnEdit);

			$(".noteDiv").append(divCell);
		})
	})
})(jQuery);