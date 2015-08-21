(function($) {
	$(document).ready(function() {
		$(".js-sectionOwner").eq(0).html($(".js-sectionOwner").eq(1).html());
		$("#js-actualSection").attr("value", $(".js-sectionOwner").eq(1).html());
		$(".js-sectionPic").eq(0).attr("value", $(".js-sectionPic").eq(1).attr("src"));
		$(".js-sectionPic").eq(0).attr("src", $(".js-sectionPic").eq(1).attr("src"));
		$(".js-section").eq(0).remove();
		$(".js-section").eq(0).addClass("js-nextSection");
		
		var positionTop = 88;
		var sectionSize = 35;

		//смена содержимого фиксированной секции
		$(document).on("scroll", function() {
			var actual = $("#js-actualSection");
			var next = $(".js-nextSection");
			var prev = $(".js-prevSection");

			if (next[0] != null) {

				if (($(next).position().top - $(document).scrollTop()) <= positionTop + 1) {

					next.css('visibility', 'hidden');
				
					actual.find(".js-sectionOwner").html(next.find(".js-sectionOwner").html());
					actual.find(".js-sectionPic").attr("src", next.find(".js-sectionPic").attr("src"));
					var index = $(".js-allSections").index(next) + 1;

					prev.removeClass("js-prevSection");
					next.removeClass("js-nextSection");
					next.addClass("js-prevSection");

					$(".js-allSections").eq(index).addClass("js-nextSection");
				
				}
			};
			
			if (prev[0] != null) {

				if (($(prev).position().top - $(document).scrollTop()) >= positionTop - 1) {
					
					var index = $(".js-allSections").index(prev) - 1;

					next.removeClass("js-nextSection");
					prev.removeClass("js-prevSection");
					prev.addClass("js-nextSection");

					prev.css('visibility', 'inherit');				

					$(".js-allSections").eq(index).addClass("js-prevSection");

					prev = $(".js-prevSection");

					if (prev.hasClass("actualSection")) {

					 	actual.find(".js-sectionOwner").html(actual.attr("value"));
					 	actual.find(".js-sectionPic").attr("src", $(".js-sectionPic").eq(0).attr("value"));

					} else {

						actual.find(".js-sectionOwner").html(prev.find(".js-sectionOwner").html());
						actual.find(".js-sectionPic").attr("src", prev.find(".js-sectionPic").attr("src"));

					};
					
				};
			} 

			if ($(document).scrollTop() == 0) {

				$(".js-sectionOwner").eq(0).html(actual.attr("value"));
				$(".js-sectionPic").eq(0).attr("src", $(".js-sectionPic").eq(0).attr("value"));
				$("#js-actualSection").removeClass("js-nextSection");

			};
		})
	
		/*var checker = 1;

		//анимация панели секции
		$(document).on("scroll", function() {

			var actual = $("#js-actualSection");
			var next = $(".js-nextSection");	
			var prev = $(".js-prevSection");

			//следующая секция существует?
			if ((next[0] != null) && (checker == 1)) {

				var position = $(next).position().top - $(document).scrollTop();
				var scrolling = $(document).scrollTop() + sectionSize;
				
				if ((position <= positionTop + sectionSize) && (position > positionTop)) { 

					checker = 0;
					
					actual.animate({
						top: positionTop - sectionSize
					}, 300, 'swing', function() {
						next.css('visibility', 'hidden');
						actual.css('top', positionTop);
						checker = 1;
					})

					$("body").animate({ scrollTop: scrolling }, 300, 'swing'); 
				} 

			}

			//предыдущая секция существует?
			if ((prev[0] != null) && (checker == 1)) {

				var position = $(prev).position().top - $(document).scrollTop();
				
				if ((position < positionTop) && (position > positionTop - sectionSize)) { actual.css('top', position + sectionSize); } 
				
				if (position == positionTop) { actual.css('top', '88px'); };

			};

		})*/
	})
})(jQuery)