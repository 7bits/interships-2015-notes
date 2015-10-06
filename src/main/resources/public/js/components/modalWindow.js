var modalWindowComponent = flight.component(
  ajaxDataMixin,
  checkMailMixin,
  addShareAnimationMixin,
  function() {

  this.defaultAttrs({
    shareUrl: '/telenote/share',
    checkUrl: '/telenote/checknote',
    deleteUrl: 'telenote/deletesync',
    addShareEmailSelector: '#js-addShareEmail',
    addShareButtonSelector: '#js-addShare',
    deleteSyncButtonSelector: '.js-deleteShare',
    modalCloseSelector: '#js-modalClose',
    documentSelector: 'document'
  });


  this.checkSharring = function(noteId) {
    this.trigger('sendData', {
      url: this.attr.checkUrl,
      data: {id: noteId},
      type: 'POST',
      dataType: 'json',
      collbackSuccess: function(data) {
        $('#js-syncUsers').prepend('<div id="js-owner" class="modal__user">' +
            '<div class="user__img">' +
              '<img src="' + data[0].avatar + '">' +
            '</div>' +
            '<div class="user__info">' + 
              '<div class="js-shareUserName user__name">' +
                data[0].name + '<span> (автор)</span></div>' +
              '<div class="js-shareUserEmail user__email">' +
                data[0].username + '</div>' +
            '</div>' +
          '</div>');

        for (var i = data.length-1; i > 0; i--) {
          $('#js-owner').after('<div id="' +
            data[i].id + '" class="js-shareUser modal__user">' +
              '<div class="user__img">' +
                '<img src="' + data[i].avatar + '">' +
              '</div>' +
              '<div class="user__info">' + 
                '<div class="js-shareUserName user__name">' + 
                  data[i].name + '</div>' +
                '<div class="js-shareUserEmail user__email">' +
                  data[i].username + '</div>' +
              '</div>' +
              '<div class="user__action">' +
                '<button class="js-deleteShare ' +
                'user__button user__button_del"></button>' +
              '</div>' +
            '</div>');
        }
      },
      collbackFail: function() {

      }
    });
  };

  this.onModalStart = function(e, noteId) {
    $('#js-textarea').blur();
    $('.js-modalWindow').attr('id', noteId);

    this.checkSharring(noteId);

    $('#js-overlay').fadeIn(200, 
      function() {
        $('.js-modalWindow').addClass('displayBlock').animate({
          opacity: 1,
          top: '45%'},
          200);

      $('#js-addShareEmail').focus();
    });
  };

  this.addingShare = function() {
    var id = $('.js-modalWindow').attr('id');
    var email = $('#js-addShareEmail').val().toLowerCase();
    var $infoLabel = $('#js-shareMessage');
    var curClass;

    var test = this.isEmail(email);

    if (test) {
      var sendData = {
        id: id,
        email: email
      };

      this.onSendData('0', {
        url: this.attr.shareUrl,
        data: sendData,
        type: 'POST',
        dataType: 'json',
        collbackSuccess: function(data) {
          $('.js-modalWindow').trigger('addShareAnimation', data);
          $('#js-addShareEmail').val('');
        },
        collbackFail: function(data) {
          $('#js-addShareEmail').focus();
          $infoLabel.text(data.responseJSON.message);
          curClass = 'messageFail';
          $infoLabel.addClass(curClass);
        }
      });
    } else {
      $('#js-addShareEmail').focus();
      $infoLabel.text('Неверный адрес!');
      curClass = 'messageFail';
      $infoLabel.addClass(curClass);
    }
  };

  this.preAddingShare = function(e, data) {
    if ($(e.target).attr('id') !== 'js-addShare') {
      if (e.which === 13) {
        this.addingShare();
      } else if (e.which === 27) {
        this.onModalClose();
      }
    } else if ($(e.target).attr('id') === 'js-addShare') {
      this.addingShare();
    }
  };

  this.deleteSync = function(e, data) {
    var id = $(e.target).closest('.js-shareUser').attr('id');
    var sendData = {
      userId: id,
      noteId: this.$node.attr('id')
    };

    this.onSendData('0', {
      url: this.attr.deleteUrl,
      data: sendData,
      type: 'POST',
      dataType: 'json',
      collbackSuccess: function(data) {
        $('#'+sendData.userId).empty().animate({
            height: '0px'
          }, 300, 'swing', function() {
            $('#'+sendData.userId).remove();
          });
      },
      collbackFail: function(data) {

      }
    });
  };

  this.plusBtnBehavior = function(e, data) {
    var $input = $(e.target);

    if ($input.val() === '') {  
      $('#js-addShare').removeClass('displayBlock');
    } else {  
      $('#js-addShare').addClass('displayBlock');
    }
  };

  this.onModalClose = function() {
    location.reload();
  };

  this.after('initialize', function() {
    this.on('modalStart', this.onModalStart);
    this.on('click', {
      'addShareButtonSelector': this.preAddingShare,
      'deleteSyncButtonSelector': this.deleteSync,
      'modalCloseSelector': this.onModalClose
    });
    this.on('keydown', {
      'addShareEmailSelector': this.preAddingShare,
      'documentSelector': this.preAddingShare
    });
    this.on('keyup', {
      'addShareEmailSelector': this.plusBtnBehavior
    });
  });
});
