function addShare(addedShareEmails) {

    var id = $('.js-modalWindow').attr('id');
    var email = $('#js-addShareEmail').val().toLowerCase();
    var $infoLabel = $('#js-shareMessage');
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
			$infoLabel.text(data.responseJSON.message);
			curClass = 'messageFail';
            $infoLabel.addClass(curClass);
                
        });

	} else {
    			
    	$('#js-addShareEmail').focus();
    	$infoLabel.text("Неверный адрес!");
    	curClass = 'messageFail';
        $infoLabel.addClass(curClass);
    		
    }

    return addedShareEmails;
}


function addingShareUser(data) {
            
    var shareUser = "<div id='js-newShare' class='shareUser js-shareUser' style='height: 0px;'></div>";
    var innerShareUser = "<div class='shareUserImg unsetImg'></div>"+
        "<div class='shareUserInfo'>"+
            "<div class='js-shareUserName shareUserName'>"+data.username+"</div>"+
            "<div class='js-shareUserEmail shareUserEmail'>"+$('.addShareEmail').val()+"</div>"+
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


function IsEmail(email) {

    var emailRegex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    return emailRegex.test(email);

}