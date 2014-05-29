define(['text!templates/RoomView.html', 'text!templates/SuccessBookingTemplate.html',
	'text!templates/FailBookingTemplate.html',
	'models/Room', 'models/RoomCollection', 'views/NavbarView'],
	function(RoomViewTemplate, SuccessBookingTemplate, FailBookingTemplate,
		Room, RoomCollection, NavbarView){

var roomdetails = Backbone.View.extend({
	el : $('#content'),
	template: _.template(RoomViewTemplate),
	selectedBtn: null,
	selectedVal: null,
	selectedDate: null,
	todayDate: null,

	initialize : function() {
	},

	initDatepicker : function() {
		var that = this;
		$("#datepicker").datepicker({
        	onSelect: function(selected, e) { 
        		$("#pairs").empty();        		     		      		
         		that.getPairs(selected);
         		that.selectedDate = selected;
    		}
        });
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

		today = mm+'/'+dd+'/'+yyyy;
        $("#datepicker").datepicker( "setDate", today );
        $("#datepicker").datepicker("option", "minDate", 0);
        $("#datepicker").datepicker("option", "dateFormat", "yy-mm-dd");
        this.todayDate = yyyy+'-'+mm+'-'+dd;
        this.selectedDate = yyyy+'-'+mm+'-'+dd;
        this.getPairs(this.selectedDate);
	},

	getPairs : function(date) {
		$("#submit-booking-btn").prop("disabled", true);
		$("#submit-booking-btn").removeClass("btn-default");
		$("#pairs").addClass("disabled");
		$("#loading-div").removeClass("disabled"); 
		var that = this;
		var allPairs;
		$.ajax({
		  url: '/api/timetable',
		  type: 'get',
		  beforeSend: function(xhr){xhr.setRequestHeader('Authorization', $.cookie('user'))},
          success: function(data) {
          		allPairs = data;
               	that.setPairsTable(data);
               	var requestURL = 	'/api/timetable/room-' +
							that.model.attributes.id +
							'/' +
							date;
				$.ajax({
				  url: requestURL,
				  type: 'get',
				  beforeSend: function(xhr){xhr.setRequestHeader('Authorization', $.cookie('user'))},
		          success: function(data) {
		               	that.adjustPairsTable(data, allPairs);
		               	$("#pairs").removeClass("disabled");
		               	$("#loading-div").addClass("disabled");
		          },
		          error : function() {
		               	that.adjustPairsTable(0, allPairs);
		               	$("#pairs").removeClass("disabled");
		               	$("#loading-div").addClass("disabled");
		          }
				});
          }
		});		
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
		var that = this;
		$("#pairs").empty();
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
			$("#pairs").append(btn);
			$('#pair' + data[i].id).click(function() { that.selectPair(this); });
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
		$("#submit-booking-btn").prop("disabled", false);
		$("#submit-booking-btn").addClass("btn-default");
	},

	bookRoom : function() {
		var that = this;
		var timetable = { id : that.selectedVal };
		var room = { id : that.model.attributes.id.toString() }
		var newBooking = {
			date: that.selectedDate,
			timetable : timetable,
			room : room,
			id: 1
		};
		$.ajax({
				url: "/api/booking",
				data: JSON.stringify(newBooking),
    			type: 'PUT',
    			beforeSend: function(xhr){xhr.setRequestHeader('Authorization', $.cookie('user'))},
   				contentType: 'application/json',
		         success: function(data) {		               
		               that.getPairs(that.selectedDate);
		               $("#success-alerts").append(SuccessBookingTemplate);
		          },
		          error: function(data) {		               
		               that.getPairs(that.selectedDate);
		               $("#success-alerts").append(FailBookingTemplate);
		          }
				});
	},

	render: function() {
		var that = this;
		var nv = new NavbarView();
		nv.render();

		$(this.el).html(that.template(that.model.toJSON()));
        this.initDatepicker();
        $("#submit-booking-btn").click(function(e) {
        	that.bookRoom();
        });
        if(that.model.attributes.roomType.rights[0].canBookRoom == false) {
        	$("#submit-booking-btn-div").remove();
        	$('#access-denial-div').removeClass("disabled");
        }
		return this;
	}

  });

  return roomdetails;

});