define([
	'text!templates/RoomView.html',
	'text!templates/SuccessBookingTemplate.html',
	'text!templates/FailBookingTemplate.html',
	'models/Room',
	'models/RoomCollection',
	'views/NavbarView'
	],
	function(
		RoomViewTemplate,
		SuccessBookingTemplate,
		FailBookingTemplate,
		Room,
		RoomCollection,
		NavbarView
		)
	{

	var roomdetails = Backbone.View.extend({

		el			: $('#content'),
		template	: _.template(RoomViewTemplate),
		selectedBtn	: null,
		selectedVal	: null,
		selectedDate: null,
		todayDate	: null,
		currentXhr	: null,

		events: {
            'click #submit-booking-btn' : 'bookRoom'
        },

        enableSubmitBtn: function() {
        	self.$("#submit-booking-btn").prop("disabled", false);
			self.$("#submit-booking-btn").addClass("btn-default");
        },

        disableSubmitBtn: function() {
        	self.$("#submit-booking-btn").prop("disabled", true);
			self.$("#submit-booking-btn").removeClass("btn-default");
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

	    getDatepickerDate: function() {
	        var today   = new Date();
	        var dd      = today.getDate();
			var mm      = today.getMonth() + 1;
			var yyyy    = today.getFullYear();

			dd = dd < 10 ? ('0' + dd) : dd;
			mm = mm < 10 ? ('0' + mm) : mm;
			return mm+'/'+dd+'/'+yyyy;
	    },

		initDatepicker : function() {
			var self = this;
			self.$("#datepicker").datepicker({
	        	onSelect: function(selected, e) { 
	        		self.$("#pairs").empty();        		     		      		
	         		self.getPairs(selected);
	         		self.selectedDate = selected;
	    		}
	        });

			var today = self.getDatepickerDate();
	        self.$("#datepicker").datepicker( "setDate", today );
	        self.$("#datepicker").datepicker("option", "minDate", 0);
	        self.$("#datepicker").datepicker("option", "dateFormat", "yy-mm-dd");

	        var currentDate = self.getCurrentDateString();
	        this.todayDate = currentDate;
	        this.selectedDate = currentDate;
	        this.getPairs(this.selectedDate);
		},

		buildPairsTable: function(data, date) {
			var self = this;
			allPairs = data;
			self.setPairsTable(data);
			var requestURL =	'/api/timetable/room-' +
			self.model.attributes.id + '/' + date;
			$.ajax({
				url: requestURL,
				type: 'get',
				beforeSend: function(xhr){xhr.setRequestHeader('Authorization', $.cookie('user'))},
				success: function(data) {
					self.adjustPairsTable(data, allPairs);
					self.$("#pairs").removeClass("disabled");
					self.$("#loading-div").addClass("disabled");
				},
				error : function() {
					self.adjustPairsTable(0, allPairs);
					self.$("#pairs").removeClass("disabled");
					self.$("#loading-div").addClass("disabled");
				}
			});
		},

		getPairs : function(date) {			
			var self = this;
			if(self.currentXhr != null) {
				self.currentXhr.abort();
			}
			self.disableSubmitBtn();
			self.$("#pairs").addClass("disabled");
			self.$("#loading-div").removeClass("disabled"); 
			var allPairs;
			$.ajax({
				url: '/api/timetable',
				type: 'get',
				beforeSend: function(xhr){xhr.setRequestHeader('Authorization', $.cookie('user'))},
				success: function(data) {
					self.buildPairsTable(data, date);
				}
			});
		},

		disableBooking: function(bookingID) {
			var self = this;
			var query = "#pair" + bookingID.replace(" ", "");
			if(this.selectedVal == bookingID.replace(" ", "")) {
				this.selectedVal = null;
				$(query).removeClass("btn-default");
				self.disableSubmitBtn();
			}
			else {		
				$(query).removeClass("btn-success");
			}
			$(query).prop( "disabled", true );
		},

		enalbeBooking: function(bookingID) {
			var query = "#pair" + bookingID.replace(" ", "");
			$(query).addClass("btn-success");
			$(query).prop( "disabled", false );
		},

		adjustPairsTable : function(data, allPairs) {
			var idArr = [];
			for(a in data) {
				idArr.push(data[a].id);
			}
			for(i = 0; i < allPairs.length; i++) {
				var query = "#pair" + allPairs[i].id;
				if(idArr.indexOf(allPairs[i].id) == -1) {
					if(this.selectedDate == this.todayDate) {
						var selDate = new Date();
						var today = new Date();
						var minutes = allPairs[i].start.split(":")[1];
						var hours = allPairs[i].start.split(":")[0];
						selDate.setMinutes(minutes);
						selDate.setHours(hours);
						selDate.setSeconds(0);
						if(selDate < today) {
							continue;
						}
					}
					$(query).addClass("btn-success");
					$(query).prop( "disabled", false );
				}
			}
		},

		setPairsTable : function(data) {
			var self = this;
			self.$("#pairs").empty();
			for(i = 0; i < data.length; i++) {
				var startSplit = data[i].start.split(':');
				var start = startSplit[0] + ":" + startSplit[1];
				var endSplit = data[i].end.split(':');
				var end = endSplit[0] + ":" + endSplit[1];
				var btn = 	'<button type="button" class="btn btn-pair" id="pair' +
							data[i].id + '" value="' +
							data[i].id + '"disabled>' +
							data[i].id + '. ' +
							start + ' - ' +
							end +
							'</button>';
				self.$("#pairs").append(btn);
				self.$('#pair' + data[i].id).click(function() { self.selectPair(this); });
			}
		},

		selectPair : function(e) {
			if(this.selectedVal) {
				this.selectedBtn.removeClass("btn-default");
				this.selectedBtn.addClass("btn-success");
			}
			var btn = $("#" + e.id);
			btn.removeClass("btn-success");
			btn.addClass("btn-default");
			this.selectedBtn = btn;
			this.selectedVal = btn.val();
			this.enableSubmitBtn();
		},

		bookRoom : function() {
			var self = this;
			var timetable = { id : self.selectedVal };
			var room = { id : self.model.attributes.id.toString() }
			var newBooking = {
				date: self.selectedDate,
				timetable : timetable,
				room : room,
				id: 1
			};
			$.ajax({
				url: "/api/booking",
				data: JSON.stringify(newBooking),
	    		type: 'PUT',
	    		beforeSend: function(xhr){
	    			xhr.setRequestHeader('Authorization', $.cookie('user'))
	    		},
	   			contentType: 'application/json',
			    success: function(data) {		               
		        	self.getPairs(self.selectedDate);
			    	self.$("#success-alerts").append(SuccessBookingTemplate);
			    },
			    error: function(data) {		               
			    	self.getPairs(self.selectedDate);
			    	self.$("#success-alerts").append(FailBookingTemplate);
			    }
			});
		},

		checkAvaliability: function() {
			if(this.model.attributes.roomType.rights[0].canBookRoom == false) {
	        	this.$("#submit-booking-btn-div").remove();
	        	this.$('#access-denial-div').removeClass("disabled");
	        }
		},

		render: function() {
			var self = this;
			var nv = new NavbarView();
			nv.render();

			$(this.el).html(self.template(self.model.toJSON()));
	        this.initDatepicker();
	        this.checkAvaliability();
	        
			return this;
		}

	});

	return roomdetails;

});