<?php

require_once 'DB.php';
include_once 'config-db.php';
$db = DB::Connect(DB_DSN);
$db->setFetchMode(DB_FETCHMODE_ASSOC);
$db->query("SET NAMES utf8");
setlocale(LC_ALL, 'hu_HU.UTF-8');

$method = $_SERVER['REQUEST_METHOD'];

// GET request
if ($method === "GET") {
    $location = strtolower($_GET['location']); // "grid1", "grid2"

    file_put_contents('debug.txt', $location);

    $queryData = [$location];
    $data = $db->getAll("SELECT * FROM drag_drop_test WHERE location = ?", $queryData);

    echo json_encode(["data" => $data]);

    exit;
}

// POST request
if ($method === "POST") {
    $name = $_POST['name'];
    $newLocation = $_POST['newLocation'];

    $queryData = [$newLocation, $name];
    $db->query("UPDATE drag_drop_test SET location = ? WHERE name = ?", $queryData);

    echo json_encode(["success" => true]);

    exit;
}
