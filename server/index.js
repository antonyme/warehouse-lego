var express = require('express')

var app = express()

app.get('/notes', function(req, res) {
  res.json({notes: "A note!"})
})

app.listen(3000)
