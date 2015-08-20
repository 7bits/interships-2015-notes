(function($) {
	$(document).ready( function() {	
		//функция обработки нажатия "+" и добавления заметки на рабочее поле
		$('.addNote').click(function() {

			var cell = "<div id='new' class='cell' style='width: 0px; margin-top: 123px; height: 2px;'>"+
				"<div class='content'></div>"+
				"<div class='control'>"+
					"<button class='shaBtn' style='visibility: hidden;'></button>"+
					"<button class='shaBtn' style='visibility: hidden;'></button>"+
				"</div>"+
			"</div>"

			if ($('#js-actualSection').attr("value") == "Мои заметки") {
				$(".js-noteSection").eq(0).prepend(cell);
				$("body").animate({scrollTop: '0'}, 200);
			} else if (($('#js-actualSection').attr("value") != "Мои заметки") && ($(".noteDiv").children().length != 0)) {

				var section = "<div class='js-section js-allSections textNoteSection'>" + 
					"<img class='js-sectionPic sectionPic' src=" + $(".js-sectionPic").eq(0).attr("value") + ">" + 
                	"<div class='js-sectionOwner sectionOwner'>" + $("#js-actualSection").eq(0).attr("value") + 
                		"<span>" + $(".js-span").eq(0).attr("value") + "</span>" + 
                	"</div>" +
				"</div>";

				$(".noteDiv").prepend(section);
				addingElements (cell)

			} else if (($('#js-actualSection').attr("value") != "Мои заметки") && ($(".noteDiv").children().length == 0)) {

				addingElements (cell)
				
			} 

			cell = $('#new');

			cell.animate({width: '350'}, 150, 'swing');

			cell.animate({
					height: '200px',
					marginTop: '0px'},
				150, 'swing', function() {
					$(cell).children().find('.delBtn').css('visibility', 'inherit');
					$(cell).children().find('.shaBtn').css('visibility', 'inherit');
				}
			)

			$(function() {
            	var data = {
            		id: '-1',
            		text: ''
            	};

            	App.Note.save(data, function(id) {
            		$('#new').attr('id', id);
            	});
            })
		})


		function addingElements (cell) {

			var noteSection = "<div class='js-noteSection noteSection js-nextSection'></div>";
			$("#js-actualSection").attr("value", "Мои заметки");

			$(".js-sectionPic").eq(0).attr("value", $(".js-userImg").attr("src"));
			$(".noteDiv").prepend(noteSection);

			$(".js-noteSection").eq(0).prepend(cell);
			$("body").animate({scrollTop: '0'}, 200);

			$(".js-sectionPic").eq(0).attr("src", $(".js-sectionPic").eq(0).attr("value"));
			$(".js-sectionOwner").eq(0).text($("#js-actualSection").eq(0).attr("value"));

			$( ".js-noteSection" ).sortable();
            $( ".js-noteSection" ).disableSelection();
		}
	})
})(jQuery);