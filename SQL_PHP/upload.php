<?php

	// Recibimos la imagen y el nombre
	if ($_SERVER['REQUEST_METHOD'] == 'POST') {
		
		$image = $_POST['photo'];
		$name = $_POST['name'];
	}

	// Ruta donde se guardarán las imágenes
	$path = "profile/$name.png";

	// Ruta completa
	$actualPath = "https://hogarruralapp.000webhostapp.com/hogarRural/img/$path";

	// Crear la imagen enviada
	file_put_contents($path, base64_decode($image));

	echo'SE SUBIÓ LA IMAGEN CON ÉXITO';

?>