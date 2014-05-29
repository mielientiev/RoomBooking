require.config({
  paths: {
    jQuery: 'libs/jquery-jquery.cookie',
    Underscore: 'libs/underscore',
    Backbone: 'libs/backbone',
    datepicker: 'libs/jquery-ui',
    text: 'libs/text',
    templates: '../templates'
  },

  shim: {
    'Backbone': ['Underscore', 'jQuery'],
    'RoomBooking': ['Backbone'],
    'datepicker' : ['jQuery']
  }
});

require(['RoomBooking'], function(RoomBooking) {
  RoomBooking.initialize();
});