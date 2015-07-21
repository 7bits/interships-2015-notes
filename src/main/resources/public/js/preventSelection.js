(function() {
    $(".cell").onmousedown = function (event) {
    	event.stopPropagation();
    };
        $(".cell").onselectstart = function (event) {
            event.stopPropagation();
        };
})(jQuery);