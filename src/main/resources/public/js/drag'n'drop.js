(function($) {
	$(document).ready(function() {
		$( ".noteDiv" ).sortable();
    	$( ".noteDiv" ).disableSelection();

    	var checkPosition = {
    		id_cur: -1,
    		id_prev: -1,
    		id_next: -1
    	};

    	$('.noteDiv').on('mousedown', '.control', function() {
  			
    		var self = $(this);

    		if (self.parent().index()-1 == -1) {
				checkPosition = {
					id_cur: self.parent().attr('id'),
					id_prev: 0,
					id_next: $('.cell:eq(' + (self.parent().index()+1) + ')').attr('id')
				}
			} else if (self.parent().index()+1 == self.parent().parent().children().length) {
				checkPosition = {
					id_cur: self.parent().attr('id'),
					id_prev: $('.cell:eq(' + (self.parent().index()-1) + ')').attr('id'),
					id_next: 0
				}
			} else {
				checkPosition = {
					id_cur: self.parent().attr('id'),
					id_prev: $('.cell:eq(' + (self.parent().index()-1) + ')').attr('id'),
					id_next: $('.cell:eq(' + (self.parent().index()+1) + ')').attr('id')
				}
			};
  		})


    	$('.noteDiv').on('mouseup', '.control', function() {
			
    		var self = $(this);

			setTimeout(function() {

				if (self.parent().index()-1 == -1) {
					var sendData = {
						id_cur: self.parent().attr('id'),
						id_prev: 0,
						id_next: $('.cell:eq(' + (self.parent().index()+1) + ')').attr('id')
					}
				} else if (self.parent().index()+1 == self.parent().parent().children().length) {
					var sendData = {
						id_cur: self.parent().attr('id'),
						id_prev: $('.cell:eq(' + (self.parent().index()-1) + ')').attr('id'),
						id_next: 0
					}
				} else {
					var sendData = {
						id_cur: self.parent().attr('id'),
						id_prev: $('.cell:eq(' + (self.parent().index()-1) + ')').attr('id'),
						id_next: $('.cell:eq(' + (self.parent().index()+1) + ')').attr('id')
					}
				};

				if (checkPosition.id_prev != sendData.id_prev || checkPosition.id_next != sendData.id_next) {
					$.ajax({
				    	type: "POST",
						url: "/telenote/order",
						dataType: "json",
						headers: {'X-CSRF-TOKEN': $("meta[name = _csrf]").attr("content") },
						data: sendData
					})
				};
			}, 500);
		})
	})		
})(jQuery);