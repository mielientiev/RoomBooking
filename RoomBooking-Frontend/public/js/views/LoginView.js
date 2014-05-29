define([
	'text!templates/loginTemplate.html',
	'text!templates/LoginFailTemplate.html',
	'views/NavbarView',
	],
	function(
		loginTemplate,
		LoginFailTemplate,
		NavbarView
		){

var loginView = Backbone.View.extend({
	el : $('#content'),

	initialize : function() {
	},

	login : function(headerValue) {
		$.cookie('user', headerValue);
		this.nv = new NavbarView();
		this.nv.render();
		document.location = "?#rooms";
	},

	authenticate: function() {
		var that = this;
		var login 		= $("#input-login").val();
		var password 	= $("#input-password").val();
		var headerValue = "Basic " + B64.encode(login + ':' + password);
		$.ajax({
                url: "/api/user-service/user",
                beforeSend: function(xhr){xhr.setRequestHeader('Authorization', headerValue)},
                success: function(data) {
                	$.cookie('user', headerValue);
                    that.login(headerValue);
                },
                error: function() {
                	$("#alert-container").html(LoginFailTemplate);
                }
            });
	},

	render: function() {
		this.nv = new NavbarView();
		this.nv.render();

		var that = this;
		$(this.el).html(loginTemplate);

		$("#btn-login").click(function(){
			that.authenticate();
		});

		return this;
	}

  });

  return loginView;

});