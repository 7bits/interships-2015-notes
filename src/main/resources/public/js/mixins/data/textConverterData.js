var textConverterMixin = function() {
  this.nl2br = function nl2br (e, str) {
    'use strict';
  	
    var breakTag = '<br>';    

    //.replace(/&/g, "&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;");
  	return (str + '').replace(/(\r\n|\n\r|\r|\n)/g, breakTag);

  };
     

  this.br2nl = function br2nl (e, str) {
  	'use strict';

    var nl = '\n';
  	
    //.replace(/&lt;/g,"<").replace(/&gt;/g,">").replace(/&amp;/g, "&");
    return (str + '').replace(/<br>/g, nl);

  };


  this.htmlspecialchars = function htmlspecialchars(e, str) {
    'use strict';

    if (typeof(str) === 'string') {
  		 
      var quot = '&quot';

      // must do &amp; first        
  	  str = str.replace(/&/g, '&amp;'); 
  	   
  	  str = str.replace(/'"'/g, quot);
  	  str = str.replace(/"'"/g, '&#039;');
  	  str = str.replace(/</g, '&lt;');
  	  str = str.replace(/>/g, '&gt;');
            	
    }
           
      return str;
  };


  this.rhtmlspecialchars = function rhtmlspecialchars(e, str) {
    'use strict';
            	
    if (typeof(str) === 'string') {
             	
      str = str.replace(/&gt;/ig, '>');
      str = str.replace(/&lt;/ig, '<');
      str = str.replace(/&#039;/g, '\'');
      str = str.replace(/&quot;/ig, '"');

      // must do &amp; last 
      str = str.replace(/&amp;/ig, '&'); 
             	
    }
            
    return str;
  };

  this.after('initialize', function() {
    this.on('nl2br', this.nl2br);
    this.on('br2nl', this.br2nl);
    this.on('htmlspecialchars', this.htmlspecialchars);
    this.on('rhtmlspecialchars', this.rhtmlspecialchars);
  });
};
