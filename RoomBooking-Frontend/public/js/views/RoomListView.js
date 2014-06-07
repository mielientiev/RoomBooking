define([
    'text!templates/RoomListTemplate.html',
    'text!templates/FailFilterTemplate.html',
    'views/RoomListItemView', 'views/paginator',
    'views/NavbarView'
    ],
    function(
        RoomListTemplate,
        FailFilterTemplate,
        RoomListItemView,
        Paginator,
        NavbarView
        )
    {

    var RoomListView = Backbone.View.extend({

        fullCollection  : null,
        filterCriteria  : null,
        criteria        : {},        
        selectedDate    : -1,
        page            : 1,

        events: {
            'click #search' : 'filterRooms',
            'keydown #places' : 'checkNumericInput',
            'keydown #computers' : 'checkNumericInput',
            'keydown #roomName' : 'checkInput',
            'change #specific-time' : 'selectSpecificTime'            
        },

        initialize : function (attrs) {
            var self = this;
            this.options = attrs;
            this.page = this.options.page;
            self.fullCollection = $.extend({}, self.model);
            this.render();
        },

        getCurrentDateString: function() {
            var today   = new Date();
            var dd      = today.getDate();
            var mm      = today.getMonth() + 1;
            var yyyy    = today.getFullYear();

            dd = dd < 10 ? ('0' + dd) : dd;
            mm = mm < 10 ? ('0' + mm) : mm;

            return yyyy+'-'+mm+'-'+dd;
        },

        filter : function(criteria) {
            var self = this;
            var col = this.model;
            var newCol = col.filter(function(room, criteria) {
                return room.filterByCriteria(room, criteria);
            });
        },

        filterByCriteria : function(room, criteria) {
            var isFiltered = true;
            isFiltered = isFiltered & (criteria.roomName == "" || room.get("roomName").indexOf(criteria.roomName) != -1);
            isFiltered = isFiltered & (criteria.roomType == 0 || criteria.roomType == 0 ||
                room.get("roomType").id == criteria.roomType);
            isFiltered = isFiltered & (room.get("places") >= criteria.places);
            isFiltered = isFiltered & (room.get("computers") >= criteria.computers);
            isFiltered = isFiltered & (!criteria.isFilteredoard || room.get("board") == criteria.board);
            isFiltered = isFiltered & (!criteria.projector || room.get("projector") == criteria.projector);
            isFiltered = isFiltered & (parseInt(criteria.all) || room.get("roomType").rights[0].canBookRoom);
            return isFiltered;
        },

        checkNumericInput: function() {
            var self = this;
            if(event.which == 13) {
                self.filterRooms();
                return;
            }
            else if((event.which < 48 || event.which > 57) && event.which != 8 && event.which != 46) {
                event.preventDefault();
                return;
            }
            else if (event.target.value.length > 3 && event.which != 8 && event.which != 46) {
                event.preventDefault();
                return;
            }
        },

        checkInput: function() {
            var self = this;
            if(event.which == 13) {
                self.filterRooms();
                return;
            }
        },

        selectSpecificTime: function() {
            var self = this;
            var curDatePicker   = self.$("#datepicker");
            var specificTime    = self.$("#specific-time");
            var pairSelect      = self.$("#pair-select");
            var max = specificTime.prop("checked") == true ? Infinity : -1;
            var min = specificTime.prop("checked") == true ? -Infinity : 0;
            self.$("#pair-select").attr("disabled", !($("#specific-time").prop("checked")));
            curDatePicker.datepicker("option", "maxDate", max);
            curDatePicker.datepicker("option", "minDate", min);
            if(specificTime.prop("checked")) {
                pairSelect.removeClass("disabled-select");                    
                self.selectedDate = self.getCurrentDateString();
            }
            else {
                pairSelect.addClass("disabled-select");
                self.selectedDate = -1;
            }
        },

        initializeFilters : function(roomTypes) {
            var self = this;

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
            var self = this;
            var date = this.selectedDate;
            var timetableID = $('#pair-select').attr('selected', 'selected').val();
            self.$("#list-container").empty();
            self.$("#pagination-div").addClass("disabled");
            self.$("#loading-div").removeClass("disabled");            
            $.ajax({
                url: "/api/room-service/rooms/" + 
                date + "/" + timetableID,
                beforeSend: function(xhr){xhr.setRequestHeader('Authorization', $.cookie('user'))},
                success: function(data) {
                    self.model.reset(data);
                    self.filter();
                    self.$("#pagination-div").removeClass("disabled");
                }
            });
        },

        getCriteria: function() {
            var criteria = {};
            criteria.roomName   = self.$("#roomName").val();
            criteria.places     = self.$("#places").val() == "" ? 0 : $("#places").val();
            criteria.computers  = self.$("#computers").val() == "" ? 0 : $("#computers").val();
            criteria.all        = self.$('input:radio[name="display-all"]:checked').val();
            criteria.roomType   = self.$('#type-select').attr('selected', 'selected').val();
            criteria.board      = self.$("#board").prop("checked");
            criteria.projector  = self.$("#projector").prop("checked");
            this.filterCriteria = criteria;
        },

        filter: function() {
            var self = this;            
            self.$('#fail-container').empty();        
            self.getCriteria();

            var arr = this.model.filter(function(room) {
                return self.filterByCriteria(room, self.filterCriteria);
            });
            this.model.reset(arr);
            self.page = 1;
            self.buildList();
            if(this.model.models.length == 0) {
                self.$('#fail-container').html(FailFilterTemplate);
            }
        },

        filterRooms : function() {
            var self = this;
            self.model = $.extend({}, self.fullCollection);
            if(self.$("#specific-time").prop("checked")) {
                self.filterByDate();
            }
            else {
                self.filter();
            }            
        },

        buildList : function() {
            var self = this;
            var len = this.model.models.length;
            var startPos = (self.page - 1) * 12;
            var endPos = Math.min(startPos + 12, len);
            self.$('#list-container').empty();
            self.$('#pagination-div').remove();
            self.$('#list-container').html('<ul class="thumbnails"></ul>');
            for (var i = startPos; i < endPos; i++) {
                self.$('.thumbnails').append(new RoomListItemView({model: self.model.models[i]}).render().el);
            };
            self.$("#loading-div").addClass("disabled");

            $(this.el).append(new Paginator({model: self.model, page: self.page, parent: this}).render().el);

        },

        changePage : function(page) {
            this.page = page;
            this.buildList();
        },

        initDatepicker: function() {
            var self = this;            
            self.$("#datepicker").datepicker({
                minDate: 0,
                maxDate: -1,
                dateFormat: "yy-mm-dd",
                onSelect: function(selected, e) {                
                self.selectedDate = selected;             
                }
            });
            self.$("#datepicker").datepicker('setDate', new Date());          
        },

        getRoomTypes: function() {
            var self = this;
            $.ajax({
                url: "/api/room-service/types",
                beforeSend: function(xhr){xhr.setRequestHeader('Authorization', $.cookie('user'))},
                success: function(data) {
                    self.initDatepicker();
                    self.initializeFilters(data);
                    self.buildList(); 
                }
            });
        },

        render : function () {
            var nv = new NavbarView();
            nv.render();

            var self = this;
            this.fullCollection = this.model;          

            $(this.el).html(RoomListTemplate);

            this.getRoomTypes();
            

            return this;
        }
    });
    
    return RoomListView
});