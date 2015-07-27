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
			var id = $(self.target).parent().attr("id");
			
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

					$('.workDiv').css('min-height', '260px');
					
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

					$('.workDiv').css('min-height', '0px');
				});
			}
		});


		window.onresize = function() {
			//определение размера рабочей области сайта
			var elementHeight = document.documentElement.clientHeight - 170;
			var bodyHeight = document.documentElement.clientHeight;
			$(".workDiv").outerHeight(elementHeight);
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


		$('.noteDiv').on('keyup', 'textarea', function() {
			var data = {
						id: $(this).parent().parent().attr('id'),
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
	});
})(jQuery);
