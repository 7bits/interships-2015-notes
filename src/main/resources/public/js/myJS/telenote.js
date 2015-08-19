(function($) {
	$(document).ready(function() {
		$(".js-sectionOwner").eq(0).text($(".js-sectionOwner").eq(1).text());
		$("#js-actualSection").attr("value", $(".js-sectionOwner").eq(1).text());
		$(".js-sectionPic").eq(0).attr("value", $(".js-sectionPic").eq(1).attr("src"));
		$(".js-sectionPic").eq(0).attr("src", $(".js-sectionPic").eq(1).attr("src"));
		$(".js-section").eq(0).remove();
		$(".js-section").eq(0).addClass("js-nextSection");

		$(document).scroll(function() {
			var actual = $("#js-actualSection");
			var next = $(".js-nextSection");
			var prev = $(".js-prevSection");

			if (next[0] != null) {
				if (($(next).position().top - $(document).scrollTop()) <= $(actual).position().top) {
				
					$(actual).find(".js-sectionOwner").text($(next).find(".js-sectionOwner").text());
					$(actual).find(".js-sectionPic").attr("src", $(next).find(".js-sectionPic").attr("src"));
					var index = $(".js-allSections").index(next) + 1;

					$(prev).removeClass("js-prevSection");
					$(next).removeClass("js-nextSection");
					$(next).addClass("js-prevSection");

					$(".js-allSections").eq(index).addClass("js-nextSection");
				
				}
			};
			
			if (prev[0] != null) {
				if (($(prev).position().top - $(document).scrollTop()) >= $(actual).position().top) {
					
					var index = $(".js-allSections").index(prev) - 1;

					$(next).removeClass("js-nextSection");
					$(prev).removeClass("js-prevSection");
					$(prev).addClass("js-nextSection");				

					$(".js-allSections").eq(index).addClass("js-prevSection");

					prev = $(".js-prevSection");

					if ($(prev).hasClass("actualSection")) {
					 	$(actual).find(".js-sectionOwner").text($(actual).attr("value"));
					 	$(actual).find(".js-sectionPic").attr("src", $(".js-sectionPic").eq(0).attr("value"));
					} else { 
						$(actual).find(".js-sectionOwner").text($(prev).find(".js-sectionOwner").text()); 
						$(actual).find(".js-sectionPic").attr("src", $(prev).find(".js-sectionPic").attr("src"));
					};
					
				};
			} 

			if ($(document).scrollTop() == 0) {
				$(".js-sectionOwner").eq(0).text($(actual).attr("value"));
				$(".js-sectionPic").eq(0).attr("src", $(".js-sectionPic").eq(0).attr("value"));
			};
		})
	})
})(jQuery)