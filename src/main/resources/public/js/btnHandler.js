(function($){
	$(document).ready(function() {
		$('.noteDiv').on('click', '.edBtn', function(self) {
			
			var id = $(self.target).parent().attr("id");
			var text = $(this).parent().find("textarea").val();
			
			var sendInfo = {
				id: id,
				text: text
			};

			$.ajax({
				type: "POST",
				url: "/telenote",
				dataType: "json",
				headers: {'X-CSRF-TOKEN': $("meta[name = _csrf]").attr("content") },
				data: sendInfo
			}).done(function(data) {
				$(self.target).parent(".cell").attr("id", data);
			});
		});

		$('.noteDiv').on('click', '.delBtn', function(self) {
			
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

			if ($(".cell").length) {

			}
		});

		window.onresize = function() {
			var elementHeight = document.documentElement.clientHeight - 170;
			var bodyHeight = document.documentElement.clientHeight;
			$(".workDiv").outerHeight(elementHeight);
			$("body").outerHeight(bodyHeight);
		};


		$('.js-enter').click(function() {
			document.getElementById('signup').classList.remove('active', 'in');
			document.getElementById('signin').classList.add('active', 'in');
			
			document.getElementById('signinHref').classList.add('active');
			document.getElementById('signupHref').classList.remove('active');
		});


		$('.js-reg').click(function() {
			document.getElementById('signin').classList.remove('active', 'in');
			document.getElementById('signup').classList.add('active', 'in');
		
			document.getElementById('signupHref').classList.add('active');
			document.getElementById('signinHref').classList.remove('active');
		});


		$('li').click(function() {
		    $(this).find('a').tab("show");
		});


		$(document).ready(function() {

			if (document.location.href.match(/.+\/?error=true/g) != null) {
				$('.js-enter').trigger('click');
			} else if (document.location.href.match(/.+\/signup/g)) {
				$('.js-reg').trigger('click');
			};	
		});
	});
})(jQuery);
