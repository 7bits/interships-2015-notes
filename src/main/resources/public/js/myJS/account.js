(function($) {
	$(document).ready(function() {
		
		//подсветка текущей темы
		$(function() {
			var style = $('#hidden').attr('value');

			$('#'+style).addClass('chosen');
		})


		//подсветка выбранной темы
		$('.js-theme').click(function() {
			var self = $(this);
			$('.chosen').removeClass('chosen');
			self.addClass('chosen');

			$('#hidden').attr('value', self.attr('id'));			
		})


		//подсветка неверных полей
		$(function() {
			if ($('#js-name') != null) {
				$('.username').css('style', 'border: 1px solid #f74e19;')
				$('#js-nameDiv').css('style', 'margin-left: 0;').animate({
						marginLeft: '327px'
					}, 200, 'swing', function() {
						$('#js-nameDiv').removeAttr('style');
				});
			} else {
				$('.username').css('style', 'border: 1px solid #ffffff;');
			};

			if ($('#js-oldPass') != null) {
				$('.currentPass').css('style', 'border: 1px solid #f74e19;');
				$('#js-errorDiv').css('style', 'margin-left: 0;').animate({
						marginLeft: '327px'
					}, 200, 'swing', function() {
						$('#js-errorDiv').removeAttr('style');
				});
			} else {
				$('.currentPass').css('style', 'border: 1px solid #ffffff;');
			};

			if ($('#js-newPass') != null) {
				$('.newPass').css('style', 'border: 1px solid #f74e19;');
				$('#js-errorDiv').css('style', 'margin-left: 0;').animate({
						marginLeft: '327px'
					}, 200, 'swing', function() {
						$('#js-errorDiv').removeAttr('style');
				});
			} else {
				$('.newPass').css('style', 'border: 1px solid #ffffff;');
			};			
		})
	})
})(jQuery);