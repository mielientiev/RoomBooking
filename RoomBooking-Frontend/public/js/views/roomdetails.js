define(['text!templates/RoomView.html', 'models/Room', 'models/RoomCollection'],
	function(RoomViewTemplate, Room, RoomCollection){

var roomdetails = Backbone.View.extend({
	el : $('#content'),
	template: _.template(RoomViewTemplate),

	initialize : function() {
	},

	initDatepicker : function() {
		var that = this;
		$("#datepicker").datepicker({
        	onSelect: function(selected,evnt) {
         		that.getPairs(selected);
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
        $("#datepicker").datepicker( "setDate", today );        
        $("#datepicker").datepicker("option", "dateFormat", "yy-mm-dd");
        this.getPairs(yyyy+'-'+mm+'-'+dd);
	},

	getPairs : function(date) {
		var that = this;
		$.ajax({
		  url: 'http://roombooking-ejournal.rhcloud.com/timetable',
		  type: 'get',
		  beforeSend: function(xhr){xhr.setRequestHeader('Authorization', 'bWVsaToxMjM0NQ')},
          success: function(data) {
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
		}
	},

	setPairsTable : function(data) {
		$("#pairs").empty();
		var template = '<button type="button" class="btn btn-success btn-pair">Button</button>';
		for(i = 0; i < data.length; i++) {
			var startSplit = data[i].start.split(':');
			var start = startSplit[0] + ":" + startSplit[1];
			var endSplit = data[i].end.split(':');
			var end = endSplit[0] + ":" + endSplit[1];
			var btn = 	'<button type="button" class="btn btn-success btn-pair" id="pair' +
						data[i].id + '">' +
						data[i].id + '. ' +
						start + ' - ' +
						end +
						'</button>'
			$("#pairs").append(btn);
		}
	},

	render: function() {
		var that = this;
		$(this.el).html(that.template(that.model.toJSON()));
        this.initDatepicker();
		return this;
	}

  });

  return roomdetails;

});