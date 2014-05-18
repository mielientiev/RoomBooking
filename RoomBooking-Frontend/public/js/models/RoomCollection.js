define(['models/Room'],
	function(Room){
		var RoomCollection = Backbone.Collection.extend({

   		url: "http://roombooking-ejournal.rhcloud.com/room-service/rooms",
   		search : function(letters){
			if(letters == "") return this;
	 
			var pattern = new RegExp(letters,"gi");
			return _(this.filter(function(data) {
			  	return pattern.test(data.get("roomName"));
			}));
		}

		});

	return RoomCollection;
});