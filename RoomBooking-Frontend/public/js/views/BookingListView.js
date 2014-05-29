define(['text!templates/BookingListTemplate.html', 'text!templates/FailFilterTemplate.html',
    'text!templates/SucessAlertTemplate.html', 'text!templates/FailBookingDeletionTemplate.html',
    'views/BookingListItemView', 'views/paginator', 'views/NavbarView'],
    function(BookingListTemplate, FailFilterTemplate, SucessAlertTemplate,
        FailBookingDeletionTemplate, BookingListItemView, Paginator, NavbarView){
    var BookingListView = Backbone.View.extend({

        dateFrom : null,
        dateTo : null,
        fullCollection : null,
        filtered: false,

        initialize : function (attrs) {
            var that = this;
            this.options = attrs;
            that.fullCollection = $.extend({}, that.model);
            this.page = this.options.page;
            var today = new Date();
            var dd = today.getDate();
            var mm = today.getMonth() + 1;
            var yyyy = today.getFullYear();

            if(dd<10) {
                dd='0'+dd
            } 

            if(mm<10) {
                mm='0'+mm
            }

            this.dateFrom = this.dateTo = yyyy+'-'+mm+'-'+dd;
            this.render();
        },

        buildList : function() {
            var that = this;
            that.$("#list-container").addClass("disabled");
            that.$("#loading-div").removeClass("disabled");
            var len = this.model.models.length;
            var startPos = (that.page - 1) * 12;
            var endPos = Math.min(startPos + 12, len);
            that.$('#list-container').empty();
            that.$('#pagination-div').remove();
            that.$('#list-container').html('<ul class="thumbnails"></ul>');
            for (var i = startPos; i < endPos; i++) {
                $('.thumbnails', this.el).append(new BookingListItemView({model: that.model.models[i]}).render().el);
            };
            $(this.el).append(new Paginator({model: that.model, page: that.page, parent: this}).render().el);
            that.$("#list-container").removeClass("disabled");
            that.$("#loading-div").addClass("disabled");
            $(function() {
                for(i = 0; i < that.model.models.length; i++) {
                    that.$("#" + that.model.models[i].attributes.id).click(function(e){
                        that.deleteBooking(e.target.id);
                    });
                }
            });
        },

        changePage : function(page) {
            this.page = page;
            this.buildList();
        },

        deleteBooking: function(id) {
            var that = this;

            $.ajax({
                url: '/api/booking/' + id,
                type: 'DELETE',
                beforeSend: function(xhr){
                    xhr.setRequestHeader('Authorization', $.cookie('user'));
                },
                success: function() {                    
                    $('#fail-container').empty();
                    $("#pagination-div").addClass("disabled");
                    $("#loading-div").removeClass("disabled");
                    $("#list-container").addClass("disabled");
                    that.model.url = that.filtered ?
                    ('/api/booking/' + that.dateFrom + '/' + that.dateTo) :
                    '/api/booking/';
                    that.model.fetch({
                    beforeSend: function(xhr) {xhr.setRequestHeader('Authorization', $.cookie('user'))},
                        success: function(){
                            that.buildList();
                            $("#pagination-div").removeClass("disabled");
                            $("#loading-div").addClass("disabled");
                            $("#list-container").removeClass("disabled");
                            $('#fail-container').html(SucessAlertTemplate);
                        },
                        error: function() {
                            $('#fail-container').html(FailFilterTemplate);
                            $("#loading-div").addClass("disabled");
                        }
                    });                   
                },
                error: function() {
                    $('#fail-container').html(FailBookingDeletionTemplate);
                }
            });
        },

        filterBookings: function() {
            var that = this;
            that.filtered = true;
            $('#fail-container').empty();
            $("#pagination-div").addClass("disabled");
            $("#loading-div").removeClass("disabled");
            $("#list-container").addClass("disabled");
            this.model.url = '/api/booking/' + that.dateFrom + '/' + that.dateTo;
            this.model.fetch({
            beforeSend: function(xhr) {xhr.setRequestHeader('Authorization', $.cookie('user'))},
                success: function(){
                    that.buildList();
                    $("#pagination-div").removeClass("disabled");
                    $("#loading-div").addClass("disabled");
                    $("#list-container").removeClass("disabled");
                },
                error: function() {
                    $('#fail-container').html(FailFilterTemplate);
                    $("#loading-div").addClass("disabled");
                }
            });

        },

        render : function () {
            this.nv = new NavbarView();
            this.nv.render();
            var that = this;         

            $(this.el).html(BookingListTemplate);
            that.buildList();

            (that.$("#from")).datepicker({
                dateFormat: "yy-mm-dd",
                onSelect: function(selected, e) {                
                    that.dateFrom = selected;             
                    (that.$("#to")).datepicker("option", "minDate", selected);
                }
            });
            that.$("#from").datepicker('setDate', new Date());

            (that.$("#to")).datepicker({
                minDate: 0,
                maxDate: Infinity,
                dateFormat: "yy-mm-dd",
                onSelect: function(selected, e) {                
                    that.dateTo = selected;             
                }
            });            
            that.$("#to").datepicker('setDate', new Date());

            that.$("#search").click(function() {
                that.filterBookings();
            });            

            return this;
        }
    });
    
    return BookingListView
});
