<?php
if($_GET["idHouse"]){
	include 'conexion.php';

	$sql = "SELECT h.name, h.rental, h.personMax, h.price, h.numOpinion, h.valoration, h.galleryImg, hd.features, hd.activities, hd.placesinterest, hd.services FROM houses h, housesdetails hd WHERE h.idHouse=hd.idHouse AND h.idHouse=".$_GET["idHouse"];
	$res = $con->query($sql);
	$rows = array();
		while($r = mysqli_fetch_assoc($res)){
		 $rows[] = $r;
		}

// Set del contenido de la respuesta
header('Content-Type: application/json; charset=utf8');

echo json_encode($rows, JSON_PRETTY_PRINT,JSON_UNESCAPED_UNICODE);

}


?>