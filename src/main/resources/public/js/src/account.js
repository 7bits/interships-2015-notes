(function($) {
  'use strict';
  
	$(document).ready(function() {

		//подсветка выбранной темы
		$('.js-theme').click(function() {
			var self = $(this);
			$('.chosen').removeClass('chosen');
			self.addClass('chosen');

			$('#hidden').attr('value', self.attr('id'));			
		});


		//подсветка неверных полей
		$(function() {
			/* else {
				$('.newPass').attr('style', 'border: 1px solid #ffffff;');
			};*/			
		});
	});
})(jQuery);
