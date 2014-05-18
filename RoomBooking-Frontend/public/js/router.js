define(
    ['views/RoomListView', 'views/RoomListItemView', 'views/roomdetails', 'models/Room', 'models/RoomCollection'],
    function(RoomListView, RoomListItemView, RoomView, Room, RoomCollection)
    {

  var Router = Backbone.Router.extend({

        routes: {
            ""                  : "list",
            "rooms/page/:page"  : "list",
            "rooms/:id"         : "roomDetails"
        },

        initialize: function () {        
        },

        changeView: function(view) {
            if ( null != this.currentView ) {
                this.currentView.undelegateEvents();
            }
            this.currentView = view;
            this.currentView.render();
        },

        list: function(page) {

            var p = page ? parseInt(page, 10) : 1;
            var r = new Room();
            var roomList = new RoomCollection();
            
            roomList.fetch({
                beforeSend: function(xhr){xhr.setRequestHeader('Authorization', 'ZXZnZXNoYToxMjM0')},
                success: function() {
                    $("#content").html(new RoomListView({model: roomList, page: p}).el);
                }
            });
        },

        roomDetails: function (id) {
            var room = new Room({id: id});
            var that = this;
            room.fetch({
                beforeSend: function(xhr){xhr.setRequestHeader('Authorization', 'bWVsaToxMjM0NQ')},
                success: function() {
                    that.changeView(new RoomView({model: room}));
                }
            });
        }

    });

  return new Router();
});