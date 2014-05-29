define([],
	function(){
		var BookingCollection = Backbone.Collection.extend({

   		url: "/api/booking"

	});

	return BookingCollection;
});