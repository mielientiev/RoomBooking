define([
	'text!templates/loginTemplate.html',
	'text!templates/LoginFailTemplate.html',
	'views/NavbarView'
	],
	function(
		loginTemplate,
		LoginFailTemplate,
		NavbarView
		)
	{

	var loginView = Backbone.View.extend({

		el : $('#content'),

		events: {
			'click #btn-login' : 'authenticate'
		},


		login : function(headerValue) {
			$.cookie('user', headerValue, { expires : 7 });
			this.nv = new NavbarView();
			this.nv.render();
			document.location = "?#rooms";
		},

		authenticate: function() {
			var self = this;
			var login 		= self.$("#input-login").val();
			var password 	= self.$("#input-password").val();
			var headerValue = "Basic " + B64.encode(login + ':' + password);
			$.ajax({
	                url: "/api/user-service/user",
	                beforeSend: function(xhr) {
	                	xhr.setRequestHeader('Authorization', headerValue)
	                },
	                success: function(data) {
	                    self.login(headerValue);
	                },
	                error: function() {
	                	self.$("#alert-container").html(LoginFailTemplate);
	                }
	            });
		},

		render: function() {

			var self = this;

			this.nv = new NavbarView();
			this.nv.render();
			
			$(this.el).html(loginTemplate);


			return this;
		}

	});

	return loginView;

});