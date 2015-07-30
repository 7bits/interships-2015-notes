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
					element.children('.dropdown').css('visibility', 'hidden');
					
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
			//активация модального окна
		    $(this).find('a').tab("show");
		});


		$(document).ready(function() {
			//обработка ошибок при авторизации

			if (document.location.href.match(/.+\/?error=true/g) != null) {
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

		$('.noteDiv').on('keyup', ".email_textbox", function(event){
    		if(event.keyCode == 13){
    			var id = $(this).parents(".cell").attr("id");
    			var email = $(this).val().toLowerCase();

    			var label = $(this).parent().find(".email_label");
   			

    			if(IsEmail(email)) {
//    				setTimeout(function(){
//    					label.css("display", "none");
//    					$(".email_btn").click();
//    				},1000);
    				
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
                            label.css("display", "block");
                            label.css("color", "#32C87A");
						}
					}).fail( function(data) {
						label.text(data.responseJSON.message);
						label.css("display", "block");
						label.css("color", "#ef6161");
                	});
				}
    			else {
    				label.text("Неправильный email!");
    				label.css("display", "block");
    				label.css("color", "#ef6161");
    				
    			}
    			
        	}
    	});
		

		//Анимация панели с кнопками
		$('.noteDiv').on('mouseenter', '.cell', function() {
			var control = $(this).children('.control');
			var delBtn = $(this).children('.control').children('.delBtn');
			var shareBtn = $(this).children('.control').children('.dropdown').children('.shaBtn');

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
			var shareBtn = $(this).children('.control').children('.dropdown').children('.shaBtn');

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
		$('.workDiv').on('click', '.cell', function() {

			if ($('textarea')[0] == null) {

				var self = $(this);
				self.css('background-color', '#d6d6d6')
				self.children('.content').children('textarea').css('background-color', '#d6d6d6');

				var del = self.children('.control').children('.delBtn');
				del.css('background-color', '#d6d6d6');

				$(del).mouseenter(function() {
					$(this).css('background-color', '#e6e6e6');
				}).mouseleave(function() {
					$(this).css('background-color', '#d6d6d6');
				})

				var sha = self.children('.control').children('.dropdown').children('.shaBtn');
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
			self.children('.content').css('display', 'inherit');		

			var del = self.children('.control').children('.delBtn');
			del.css('background-color', '#f5f5f5');

			$(del).mouseenter(function() {
				$(this).css('background-color', '#e2e2e2');
			}).mouseleave(function() {
				$(this).css('background-color', '#f5f5f5');
			})

			var sha = self.children('.control').children('.dropdown').children('.shaBtn');
			sha.css('background-color', '#f5f5f5');

			$(sha).mouseenter(function() {
				$(this).css('background-color', '#e2e2e2');
			}).mouseleave(function() {
				$(this).css('background-color', '#f5f5f5');
			})

			self.children('.control').css('border-color', '#dcdcdc');
		})
	});
})(jQuery);
