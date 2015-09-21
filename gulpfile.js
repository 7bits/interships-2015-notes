var gulp = require('gulp');
var postcss = require('gulp-postcss');
var autoprefixer = require('autoprefixer');
var mqpacker = require('css-mqpacker');
var csswring = require('csswring');
var concat = require('gulp-concat');
var runSequence = require('run-sequence');
var minify = require('gulp-minify-css');
var browserSync = require('browser-sync').create();
var uglify = require('gulp-uglify');
var hash = require('gulp-hash-filename');

gulp.task('build', function () {
  runSequence('css', 'js');
});

gulp.task('css', function () {
  var processors = [
    autoprefixer({browsers: ['last 4 version']}),
    mqpacker,
    csswring
  ];
  return gulp
    .src('src/main/resources/public/css/src/*.css')
    .pipe(postcss(processors))
    .pipe(minify())
    .pipe(concat('style.css'))
    .pipe(hash())
    .pipe(gulp.dest('src/main/resources/public/css/gulp'));
});

gulp.task('js', function () {
  return gulp
    .src('src/main/resources/public/js/src/*.js')
    .pipe(uglify())
    .pipe(concat('script.js'))
    .pipe(hash())
    .pipe(gulp.dest('src/main/resources/public/js/gulp'));
});

gulp.task('browser-sync', function () {

  browserSync.init({
    server: {
      proxy: 'http://localhost:9000',
      files: 'src/main/resources/public/**/*'
    }
  });
});
