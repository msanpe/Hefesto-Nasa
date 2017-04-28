var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var request = require('request');

app.use(bodyParser.json());

app.get('/weather', function (req, res) {

	var lat = 44.77216, long = -0.49506;

	request(`http://api.wunderground.com/api/873bb568da693b57/conditions/q/${lat},${long}.json`, function (error, response, body) {

		var wind_kph;
		var relative_humidity;
		var wind_degrees;
		var wind_dir;

		if(body.current_observation) {

			console.log(`Curent current_observation: ${body.current_observation}`);

			if(body.current_observation.wind_kph)
				wind_kph = body.current_observation.wind_kph;

			if(body.current_observation.wind_kph)
				relative_humidity = body.current_observation.relative_humidity;

			if(body.current_observation.wind_kph)
				wind_degrees = body.current_observation.wind_degrees;

			if(body.current_observation.wind_kph)
				wind_dir = body.current_observation.wind_dir;
		}

		// console.log(`\n\nResponse: ${body}\n\n`);

		var respuesta = {
			velocidad_viento: wind_kph,
			humedad_relativa: relative_humidity,
			angulo_viento: wind_degrees,
			direccion_viento: wind_dir
		};

		res.send(respuesta);
	});

	// var user = { name: 'Raúl', apellido: 'Pera'};
	// res.send(user);
});

app.listen(3000, function () {
  console.log('Example app listening on port 3000!');
});




// "wind_kph", "relative_humidity", "wind_degrees", "wind_dir"