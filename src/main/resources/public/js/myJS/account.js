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
			if ($('#js-name')[0] != null) {
				$('.username').attr('style', 'border: 1px solid #f74e19;')
				$('#js-nameDiv').attr('style', 'margin-left: 115px;').animate({
						marginLeft: '327px'
					}, 200, 'swing', function() {
						$('#js-nameDiv').removeAttr('style');
				});
			} else {
				$('.username').attr('style', 'border: 1px solid #ffffff;');
			};

			if ($('#js-oldPass')[0] != null) {
				$('.currentPass').attr('style', 'border: 1px solid #f74e19;');
				$('#js-errorDiv').attr('style', 'margin-left: 248px;').animate({
						marginLeft: '327px'
					}, 200, 'swing', function() {
						$('#js-errorDiv').removeAttr('style');
				});
			} else {
				$('.currentPass').attr('style', 'border: 1px solid #ffffff;');
			};

			if ($('#js-newPass')[0] != null) {
				$('.newPass').attr('style', 'border: 1px solid #f74e19;');
				$('#js-errorDiv').attr('style', 'margin-left: 248px;').animate({
						marginLeft: '327px'
					}, 200, 'swing', function() {
						$('#js-errorDiv').removeAttr('style');
				});
			} else {
				$('.newPass').attr('style', 'border: 1px solid #ffffff;');
			};			
		})
	})
})(jQuery);