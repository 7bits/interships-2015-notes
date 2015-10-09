var errorAnimationMixin = function() {

  this.onErrorAnimate = function(e, data) {
    data.attr('style', 'margin-left: 0px;');
    var width = document.documentElement.clientWidth;
    var margin;

    if (width > 1200) {
      margin = '282px';
    } else if (width > 600 && width < 1200) {
      margin = '202px';
    } else {
      margin = '200px';
    }

    data.animate({
      marginLeft: margin
    }, 200, 'swing', function() {
      data.removeAttr('style');
    });
  };

  this.after('initialize', function() {
    this.on('errorAnimate', this.onErrorAnimate);
  });
};
