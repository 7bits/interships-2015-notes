var addNoteComponent = flight.component(
  ajaxDataMixin,
  function() {

  this.defaultAttrs({
    telenoteUrl: '/telenote'
  });

  this.onAddNote = function() {
    var $note = '<div id="js-new" class="js-note noteSection__note freshNote">' +
      '<div class="js-content noteSection__content clickable"></div>' +
      '<div class="js-control noteSection__control">' +
      '<button class="noteSection__delBtn js-delBtn" ' +
      'style="visibility: hidden;"></button>' +
      '<button class="noteSection__shaBtn js-shaBtn" ' +
      'style="visibility: hidden;""></button>' +
      '</div>' +
      '</div>';

    var myNotesSpan = 'Мои заметки<span class="js-span"></span>';

    if ($('#js-actualSection').attr('value') === myNotesSpan) {
      $('.js-noteSection').eq(0).prepend($note);
      $('body').animate({scrollTop: '0'}, 200);
    } else if (($('#js-actualSection').attr('value') !== myNotesSpan) &&
      ($('#js-noteDiv').children().length !== 0)) {

      var section = '<div class="js-section ' +
        'js-allSections ' +
        'noteSpace__sectionInfo ' +
        'js-nextSection">' +
        '<img class="js-sectionPic noteSpace__picture" ' +
        'src=' + $('.js-sectionPic').eq(0).attr('value') + '>' +
        '<div class="js-sectionOwner noteSpace__owner">' +
        $('#js-actualSection').eq(0).attr('value') + '</div>' +
        '</div>';

      $('#js-noteDiv').prepend(section);
      this.addingElements($note);

    } else if (($('#js-actualSection').attr('value') !== myNotesSpan) &&
      !$('#js-noteDiv').children().length) {

      this.addingElements($note);

    }

    $note = $('#js-new');

    $note.animate({width: '350'}, 150, 'swing');

    $note.animate({
        height: '200px',
        marginTop: '0px',
        marginBottom: '32px'},
      150, 'swing', function() {
        $note.children().find('.js-delBtn').css('visibility', 'inherit');
        $note.children().find('.js-shaBtn').css('visibility', 'inherit');
        $note.removeClass('freshNote');
      }
    );

    var newNoteId = -1;
    var data = {
      id: newNoteId,
      text: ''
    };

    this.onSendData('0', {
      data: data,
      type: 'POST',
      dataType: 'json',
      url: this.attr.telenoteUrl,
      collbackSuccess: function(id) {
        $('#js-new').attr('id', id);
        noteComponent.attachTo($('#' + id));
      },
      collbackFail: function() {

      }
    })
  };

  this.addingElements = function(note) {

    var noteSection = '<div class="js-noteSection noteSpace__noteSection" ' +
      'id="ns_"></div>';
    $('#js-actualSection')
      .attr('value', 'Мои заметки<span class="js-span"></span>');

    $('.js-sectionPic')
      .eq(0)
      .attr('value', $('.js-userImg')
        .attr('src'));
    $('#js-noteDiv').prepend(noteSection);

    $('.js-noteSection').eq(0).prepend(note);
    $('body').animate({scrollTop: '0'}, 200);

    $('.js-sectionPic')
      .eq(0)
      .attr('src', $('.js-sectionPic')
        .eq(0)
        .attr('value'));
    $('.js-sectionOwner')
      .eq(0)
      .html($('#js-actualSection')
        .eq(0)
        .attr('value'));

    $('.js-noteSection').sortable();
    $('.js-noteSection').disableSelection();
  };

  this.after('initialize', function() {
    this.on('click', this.onAddNote);
  });
});
