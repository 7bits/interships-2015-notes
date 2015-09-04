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


		var shareData = {
			addedShareEmails: [],
			$clickedNoteSection: null
		}
		//расшаривание
		$('#js-addShare').click(function() {

    		shareData.addedShareEmails = addShare(shareData.addedShareEmails);

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
		

		//Обработчик кнопки share и генерация модального окна
		$('body').on('click', '.js-shaBtn', function() {

			var $note = $(this);
			modalStart($note);
			shareData.$clickedNoteSection = $note.closest(".js-noteSection");

		})


		//Выход из модального окна
		$('body').on('click', '#js-modalClose', function() {

			shareData = modalClose(shareData);

		});


		$('#js-addShareEmail').blur(function() {
			
			$('#js-shareMessage').addClass('displayNone');
		
		})


		$("#js-addShareEmail").on('input', function() {

			$('#js-shareMessage').removeClass('messageFail');

        });


		//поведение плюсика в шаринге
		$('#js-addShareEmail').keyup(function(key){

			var $emailInput = $(this);
			addShareBtn($emailInput, key);

		})


		//удаление синхронизации
        $('.js-modalWindow').on('click', '.js-deleteShare', function() {

        	var $deleteShare = $(this);
            deleteShare($deleteShare);

        })


		$(document).keydown(function (key) {

            if ((key.which == 27) && ($('.js-modalWindow').css('display') == 'block')) { $('#js-modalClose').click(); }
    
        })

	});
})(jQuery);
