(function($) {
	$(document).ready(function() {
		$(function() {
			var style = $('#hidden').attr('value');

			$('#'+style).addClass('chosen');
		})

		$('.theme').click(function() {
			var self = $(this);
			$('.chosen').removeClass('chosen');
			self.addClass('chosen');

			$('#hidden').attr('value', self.attr('id'));			
		})
	})
})(jQuery);