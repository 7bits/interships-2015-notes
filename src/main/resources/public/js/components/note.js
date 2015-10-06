var noteComponent = flight.component(
  ajaxDataMixin,
  textConverterMixin,
  deleteNoteMixin,
  function() {

  this.defaultAttrs({
    deleteUrl: '/telenote/',
    telenoteUrl: '/telenote',
    delBtnSelector: '.js-delBtn',
    shareBtnSelector: '.js-shaBtn',
    contentSelector: '.js-content',
    textareaSelector: '#js-textarea'
  });

  var timeoutId = {};
  var oldVal = '';

  this.onOpenSharring = function(e, data) {
    $('.js-modalWindow').trigger('modalStart', this.$node.attr('id'));
  };

  this.onDeleteNote = function(e, data) {
    var id = this.$node.attr('id');

    clearTimeout(timeoutId);
    
    this.onSendData('0', {
      data: id,
      url: this.attr.deleteUrl + id,
      type: 'DELETE',
      dataType: 'json',
      collbackSuccess: function(data) {
        $('.js-delBtn').trigger('deleteNote', data);
      },
      collbackFail: function(data) {

      }
    });
  };

  this.mouseHover = function() {
    var $note = this.$node;
    var $control = $note.find('.js-control');
    var $delBtn = $note.find('.js-delBtn');
    var $shareBtn = $note.find('.js-shaBtn');

    $delBtn.removeClass('btnHeightZero');
    $shareBtn.removeClass('btnHeightZero');
    $control.css('visibility', 'inherit');
    $control.stop();

    $control.animate({
      height: '40px',
      marginTop: '0px'
    }, 200, 'swing', function() {
      $delBtn.css('height', '40px');
      $shareBtn.css('height', '40px');
    });
  };

  this.mouseLeave = function() {
    var $note = this.$node;
    var $control = $note.find('.js-control');
    var $delBtn = $note.find('.js-delBtn');
    var $shareBtn = $note.find('.js-shaBtn');

    $delBtn.addClass('btnHeightZero');
    $shareBtn.addClass('btnHeightZero');

    $control.animate({
      height: '0px',
      marginTop: '40px'
    }, 200, 'swing', function() {
      $control.css('visibility', 'hidden');
    });
  };

  this.divToTextarea = function() {
  // //блокируем заметку
  //   var cmd = {
  //     id: $content
  //       .parent('.js-note')
  //         .attr('id'),
  //     command: 'block'
  //   };
    
  //   sendCommand(cmd);
    var $content = this.$node.find('.js-content');
    $content.removeClass('displayBlock').addClass('displayNone');
    var $note = $content.parent('.js-note');

    var textarea = '<textarea id="js-textarea" ' +
      'class="textarea" name="text" maxlength="20000">';

    $note.prepend(textarea);

    this.on(this.select('textareaSelector'), 'blur', this.textareaToDiv);
    this.on(this.select('textareaSelector'), 'input', this.onInputText);

    var text = $content.html();

    text = this.br2nl('0', text);
    text = this.rhtmlspecialchars('0', text);

    var $textarea = $('#js-textarea').text(text);
    $content.text('');

    $textarea.focus();
    $textarea.scrollTop(0);

  };

  this.textareaToDiv = function() {
    clearTimeout(timeoutId);

    // var cmd = {
    //   id: $textarea
    //     .parent('.js-note')
    //       .attr('id'),
    //   command : 'unblock'
    // };
             
    // sendCommand(cmd);

    var $textarea = $('#js-textarea');
    var id = $textarea.parent('.js-note').attr('id');
    var data = {
      id: id,
      text: $textarea.val()
    }; 

    data.text = this.htmlspecialchars('0', data.text);
    data.text = this.nl2br('0', data.text);

    var $content = $textarea.siblings('.js-content').html(data.text);

    $textarea.remove();
    $content.removeClass('displayNone').addClass('displayBlock');

    this.onSendData('0', {
      data: data,
      type: 'POST',
      url: this.attr.telenoteUrl,
      dataType: 'json',
      collbackSuccess: function() {
        if ($(window).width() > 780) {
          $('#js-status').text('Все заметки сохранены');
        } else {
          $('#js-minStatus').text('');
          $('#js-minStatus').addClass('minstatusImg');
        }
      },
      collbackFail: function() {

      }
    });
  };

  this.autosave = function(id) {
    var text = $('#js-textarea').val();

    var data = {
      id: id,
      text: text
    };

    $('.js-note[id=' + data.id + '] .js-content').text(text);

    data.text = this.htmlspecialchars(data.text);

    ////отправить другим пользователям
    //var cmd = {
    //  id: data.id,
    //  text: data.text,
    //  command: 'block,text'
    //};
    //
    //sendCommand(cmd);

    data.text = this.nl2br(data.text);

    this.onSendData('0', {
      data: data,
      dataType: 'json',
      type: 'POST',
      url: this.attr.telenoteUrl,
      collbackSuccess: function(data) {
        if ($(window).width() > 780) {
          $('#js-status')
            .text('Все заметки сохранены');
        } else {
          $('#js-minStatus')
            .addClass('minstatusImg')
            .text('');
        }
      },
      collbackFail: function(data) {

      }
    });
  };

  this.onInputText = function() {
    clearTimeout(timeoutId);

    var currentVal = $('#js-textarea').val();

    if(currentVal === oldVal) { return; }
    oldVal = currentVal;

    if ($(window).width() > 780) {
      $('#js-status').text('Сохранение...');
    } else {
      $('#js-minStatus')
        .removeClass('minstatusImg')
        .text('...');
    }

    var that = this;
    var id = this.$node.attr('id');
    timeoutId = setTimeout(function() {
      that.autosave(id)
    }, 750);
  };

  this.after('initialize', function() {
    this.on('click', {
      'shareBtnSelector': this.onOpenSharring,
      'delBtnSelector': this.onDeleteNote,
      'contentSelector': this.divToTextarea
    });
    this.on(this.$node, 'mouseenter', this.mouseHover);
    this.on(this.$node, 'mouseleave', this.mouseLeave);
  });
});
