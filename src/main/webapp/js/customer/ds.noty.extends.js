(function($){
	ds = (typeof ds !== "undefined")?ds:{};
	ds.log =  function (msg,title,img,errFunc) {
		$.gritter.add({
            title: title||"",
            text: (msg||"System Error"),
            sticky:false,
            img:img||"",
            class_name: 'gritter-light'
        });
	};
	ds.clearLog =function() {
		$.gritter.removeAll();
	};
})(jQuery);