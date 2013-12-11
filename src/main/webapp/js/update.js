define(['jquery', 'underscore', 'aerogear'], function($, _, aerogear){
    $(function(){

        //Props to lholmquist
        var parseQueryString = function( locationString ) {
            //taken from https://developers.google.com/accounts/docs/OAuth2Login
            // First, parse the query string
            var params = {},
                queryString = locationString.substr( locationString.indexOf( "#" ) + 1 ),
                regex = /([^&=]+)=([^&]*)/g,
                m;
            while ( ( m = regex.exec(queryString) ) ) {
                params[decodeURIComponent(m[1])] = decodeURIComponent(m[2]);
            }
            return params;
        };

        var queryString = parseQueryString(location.search);

        console.log("Query string: " + queryString);

        if(queryString) {

            var info = _.template( $("#reset-info-template").text() );

            var resetPipeline = AeroGear.Pipeline([
                {
                    name: "reset",
                    settings: {
                        baseURL: "http://localhost:8080/password-reset/"
                    }
                }
            ]).pipes.reset;

            var reset = function ( token ) {
                resetPipeline.save({
                    query: {id : token},
                    success: function( ) {
                        console.log("Yay it works");
                        $("#update-page").html( email );
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        console.log("Oh noes! " + textStatus);
                    }
                });
            };

            $( "#update-button" ).click( function() {

                console.log("Click");
                info ( queryString );
            });
        }


    });
});
