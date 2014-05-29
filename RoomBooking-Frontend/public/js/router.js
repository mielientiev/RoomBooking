define(
    [
        'views/LoginView',
        'views/RoomListView',
        'views/roomdetails',
        'views/BookingListView',
        'models/Room',
        'models/RoomCollection',
        'models/BookingCollection'
    ],
    function(
        LoginView,
        RoomListView,
        RoomView,
        BookingListView,
        Room,
        RoomCollection,
        BookingCollection
        )
    {

        var Router = Backbone.Router.extend({

            routes: {
                "login"     : "login",
                "logout"    : "logout",
                ""          : "redirectRooms",
                "rooms"     : "rooms",
                "rooms/:id" : "roomDetails",
                "bookings"  : "bookings"
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

            redirectRooms: function() {
                document.location = "#rooms";
            },

            login: function() {
                var that = this;
                var header = $.cookie('user');
                $.ajax({
                    url: "/api/user-service/user",
                    beforeSend: function(xhr){xhr.setRequestHeader('Authorization', header)},
                    success: function() {
                        window.history.back();
                    },
                    error: function() {
                        that.changeView(new LoginView());
                    }
                });
            },

            logout: function() {
                $.removeCookie('user');
                document.location = "?#login";
            },

            rooms: function() {
                var roomList = new RoomCollection();
                var header = $.cookie('user');
                roomList.fetch({
                    beforeSend: function(xhr){xhr.setRequestHeader('Authorization', header)},
                    success: function() {
                        $("#content").html(new RoomListView({model: roomList, page: 1}).el);
                    },
                    error: function(xhr, error) {
                        if(error.status == 401) {
                            document.location = "?#login";
                        }
                        else {
                            $("#content").html(new RoomListView({model: roomList, page: 1}).el);
                        }
                    }
                });
            },

            bookings: function() {
                var bookingList = new BookingCollection();
                var header = $.cookie('user');
                bookingList.fetch({
                    beforeSend: function(xhr){xhr.setRequestHeader('Authorization', header)},
                    success: function() {
                        $("#content").html(new BookingListView({model: bookingList, page: 1}).el);
                    },
                    error: function(xhr, error) {
                        if(error.status == 401) {
                            document.location = "?#login";
                        }
                        else {
                            $("#content").html(new BookingListView({model: bookingList, page: 1}).el);
                        }
                    }
                });
            },

            roomDetails: function (id) {
                var room = new Room({id: id});
                var that = this;
                var header = $.cookie('user');
                room.fetch({
                    beforeSend: function(xhr){xhr.setRequestHeader('Authorization', header)},
                    success: function() {
                        that.changeView(new RoomView({model: room}));
                    }
                });
            }

        });

        return new Router();
    });