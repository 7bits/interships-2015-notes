function nl2br (str) {
	
    var breakTag = "<br>";    
	return (str + '').replace(/(\r\n|\n\r|\r|\n)/g, breakTag)	;//.replace(/&/g, "&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;");

} 
   

function br2nl (str) {
	
    var nl = "\n";
	return (str + '').replace(/<br>/g, nl);//.replace(/&lt;/g,"<").replace(/&gt;/g,">").replace(/&amp;/g, "&");
	//			return (str + '').replace(/(<br>)|(<br \/>)/g, nl).replace(/&lt;/g,"<").replace(/&gt;/g,">");//.replace(/&amp;/g, "&");

}


function htmlspecialchars(str) {
         	
    if (typeof(str) == "string") {
		        
	   str = str.replace(/&/g, "&amp;"); /* must do &amp; first */
	   var quot = "&quot";
	   str = str.replace(/'"'/g, quot);
	   str = str.replace(/"'"/g, "&#039;");
	   str = str.replace(/</g, "&lt;");
	   str = str.replace(/>/g, "&gt;");
          	
    }
         
    return str;
}


function rhtmlspecialchars(str) {
          	
    if (typeof(str) == "string") {
           	
        str = str.replace(/&gt;/ig, ">");
        str = str.replace(/&lt;/ig, "<");
        str = str.replace(/&#039;/g, "'");
        str = str.replace(/&quot;/ig, '"');
        str = str.replace(/&amp;/ig, '&'); /* must do &amp; last */
           	
    }
          
    return str;
}