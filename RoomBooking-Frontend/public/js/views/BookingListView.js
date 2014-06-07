define(['text!templates/BookingListTemplate.html',
        'text!templates/FailFilterTemplate.html',
        'text!templates/SucessAlertTemplate.html',
        'text!templates/FailBookingDeletionTemplate.html',
        'views/BookingListItemView',
        'views/paginator',
        'views/NavbarView'
        ],
    function(
        BookingListTemplate,
        FailFilterTemplate,
        SucessAlertTemplate,
        FailBookingDeletionTemplate,
        BookingListItemView,
        Paginator,
        NavbarView
        )
    {
    var BookingListView = Backbone.View.extend({

        dateFrom        : null,
        dateTo          : null,
        fullCollection  : null,
        filtered        : false,

        events: {
            'click #search' : 'filterBookings'
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

        initialize : function (attrs) {
            var self = this;

            this.options = attrs;
            this.fullCollection = $.extend({}, self.model);
            this.page = this.options.page;
            

            this.dateFrom = this.dateTo = this.getCurrentDateString();
            this.render();
        },

        initBookingBtnHandlers: function() {
            var self = this;
            $(function() {
                for(var i = 0; i < self.model.models.length; i++) {
                    self.$("#" + self.model.models[i].attributes.id).click(function(e){
                        self.deleteBooking(e.target.id);
                    });
                }
            });
        },

        buildList : function() {
            var self = this;
            self.$("#list-container").addClass("disabled");
            self.$("#loading-div").removeClass("disabled");
            
            var len = this.model.models.length;
            var startPos = (self.page - 1) * 12;
            var endPos = Math.min(startPos + 12, len);

            self.$('#list-container').empty();
            self.$('#pagination-div').remove();
            self.$('#list-container').html('<ul class="thumbnails"></ul>');

            for (var i = startPos; i < endPos; i++) {
                var bookingView = new BookingListItemView( { model: self.model.models[i] } ).render().el;
                $('.thumbnails', this.el).append(bookingView);
            }

            var paginator = new Paginator( { model: self.model, page: self.page, parent: this } ).render().el;
            $(this.el).append(paginator);

            self.$("#list-container").removeClass("disabled");
            self.$("#loading-div").addClass("disabled");

            self.initBookingBtnHandlers();

            if(self.model.models.length == 0) {
                self.$('#fail-container').html(FailFilterTemplate);
            }
        },

        changePage : function(page) {
            this.page = page;
            this.buildList();
        },

        initLoader: function() {
            self.$('#fail-container').empty();
            self.$("#pagination-div").addClass("disabled");
            self.$("#loading-div").removeClass("disabled");
            self.$("#list-container").addClass("disabled");
        },

        disableLoader: function() {
            self.$("#pagination-div").removeClass("disabled");
            self.$("#loading-div").addClass("disabled");
            self.$("#list-container").removeClass("disabled");
        },

        handleDeletedBooking: function() {
            var self = this;
            self.initLoader();
            self.model.url = self.filtered ?
                ('/api/booking/' + self.dateFrom + '/' + self.dateTo) :
                ('/api/booking/');
        },

        deleteBooking: function(id) {
            var self = this;

            $.ajax({
                url: '/api/booking/' + id,
                type: 'DELETE',
                beforeSend: function(xhr){
                    xhr.setRequestHeader('Authorization', $.cookie('user'));
                },
                success: function() {                   
                    self.handleDeletedBooking();
                    self.model.fetch({
                    beforeSend: function(xhr) {xhr.setRequestHeader('Authorization', $.cookie('user'))},
                        success: function(){
                            self.buildList();
                            self.disableLoader();
                            self.$('#fail-container').html(SucessAlertTemplate);
                        },
                        error: function() {
                            self.$('#fail-container').html(FailFilterTemplate);
                            self.$("#loading-div").addClass("disabled");
                        }
                    });                   
                },
                error: function() {
                    self.$('#fail-container').html(FailBookingDeletionTemplate);
                }
            });
        },

        filterBookings: function() {
            var self = this;
            self.filtered = true;
            self.initLoader();
            this.model.url = '/api/booking/' + self.dateFrom + '/' + self.dateTo;
            this.model.fetch({
            beforeSend: function(xhr) {xhr.setRequestHeader('Authorization', $.cookie('user'))},
                success: function(){
                    self.buildList();
                    self.disableLoader();
                },
                error: function() {
                    self.$('#fail-container').html(FailFilterTemplate);
                    self.$("#loading-div").addClass("disabled");
                }
            });

        },

        initDatepicker: function() {
            var self = this;
            (self.$("#from")).datepicker({
                dateFormat: "yy-mm-dd",
                onSelect: function(selected) {
                    self.dateFrom = selected;             
                    (self.$("#to")).datepicker("option", "minDate", selected);
                }
            });
            self.$("#from").datepicker('setDate', new Date());

            (self.$("#to")).datepicker({
                minDate: 0,
                maxDate: Infinity,
                dateFormat: "yy-mm-dd",
                onSelect: function(selected) {
                    self.dateTo = selected;             
                }
            });            
            self.$("#to").datepicker('setDate', new Date());
        },

        render : function () {
            this.nv = new NavbarView();
            this.nv.render();
            var self = this;         

            $(this.el).html(BookingListTemplate);            
            self.buildList();
            self.initDatepicker();           

            return this;
        }
    });
    
    return BookingListView
});