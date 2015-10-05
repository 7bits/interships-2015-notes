var noteComponent = flight.component(ajaxDataMixin, function() {
  this.defaultAttrs({
    shareBtn: '.js-shaBtn'
  });

  this.onOpenSharring = function(e, data) {
    $('.js-modalWindow').trigger('modalStart', this.$node.attr('id'));
  };

  this.after('initialize', function() {
    this.on(this.select('shareBtn'), 'click', this.onOpenSharring);
  });
});
