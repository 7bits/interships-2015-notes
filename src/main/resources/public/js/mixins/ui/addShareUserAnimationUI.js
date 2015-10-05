var addShareAnimationMixin = function() {

  this.addShareAnimation = function(e, data) {
    var shareUser = '<div id="js-newShare" ' +
      'class="modal__user js-shareUser" style="height: 0px;""></div>';
    var innerShareUser = '<div class="user__img">' + 
      '<img src="' + data.user.avatar + '"">' + 
      '</div>'+
      '<div class="user__info">' +
        '<div class="js-shareUserName user__name">' + 
          data.user.name + '</div>'+
        '<div class="js-shareUserEmail user__email">' +
          $('#js-addShareEmail').val() + '</div>' +
      '</div>' +
      '<div class="user__action">' +
        '<button class="js-deleteShare user__button user__button_del"></button>' +
      '</div>';

    $('#js-syncUsers').append(shareUser);

    shareUser = $('#js-newShare').animate({
        height: '62px'
      },
      300, 'swing', function() {
        shareUser.append(innerShareUser);
        shareUser.removeAttr('id').removeAttr('style');
        shareUser.attr('id', data.user.id);
    });
  };

  this.after('initialize', function() {
    this.on('addShareAnimation', this.addShareAnimation);
  });
};
