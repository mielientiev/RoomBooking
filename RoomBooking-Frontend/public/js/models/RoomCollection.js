define(['models/Room'],
	function(Room){
		var RoomCollection = Backbone.Collection.extend({

<<<<<<< HEAD
   		url: "http://roombooking-ejournal.rhcloud.com/room-service/rooms",
   		search : function(letters){
			if(letters == "") return this;
	 
			var pattern = new RegExp(letters,"gi");
			return _(this.filter(function(data) {
			  	return pattern.test(data.get("roomName"));
			}));
		}
=======

   		url: "http://roombooking-ejournal.rhcloud.com/room-service/rooms"
>>>>>>> e47248977b26b53e6106a130b9d1d9b4385c81e1

		});

	return RoomCollection;
});