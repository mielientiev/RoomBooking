define(['router'], function(router) {
	var initialize = function() {
		Backbone.history.start();
	};


	return {
		initialize: initialize
	};
});
