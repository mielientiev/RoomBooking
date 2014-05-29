define(['text!templates/BookingListView.html'], function(BookingListView){
    var BookingListItemView = Backbone.View.extend({

        tagName: "li",
        template: _.template(BookingListView),

        className: "bookinglist",

        initialize: function () {
            this.model.bind("change", this.render, this);
            this.model.bind("destroy", this.close, this);
        },

        render: function () {
            $(this.el).html(this.template(this.model.toJSON()));
            return this;
        }

    });
    return BookingListItemView;
});