(function($) {
  'use strict';
  
  $(document).ready(function() { 
    //функция обработки нажатия "+" и добавления заметки на рабочее поле
    $('#js-addNote').click(function() {

      var $note = '<div id="js-new" class="js-note noteSection__note" ' + 
                  'style="width: 0px; ' +
                  'margin-top: 123px; ' + 
                  'margin-bottom: 130px;' + 
                  'height: 2px;">' +
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
        addingElements($note);

      } else if (($('#js-actualSection').attr('value') !== myNotesSpan) &&
        !$('#js-noteDiv').children().length) {

        addingElements($note);
        
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
          $note.removeAttr('style');
        }
      );

      var newNoteId = -1;

      $(function() {
              var data = {
                id: newNoteId,
                text: ''
              };

              App
                .Note
                  .save(data, function(id) {
                    $('#js-new').attr('id', id);
                  });
      });
    });


    function addingElements (note) {

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
    }
  });
})(jQuery);
