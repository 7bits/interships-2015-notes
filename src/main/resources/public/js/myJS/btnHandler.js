(function($) {
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

		$('body').on('click', '.js-delBtn', function(self) {
			//функция удаления заметки из базы и с рабочего поля
			var id = $(this).closest('.js-note').attr("id");
			
			$.ajax({
				type: "DELETE",
				headers: {'X-CSRF-TOKEN': $("meta[name = _csrf]").attr("content") },
				url: "/telenote/" + id
			}).done( function() {
				clearTimeout(timeoutId);
				var note = $(".js-note[id=" + id + "]");
				note.css('min-width', '0px');
				note.children('.delBtn').css('visibility', 'hidden');
				note.children('.shaBtn').css('visibility', 'hidden');
					
				note.animate({
						height: '2px',
						border: '0px',
						marginTop: '123px'
					}, 150, 'swing');

				note.animate({
						width: '0px',
					}, 150, 'swing', function() {

						note.remove();

						var actual = $("#js-actualSection");
						var actualOwner = actual.find(".js-sectionOwner");
						var actualPic = actual.find(".js-sectionPic");

						var allSections = $('.js-noteSection');

						allSections.each(function() {
							
							var thisNoteSection = $(this);

							if (thisNoteSection.find(".js-note").length == 0) {
								if (thisNoteSection.index() == 0) {
									
									var nextSection = $('.js-section').eq(0);
									var nextOwner = nextSection.find(".js-sectionOwner");
									var nextPic = nextSection.find(".js-sectionPic");

									$(".js-noteSection").eq(0).remove();
									

									actual.attr("value", nextSection.text());
									actualOwner.text(nextOwner.text());
									actualPic.attr("value", nextPic.attr("src"));
									actualPic.attr("src", nextPic.attr("src"));
									nextSection.remove();
									
									nextSection = $('js-section').eq(0);

									if (nextSection[0] != null) { nextSection.addClass("js-nextSection"); };
								
								} else {

									var deletedSection = $(".js-allSections").eq(thisNoteSection.index() - 1);

									if (actualOwner.text() == deletedSection.find(".js-sectionOwner").text()) {
										
										var nextSection = $(".js-nextSection");
										var nextOwner = nextSection.find(".js-sectionOwner");
										var nextPic = nextSection.find(".js-sectionPic");

										actualOwner.text(nextOwner.text());
										actualPic.attr("src", nextPic.attr("src"));
										actualPic.attr("value", nextPic.attr("src"));

										deletedSection.remove();
										thisNoteSection.remove();

										$(".js-prevSection").removeClass("js-prevSection");

										nextSection.removeClass("js-nextSection").addClass("js-prevSection");

										$("js-allSections").eq(nextSection.index() + 1).addClass("js-nextSection");

									} else {
										
										var next = $("js-allSections").eq(deletedSection.index() + 2)

										if (next[0] != null) { next.addClass("js-nextSection"); };

										deletedSection.remove();
										thisNoteSection.remove();
									
									};

								};
							
							}
						})
				});


				if (document.documentElement.clientWidth > 840) {
					$('#js-status').text("Все заметки сохранены");
				} else {
					$('#js-minStatus').text('');
					$('#js-minStatus').css('background-image', "url('/img/ok.png')");
				};
			});
		});



		var oldVal ="";
		$('body').on('input', '#js-textarea', function() {
			var currentVal = $(this).val();

            if(currentVal == oldVal) {
                return; //проверка изменения состояния текста
            }

            oldVal = currentVal;

            if (document.documentElement.clientWidth > 800) {
            	$('#js-status').text("Сохранение...");
            } else {
            	$('#js-minStatus').addClass('minStatusTyping');
            	$('#js-minStatus').text('...');
            };
			
		});


		//автосейвер
		$('body').on('keyup', '#js-textarea', function() {
			
			clearTimeout(timeoutId);

			var text = $(this).val();

			var data = {
            	id: $(this).closest('.js-note').attr('id'),
            	text: text
            }

            $(".js-note[id=" + data.id + "] .js-content").text(text);

			timeoutId = setTimeout(function() {
				    data.text = htmlspecialchars(data.text);
				    //отправить другим пользователям
				    var cmd = {
                        id: data.id,
                        text: data.text,
                        command: "block,text"
                    };

                    sendCommand(cmd);

                    data.text = nl2br(data.text);

					App.Note.save(data, function() {

						if (document.documentElement.clientWidth > 800) {
							
							$('#js-status').text("Все заметки сохранены");
						
						} else {
							
							$('#js-minStatus').text('');
							$('#js-minStatus').removeClass('minStatusTyping');
						
						};
					});
				}, 750);
		})


		function IsEmail(email) {
		  var emailRegex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		  return emailRegex.test(email);
		}


		var addedShareEmails = [];
		var addedShareName = [];
		//расшаривание
		$('#js-addShare').click(function() {

    		var id = $('.js-modalWindow').attr('id');
    		var email = $('#js-addShareEmail').val().toLowerCase();
    		var infoLabel = $('#js-shareMessage');
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

						addedShareEmails.splice(0, 0, $('#js-addShareEmail').val());

						$('#js-addShareEmail').val('');
					
					}
				}).fail(function(data) {
					
					$('#js-addShareEmail').focus();
					infoLabel.text(data.responseJSON.message);
					curClass = 'messageFail';
                    infoLabel.addClass(curClass);
                
                });
			}
    		else {
    			
    			$('#js-addShareEmail').focus();
    			infoLabel.text("Неверный адрес!");
    			curClass = 'messageFail';
                infoLabel.addClass(curClass);
    		
    		}
    	});


		function addingShareUser(data) {
			
			var shareUser = "<div id='js-newShare' class='shareUser js-shareUser' style='height: 0px;'></div>";
			var innerShareUser = "<div class='shareUserImg unsetImg'></div>"+
			"<div class='shareUserInfo'>"+
				"<div class='shareUserName'>"+data.username+"</div>"+
				"<div class='shareUserEmail'>"+$('.addShareEmail').val()+"</div>"+
			"</div>"+
			"<div class='shareActionDiv'>"+
					"<button class='js-deleteShare deleteShare'></button>"+
			"</div>";

			$('#js-syncUsers').append(shareUser);

			shareUser = $('#js-newShare').animate({
				height: '62px'
			}, 300, 'swing', function() {
				shareUser.append(innerShareUser);
				shareUser.removeAttr('id').removeAttr('style');
			})
		}
		

		//Анимация панели с кнопками
		$('body').on('mouseenter', '.js-note', function() {
			var control = $(this).find('.js-control');
			var delBtn = $(this).find('.js-delBtn');
			var shareBtn = $(this).find('.js-shaBtn');

			delBtn.removeClass('btnHeightZero');
			shareBtn.removeClass('btnHeightZero');
			

			control.css('visibility', 'inherit');

			control.stop();

			control.animate({
				height: '40px',
				marginTop: '0px'
			}, 200, 'swing', function() {
				delBtn.css('height', '40px');
				shareBtn.css('height', '40px');
			});
		}).on('mouseleave', '.js-note', function() {
			var control = $(this).find('.js-control');
			var delBtn = $(this).find('.js-delBtn');
			var shareBtn = $(this).find('.js-shaBtn');

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
		$('body').on('click', '.js-content', function() {

			if ($('#js-textarea')[0] == null) {

                //блокируем заметку
                var cmd = {
                    id: $(this).parent('.js-note').attr("id"),
                    command : "block"
                }
                sendCommand(cmd);

				var self = $(this).parent('.js-note');

				var textarea = "<textarea id='js-textarea' class='textarea' name='text' maxlength='20000'>"

				var content = self.children('.js-content').removeClass('displayBlock').addClass('displayNone');
				self.prepend(textarea);

				var text = content.html();

				text = br2nl(text);
				text = rhtmlspecialchars(text);

				$('.js-textarea').text(text);
				content.text('');

				$('.js-textarea').focus();
				$('.js-textarea').scrollTop(0);

			} else {
				$('.js-textarea').trigger('blur');
				$(this).trigger('click');
			};

		}).on('blur', '#js-textarea', function() {
           
           var cmd = {
               id: $(this).parent('.js-note').attr("id"),
               command : "unblock"
           }
           
           sendCommand(cmd);

			var self = $(this).closest('.js-note');
			
			var textarea = $('.js-textarea');

			var text = textarea.val();
			text = htmlspecialchars(text);
			text = nl2br(text);

			self.children('.js-content').html(text);
			textarea.remove();
			self.children('.js-content').removeClass('displayNone').addClass('displayBlock');	
		
		})


		$(function() {
			
			$.each($('.js-content'), function(i, item) {
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
		        var quot = "&quot";
		        str = str.replace(/'"'/g, quot);
		        str = str.replace(/"'"/g, "&#039;");
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

		var clickedNoteSection;
		//Обработчик кнопки share и генерация модального окна
		$('body').on('click', '.js-shaBtn', function() {

			$('.js-textarea').trigger('blur');

			var self = $(this).closest('.js-note');
			var id = self.attr('id');
			$('.js-modalWindow').attr('id', id);

			checkSharedNote(id);

			$('#js-overlay').fadeIn(200, 
				function() {
					$('.js-modalWindow').addClass('displayBlock').animate({
						opacity: 1,
						top: '45%'},
						200);

					$('#js-addShareEmail').focus();
			});
			clickedNoteSection = $(this).closest(".js-noteSection");
		})

		//Выход из модального окна
		$('body').on('click', '#js-modalClose', function() {

			$('.js-modalWindow').animate({
				opacity: 0,
				top: '45%'},
				200, function() {
					$(this).addClass('displayNone');
					$('#js-overlay').fadeOut(200);
					$('#js-syncUsers').empty();
					$('#js-shareMessage').addClass('displayNone');
			});

			var curnote = clickedNoteSection.find(".js-note[id='" + $('.js-modalWindow').attr("id") + "']");

			if(addedShareEmails.length != 0) { // если заметка кому-то расшарена, то удаляем ее, переносим другим

				var noteDiv = $('.noteDiv');
				addedShareEmails.forEach(function(item, i, arr) {
					var otherShareUserEmail = item;
					var shareUserNames = $(".js-shareUserEmail");
					var userName;

					shareUserNames.each(function() {
						if($(this).text() == item) userName = $(this).siblings(".js-shareUserName").text();
					})

					var otherNoteSection = $(".js-noteSection[id='ns_" + otherShareUserEmail + "']");
					var copyCurnote = curnote.clone();

					if(otherNoteSection.length > 0) {
						otherNoteSection.prepend(copyCurnote);
					} else {
						otherNoteSection = "<div class='js-noteSection noteSection ui-sortable' id='ns_" + otherShareUserEmail + "'></div>";

						var section = "<div class='js-section js-allSections textNoteSection js-nextSection'>" +
                        					"<img class='js-sectionPic sectionPic' src=" + $(".js-sectionPic").eq(0).attr("value") + ">" +
                                        	"<div class='js-sectionOwner sectionOwner'>" + "Общие с " + userName + "<span class='js-span'> (" +otherShareUserEmail + ")</span>" + "</div>" +
                        				"</div>";

						noteDiv.append(section);
						noteDiv.append(otherNoteSection);
                        $(".js-noteSection[id='ns_" + otherShareUserEmail + "']").append(copyCurnote);




					}

					//otherNoteSections.splice(0, 0, );
				})

				if(clickedNoteSection.length == 1 && clickedNoteSection.attr('id') == "ns_"){
					//clickedNoteSection.remove();

					var nextSection = $('.js-section').eq(0);
									var nextOwner = nextSection.find(".js-sectionOwner");
									var nextPic = nextSection.find(".js-sectionPic");

									clickedNoteSection.remove();
					var actual = $("#js-actualSection");
					var actualOwner = actual.find(".js-sectionOwner");
					var actualPic = actual.find(".js-sectionPic");

									actual.attr("value", nextSection.text());
									actualOwner.text(nextOwner.text());
									actualPic.attr("value", nextPic.attr("src"));
									actualPic.attr("src", nextPic.attr("src"));
									nextSection.remove();

									nextSection = $('js-section').eq(0);

									if (nextSection[0] != null) { nextSection.addClass("js-nextSection"); };
				} else if(clickedNoteSection.attr('id') == "ns_") {
					curnote.remove();
				}
			}
			addedShareEmails = []; // обнуляем добавленные имейлы
			clickedNoteSection = null;
		});


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

					$('.syncUsers').prepend("<div id='js-owner' class='shareUser'>" +
					 		"<div class='shareUserImg unsetImg'></div>" +
					 		"<div class='shareUserInfo'>" + 
					 			"<div class='js-shareUserName shareUserName'>" + data[0].name + "<span> (автор)</span></div>" +
					 			"<div class='js-shareUserEmail shareUserEmail'>" + data[0].email + "</div>" + 
					 		"</div>" +
					 	"</div>");

					for (var i = data.length-1; i > 0; i--) {
					 	$('#js-owner').after("<div id='" +  data[i].id + "' class='js-shareUser shareUser'>" +
					 		"<div class='shareUserImg unsetImg'></div>" +
					 		"<div class='shareUserInfo'>" + 
					 			"<div class='js-shareUserName shareUserName'>" + data[i].name + "</div>" +
					 			"<div class='js-shareUserEmail shareUserEmail'>" + data[i].email + "</div>" + 
					 		"</div>" +
					 		"<div class='shareActionDiv'>"+
					 			"<button class='js-deleteShare deleteShare'></button>"+
					 		"</div>"+
					 	"</div>");
					};
				}
			})
		}



		$('#js-addShareEmail').blur(function() {
			$('#js-shareMessage').addClass('displayNone');
		})

		$("#js-addShareEmail").on('input', function() {
			$('#js-shareMessage').removeClass('messageFail');
        });

		//поведение плюсика в шаринге
		$('#js-addShareEmail').keyup(function(e){

			if ($(this).val() == "") {
			
				$('#js-addShare').addClass('displayNone');
			
			} else {
			
				$('#js-addShare').addClass('displayBlock');

				var code = e.which; // recommended to use e.which, it's normalized across browsers
            
                if(code == 13) { // 13 - enterKey
            
                	$('#js-addShare').click();
            
                }
			};
		})


		//удаление синхронизации
        $('.js-modalWindow').on('click', '.js-deleteShare', function() {

            var sendInfo = {
              userId: $(this).closest('.js-shareUser').attr('id'),
              noteId: $(this).closest('.js-modalWindow').attr('id')
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


		$(document).keydown(function (key) {

            if ((key.which == 27) && ($('.js-modalWindow').css('display') == 'block')) { $('#js-modalClose').click(); }
    
        })

	});
})(jQuery);
