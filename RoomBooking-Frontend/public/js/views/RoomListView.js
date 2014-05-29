define(['text!templates/RoomListTemplate.html', 'text!templates/FailFilterTemplate.html',
    'views/RoomListItemView', 'views/paginator', 'views/NavbarView'],
    function(RoomListTemplate, FailFilterTemplate,
        RoomListItemView, Paginator, NavbarView){
    var RoomListView = Backbone.View.extend({

        fullCollection : null,
        criteria : {},
        filterCriteria : null,
        selectedDate: -1,
        page: 1,

        initialize : function (attrs) {
            var that = this;
            this.options = attrs;
            this.page = this.options.page;
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
            b = b & (criteria.roomType == 0 || criteria.roomType == 0 ||
                room.get("roomType").id == criteria.roomType);
            b = b & (room.get("places") >= criteria.places);
            b = b & (room.get("computers") >= criteria.computers);
            b = b & (!criteria.board || room.get("board") == criteria.board);
            b = b & (!criteria.projector || room.get("projector") == criteria.projector);
            b = b & (parseInt(criteria.all) || room.get("roomType").rights[0].canBookRoom);
            return b;
        },

        initializeFilters : function(roomTypes) {
            var that = this;
            $("#search").click(function() {
                that.filterRooms();
            });
            for(i = 0; i < roomTypes.length; i++) {
                var option = '<option value="' +
                roomTypes[i].id +
                '">' +
                roomTypes[i].roomType
                + '</option>';                
                $("#type-select").append(option);     
            }            
            $.ajax({
                url: '/api/timetable',
                beforeSend: function(xhr){xhr.setRequestHeader('Authorization', $.cookie('user'))},
                success: function(pairs) {
                    
                    for(i = 0; i < pairs.length; i++) {
                        var option = '<option value="' +
                        pairs[i].id +
                        '"> ' +
                        pairs[i].id + ". " + pairs[i].start + "-" + pairs[i].end
                        + '</option>';                
                        $("#pair-select").append(option);     
                    }
                }
            });

        },

        filterByDate: function() {
            $("#list-container").empty();
            $("#pagination-div").addClass("disabled");
            $("#loading-div").removeClass("disabled");
            var that = this;
            var date = this.selectedDate;
            var timetableID = $('#pair-select').attr('selected', 'selected').val();
            $.ajax({
                url: "/api/room-service/rooms/" + 
                date + "/" + timetableID,
                beforeSend: function(xhr){xhr.setRequestHeader('Authorization', $.cookie('user'))},
                success: function(data) {
                    that.model.reset(data);
                    that.filter();
                    $("#pagination-div").removeClass("disabled");
                }
            });
        },

        filter: function() {
            var that = this;
            $('#fail-container').empty();
            var criteria = {};
            criteria.roomName = $("#roomName").val();
            criteria.places = $("#places").val() == "" ? 0 : $("#places").val();
            criteria.computers = $("#computers").val() == "" ? 0 : $("#computers").val();
            criteria.all = $('input:radio[name="display-all"]:checked').val();
            criteria.roomType = $('#type-select').attr('selected', 'selected').val();
            criteria.board = $("#board").prop("checked");
            criteria.projector = $("#projector").prop("checked");
            this.filterCriteria = criteria;
            var arr = this.model.filter(function(room) {
                return that.filterByCriteria(room, that.filterCriteria);
            });
            this.model.reset(arr);
            that.page = 1;
            that.buildList();
            if(this.model.models.length == 0) {
                $('#fail-container').html(FailFilterTemplate);
            }
        },

        filterRooms : function() {
            var that = this;
            that.model = $.extend({}, that.fullCollection);
            if($("#specific-time").prop("checked")) {
                that.filterByDate();
            }
            else {
                that.filter();
            }            
        },

        buildList : function() {
            var that = this;
            var len = this.model.models.length;
            var startPos = (that.page - 1) * 12;
            var endPos = Math.min(startPos + 12, len);
            $('#list-container').empty();
            $('#pagination-div').remove();
            $('#list-container').html('<ul class="thumbnails"></ul>');
            for (var i = startPos; i < endPos; i++) {
                $('.thumbnails', this.el).append(new RoomListItemView({model: that.model.models[i]}).render().el);
            };
            $("#loading-div").addClass("disabled");
            $(this.el).append(new Paginator({model: that.model, page: that.page, parent: this}).render().el);
        },

        changePage : function(page) {
            this.page = page;
            this.buildList();
        },

        initDatepicker: function() {
            var that = this;
            $("#datepicker").datepicker({
                minDate: 0,
                maxDate: -1,
                dateFormat: "yy-mm-dd",
                onSelect: function(selected, e) {                
                that.selectedDate = selected;             
                }
            });
            $("#datepicker").datepicker('setDate', new Date());
            $("#specific-time").change(function() {
                var max = $("#specific-time").prop("checked") == true ? Infinity : -1;
                var min = $("#specific-time").prop("checked") == true ? -Infinity : 0;
                $("#pair-select").attr("disabled", !($("#specific-time").prop("checked")));
                $("#datepicker").datepicker("option", "maxDate", max);
                $("#datepicker").datepicker("option", "minDate", min);
                if($("#specific-time").prop("checked")) {
                    $("#pair-select").removeClass("disabled-select");
                    var today = new Date();
                    var dd = today.getDate() + 1;
                    var mm = today.getMonth() + 1;
                    var yyyy = today.getFullYear();

                    if(dd<10) {
                        dd='0'+dd;
                    } 

                    if(mm<10) {
                        mm='0'+mm;
                    }
                    that.selectedDate = yyyy+'-'+mm+'-'+dd;
                    console.log(that.selectedDate);
                }
                else {
                    $("#pair-select").addClass("disabled-select");
                    that.selectedDate = -1;
                }
            });          
        },

        render : function () {
            var nv = new NavbarView();
            nv.render();

            var that = this;
            this.fullCollection = this.model;          

            $(this.el).html(RoomListTemplate);

            $.ajax({
                url: "/api/room-service/types",
                beforeSend: function(xhr){xhr.setRequestHeader('Authorization', $.cookie('user'))},
                success: function(data) {
                    that.initDatepicker();
                    that.initializeFilters(data);
                    that.buildList(); 
                }
            });
     
            


            return this;
        }
    });
    
    return RoomListView
});