var accountComponent = flight.component(function() {

  this.defaultAttrs({
    themeSelector: '.js-theme'
  });

  this.lightingFields = function() {
    var style = $('#hidden').attr('value');
    $('#'+style).addClass('chosen');

    if ($('#js-name')[0] != null) {
      $('#js-username').addClass('badFieldBorder');
      $('#js-nameDiv').attr('style', 'margin-left: 115px;').animate({
        marginLeft: '327px'
      }, 200, 'swing', function() {
        $('#js-nameDiv').removeAttr('style');
      });
    }

    if ($('#js-oldPass')[0] != null) {
      $('#js-currentPass').addClass('badFieldBorder');
      $('#js-errorDiv').attr('style', 'margin-left: 248px;').animate({
        marginLeft: '327px'
      }, 200, 'swing', function() {
        $('#js-errorDiv').removeAttr('style');
      });
    }

    if ($('#js-newPassErr')[0] != null) {
      $('#js-newPass').addClass('badFieldBorder');
      $('#js-errorDiv').attr('style', 'margin-left: 248px;').animate({
        marginLeft: '327px'
      }, 200, 'swing', function() {
        $('#js-errorDiv').removeAttr('style');
      });
    }
  };

  this.onClickTheme = function() {
    var self = $(this);
    $('.chosen').removeClass('chosen');
    self.addClass('chosen');

    $('#hidden').attr('value', self.attr('id'));
  };

  this.after('initialize', function() {
    this.on(document, 'ready', this.lightingFields);
    this.select('themeSelector').on('click', this.onClickTheme);
  })
});
