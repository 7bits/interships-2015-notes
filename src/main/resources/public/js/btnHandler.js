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
					$(".cell[id=" + id + "]").remove();
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


		$('.noteDiv').on('keyup', 'textarea', function() {
			var timeout_id;
			var data = {
						id: $(this).parent().parent().attr('id'),
						text: $(this).val()
				}

			$(function() {
				clearTimeout(timeout_id);

				timeout_id = setTimeout(function() {
					App.Note.save(data, function() {});
				}, 1500);
			})
		})
	});
})(jQuery);
