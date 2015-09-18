function isEmail(email) {
  'use strict';

  var emailRegex =
  /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
  return emailRegex.test(email);

}


function addingShareUser(data) {
  'use strict';

  var shareUser = '<div id="js-newShare" ' +

  'class="shareUser js-shareUser" style="height: 0px;""></div>';
  var innerShareUser = '<div class="shareUserImg">' + 
          '<img src="' + data.user.avatar + '"">' + 
      '</div>'+
      '<div class="shareUserInfo">' +
          '<div class="js-shareUserName shareUserName">' + 
            data.user.name + '</div>'+
          '<div class="js-shareUserEmail shareUserEmail">' +
            $('.addShareEmail').val() + '</div>' +
      '</div>' +
      '<div class="shareActionDiv">' +
          '<button class="js-deleteShare deleteShare"></button>' +
      '</div>';

  $('#js-syncUsers').append(shareUser);

  shareUser = $('#js-newShare').animate({
      height: '62px'
  }, 300, 'swing', function() {
      shareUser.append(innerShareUser);
      shareUser.removeAttr('id').removeAttr('style');
      shareUser.attr('id', data.user.id);
  });
}


function addShare(addedShareEmails) {
  'use strict';

   var id = $('.js-modalWindow').attr('id');
   var email = $('#js-addShareEmail').val().toLowerCase();
   var $infoLabel = $('#js-shareMessage');
   var curClass;
   			
   if(isEmail(email)) {
    				
    var sendInfo = {
    	id: id,
    	email: email
    };

    $.ajax({
	    type: 'POST',
		url: '/telenote/share',
		dataType: 'json',
		headers: {'X-CSRF-TOKEN': $('meta[name = _csrf]')
      .attr('content') },
		data: sendInfo,
		success: function(data){

			addingShareUser(data);

			addedShareEmails.splice(0, 0, $('#js-addShareEmail').val());

			$('#js-addShareEmail').val('');
					
		}
	}).fail(function(data) {
					
		$('#js-addShareEmail').focus();
		$infoLabel.text(data.responseJSON.message);
		curClass = 'messageFail';
          $infoLabel.addClass(curClass);
                
      });

  } else {
    			
    $('#js-addShareEmail').focus();
    $infoLabel.text('Неверный адрес!');
    curClass = 'messageFail';
    $infoLabel.addClass(curClass);
    		
  }

  return addedShareEmails;
}