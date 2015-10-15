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
var i18n = require("i18next");
i18n.init({ lng: 'en-US' });

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

gulp.task('build', function () {
  runSequence('css-dev', 'js');
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
    .pipe(concat('bundle.css'))
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
    .pipe(concat('bundle' + version + '.css'))
    .pipe(gulp.dest('src/main/resources/public/css/gulp'));
});

gulp.task('js', function () {
  var version = readAssetsVersion('develop');
  return gulp
    .src('src/main/resources/public/js/src/*.js')
    .pipe(uglify())
    .pipe(concat('bundle' + version + '.js'))
    .pipe(gulp.dest('src/main/resources/public/js/gulp'));
});

gulp.task('imgmin', function () {
  return gulp
    .src('src/main/resources/public/img/*.png')
    .pipe(imagemin({quality: '65-80', speed: 3})())
    .pipe(gulp.dest('src/main/resources/public/img/gulp'));
});

gulp.task('i18next', function()
{
    gulp.src('app/**')
        .pipe(i18next({
            locales : ['en-US','ru'],
            output  : '../locales'
        }))
        .pipe(gulp.dest('locales'));
});