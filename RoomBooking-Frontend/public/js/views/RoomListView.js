define(['views/RoomListItemView', 'views/paginator'], function(RoomListItemView, Paginator){
    var RoomListView = Backbone.View.extend({

        initialize: function (attrs) {
            this.options = attrs;
            this.render();
        },

        render: function () {
            var rooms = this.model.models;
            var len = rooms.length;
            var startPos = (this.options.page - 1) * 8;
            var endPos = Math.min(startPos + 8, len);

            $(this.el).html('<ul class="thumbnails"></ul>');

            for (var i = startPos; i < endPos; i++) {
                $('.thumbnails', this.el).append(new RoomListItemView({model: rooms[i]}).render().el);
            }

            $(this.el).append(new Paginator({model: this.model, page: this.options.page}).render().el);

            return this;
        }
    });
    
    return RoomListView
});