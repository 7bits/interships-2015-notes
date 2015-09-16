//функция запроса синхронизированных пользователей
function checkSharedNote(noteId) {

  var sendInfo = {
    id: noteId
  };

  $.ajax({
    type: 'POST',
    url: '/telenote/checknote',
    dataType: 'json',
    data: sendInfo,
    headers: {'X-CSRF-TOKEN': $('meta[name = _csrf]').attr('content') },
    success: function(data) {

      $('.syncUsers').prepend('<div id="js-owner" class="shareUser">' +
          '<div class="shareUserImg">' +
            '<img src="' + data[0].avatar + '">' +
          '</div>' +
          '<div class="shareUserInfo">' + 
            '<div class="js-shareUserName shareUserName">' +
              data[0].name + '<span> (автор)</span></div>' +
            '<div class="js-shareUserEmail shareUserEmail">' +
              data[0].username + '</div>' +
          '</div>' +
        '</div>');

      for (var i = data.length-1; i > 0; i--) {
        $('#js-owner').after('<div id="' +
          data[i].id + '" class="js-shareUser shareUser">' +
            '<div class="shareUserImg">' +
              '<img src="' + data[i].avatar + '">' +
            '</div>' +
            '<div class="shareUserInfo">' + 
              '<div class="js-shareUserName shareUserName">' + 
                data[i].name + '</div>' +
              '<div class="js-shareUserEmail shareUserEmail">' +
                data[i].username + '</div>' +
            '</div>' +
            '<div class="shareActionDiv">' +
              '<button class="js-deleteShare deleteShare"></button>' +
            '</div>' +
           '</div>');
      }
    }
  });
}


function modalStart($note) {
	$('#js-textarea').blur();

	var id = $note.closest('.js-note').attr('id');
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
}


function modalClose(data) {

	var $curnote = data.$clickedNoteSection.find('.js-note[id="' +
    $('.js-modalWindow').attr('id') + '"]');

  // если заметка кому-то расшарена, то удаляем ее, переносим другим
	if(data.addedShareEmails != null) { 

		if (data.addedShareEmails.length !== 0) {

			var $noteDiv = $('#js-noteDiv');

			data.addedShareEmails.forEach(function(item, i, arr) {
				var otherShareUserEmail = item;
				var $shareUserNames = $('.js-shareUserEmail');
				var userName;
				var userPicLink = 'http://www.gravatar.com/avatar/' + 
          $.md5(otherShareUserEmail) + 
				'?d=http%3A%2F%2Ftele-notes.' +
        '7bits.it%2Fresources%2Fpublic%2Fimg%2FshareNotRegUser.png';

				$shareUserNames.each(function() {
					if($(this).text() === item) { userName = $(this)
              .siblings('.js-shareUserName')
                .text(); }
				});

				var $otherNoteSection = $('js-noteSection[id="ns_' +
          otherShareUserEmail + '"]');
				var $copyCurnote = $curnote.clone();

				if($otherNoteSection.length > 0) {

					$otherNoteSection.prepend($copyCurnote);
				
				} else {
					
					$otherNoteSection = '<div class="js-noteSection ' +
              'noteSection ' +
              'ui-sortable" ' +
            'id="ns_' + otherShareUserEmail + '"></div>';

					var section = '<div class="js-section ' +
              'js-allSections ' +
              'textNoteSection">' +
	            '<img class="js-sectionPic sectionPic" src=' + userPicLink + '>' +
	            '<div class="js-sectionOwner sectionOwner">' +
              'Общие с ' + userName + 
              '<span class="js-span"> (' + otherShareUserEmail +
              ')</span></div>' +
	          '</div>';

					$noteDiv.append(section);
					$noteDiv.append($otherNoteSection);
	                $('.js-noteSection[id="ns_' + 
                    otherShareUserEmail + '"]')
                    .append($copyCurnote);

	        if ($('.js-allSection').length < 2) { $('.js-section')
            .eq($('.js-section')
              .length - 1)
                .addClass('js-nextSection'); }

				}

				//otherNoteSections.splice(0, 0, );
			});

			if(data.$clickedNoteSection.children().length === 1 &&
       data.$clickedNoteSection.attr('id') === 'ns_') {

				var $nextSection = $('.js-section').eq(0);
				var $nextOwner = $nextSection.find('.js-sectionOwner');
				var $nextPic = $nextSection.find('.js-sectionPic');

				data.$clickedNoteSection.remove();
				var $actual = $('#js-actualSection');
				var $actualOwner = $actual.find('.js-sectionOwner');
				var $actualPic = $actual.find('.js-sectionPic');

				$actual.attr('value', $nextOwner.html());
				$actualOwner.html($nextOwner.html());
				$actualPic.attr('value', $nextPic.attr('src'));
				$actualPic.attr('src', $nextPic.attr('src'));
				$nextSection.remove();

				$nextSection = $('js-section').eq(0);

				if ($nextSection[0] != null) { $nextSection
          .addClass('js-nextSection'); }
			
			} else if(data.$clickedNoteSection.attr('id') === 'ns_') {
				
				$curnote.remove();
			
			}
		}
	}

	$('.js-modalWindow').animate({
		opacity: 0,
		top: '45%'},
		200, function() {
			$(this).addClass('displayNone');
			$(this).removeClass('displayBlock');
			$('#js-overlay').fadeOut(200);
			$('#js-syncUsers').empty();
			$('#js-shareMessage').addClass('displayNone');
			$('#js-shareMessage').removeClass('displayBlock');
			location.reload();
	});

	data.addedShareEmails = []; // обнуляем добавленные имейлы
	data.$clickedNoteSection = null;

	return data;
}


function addShareBtn($emailInput, key) {

	if ($emailInput.val() === '') {
			
		$('#js-addShare').addClass('displayNone');
			
	} else {
			
		$('#js-addShare').addClass('displayBlock');

    // recommended to use e.which, it's normalized across browsers
		var code = key.which; 
            
    	// 13 - enterKey
        if(code === 13) { $('#js-addShare').click(); }
	
	}
}


function deleteShare($deleteShare) {
	
	var sendInfo = {
      	userId: $deleteShare.closest('.js-shareUser').attr('id'),
      	noteId: $deleteShare.closest('.js-modalWindow').attr('id')
    };

    $.ajax({
     	type: 'POST',
      url: '/telenote/deletesync',
     	dataType: 'json',
      data: sendInfo,
      headers: {'X-CSRF-TOKEN': $('meta[name = _csrf]').attr('content') },
      success: function(data) {
            
        $('#'+sendInfo.userId).empty().animate({
            
        	height: '0px'
            
          }, 300, 'swing', function() {
            	
          	$('#'+sendInfo.userId).remove();
            	
          	var result = [];

           	$('.shareUserEmail').each(function() {

            		result.push($(this).text());

            });

            	return result;
            
          });
        }
    });
}
