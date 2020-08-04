<?php
//header("Content-Type: application/json");
include 'conexion.php';

$sql = "SELECT * FROM houses";
$res = $con->query($sql);

$rows = array();

	while($r = mysqli_fetch_assoc($res)){
	 $rows[] = $r;
	}

// Set del contenido de la respuesta
header('Content-Type: application/json; charset=utf8');

echo json_encode($rows, JSON_PRETTY_PRINT,JSON_UNESCAPED_UNICODE);

?>