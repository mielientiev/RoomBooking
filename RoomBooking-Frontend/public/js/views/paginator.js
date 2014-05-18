define([], function() {
    var Paginator = Backbone.View.extend({

        className: "pagination pagination-center",

<<<<<<< HEAD

=======
>>>>>>> e47248977b26b53e6106a130b9d1d9b4385c81e1
        initialize:function (attrs) {
            this.options = attrs;
            this.model.bind("reset", this.render, this);
            this.render();
        },

        render:function () {
<<<<<<< HEAD
            $(this.el).attr('id', 'pagination-div');
=======
>>>>>>> e47248977b26b53e6106a130b9d1d9b4385c81e1

            var items = this.model.models;
            var len = items.length;
            var pageCount = Math.ceil(len / 8);

            $(this.el).html('<ul />');

<<<<<<< HEAD

            //TODO:
            //!!!
            //onclick do not render the whole page, just refresh the list

=======
>>>>>>> e47248977b26b53e6106a130b9d1d9b4385c81e1
            for (var i=0; i < pageCount; i++) {
                $('ul', this.el).append("<li" + ((i + 1) === this.options.page ? " class='active'" : "") + "><a href='#rooms/page/"+(i+1)+"'>" + (i+1) + "</a></li>");
            }

            return this;
        }
    });

    return Paginator;
});