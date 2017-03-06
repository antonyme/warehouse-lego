var express = require('express')
var stormpath = require('express-stormpath')
var bodyParser = require('body-parser')
var app = express()

//DATA STRUCT
var OrderEntry = function(color, quantity) {
  this.color = color;
  this.quantity = quantity;
}
var ClientOrder = function(clientId) {
  this.clientId = clientId;
  this.toPrepare = [];
  this.prepared = [];

  this.getToPrepare = function() {
    return this.toPrepare;
  }
}
var StockEntry = function(id, color) {
  this.id = id;
  this.color = color;
}

//DATA
var inStock = []
inStock.push(new StockEntry("123456789", "blue"));
inStock.push(new StockEntry("123456790", "green"));
var clientOrders = []



//STARTUP
app.use(stormpath.init(app, {
  expand: {
    customData: true,
  },
  web: {
    produces: ['application/json']
  }
}))
app.use(bodyParser.json())
app.use(bodyParser.urlencoded({extended: false}))

//ROUTES
app.get('/stock', stormpath.apiAuthenticationRequired, function(req, res) {
  res.json({stock : inStock})
})

app.get('/clientOrders', stormpath.apiAuthenticationRequired, function(req, res) {
  res.json({clientOrders: clientOrders})
})

app.post('/newClientOrder', stormpath.apiAuthenticationRequired, function(req, res) {
  if(!req.body.clientOrder) {
    console.log(req.body);
    res.status(400).send("400 Bad Request")
  }
  else {
    clientOrders.push(req.body.clientOrder);
    res.status(200).end()
  }
})

app.listen(3000)
