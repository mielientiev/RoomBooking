define(['text!templates/RoomView.html', 'models/Room', 'models/RoomCollection'],
	function(RoomViewTemplate, Room, RoomCollection){

var roomdetails = Backbone.View.extend({
	el : $('#content'),
	template: _.template(RoomViewTemplate),
<<<<<<< HEAD
	selectedBtn: null,
	selectedVal: null,
	selectedDate: null,
	todayDate: null,
=======
>>>>>>> e47248977b26b53e6106a130b9d1d9b4385c81e1

	initialize : function() {
	},

	initDatepicker : function() {
		var that = this;
		$("#datepicker").datepicker({
<<<<<<< HEAD
        	onSelect: function(selected, e) { 
        		$("#pairs").empty();       		      		
         		that.getPairs(selected);
         		that.selectedDate = selected;
=======
        	onSelect: function(selected,evnt) {
         		that.getPairs(selected);
>>>>>>> e47248977b26b53e6106a130b9d1d9b4385c81e1
    		}
        });
        var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1;
		var yyyy = today.getFullYear();

		if(dd<10) {
    		dd='0'+dd
		} 

		if(mm<10) {
    		mm='0'+mm
		} 

		today = mm+'/'+dd+'/'+yyyy;
<<<<<<< HEAD
        $("#datepicker").datepicker( "setDate", today );
        $( "#datepicker" ).datepicker("option", "minDate", 0);
        $("#datepicker").datepicker("option", "dateFormat", "yy-mm-dd");
        this.selectedDate = yyyy+'-'+mm+'-'+dd;
        this.getPairs(this.selectedDate);
=======
        $("#datepicker").datepicker( "setDate", today );        
        $("#datepicker").datepicker("option", "dateFormat", "yy-mm-dd");
        this.getPairs(yyyy+'-'+mm+'-'+dd);
>>>>>>> e47248977b26b53e6106a130b9d1d9b4385c81e1
	},

	getPairs : function(date) {
		var that = this;
<<<<<<< HEAD
		var allPairs;
=======
>>>>>>> e47248977b26b53e6106a130b9d1d9b4385c81e1
		$.ajax({
		  url: 'http://roombooking-ejournal.rhcloud.com/timetable',
		  type: 'get',
		  beforeSend: function(xhr){xhr.setRequestHeader('Authorization', 'bWVsaToxMjM0NQ')},
          success: function(data) {
<<<<<<< HEAD
          		allPairs = data;
               	that.setPairsTable(data);
               	var requestURL = 	'http://roombooking-ejournal.rhcloud.com/timetable/room-' +
							that.model.attributes.id +
							'/' +
							date;
				$.ajax({
				  url: requestURL,
				  type: 'get',
				  beforeSend: function(xhr){xhr.setRequestHeader('Authorization', 'bWVsaToxMjM0NQ')},
		          success: function(data) {
		               	that.adjustPairsTable(data, allPairs);
		          },
		          error : function() {
		               	that.adjustPairsTable(0, allPairs);
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
		console.log(idArr);
		for(i = 0; i < allPairs.length; i++) {
			var query = "#pair" + allPairs[i].id;
			if(idArr.indexOf(allPairs[i].id) != -1) {
				$(query).addClass("btn-danger");
			}
			else {
				$(query).addClass("btn-success");
				$(query).prop( "disabled", false );
			}
=======
               	that.setPairsTable(data);
          }
		});

		var requestURL = 	'http://roombooking-ejournal.rhcloud.com/timetable/room-' +
							this.model.attributes.id +
							'/' +
							date;
		$.ajax({
		  url: requestURL,
		  type: 'get',
		  beforeSend: function(xhr){xhr.setRequestHeader('Authorization', 'bWVsaToxMjM0NQ')},
          success: function(data) {
               	that.adjustPairsTable(data);
          }
		});
	},

	adjustPairsTable : function(data) {
		for(a in data) {
			var query = "#pair" + data[a].id;
			$(query).removeClass("btn-success");
			$(query).addClass("btn-warning");
			$(query).prop( "disabled", true );
>>>>>>> e47248977b26b53e6106a130b9d1d9b4385c81e1
		}
	},

	setPairsTable : function(data) {
<<<<<<< HEAD
		var that = this;
		$("#pairs").empty();
=======
		$("#pairs").empty();
		var template = '<button type="button" class="btn btn-success btn-pair">Button</button>';
>>>>>>> e47248977b26b53e6106a130b9d1d9b4385c81e1
		for(i = 0; i < data.length; i++) {
			var startSplit = data[i].start.split(':');
			var start = startSplit[0] + ":" + startSplit[1];
			var endSplit = data[i].end.split(':');
			var end = endSplit[0] + ":" + endSplit[1];
<<<<<<< HEAD
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
			this.selectedBtn.removeClass("btn-primary");
			this.selectedBtn.addClass("btn-success");
		}
		var btn = $("#" + e.id);
		btn.removeClass("btn-success");
		btn.addClass("btn-primary");
		this.selectedBtn = btn;
		this.selectedVal = btn.val();
		$("#submit-booking-btn").prop("disabled", false);
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
				url: "http://roombooking-ejournal.rhcloud.com/booking",
				data: JSON.stringify(newBooking),
    			type: 'PUT',
    			beforeSend: function(xhr){xhr.setRequestHeader('Authorization', 'bWVsaToxMjM0NQ')},
   				contentType: 'application/json',
		         success: function(data) {
		               	that.returnToSelect();
		          }
				});
		console.log(newBooking);

	},

	returnToSelect : function() {
		window.location = "#";
=======
			var btn = 	'<button type="button" class="btn btn-success btn-pair" id="pair' +
						data[i].id + '">' +
						data[i].id + '. ' +
						start + ' - ' +
						end +
						'</button>'
			$("#pairs").append(btn);
		}
>>>>>>> e47248977b26b53e6106a130b9d1d9b4385c81e1
	},

	render: function() {
		var that = this;
		$(this.el).html(that.template(that.model.toJSON()));
        this.initDatepicker();
<<<<<<< HEAD
        $("#submit-booking-btn").click(function(e) {
        	that.bookRoom();
        })
=======
>>>>>>> e47248977b26b53e6106a130b9d1d9b4385c81e1
		return this;
	}

  });

  return roomdetails;

});