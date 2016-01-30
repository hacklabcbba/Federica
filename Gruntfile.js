module.exports = function(grunt) {
  grunt.loadNpmTasks('grunt-contrib-less');
  grunt.loadNpmTasks('grunt-contrib-watch');
  // Project configuration.
  grunt.initConfig({
    less: {
      development: {
        options: {
          sourceMap: true,
          sourceMapFilename: 'src/main/webapp/css/style.css.map', // where file is generated and located
          sourceMapURL: 'style.css.map', // the complete url and filename put in the compiled css file
          sourceMapRootpath: '/federica/' // adds this path onto the sourcemap filename and less file paths
        },
        files: {
          'src/main/webapp/css/styles.css': 'src/main/webapp/css/styles.less'
        }
      }
    },
    watch: {
      less: {
        files: ['src/main/webapp/css/styles.less'],
        tasks: ['less:development'],
        options: {
          spawn: true
        }
      }
    }
  });
};

