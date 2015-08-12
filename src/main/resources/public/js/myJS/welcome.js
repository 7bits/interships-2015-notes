(function($){
	$(document).ready(function () {
		$(function() {
			if ($('#js-loginError')[0] != null) {
				$('#js-logText').css('border', '1px solid #f74e19');
				ErrorsAnimate($('.loginError'));
			} else {
				$('#js-logText').css('border', '1px solid #ffffff');
			};

			if ($('#js-regEmailMess')[0] != null) {
				$('#js-regEmail').css('border', '1px solid #f74e19');
				ErrorsAnimate($('.emailError'));
			} else {
				$('#js-regEmail').css('border', '1px solid #ffffff');
			};

			if ($('#js-regUsernameMess')[0] != null) {
				$('#js-regUsername').css('border', '1px solid #f74e19');
				ErrorsAnimate($('.nameError'));
			} else {
				$('#js-regUsername').css('border', '1px solid #ffffff');
			};

			if ($('#js-regPassMess')[0] != null) {
				$('#js-regPass').css('border', '1px solid #f74e19');
				ErrorsAnimate($('.passError'));
			} else {
				$('#js-regPass').css('border', '1px solid #ffffff');
			};
		})

		function ErrorsAnimate(element) {
			element.attr('style', 'margin-left: 0px;');
			var width = document.documentElement.clientWidth;
			var margin;

			if (width > 1200) {
				margin = '282px';
			} else if (width > 600 && width < 1200) {
				margin = '202px';
			} else {
				margin = '200px';
			};

			element.animate({
					marginLeft: margin
				}, 200, 'swing', function() {
					element.removeAttr('style');
			});
		}
	})
})(jQuery);