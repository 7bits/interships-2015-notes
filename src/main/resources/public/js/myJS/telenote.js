(function($) {
	$(document).ready(function() {

		$($(".js-section")[0]).addClass("js-nextSection");

		$(document).scroll(function() {
			var actual = $("#js-actualSection");
			var next = $(".js-nextSection");
			var prev = $(".js-prevSection");

			if (next[0] != null) {
				if (($(next).position().top - $(document).scrollTop()) <= $(actual).position().top) {
				
					$(actual).text($(next).text());
					var index = $(".js-allSections").index(next) + 1;

					$(prev).removeClass("js-prevSection");
					$(next).removeClass("js-nextSection");
					$(next).addClass("js-prevSection");

					$($(".js-allSections")[index]).addClass("js-nextSection");
				
				}
			};
			
			if (prev[0] != null) {
				if (($(prev).position().top - $(document).scrollTop()) >= $(actual).position().top) {
					
					var index = $(".js-allSections").index(prev) - 1;

					$(next).removeClass("js-nextSection");
					$(prev).removeClass("js-prevSection");
					$(prev).addClass("js-nextSection");				

					$($(".js-allSections")[index]).addClass("js-prevSection");

					prev = $(".js-prevSection");

					if ($(prev).hasClass("actualSection")) { $(actual).text($(actual).attr("value")); }
					else { $(actual).text($(prev).text()); };
					
				};
			}; 

			if ($(document).scrollTop() == 0) {
				$(actual).text($(actual).attr("value"));
			};
		})
	})
})(jQuery)