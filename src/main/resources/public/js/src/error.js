(function ($) {
  'use strict';

	$(document).ready(function() {
		window.onresize = function() {
			var bodyHeight = document.documentElement.clientHeight;
			$('body').outerHeight(bodyHeight);
		};
	});
})(jQuery);
