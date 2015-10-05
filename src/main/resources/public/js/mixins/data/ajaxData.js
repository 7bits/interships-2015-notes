var ajaxDataMixin = function() {

  this.onSendData = function(e, dataArray) {
    $.ajax({
      url: dataArray.url,
      data: dataArray.data,
      type: dataArray.type,
      dataType: 'json',
      headers: {'X-CSRF-TOKEN': $('meta[name = _csrf]').attr('content') },
      success: function(data) {
        dataArray.collbackSuccess(data);
      },
      error: function(data) {
        dataArray.collbackFail(data);
      }
    });
  };

  this.after('initialize', function() {
    this.on('sendData', this.onSendData);
  });
};
