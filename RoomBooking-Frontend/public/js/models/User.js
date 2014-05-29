define([],
    function(){
    var User = Backbone.Model.extend({
        urlRoot: "/api/user-service/user"
    });
return User;
});