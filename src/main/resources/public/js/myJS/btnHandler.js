(function($) {
	$(document).ready(function () {

        connect();

		App.Note.save = function(data, callback) {
			$.ajax({
				type: "POST",
				url: "/telenote",
				dataType: "json",
				headers: {'X-CSRF-TOKEN': $("meta[name = _csrf]").attr("content") },
				data: data
			}).done(function(data) {
				callback(data);
			})
		};


		var timeoutId;

		//функция удаления заметки из базы и с рабочего поля
		$('body').on('click', '.js-delBtn', function() {
			
			var id = $(this).closest('.js-note').attr("id");
			deleteNote(timeoutId, id);
			
		});



		var oldVal ="";
		$('body').on('input', '#js-textarea', function() {
			var currentVal = $(this).val();

            if(currentVal == oldVal) {
                return; //проверка изменения состояния текста
            }

            oldVal = currentVal;

            if ($(window).width() > 800) {

            	$('#js-status').text("Сохранение...");

            } else {

            	$('#js-minStatus').addClass('minStatusTyping');
            	$('#js-minStatus').text('...');

            };
			
		});


		//автосейвер
		$('body').on('keyup', '#js-textarea', function() {
			
			clearTimeout(timeoutId);

			var $textarea = $(this)
			timeoutId = setTimeout(function() {
				autosaver($textarea);
			}, 750);

		})


		var addedShareEmails = [];
		//расшаривание
		$('#js-addShare').click(function() {

    		addedShareEmails = addShare(addedShareEmails);

    	});
		

		//Анимация панели с кнопками
		$('body').on('mouseenter', '.js-note', function() {
			
			var $note = $(this);
			mouseHover($note);

		}).on('mouseleave', '.js-note', function() {
			
			var $note = $(this);
			mouseLeave($note);

		})


		//подмена активного элемента
		$('body').on('click', '.js-content', function() {

			if ($('#js-textarea')[0] == null) {

                var $content = $(this);
                divToTextarea($content);

			} else {

				$('#js-textarea').blur();
				$(this).click();
				
			};

		}).on('blur', '#js-textarea', function() {
           
           	var $textarea = $(this);
           	textareaToDiv($textarea)	
		
		})


		$(function() {
			
			$.each($('.js-content'), function(i, item) {
				var text = $(item).text();
				$(item).text('');
				$(item).html(text);
			})
	
		})
		

		var $clickedNoteSection;
		//Обработчик кнопки share и генерация модального окна
		$('body').on('click', '.js-shaBtn', function() {

			$('#js-textarea').blur();

			var id = $(this).closest('.js-note').attr('id');
			$('.js-modalWindow').attr('id', id);

			checkSharedNote(id);

			$('#js-overlay').fadeIn(200, 
				function() {
					$('.js-modalWindow').addClass('displayBlock').animate({
						opacity: 1,
						top: '45%'},
						200);

					$('#js-addShareEmail').focus();
			});
			$clickedNoteSection = $(this).closest(".js-noteSection");
		})

		//Выход из модального окна
		$('body').on('click', '#js-modalClose', function() {

			$('.js-modalWindow').animate({
				opacity: 0,
				top: '45%'},
				200, function() {
					$(this).addClass('displayNone');
					$('#js-overlay').fadeOut(200);
					$('#js-syncUsers').empty();
					$('#js-shareMessage').addClass('displayNone');
			});

			var $curnote = $clickedNoteSection.find(".js-note[id='" + $('.js-modalWindow').attr("id") + "']");

			if(addedShareEmails.length != 0) { // если заметка кому-то расшарена, то удаляем ее, переносим другим

				var $noteDiv = $('#js-noteDiv');

				addedShareEmails.forEach(function(item, i, arr) {
					var otherShareUserEmail = item;
					var $shareUserNames = $(".js-shareUserEmail");
					var userName;

					$shareUserNames.each(function() {
						if($(this).text() == item) userName = $(this).siblings(".js-shareUserName").text();
					})

					var $otherNoteSection = $(".js-noteSection[id='ns_" + otherShareUserEmail + "']");
					var $copyCurnote = curnote.clone();

					if(otherNoteSection.length > 0) {
						otherNoteSection.prepend($copyCurnote);
					} else {
						otherNoteSection = "<div class='js-noteSection noteSection ui-sortable' id='ns_" + otherShareUserEmail + "'></div>";

						var section = "<div class='js-section js-allSections textNoteSection js-nextSection'>" +
                        					"<img class='js-sectionPic sectionPic' src=" + $(".js-sectionPic").eq(0).attr("value") + ">" +
                                        	"<div class='js-sectionOwner sectionOwner'>" + "Общие с " + userName + "<span class='js-span'> (" +otherShareUserEmail + ")</span>" + "</div>" +
                        				"</div>";

						$noteDiv.append(section);
						$noteDiv.append(otherNoteSection);
                        $(".js-noteSection[id='ns_" + otherShareUserEmail + "']").append($copyCurnote);

					}

					//otherNoteSections.splice(0, 0, );
				})

				if($clickedNoteSection.length == 1 && $clickedNoteSection.attr('id') == "ns_"){
					//clickedNoteSection.remove();

					var $nextSection = $('.js-section').eq(0);
									var $nextOwner = $nextSection.find(".js-sectionOwner");
									var $nextPic = $nextSection.find(".js-sectionPic");

									clickedNoteSection.remove();
					var $actual = $("#js-actualSection");
					var $actualOwner = $actual.find(".js-sectionOwner");
					var $actualPic = $actual.find(".js-sectionPic");

									$actual.attr("value", $nextSection.text());
									$actualOwner.text($nextOwner.text());
									$actualPic.attr("value", $nextPic.attr("src"));
									$actualPic.attr("src", $nextPic.attr("src"));
									$nextSection.remove();

									$nextSection = $('js-section').eq(0);

									if ($nextSection[0] != null) { $nextSection.addClass("js-nextSection"); };
				} else if(clickedNoteSection.attr('id') == "ns_") {
					curnote.remove();
				}
			}
			addedShareEmails = []; // обнуляем добавленные имейлы
			$clickedNoteSection = null;
		});


		//функция запроса синхронизированных пользователей
		function checkSharedNote(noteId) {

			sendInfo = {
				id: noteId
			};

			$.ajax({
	    		type: "POST",
				url: "/telenote/checknote",
				dataType: "json",
				data: sendInfo,
				headers: {'X-CSRF-TOKEN': $("meta[name = _csrf]").attr("content") },
				success: function(data) {

					$('.syncUsers').prepend("<div id='js-owner' class='shareUser'>" +
					 		"<div class='shareUserImg unsetImg'></div>" +
					 		"<div class='shareUserInfo'>" + 
					 			"<div class='js-shareUserName shareUserName'>" + data[0].name + "<span> (автор)</span></div>" +
					 			"<div class='js-shareUserEmail shareUserEmail'>" + data[0].email + "</div>" + 
					 		"</div>" +
					 	"</div>");

					for (var i = data.length-1; i > 0; i--) {
					 	$('#js-owner').after("<div id='" +  data[i].id + "' class='js-shareUser shareUser'>" +
					 		"<div class='shareUserImg unsetImg'></div>" +
					 		"<div class='shareUserInfo'>" + 
					 			"<div class='js-shareUserName shareUserName'>" + data[i].name + "</div>" +
					 			"<div class='js-shareUserEmail shareUserEmail'>" + data[i].email + "</div>" + 
					 		"</div>" +
					 		"<div class='shareActionDiv'>"+
					 			"<button class='js-deleteShare deleteShare'></button>"+
					 		"</div>"+
					 	"</div>");
					};
				}
			})
		}



		$('#js-addShareEmail').blur(function() {
			$('#js-shareMessage').addClass('displayNone');
		})

		$("#js-addShareEmail").on('input', function() {
			$('#js-shareMessage').removeClass('messageFail');
        });

		//поведение плюсика в шаринге
		$('#js-addShareEmail').keyup(function(e){

			if ($(this).val() == "") {
			
				$('#js-addShare').addClass('displayNone');
			
			} else {
			
				$('#js-addShare').addClass('displayBlock');

				var code = e.which; // recommended to use e.which, it's normalized across browsers
            
                if(code == 13) { // 13 - enterKey
            
                	$('#js-addShare').click();
            
                }
			};
		})


		//удаление синхронизации
        $('.js-modalWindow').on('click', '.js-deleteShare', function() {

            var sendInfo = {
              userId: $(this).closest('.js-shareUser').attr('id'),
              noteId: $(this).closest('.js-modalWindow').attr('id')
            };

            $.ajax({
            	type: "POST",
                url: "/telenote/deletesync",
            	dataType: "json",
            	data: sendInfo,
            	headers: {'X-CSRF-TOKEN': $("meta[name = _csrf]").attr("content") },
            	success: function(data) {
                    $('#'+sendInfo.userId).empty().animate({
                    	height: '0px'
                    }, 300, 'swing', function() {
                    	$('#'+sendInfo.userId).remove();
                    })
                }
            });
        })


		$(document).keydown(function (key) {

            if ((key.which == 27) && ($('.js-modalWindow').css('display') == 'block')) { $('#js-modalClose').click(); }
    
        })

	});
})(jQuery);
