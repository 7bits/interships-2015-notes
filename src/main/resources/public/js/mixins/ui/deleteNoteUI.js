var deleteNoteMixin = function() {
  this.defaultAttrs({
    deleteBtnSelector: '.js-delBtn'
  })
  
  this.deleteNote = function(e, id) {
    var $note = $('.js-note[id=' + id + ']');
    $note.empty();

    $note.animate({
        height: '2px',
        border: '0px',
        marginTop: '123px'
      }, 150, 'swing');
          
      $note.animate({
          width: '0px',
        }, 150, 'swing', function() {

      $note.remove();

      var $actual = $('#js-actualSection');
      var $actualOwner = $actual.find('.js-sectionOwner');
      var $actualPic = $actual.find('.js-sectionPic');

      var $allSections = $('.js-noteSection');

      $allSections.each(function() {
        var $thisNoteSection = $(this);

        if (!$thisNoteSection.find('.js-note').length) {
          var $nextSection;
          var $nextOwner;
          var $nextPic;

          if ($thisNoteSection.index() === 0) {
                    
            $nextSection = $('.js-section').eq(0);
            $nextOwner = $nextSection.find('.js-sectionOwner');
            $nextPic = $nextSection.find('.js-sectionPic');
            if ($nextSection[0] != null) {
              $('.js-noteSection')
                .eq(0)
                .remove();  
              $actual
                .attr('value', $nextOwner
                .html());
              $actualOwner
                .html($nextOwner
                .html());
              $actualPic
                .attr('value', $nextPic
                .attr('src'));
              $actualPic
                .attr('src', $nextPic
                .attr('src'));
              $nextSection.remove();
              $nextSection = $('js-section').eq(0);
              if ($nextSection[0] != null) {
                $nextSection
                  .addClass('js-nextSection'); 
              }
            } else {
              $('.js-noteSection')
                .eq(0)
                .remove();
              $actual.removeAttr('value');
              $actualOwner.text('');
              $actualPic.attr('src', '/img/gulp/shareNotRegUser.png');
              $actualPic.attr('value', '/img/gulp/shareNotRegUser.png');
            }
          } else {
            var $deletedSection = $('.js-allSections')
              .eq($thisNoteSection
              .index() - 1);
            if ($actualOwner.text() === $deletedSection
              .find('.js-sectionOwner').text()) {
              $nextSection = $('.js-nextSection');
              $nextOwner = $nextSection.find('.js-sectionOwner');
              $nextPic = $nextSection.find('.js-sectionPic');
              $actualOwner.html($nextOwner.html());
              $actualPic.attr('src', $nextPic.attr('src'));
              $actualPic.attr('value', $nextPic.attr('src'));
              $deletedSection.remove();
              $thisNoteSection.remove();
              $('.js-prevSection').removeClass('js-prevSection');
              $nextSection
                .removeClass('js-nextSection')
                .addClass('js-prevSection');
              $('js-allSections')
                .eq($nextSection.index() + 1)
                .addClass('js-nextSection');
            } else {
              var next = $('js-allSections').eq($deletedSection.index() + 2);
              if (next[0] != null) { next.addClass('js-nextSection'); }
              $deletedSection.remove();
              $thisNoteSection.remove(); 
            }
          }
        }
      });
    });


    if ($(window).width() > 780) {
      $('#js-status').text('Все заметки сохранены');
    } else {
      $('#js-minStatus')
        .text('')
        .addClass('minstatusImg');
    }
  };

  this.after('initialize', function() {
    this.on(this.select('deleteBtnSelector'), 'deleteNote', this.deleteNote);
  });
};
