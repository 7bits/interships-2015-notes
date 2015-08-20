(function($){
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

		$('.noteDiv').on('click', '.delBtn', function(self) {
			//функция удаления заметки из базы и с рабочего поля
			var id = $(this).closest('.cell').attr("id");
			var thisTextNoteSection = $(this).closest(".textNoteSection");
			
			$.ajax({
				type: "DELETE",
				headers: {'X-CSRF-TOKEN': $("meta[name = _csrf]").attr("content") },
				url: "/telenote/" + id
			}).done( function() {
				clearTimeout(timeoutId);
				var cell = $(".cell[id=" + id + "]");
				cell.css('min-width', '0px');
				cell.children('.delBtn').css('visibility', 'hidden');
				cell.children('.shaBtn').css('visibility', 'hidden');
					
				cell.animate({
						height: '2px',
						border: '0px',
						marginTop: '123px'
					}, 150, 'swing');

				cell.animate({
						width: '0px',
					}, 150, 'swing', function() {
						cell.remove();

						if(thisTextNoteSection.find(".cell").length == 0) {
								thisTextNoteSection.remove();
						}

						if ($('.cell').length == 0) {
							$('.noteDiv')[0].innerHTML += '<span id="emptyList">У вас нет заметок</span>';
						};
				});

				if (document.documentElement.clientWidth > 840) {
					$('.status').text("Все заметки сохранены");
				} else {
					$('.minStatus').text('');
					$('.minStatus').css('background-image', "url('/img/ok.png')");
				};
			});
		});


		window.onresize = function() {
			//определение размера рабочей области сайта
			var bodyHeight = document.documentElement.clientHeight;
			$("body").outerHeight(bodyHeight);
		};


		$('.js-enter').click(function() {
			//вызов соответствующего таба по нажатии на ссылку
			document.getElementById('signup').classList.remove('active', 'in');
			document.getElementById('signin').classList.add('active', 'in');
			
			document.getElementById('signinHref').classList.add('active');
			document.getElementById('signupHref').classList.remove('active');
		});


		$('.js-reg').click(function() {
			//вызов соответствующего таба по нажатии на ссылку
			document.getElementById('signin').classList.remove('active', 'in');
			document.getElementById('signup').classList.add('active', 'in');
		
			document.getElementById('signupHref').classList.add('active');
			document.getElementById('signinHref').classList.remove('active');
		});



		var oldVal ="";
		$('.noteDiv').on('input', 'textarea', function() {
			var currentVal = $(this).val();

            if(currentVal == oldVal) {
                return; //проверка изменения состояния текста
            }

            oldVal = currentVal;

            if (document.documentElement.clientWidth > 800) {
            	$('.status').text("Сохранение...");
            } else {
            	$('.minStatus').addClass('minStatusTyping');
            	$('.minStatus').text('...');
            };
			
		})


		//автосейвер
		$('.noteDiv').on('keyup', 'textarea', function() {
			clearTimeout(timeoutId);

			var text = $(this).val();

			var data = {
            	id: $(this).closest('.cell').attr('id'),
            	text: text
            }

			timeoutId = setTimeout(function() {
				data.text = htmlspecialchars(data.text);
				data.text = nl2br(data.text);

				timeoutId = setTimeout(function() {
				    //отправить другим пользователям
				    var cmd = {
                        id: data.id,
                        text: data.text,
                        command: "block,text"
                    };

                    sendCommand(cmd);

					App.Note.save(data, function() {
						if (document.documentElement.clientWidth > 800) {
							$('.status').text("Все заметки сохранены");
						} else {
							$('.minStatus').text('');
							$('.minStatus').removeClass('minStatusTyping');
						};
					});
				}, 750);

		})


		function IsEmail(email) {
		  var emailRegex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		  return emailRegex.test(email);
		}


		//расшаривание
		$('.addShare').click(function() {

    		var id = $('.modalWindow').attr('id');
    		var email = $('.addShareEmail').val().toLowerCase();
    		var infoLabel = $('.shareMessage');
    		var curClass;
   			
    		if(IsEmail(email)) {
    				
    			var sendInfo = {
    				id: id,
    				email: email
    			}

    			$.ajax({
	    			type: "POST",
					url: "/telenote/share",
					dataType: "json",
					headers: {'X-CSRF-TOKEN': $("meta[name = _csrf]").attr("content") },
					data: sendInfo,
					success: function(data){

						addingShareUser(data)

						$('.addShareEmail').val('');
					}
				}).fail(function(data) {
					$('.addShareEmail').trigger('focus');
					infoLabel.text(data.responseJSON.message);
					curClass = 'messageFail';
                    infoLabel.addClass(curClass);
                });
			}
    		else {
    			$('.addShareEmail').trigger('focus');
    			infoLabel.text("Неверный адрес!");
    			curClass = 'messageFail';
                infoLabel.addClass(curClass);
    		}

//    		setTimeout(function() {
//    			infoLabel.removeClass(curClass);
//    		}, 5000)
    	});


		function addingShareUser(data) {
			
			var shareUser = "<div id='js-newShare' class='shareUser' style='height: 0px;'></div>";
			var innerShareUser = "<div class='shareUserImg unsetImg'></div>"+
			"<div class='shareUserInfo'>"+
				"<div class='shareUserName'>"+data.username+"</div>"+
				"<div class='shareUserEmail'>"+$('.addShareEmail').val()+"</div>"+
			"</div>"+
			"<div class='shareActionDiv'>"+
					"<button class='deleteShare'></button>"+
			"</div>";

			$('.syncUsers').append(shareUser);

			shareUser = $('#js-newShare').animate({
				height: '62px'
			}, 300, 'swing', function() {
				shareUser.append(innerShareUser);
				shareUser.removeAttr('id').removeAttr('style');
			})
		}
		

		//Анимация панели с кнопками
		$('.noteDiv').on('mouseenter', '.cell', function() {
			var control = $(this).find('.control');
			var delBtn = $(this).find('.delBtn');
			var shareBtn = $(this).find('.shaBtn');

			control.css('visibility', 'inherit');

			control.stop();

			control.animate({
				height: '40px',
				marginTop: '0px'
			}, 200, 'swing', function() {
				delBtn.css('height', '40px');
				shareBtn.css('height', '40px');
			});
		}).on('mouseleave', '.cell', function() {
			var control = $(this).find('.control');
			var delBtn = $(this).find('.delBtn');
			var shareBtn = $(this).find('.shaBtn');

			delBtn.addClass('btnHeightZero');
			shareBtn.addClass('btnHeightZero');

			control.animate({
				height: '0px',
				marginTop: '40px'
			}, 200, 'swing', function() {
				control.css('visibility', 'hidden');
			});
		})


		//подмена активного элемента
		$('.workDiv').on('click', '.clickable', function() {

			if ($('textarea')[0] == null) {

                //блокируем заметку
                var cmd = {
                    id: $(this).parent('.cell').attr("id"),
                    command : "block"
                }
                sendCommand(cmd);

				var self = $(this).parent('.cell');

				var textarea = document.createElement('textarea');
				textarea.setAttribute('name', 'text');
				textarea.setAttribute('maxlength', '20000');
				textarea.classList.add('js-textarea', 'textarea');

				var content = self.children('.content').css('display', 'none');
				self.prepend(textarea);

				var text = content.html();

				text = br2nl(text);
				text = rhtmlspecialchars(text);

				$('.js-textarea').text(text);
				content.text('');

				$('.js-textarea').trigger('focus');
				$('.js-textarea').scrollTop(0);

			} else {
				$('.js-textarea').trigger('blur');
				$(this).trigger('click');
			};

		}).on('blur', 'textarea', function() {
           var cmd = {
               id: $(this).parent('.cell').attr("id"),
               command : "unblock"
           }
           sendCommand(cmd);

			var self = $(this).closest('.cell');
			
			var textarea = $('.js-textarea');

			var text = textarea.val();
			text = htmlspecialchars(text);
			text = nl2br(text);

			self.children('.content').html(text);
			textarea.remove();
			self.children('.content').css('display', 'block');	
		})


		$(function() {
			$.each($('.content'), function(i, item) {
				var text = $(item).text();
				$(item).text('');
				$(item).html(text);
			})
		})


		function nl2br (str) {
		    var breakTag = "<br>";    
		    return (str + '').replace(/(\r\n|\n\r|\r|\n)/g, breakTag)	;//.replace(/&/g, "&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;");
		}    

		function br2nl (str) {
			var nl = "\n";
			return (str + '').replace(/<br>/g, nl);//.replace(/&lt;/g,"<").replace(/&gt;/g,">").replace(/&amp;/g, "&");
			//			return (str + '').replace(/(<br>)|(<br \/>)/g, nl).replace(/&lt;/g,"<").replace(/&gt;/g,">");//.replace(/&amp;/g, "&");

		}

		function htmlspecialchars(str) {
         if (typeof(str) == "string") {
          str = str.replace(/&/g, "&amp;"); /* must do &amp; first */
          str = str.replace(/"/g, "&quot;");
          str = str.replace(/'/g, "&#039;");
          str = str.replace(/</g, "&lt;");
          str = str.replace(/>/g, "&gt;");
          }
         return str;
         }

         function rhtmlspecialchars(str) {
          if (typeof(str) == "string") {
           str = str.replace(/&gt;/ig, ">");
           str = str.replace(/&lt;/ig, "<");
           str = str.replace(/&#039;/g, "'");
           str = str.replace(/&quot;/ig, '"');
           str = str.replace(/&amp;/ig, '&'); /* must do &amp; last */
           }
          return str;
          }

		//Обработчик кнопки share и генерация модального окна
		$('.workDiv').on('click', '.shaBtn', function() {

			$('textarea').trigger('blur');

			var self = $(this).closest('.cell');
			var id = self.attr('id');
			$('.modalWindow').attr('id', id);

			checkSharedNote(id);

			$('#overlay').fadeIn(200, 
				function() {
					$('.modalWindow').css('display', 'block').animate({
						opacity: 1,
						top: '45%'},
						200);

					$('.addShareEmail').trigger('focus');
			});
		})

		//Выход из модального окна
		$('body').on('click', '#modalClose', function() {

			$('.modalWindow').animate({
				opacity: 0,
				top: '45%'},
				200, function() {
					$(this).css('display', 'none');
					$('#overlay').fadeOut(200);
					$('.syncUsers').empty();
					$('.shareMessage').css('display', 'none');
			});

			
		})


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

					$('.syncUsers').prepend("<div id='owner' class='shareUser'>" +
					 		"<div class='shareUserImg unsetImg'></div>" +
					 		"<div class='shareUserInfo'>" + 
					 			"<div class='shareUserName'>" + data[0].username + "<span> (автор)</span></div>" + 
					 			"<div class='shareUserEmail'>" + data[0].email + "</div>" + 
					 		"</div>" +
					 	"</div>");

					for (var i = data.length-1; i > 0; i--) {
					 	$('#owner').after("<div id='" +  data[i].id + "' class='shareUser'>" +
					 		"<div class='shareUserImg unsetImg'></div>" +
					 		"<div class='shareUserInfo'>" + 
					 			"<div class='shareUserName'>" + data[i].username + "</div>" + 
					 			"<div class='shareUserEmail'>" + data[i].email + "</div>" + 
					 		"</div>" +
					 		"<div class='shareActionDiv'>"+
					 			"<button class='deleteShare'></button>"+
					 		"</div>"+
					 	"</div>");
					};
				}
			})
		}



		$('.addShareEmail').focus(function() {
			$('.addShareEmail').css('border-bottom', '1px solid #383838');
		}).blur(function() {
			$('.addShareEmail').css('border-bottom', '0');
			$('.shareMessage').css('display', 'none');
		})

		$(".addShareEmail").on('input', function() {
			$('.shareMessage').removeClass('messageFail');
        });

		//поведение плюсика в шаринге
		$('.addShareEmail').keyup(function(e){
			if ($(this).val() == "") {
				$('.addShare').css('display', 'none');
			} else {
				$('.addShare').css('display', 'block');

				var code = e.which; // recommended to use e.which, it's normalized across browsers
                if(code == 13) { // 13 - enterKey
                	$('.addShare').click();
                }
			};
		})


		//удаление синхронизации
        $('.modalWindow').on('click', '.deleteShare', function() {

            var sendInfo = {
              userId: $(this).closest('.shareUser').attr('id'),
              noteId: $(this).closest('.modalWindow').attr('id')
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


		$(document).keydown(function (e) {
			var code = e.which;

            if (e.which == 27)  // 27 - escapeKey
            	if($('.modalWindow').css('display') == 'block')
					$('#modalClose').click();
        })

	});
})(jQuery);
