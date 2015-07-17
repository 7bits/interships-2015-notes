(function($){
	$(document).ready(function() {
		$('.noteDiv').on('click', '.edBtn', function(self) {
			
			var id = $(self.target).parent().attr("id");
			var text = $(this).parent().find("textarea").val();
			
			var sendInfo = {
				id: id,
				text: text
			};

			$.ajax({
				type: "POST",
				url: "/telenote",
				dataType: "json",
				data: sendInfo
			}).done(function(data){
				$(self.target).parent(".cell").attr("id", data);
			});
		});
	});
})(jQuery);

(function($){
	$(document).ready(function() {
		$('.noteDiv').on('click', '.delBtn', function(self) {
			
			var id = $(self.target).parent().attr("id");
			var text = $(this).parent().find("textarea").val();


			$.ajax({
				type: "DELETE",
				url: "/telenote/" + id
			}).done( function() {
				$(".cell[id=" + id + "]").remove();
			});
		});
	});
})(jQuery);
