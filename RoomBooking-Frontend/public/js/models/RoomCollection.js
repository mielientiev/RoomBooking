define(['models/Room'],
	function(Room){
		var RoomCollection = Backbone.Collection.extend({

   		url: "/api/room-service/rooms"

	});

	return RoomCollection;
});