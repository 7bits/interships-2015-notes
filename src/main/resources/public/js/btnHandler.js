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

		function IsEmail(email) {
		  var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		  return regex.test(email);
		}

		$(".email_textbox").keyup(function(event){
    		if(event.keyCode == 13){
    			var id = $(this).parents(".cell").attr("id");
    			var email = $(this).val();    			

    			var label = $(this).parent().find(".email_label");
   			

    			if(IsEmail(email)) {    				
    				label.css("color", "rgb(94, 236, 151)");
    				label.text("Правильный email!");
    				label.css("display", "block");

    				setTimeout(function(){	    		
    					label.css("display", "none");    					
    					$(".email_btn").click();
    				},1000);
    				
    				var sendInfo = {
    					id: id,
    					email: email
    				}

    				$.ajax({
	    				type: "POST",
						url: "/telenote/share",
						dataType: "json",
						headers: {'X-CSRF-TOKEN': $("meta[name = _csrf]").attr("content") },
						data: sendInfo
					}).done( function() {
						alert("Расшарено");
						// label.css("color", "rgb(94, 236, 151)");
    		// 			label.text("Расшарено!");
    		// 			label.css("display", "block");					
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
