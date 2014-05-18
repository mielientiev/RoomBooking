define(['text!templates/RoomListTemplate.html' ,'views/RoomListItemView', 'views/paginator'],
    function(RoomListTemplate, RoomListItemView, Paginator){
    var RoomListView = Backbone.View.extend({

        fullCollection : null,
        criteria : {roomType : "All types", all: false},
        filterCriteria : null,
        roomTypeObj : {"All types" : 0},

        initialize : function (attrs) {
            var that = this;
            this.options = attrs;
            that.fullCollection = $.extend({}, that.model);
            this.render();
        },

        filter : function(criteria) {
            var that = this;
            var col = this.model;
            var newCol = col.filter(function(room, criteria) {
                return room.filterByCriteria(room, criteria);
            });
        },

        filterByCriteria : function(room, criteria) {
            var b = true;
            b = b & (criteria.roomName == "" || room.get("roomName").indexOf(criteria.roomName) != -1);
            b = b & (criteria.roomType.id == 0 || criteria.roomType.id == undefined ||
                room.get("roomType").id == criteria.roomType.id);
            b = b & (room.get("places") >= criteria.places);
            b = b & (room.get("computers") >= criteria.computers);
            b = b & (!criteria.board || room.get("board") == criteria.board);
            b = b & (!criteria.projector || room.get("projector") == criteria.projector);
            b = b & (criteria.all || room.get("roomType").rights[0].canBookRoom);
            return b;
        },

        getAvailRooms : function() {
            var availiable = roomList.filter(function(room) {
                return room.get("roomType").rights[0].canBookRoom == true;
            });
            var availCol = new RoomCollection();
            availCol.reset(availiable);
        },

        initializeFilters : function(roomTypes) {
            var that = this;
            $("#search").click(function() {
                that.filterRooms();
            });
            var item = [];
            for(i = 0; i < roomTypes.length; i++) {
                var li = '<li><a class="my-option" id="' +
                roomTypes[i].id +
                '">' +
                roomTypes[i].roomType
                + '</a></li>';                
                $("#typeList").append(li);                
                item[i] = '#' + roomTypes[i].id;
                that.roomTypeObj[$(item[i]).text()] = roomTypes[i].id;
                $(item[i]).click(function(event){          
                    $('#type').html(event.target.innerHTML);  
                    that.criteria.roomType = event.target.innerHTML;
                });
            }

            $("#all-types").click(function(event){         
                    $('#type').html(event.target.innerHTML); 
                    that.criteria.roomType = "";
            });

            $("#avail-only").click(function(event){         
                    $('#avaliable').html(event.target.innerHTML);
                    that.criteria.all = false;
            });
            $("#all").click(function(event){         
                    $('#avaliable').html(event.target.innerHTML);  
                    that.criteria.all = true;
            });
        },



        filterRooms : function() {
            var that = this;

            that.model = $.extend({}, that.fullCollection);
            var criteria = {};
            criteria.roomName = $("#roomName").val();
            criteria.places = $("#places").val() == "" ? 0 : $("#places").val();
            criteria.computers = $("#computers").val() == "" ? 0 : $("#computers").val();
            criteria.all = that.criteria.all;
            criteria.roomType = { id : that.roomTypeObj[that.criteria.roomType]};
            console.log(that.roomTypeObj);
            criteria.board = $("#board").prop("checked");
            criteria.projector = $("#projector").prop("checked");
            this.filterCriteria = criteria;
            var arr = this.model.filter(function(room) {
                return that.filterByCriteria(room, that.filterCriteria);
            });
            this.model.reset(arr);
            that.buildList();
        },

        buildList : function() {
            var that = this;
            var len = this.model.models.length;
            var startPos = (this.options.page - 1) * 8;
            var endPos = Math.min(startPos + 8, len);
            $('#list-container').empty();
            $('#pagination-div').remove();
            $('#list-container').html('<ul class="thumbnails"></ul>');
            for (var i = startPos; i < endPos; i++) {
                $('.thumbnails', this.el).append(new RoomListItemView({model: that.model.models[i]}).render().el);
            };
            $(this.el).append(new Paginator({model: that.model, page: this.options.page}).render().el);
        },

        render : function () {
            var that = this;
            this.fullCollection = this.model;
            var rooms = this.model.models;           

            $(this.el).html(RoomListTemplate);

            $.ajax({
                url: "http://roombooking-ejournal.rhcloud.com/room-service/types",
                beforeSend: function(xhr){xhr.setRequestHeader('Authorization', 'ZXZnZXNoYToxMjM0')},
                success: function(data) {
                    that.initializeFilters(data);
                }
            });

            this.buildList();

            return this;
        }
    });
    
    return RoomListView
});