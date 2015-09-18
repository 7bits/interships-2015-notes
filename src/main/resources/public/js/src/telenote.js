(function($) {
  'use strict';
  
	$(document).ready(function() {
		$('.js-sectionOwner')
      .eq(0)
        .html($('.js-sectionOwner')
          .eq(1)
            .html());
		$('#js-actualSection')
      .attr('value', $('.js-sectionOwner')
        .eq(1)
          .html());
		$('.js-sectionPic')
      .eq(0)
        .attr('value', $('.js-sectionPic')
          .eq(1)
            .attr('src'));
		$('.js-sectionPic')
      .eq(0)
        .attr('src', $('.js-sectionPic')
          .eq(1)
            .attr('src'));
		$('.js-section')
      .eq(0)
        .remove();
		$('.js-section')
      .eq(0)
        .addClass('js-nextSection');
		
		var positionTop;
		
    if ($(window).width() > 800) positionTop = 88;
		else positionTop = 60; 
		
		$(window).resize(function(){ 

			if ($(window).width() > 800) positionTop = 88;
			else positionTop = 60; 
		
    }); 

		var sectionSize = 35;

		//смена содержимого фиксированной секции
		$(document).on('scroll', function() {
			var $actual = $('#js-actualSection');
			var $next = $('.js-nextSection');
			var $prev = $('.js-prevSection');
      var index;

			if ($next[0] != null) {

				if (($next.position().top - $(document).scrollTop()) <= 
          positionTop + 1) {

					$next.css('visibility', 'hidden');
				
					$actual
            .find('.js-sectionOwner')
              .html($next
                .find('.js-sectionOwner')
                  .html());
					$actual.find('.js-sectionPic')
            .attr('src', $next
              .find('.js-sectionPic')
                .attr('src'));
					index = $('.js-allSections').index($next) + 1;

					$prev.removeClass('js-prevSection');
					$next.removeClass('js-nextSection');
					$next.addClass('js-prevSection');

					$('.js-allSections')
            .eq(index)
              .addClass('js-nextSection');
				
				}
			}
			
			if ($prev[0] != null) {

				if (($prev.position().top - $(document).scrollTop()) >=
          positionTop - 1) {
					
					index = $('.js-allSections').index($prev) - 1;

					$next.removeClass('js-nextSection');
					$prev.removeClass('js-prevSection');
					$prev.addClass('js-nextSection');

					$prev.css('visibility', 'inherit');				

					$('.js-allSections')
            .eq(index)
              .addClass('js-prevSection');

					$prev = $('.js-prevSection');

					if ($prev.hasClass('actualSection')) {

					 	$actual
              .find('.js-sectionOwner')
                .html($actual.attr('value'));
					 	$actual
              .find('.js-sectionPic')
                .attr('src', $('.js-sectionPic')
                  .eq(0)
                    .attr('value'));

					} else {

						$actual
              .find('.js-sectionOwner')
                .html($prev
                  .find('.js-sectionOwner')
                    .html());
						$actual
              .find('.js-sectionPic')
                .attr('src',  $prev
                  .find('.js-sectionPic')
                    .attr('src'));

					}
					
				}
			} 

			if ($(document).scrollTop() === 0) {

				$('.js-sectionOwner')
          .eq(0)
            .html($actual.attr('value'));
				$('.js-sectionPic')
          .eq(0)
            .attr('src', $('.js-sectionPic')
              .eq(0)
                .attr('value'));
				$('#js-actualSection').removeClass('js-nextSection');

			}
		});
	});
})(jQuery);