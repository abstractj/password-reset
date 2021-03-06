define(['jquery', 'underscore', 'aerogear'], function($, _, aerogear){
  $(function(){

      var info = _.template( $("#info-template").text() );

      var forgotPipeline = AeroGear.Pipeline([
          {
              name: "forgot",
              settings: {
                  baseURL: "http://localhost:8080/password-reset/rest/"
              }
          }
      ]).pipes.forgot;

      var forgot = function ( email ) {
        forgotPipeline.read({
            query: {email : email},
            success: function( ) {
              $("#reset-page").html( email );
            },
            error: function (jqXHR, textStatus, errorThrown) {
              console.log("Oh noes! " + textStatus);
            }
        });
      };

      $( "#reset-button" ).click( function() {
          var email = info( {'email': $("#email").val()} );
          forgot ( email );
      });

  });
});
