define(['text!templates/RoomListView.html'], function(RoomListView){
    var RoomListItemView = Backbone.View.extend({

        tagName: "li",
        template: _.template(RoomListView),

        className: "roomlist",

        initialize: function () {
            this.model.bind("change", this.render, this);
            this.model.bind("destroy", this.close, this);
        },

        render: function () {
            $(this.el).html(this.template(this.model.toJSON()));
            return this;
        }

    });
    return RoomListItemView;
});