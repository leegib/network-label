module.exports = {
  entry: {
    "index": "./js/index.js"
  },
  output: {
    filename: "app/assets/js/[name]/entry.js"
  },
  module: {
    loaders: [{
      test: /\.js$/, loader: "babel-loader", exclude: /node_modules/, query: { presets: ["es2015"] }
    }]
  }
};