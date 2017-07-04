(function($){
	ds = (typeof ds !== "undefined")?ds:{};
	
	ds.post =  function (url,params,succFunc,errFunc) {
		var deferred = $.Deferred();
		$.ajax({
	        "type" : "post",
	        "url" : url,
	        "dataType" : "json",
	        "data" : params,
	        "success" : function (data) {
	            if(succFunc)succFunc(data);
	            deferred.resolve();
	        },
	        "error" : function (data) {
	        	if(errFunc)errFunc(data);
	        	deferred.reject();
	        }
		});
		return deferred.promise();
	};
})(jQuery);