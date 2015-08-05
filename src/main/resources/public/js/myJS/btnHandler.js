(function($){
	$(document).ready(function () {

		App.Note.save = function(data, callback) {
			$.ajax({
				type: "POST",
				url: "/telenote",
				dataType: "json",
				headers: {'X-CSRF-TOKEN': $("meta[name = _csrf]").attr("content") },
				data: data
			}).done(function(data) {
				callback(data);
			});
		};


		var timeoutId;

		$('.noteDiv').on('click', '.delBtn', function(self) {
			//функция удаления заметки из базы и с рабочего поля
			var id = $(this).closest('.cell').attr("id");
			
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


		$('li').click(function() {
			//активация таба
		    $(this).find('a').tab("show");
		});


		$(document).ready(function() {
			//обработка ошибок при авторизации

			if ($('.js-login-form').data('error') == true) {
				$('.js-enter').trigger('click');
			} else if (document.location.href.match(/.+\/signup/g)) {
				$('.js-reg').trigger('click');
			};	
		});


		var oldVal ="";
		$('.noteDiv').on('keydown', 'textarea', function() {
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
			var data = {
				id: $(this).closest('.cell').attr('id'),
				text: $(this).val()
			}

			$(function() {
				clearTimeout(timeoutId);

				timeoutId = setTimeout(function() {
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
					success: function(data) {
						infoLabel.text(data.message);
						infoLabel.css('display', 'block');
						infoLabel.css('color', '#32c87a');
					}
				}).fail(function(data) {
					infoLabel.text(data.responseJSON.message);
                    infoLabel.css("display", "block");
                    infoLabel.css("color", "#f74e19");
                });
			}
    		else {
    			$('.addShareEmail').trigger('focus');
    			infoLabel.text("Неверный адрес!");
                infoLabel.css("display", "block");
                infoLabel.css("color", "#f74e19");
    		}
    	});
		

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
		$('.workDiv').on('click', '.content', function() {

			if ($('textarea')[0] == null) {

				var self = $(this).parent();
				self.css('background-color', '#d6d6d6')
				self.children('.content').children('textarea').css('background-color', '#d6d6d6');

				var del = self.children('.control').children('.delBtn');
				del.css('background-color', '#d6d6d6');

				$(del).mouseenter(function() {
					$(this).css('background-color', '#e6e6e6');
				}).mouseleave(function() {
					$(this).css('background-color', '#d6d6d6');
				})

				var sha = self.children('.control').children('.shaBtn');
				sha.css('background-color', '#d6d6d6');

				$(sha).mouseenter(function() {
					$(this).css('background-color', '#e6e6e6');
				}).mouseleave(function() {
					$(this).css('background-color', '#d6d6d6');
				})

				self.children('.control').css('border-color', '#bbbbbb');

				var textarea = document.createElement('textarea');
				textarea.setAttribute('name', 'text');
				textarea.setAttribute('maxlength', '20000');
				textarea.classList.add('js-textarea', 'textarea');

				self.children('.content').css('display', 'none');
				self.prepend(textarea);

				$('.js-textarea').text(self.children('.content').text());
				self.children('.content').text('');

				$('.js-textarea').trigger('focus');
			} else {
				$('.js-textarea').trigger('blur');
				$(this).trigger('click');
			};

		}).on('blur', 'textarea', function() {

			var self = $(this).closest('.cell');
			self.css('background-color', '#f5f5f5')
			
			var textarea = $('.js-textarea');
			self.children('.content').text(textarea.val());

			textarea.remove();
			self.children('.content').css('display', 'block');		

			var del = self.children('.control').children('.delBtn');
			del.css('background-color', '#f5f5f5');

			$(del).mouseenter(function() {
				$(this).css('background-color', '#e2e2e2');
			}).mouseleave(function() {
				$(this).css('background-color', '#f5f5f5');
			})

			var sha = self.children('.control').children('.shaBtn');
			sha.css('background-color', '#f5f5f5');

			$(sha).mouseenter(function() {
				$(this).css('background-color', '#e2e2e2');
			}).mouseleave(function() {
				$(this).css('background-color', '#f5f5f5');
			})

			self.children('.control').css('border-color', '#dcdcdc');
		})


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
			});

			$('.addShareEmail').trigger('focus');
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


		//поведение плюсика в шаринге
		$('.addShareEmail').keydown(function() {
			if ($(this).val() == "") {
				$('.addShare').css('display', 'none');
			} else {
				$('.addShare').css('display', 'block');
			};
		})
	});
})(jQuery);
