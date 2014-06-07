define([], function() {
    var Paginator = Backbone.View.extend({

        className: "pagination pagination-center",


        initialize:function (attrs) {
            this.options = attrs;
        },

        render:function () {
            var that = this;
            
            $(this.el).empty();
            $(this.el).attr('id', 'pagination-div');            

            var items = this.model.models;
            var len = items.length;
            var pageCount = Math.ceil(len / 12);

            $(this.el).html('<ul />');

            for (var i = 0; i < pageCount; i++) {
                var e = $("<li><button>" + (i + 1) + "</button></li>");
                $('button', e).attr('id', 'page' + (i + 1));
                $('button', e).attr('value', (i + 1));
                $('button', e).addClass('btn btn-default');
                $('button', e).attr('disabled', (i + 1) == that.options.page);
                $('button', e).click(function(event) {
                    that.options.parent.changePage(event.target.value);
                });
                $('ul', this.el).append(e);
            }

            return this;
        }
    });

    return Paginator;
});