define(['jquery', 'underscore', 'aerogear'], function($, _, aerogear){
    $(function(){

        //Props to lholmquist
        var parseQueryString = function( locationString ) {
            //taken from https://developers.google.com/accounts/docs/OAuth2Login
            // First, parse the query string
            var params = {},
                queryString = locationString.substr( locationString.indexOf( "?" ) + 1 ),
                regex = /([^&=]+)=([^&]*)/g,
                m;
            while ( ( m = regex.exec(queryString) ) ) {
                params[decodeURIComponent(m[1])] = decodeURIComponent(m[2]);
            }
            return params;
        };

        var token = parseQueryString(window.location.search).id;

        console.log("Query string: " + token);

        if(token) {

            var info = _.template( $("#reset-info-template").text() );

            var resetPipeline = AeroGear.Pipeline([
                {
                    name: "reset",
                    settings: {
                        baseURL: "http://localhost:8080/password-reset/"
                    }
                }
            ]).pipes.reset;

            var reset = function ( email, password, confirmation ) {
                resetPipeline.save( token, {
                    query: {
                        id : token,
                        email: email,
                        password: password,
                        confirmation: confirmation
                    },
                    success: function( data ) {
                        console.log("Yay it works" + data);
                        $("#update-page").html( email );
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log("Oh noes! " + textStatus);
                        console.log("\n" + errorThrown);

                    }
                });
            };

            $( "#update-button" ).click( function() {
                var email = info( {'email': $("#email").val()}),
                    password = info( {'password': $("#password").val()}),
                    confirmation = info( {'confirmation': $("#confirmation").val()} );


                reset ( token );
            });
        }


    });
});
