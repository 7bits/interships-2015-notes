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


		var timeout_id;
		$('.noteDiv').on('click', '.delBtn', function(self) {
			//функция удаления заметки из базы и с рабочего поля
			var id = $(self.target).parent().parent().attr("id");
			
			if (id == '-1') {
				$(".cell[id=" + id + "]").remove();
			} else {
				$.ajax({
					type: "DELETE",
					headers: {'X-CSRF-TOKEN': $("meta[name = _csrf]").attr("content") },
					url: "/telenote/" + id
				}).done( function() {
					clearTimeout(timeout_id);
					var element = $(".cell[id=" + id + "]");
					element.css('min-width', '0px');
					element.children('.delBtn').css('visibility', 'hidden');
					element.children('.shaBtn').css('visibility', 'hidden');
					
					element.animate({
							height: '2px',
							border: '0px',
							marginTop: '123px'
						}, 150, 'swing');

					element.animate({
							width: '0px',
						}, 150, 'swing', function() {
							element.remove();

							if ($('.cell').length == 0) {
								$('.noteDiv')[0].innerHTML += '<span id="emptyList">У вас нет заметок</span>';
							};	
					});

					if (document.documentElement.clientWidth > 840) {
						$('.status').text("Все заметки сохранены");
					} else {
						$('.minStatus').text('');
						$('.minStatus').css('background-image', 'url(../img/ok.png)');
					};
				});
			}
		});


		window.onresize = function() {
			//определение размера рабочей области сайта
			//var elementHeight = document.documentElement.clientHeight;
			var bodyHeight = document.documentElement.clientHeight;
			//$(".workDiv").outerHeight(elementHeight);
			$("body").outerHeight(bodyHeight);
			//$(".noteDiv").css('min-height', bodyHeight + 'px');
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
                return; //check to prevent multiple simultaneous triggers
            }
            oldVal = currentVal;

            if (document.documentElement.clientWidth > 840) {
            	$('.status').text("Сохранение...");
            } else {
            	$('.minStatus').css('background-image', 'none');
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
				clearTimeout(timeout_id);

				timeout_id = setTimeout(function() {
					App.Note.save(data, function() {
						if (document.documentElement.clientWidth > 840) {
							$('.status').text("Все заметки сохранены");
						} else {
							$('.minStatus').text('');
							$('.minStatus').css('background-image', 'url(../img/ok.png)');
						};
					});
				}, 750);
			})
		})

		function IsEmail(email) {
		  var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		  return regex.test(email);
		}

		$('body').on('keyup', ".textboxRegProperty", function(event){
    		if(event.keyCode == 13){
    			var id = $(this).attr("id");
    			var email = $(this).val().toLowerCase();

    			var label = $('.js-shareStatus');
   			
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
							label.text(data.message);
							label.css('display', 'block');
							label.css('color', '#32c87a');
						}
					}).fail( function(data) {
						label.text(data.responseJSON.message);
                        label.css("display", "block");
                        label.css("color", "#ef6161 ");
                	});
				}
    			else {
    				label.text("Неправильный email!");
                   	label.css("display", "block");
                   	label.css("color", "#ef6161 ");
    			}
    			
        	}
    	});
		

		//Анимация панели с кнопками
		$('.noteDiv').on('mouseenter', '.cell', function() {
			var control = $(this).children('.control');
			var delBtn = $(this).children('.control').children('.delBtn');
			var shareBtn = $(this).children('.control').children('.shaBtn');

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
			var control = $(this).children('.control');
			var delBtn = $(this).children('.control').children('.delBtn');
			var shareBtn = $(this).children('.control').children('.shaBtn');

			delBtn.css('height', '0px');
			shareBtn.css('height', '0px');

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


		//Обработчик кнопки share и генерация модельного окна
		$('.workDiv').on('click', '.shaBtn', function() {

			var self = $(this);
			$('textarea').trigger('blur');

			if ($('#dialog')[0] == null) {
				var container = $('.container');

				var modal = document.createElement('div');
				modal.setAttribute('id', 'dialog');

				var text = document.createElement('input');
				text.setAttribute('class', 'textboxRegProperty');
				text.setAttribute('type', 'textbox');
				text.setAttribute('id', self.closest('.cell').attr("id"));
				text.setAttribute('style', 'color: black; font-size: 15px; width: 100%;');
				text.setAttribute('placeholder', 'Введите email');

				var label = document.createElement('label');
				label.setAttribute('style', 'font-size: 15px; color: white; display: none');
				label.setAttribute('class', 'js-shareStatus');

				modal.appendChild(text);
				modal.appendChild(label);
				container.append(modal);

				$('#dialog').dialog();
				$('.ui-dialog-titlebar').css('padding', '0px');
				$('.ui-dialog-titlebar').css('border-radius', '0px');
				$('.ui-dialog-titlebar-close').text('X');
				$('.ui-dialog-titlebar-close').attr('style', 'color: black; text-align: center; font-size: 13px;');

				$('.textboxRegProperty').trigger('focus');
			} else {
				$('.ui-dialog-titlebar-close').trigger('click');
				self.trigger('click');
			};
			
		})


		$('body').on('click', '.ui-dialog-titlebar-close', function() {
			$(this).closest('.ui-dialog').remove();
			$('#dialog').remove();
		})
	});
})(jQuery);
