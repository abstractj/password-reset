require.config({
  paths: {
    jquery: 'components/jquery/jquery.min',
    underscore: 'components/underscore/underscore-min',
    aerogear: 'components/aerogear/aerogear.min'
  },
  shim: {
    underscore: {
      exports: '_'
    },
    backbone: {
      deps: [ 'underscore', 'jquery' ],
      exports: 'Backbone'
    }
  }
});

require([ 'main' ]);
