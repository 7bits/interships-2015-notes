(function($) {
	$(document).ready( function() {
		$('.delBtn').click(function() {

			$(this).parent().remove();
		})
	})
})(jQuery);