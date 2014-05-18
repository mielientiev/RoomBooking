define(['models/Room'],
	function(Room){
		var RoomCollection = Backbone.Collection.extend({


   		url: "http://roombooking-ejournal.rhcloud.com/room-service/rooms"

		});

	return RoomCollection;
});