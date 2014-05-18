define(['text!templates/editdrug.html'], function(editdrugTemplate) {
  var editdrugView = Backbone.View.extend({
    el: $('#content'),

    events: {
      "submit form": "editdrug"
    },


    editdrug: function() {
      var operation = this.id ? ('/editdrug/' + this.id) : '/adddrug';

      if(
        $('#name').val() == "" ||
        $('#quantity_in_pack').val() == "" ||
        $('#registration_number').val() == ""
        ) {
          jAlert('All * fileds must be filled', 'Creation Error');
          return;
      }

      $.post(operation, {
        name: $('#name').val(),
        release_form: $('#release_form option:selected').val(),
        quantity_in_pack: $('#quantity_in_pack').val(),
        registration_number: $('#registration_number').val(),
        recipe_required: $('#recipe_required')[0].checked * 1,
        description: $('#description').val()
      }, function(data) {
        console.log(data);
      }).success(function() {
        window.location = '/#drugredactor';
      }).fail(function() {
        jAlert('Invalid fields values \n Check for reigstration number to be unique', 'Database Error');
      });      
      return false;
    },

    render: function() {
      this.$el.html(editdrugTemplate);
      var drug = null;
      $.get('/drugs/drug/' + this.id, function(drug) { 
          $('#name').val(drug[0].name);
          $('#release_form option:selected').val(drug[0].release_form);
          $('#quantity_in_pack').val(drug[0].quantity_in_pack);
          $('#registration_number').val(drug[0].registration_number);
          $('#recipe_required')[0].checked = drug[0].recipe_required;
          $('#description').text(drug[0].description);
      });
    }
  });

  return editdrugView;
});
