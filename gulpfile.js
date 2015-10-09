var gulp = require('gulp');
var postcss = require('gulp-postcss');
var autoprefixer = require('autoprefixer');
var mqpacker = require('css-mqpacker');
var csswring = require('csswring');
var concat = require('gulp-concat');
var runSequence = require('run-sequence');
var minify = require('gulp-minify-css');
var uglify = require('gulp-uglify');
var imagemin = require('imagemin-pngquant');
var assets = require('postcss-assets');
var yaml = require('js-yaml');
var fs = require('fs');

function readAssetsVersion(name) {
  var version = '';

  try {
    var doc = yaml.safeLoad(fs.readFileSync('target/classes/config/application-' + name + '.yml'));
    version = doc.assets.version;
  } catch (e) {
    console.log(JSON.stringify(e));
    console.error('Fatal error. Before running scripts packaging package spring application');
    version = '';
  }

  return version;
}

gulp.task('build-dev', function () {
  runSequence('css-dev', 'js-dev');
});

gulp.task('build-staging', function () {
  runSequence('css-staging', 'js-staging');
});

gulp.task('css-dev', function () {
  var processors = [
    autoprefixer({browsers: ['last 4 version']}),
    assets({loadPaths: ['src/main/resources/public/img/gulp/']}),
    mqpacker,
    csswring
  ];

  return gulp
    .src('src/main/resources/public/css/src/*.css')
    .pipe(postcss(processors))
    .pipe(minify())
    .pipe(concat('style.css'))
    .pipe(gulp.dest('src/main/resources/public/css/gulp'));
});

gulp.task('css-staging', function () {
  var processors = [
    autoprefixer({browsers: ['last 4 version']}),
    assets({loadPaths: ['src/main/resources/public/img/gulp/']}),
    mqpacker,
    csswring
  ];
  var version = readAssetsVersion('staging');

  return gulp
    .src('src/main/resources/public/css/src/*.css')
    .pipe(postcss(processors))
    .pipe(minify())
    .pipe(concat('style' + version + '.css'))
    .pipe(gulp.dest('src/main/resources/public/css/gulp'));
});

gulp.task('js-dev', function () {
  return gulp
    .src([
      'src/main/resources/public/js/libs/*.js',
      'src/main/resources/public/js/mixins/data/*.js',
      'src/main/resources/public/js/mixins/ui/*.js',
      'src/main/resources/public/js/components/*.js',
      'src/main/resources/public/js/init/*.js'
    ])
    .pipe(uglify())
    .pipe(concat('script.js'))
    .pipe(gulp.dest('src/main/resources/public/js/gulp'));
});

gulp.task('js-staging', function () {
  var version = readAssetsVersion('staging');

  return gulp
    .src([
      'src/main/resources/public/js/libs/*.js',
      'src/main/resources/public/js/mixins/data/*.js',
      'src/main/resources/public/js/mixins/ui/*.js',
      'src/main/resources/public/js/components/*.js',
      'src/main/resources/public/js/init/*.js'
    ])
    .pipe(uglify())
    .pipe(concat('script' + version + '.js'))
    .pipe(gulp.dest('src/main/resources/public/js/gulp'));
});

gulp.task('imgmin', function () {
  return gulp
    .src('src/main/resources/public/img/*.png')
    .pipe(imagemin({quality: '65-80', speed: 3})())
    .pipe(gulp.dest('src/main/resources/public/img/gulp'));
});
