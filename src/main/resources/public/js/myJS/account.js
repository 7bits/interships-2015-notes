(function($) {
	$(document).ready(function() {

		$('#select').change(function() {
			
			if ($(this).val() != 'Сменить тему') {
				$.ajax({
					type: "GET",
					url: "/account/style/" + $('select').val(),
					dataType: "json",
					headers: {'X-CSRF-TOKEN': $("meta[name = _csrf]").attr("content") },
					success: function() {
						$('#dinamic').attr('href', 'css/myCSS/'+$('#select').val()+'.css');
					}
				})
			};
		})


		$('#changeNameBtn').click(function() {

			var changeName = $('#changeNameTextbox').val();
			if ($('.user').text() != changeName) {

				if (!checkUsername(changeName)) {
					$.ajax({
						type: "GET",
						url: "/account/changename/" + changeName,
						dataType: "json",
						headers: {'X-CSRF-TOKEN': $("meta[name = _csrf]").attr("content") },
						success: function() {
							$('.user').text(changeName);
						}
					})
				};
			};
		})

		function checkUsername(username) {
			var regexName = /\\s/
			return regexName.test(username);
		}


		$('#changePassBtn').click(function() {
			var oldPass = $('#changePassTextboxOld').val();
			var newPass = $('#changePassTextboxNew').val();
			var curClass;

			if ((oldPass == newPass) || (newPass == '') || (oldPass == '')) {
				return;
			} else {
				var data = {
					oldPass: oldPass,
					newPass: newPass
				};

				$.ajax({
					type: "POST",
					url: "/account/changepass",
					dataType: "json",
					data: data,
					headers: {'X-CSRF-TOKEN': $("meta[name = _csrf]").attr("content") },
					success: function() {
						$('#changePassTextboxOld').val('');
						$('#changePassTextboxNew').val('');
						$('#changePassMessage').removeClass('changePassMessage').addClass('changePassMessageSuccess');
						$('#changePassMessage').text('пароль изменён');

						curClass = 'changePassMessageSuccess';
					}
				}).fail(function(data) {
					$('#changePassMessage').removeClass('changePassMessage').addClass('changePassMessageError');
					$('#changePassMessage').text('ошибка');

					curClass = 'changePassMessageError';
				})

				setTimeout(function() {
					$('#changePassMessage').removeClass(curClass).addClass('changePassMessage');
				}, 5000);
			};
		})
	})
})(jQuery);