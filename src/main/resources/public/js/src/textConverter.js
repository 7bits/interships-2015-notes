function nl2br (str) {
	
  var breakTag = '<br>';    

  //.replace(/&/g, "&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;");
	return (str + '').replace(/(\r\n|\n\r|\r|\n)/g, breakTag);

} 
   

function br2nl (str) {
	
  var nl = '\n';
	
  //.replace(/&lt;/g,"<").replace(/&gt;/g,">").replace(/&amp;/g, "&");
  return (str + '').replace(/<br>/g, nl);

}


function htmlspecialchars(str) {
         	
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
}


function rhtmlspecialchars(str) {
          	
  if (typeof(str) === 'string') {
           	
    str = str.replace(/&gt;/ig, '>');
    str = str.replace(/&lt;/ig, '<');
    str = str.replace(/&#039;/g, '\'');
    str = str.replace(/&quot;/ig, '"');

    // must do &amp; last 
    str = str.replace(/&amp;/ig, '&'); 
           	
  }
          
  return str;
}
