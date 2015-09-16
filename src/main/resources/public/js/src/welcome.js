//функция анимации ошибок
function errorsAnimate(element) {
  element.attr('style', 'margin-left: 0px;');
  var width = document.documentElement.clientWidth;
  var margin;

  if (width > 1200) {
    margin = '282px';
  } else if (width > 600 && width < 1200) {
    margin = '202px';
  } else {
    margin = '200px';
  }

  element.animate({
      marginLeft: margin
    }, 200, 'swing', function() {
      element.removeAttr('style');
    });
}


(function($) {
	$(document).ready(function() {
		
	  //подсветка неверных полей
		$(function() {
			if ($('#js-loginError')[0] != null) {
		
    		$('#js-logText').addClass('badFieldBorder');
				errorsAnimate($('.js-loginError'));
		
    	} else {
		
    		$('#js-logText').removeClass('badFieldBorder');
		
    	}

			if ($('#js-regEmailMess')[0] != null) {
		
    		$('#js-regEmail').addClass('badFieldBorder');
				errorsAnimate($('.js-emailError'));
		
    	} else {
		
    		$('#js-regEmail').removeClass('badFieldBorder');
		
    	}

			if ($('#js-regUsernameMess')[0] != null) {
		
    		$('#js-regUsername').addClass('badFieldBorder');
				errorsAnimate($('.js-nameError'));
		
    	} else {
		
    		$('#js-regUsername').removeClass('badFieldBorder');
		
    	}

			if ($('#js-regPassMess')[0] != null) {
		
    		$('#js-regPass').addClass('badFieldBorder');
				errorsAnimate($('.js-passError'));
		
    	} else {
		
    		$('#js-regPass').removeClass('badFieldBorder');
		
    	}
		});
	});
})(jQuery);
