var checkMailMixin = function() {
  this.isEmail = function(string) {
    var emailRegex =
      /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    return emailRegex.test(string);
  };

  this.after('initialize', function() {
    this.on('checkmail', this.isEmail);
  });
};
