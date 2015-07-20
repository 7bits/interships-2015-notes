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
				headers: {'X-CSRF-TOKEN': $("meta[name = _csrf]").attr("content") },
				data: sendInfo
			}).done(function(data) {
				$(self.target).parent(".cell").attr("id", data);
			});
		});

		$('.noteDiv').on('click', '.delBtn', function(self) {
			
			var id = $(self.target).parent().attr("id");
			
			if (id == '-1') {
				$(".cell[id=" + id + "]").remove();
			} else {
				$.ajax({
					type: "DELETE",
					headers: {'X-CSRF-TOKEN': $("meta[name = _csrf]").attr("content") },
					url: "/telenote/" + id
				}).done( function() {
					$(".cell[id=" + id + "]").remove();
				});
			}

			if ($(".cell").length) {

			}
		});

		window.onresize = function() {
			var elementHeight = document.documentElement.clientHeight - 170;
			var bodyHeight = document.documentElement.clientHeight;
			$(".workDiv").outerHeight(elementHeight);
			$("body").outerHeight(bodyHeight);
		};
		
	});
})(jQuery);
