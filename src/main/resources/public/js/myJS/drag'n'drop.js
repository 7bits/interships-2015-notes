(function($) {
	$(document).ready(function() {
		$( ".noteDiv" ).sortable();
    	$( ".noteDiv" ).disableSelection();

    	var checkPosition = {
    		id_cur: -1,
    		id_prev: -1,
    		id_next: -1
    	};

    	$('.noteDiv').on('mousedown', '.cell, .content, .control', function() {
    		var self = $(this).closest('.cell');
    		self.css('cursor', 'move');
    		self.children('.control').css('cursor', 'move');

			var prev_index = self.index() - 1;
			var next_index = self.index() + 1;

			var id_cur = self.attr('id');
			var id_prev = (prev_index == -1) 				   ? 0 : $('.cell:eq(' + prev_index + ')').attr('id');
			var id_next = (next_index == $('.content').length) ? 0 : $('.cell:eq(' + next_index + ')').attr('id');

			checkPosition = {
				id_cur: id_cur,
				id_prev: id_prev,
				id_next: id_next
			}
  		})


    	$('.noteDiv').on('mouseup', '.cell, .content, .control', function() {
    		var self = $(this).closest('.cell');
    		self.css('cursor', '-webkit-grab');
    		self.children('.control').css('cursor', '-webkit-grab');

			setTimeout(function() {
				var prev_index = self.index() - 1;
				var next_index = self.index() + 1;

				var id_cur = self.attr('id');
				var id_prev = (prev_index == -1) 				   ? 0 : $('.cell:eq(' + prev_index + ')').attr('id');
				var id_next = (next_index == $('.content').length) ? 0 : $('.cell:eq(' + next_index + ')').attr('id');

				var sendData = {
					id_cur: id_cur,
					id_prev: id_prev,
					id_next: id_next
				}

				if (checkPosition.id_prev != sendData.id_prev || checkPosition.id_next != sendData.id_next) {
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