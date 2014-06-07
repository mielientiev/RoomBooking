define(['text!templates/NavbarTemplate.html',
		'text!templates/NavbarUnregTemplate.html',
		'text!templates/NavbarAdminTemplate.html'
		],
	function(
		NavbarTemplate,
		NavbarUnregTemplate,
		NavbarAdminTemplate
		)
	{

		var NavbarView = Backbone.View.extend({
			el : $('#navbar-main'),
			template : NavbarTemplate,

			initialize : function() {
				this.template = NavbarTemplate;
			},

			buildNavbar: function(data) {
				this.template = this.template.replace('%FIRSTNAME%', data.firstName);
				this.template = this.template.replace('%SECONDNAME%', data.secondName);
				$(this.el).html(this.template);
			},

			render: function() {
				var that = this;
				$.ajax({
					url: '/api/user-service/user',
					type: 'get',
					beforeSend: function(xhr){xhr.setRequestHeader('Authorization', $.cookie('user'))},
					success: function(data) {
					that.buildNavbar(data);
				},
				error: function() {
					$(that.el).html(NavbarUnregTemplate);
				}
			});
			return this;
		}

	});

	return NavbarView;

});