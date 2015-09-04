function deleteNote(timeoutId, id) {
			
	$.ajax({
		type: "DELETE",
		headers: {'X-CSRF-TOKEN': $("meta[name = _csrf]").attr("content") },
		url: "/telenote/" + id
	}).done( function() {
		clearTimeout(timeoutId);
		var $note = $(".js-note[id=" + id + "]");
		$note.css('min-width', '0px');
		$note.children('.delBtn').css('visibility', 'hidden');
		$note.children('.shaBtn').css('visibility', 'hidden');
					
		$note.animate({
			height: '2px',
			border: '0px',
			marginTop: '123px'
		}, 150, 'swing');

		$note.animate({
			width: '0px',
		}, 150, 'swing', function() {

			$note.remove();

			var $actual = $("#js-actualSection");
			var $actualOwner = $actual.find(".js-sectionOwner");
			var $actualPic = $actual.find(".js-sectionPic");

			var $allSections = $('.js-noteSection');

			$allSections.each(function() {
							
				var $thisNoteSection = $(this);

				if ($thisNoteSection.find(".js-note").length == 0) {
					if ($thisNoteSection.index() == 0) {
									
						var $nextSection = $('.js-section').eq(0);
						var $nextOwner = $nextSection.find(".js-sectionOwner");
						var $nextPic = $nextSection.find(".js-sectionPic");

						$(".js-noteSection").eq(0).remove();
									

						$actual.attr("value", $nextSection.text());
						$actualOwner.text($nextOwner.text());
						$actualPic.attr("value", $nextPic.attr("src"));
						$actualPic.attr("src", $nextPic.attr("src"));
						$nextSection.remove();
									
						$nextSection = $('js-section').eq(0);

						if ($nextSection[0] != null) { $nextSection.addClass("js-nextSection"); };
								
					} else {

						var $deletedSection = $(".js-allSections").eq($thisNoteSection.index() - 1);

						if ($actualOwner.text() == $deletedSection.find(".js-sectionOwner").text()) {
										
							var $nextSection = $(".js-nextSection");
							var $nextOwner = $nextSection.find(".js-sectionOwner");
							var $nextPic = $nextSection.find(".js-sectionPic");

							$actualOwner.text($nextOwner.text());
							$actualPic.attr("src", $nextPic.attr("src"));
							$actualPic.attr("value", $nextPic.attr("src"));

							$deletedSection.remove();
							$thisNoteSection.remove();

							$(".js-prevSection").removeClass("js-prevSection");

							$nextSection.removeClass("js-nextSection").addClass("js-prevSection");

							$("js-allSections").eq($nextSection.index() + 1).addClass("js-nextSection");

						} else {
										
							var next = $("js-allSections").eq($deletedSection.index() + 2)

							if (next[0] != null) { next.addClass("js-nextSection"); };

							$deletedSection.remove();
							$thisNoteSection.remove();
									
						};

					};
							
				}
			})
		});


		if ($(window).width() > 840) {
			$('#js-status').text("Все заметки сохранены");
		} else {
			$('#js-minStatus').text('');
			$('#js-minStatus').css('background-image', "url('/img/ok.png')");
		};
	});
}