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

			var btnShare = document.createElement("button");
			btnShare.classList.add("shaBtn");

			var btnEdit = document.createElement("button"); 
			btnEdit.classList.add("edBtn")

			divCell.appendChild(divContent);
			divCell.appendChild(btnDel);
			divCell.appendChild(btnShare);
			divCell.appendChild(btnEdit);

			$(".noteDiv").append(divCell);
		})
	})
})(jQuery);