var welcomeComponent = flight.component(
  errorAnimationMixin,
  function() {

  this.lightingWelcomeFields = function() {
    if ($('#js-loginError')[0] != null) {
      $('#js-logText').addClass('badFieldBorder');
      this.onErrorAnimate('0', $('.js-loginError'));
    } else {
      $('#js-logText').removeClass('badFieldBorder');
    }

    if ($('#js-regEmailMess')[0] != null) {
      $('#js-regEmail').addClass('badFieldBorder');
      this.onErrorAnimate('0', $('.js-emailError'));
    } else {
      $('#js-regEmail').removeClass('badFieldBorder');
    }

    if ($('#js-regUsernameMess')[0] != null) {
      $('#js-regUsername').addClass('badFieldBorder');
      this.onErrorAnimate('0', $('.js-nameError'));
    } else {
      $('#js-regUsername').removeClass('badFieldBorder');
    }

    if ($('#js-regPassMess')[0] != null) {
      $('#js-regPass').addClass('badFieldBorder');
      this.onErrorAnimate('0', $('.js-passError'));
    } else {
      $('#js-regPass').removeClass('badFieldBorder');
    }
  };

  this.after('initialize', function() {
    this.on('ready', this.lightingWelcomeFields)
  });
});
