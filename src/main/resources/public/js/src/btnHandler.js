(function($) {

	$(document).ready(function () {

    //connect();

		$(function() {
			
			$.each($('.js-content'), function(i, item) {
				var text = $(item).text();
				$(item).text('');
				$(item).html(text);
			});
	
		});


		$('#js-addShareEmail').blur(function() {
			
			$('#js-shareMessage').addClass('displayNone');
		
		});


		$('#js-addShareEmail').on('input', function() {

			$('#js-shareMessage').removeClass('messageFail');

    });

	});
})(jQuery);
