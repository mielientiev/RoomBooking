define(['models/RoomCollection'],
    function(RoomCollection){
    var Room = Backbone.Model.extend({
        urlRoot: "/api/room-service/room"
    });
return Room;
});