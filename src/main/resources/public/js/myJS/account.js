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
						$('#dinamic').attr('href', 'css/myCSS/'+$('select').val()+'.css');
					}
				})
			};
		})


		$('#changeNameBtn').click(function() {
			if ($('.user').text() != $('#changeNameTextbox').val()) {

				if (!checkUsername($('#changeNameTextbox').val())) {
					$.ajax({
						type: "GET",
						url: "/account/changename/" + $('#changeNameTextbox').val(),
						dataType: "json",
						headers: {'X-CSRF-TOKEN': $("meta[name = _csrf]").attr("content") },
						success: function() {
							$('.user').text($('#changeNameTextbox').val());
						}
					})
				};
			};
		})

		function checkUsername(username) {
			var regexName = /\\s/
			return regexName.test(username);
		}

	})
})(jQuery);