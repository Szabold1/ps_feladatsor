<?php

// $db->getOne("SELECT name FROM teszt WHERE id = 1"); // gets one value
// $db->getAll("SELECT name FROM teszt"); // gets all values in array
// $db->getAssoc("SELECT id, name FROM teszt WHERE id = 1"); // gets all values in associative array
// $db->getCol("SELECT name FROM teszt"); // gets one column
// $db->getRow("SELECT * FROM teszt WHERE id = 1"); // gets one row
// $db->query("UPDATE teszt SET name = 'newName' WHERE id = 1"); // use for UPDATE, INSERT, DELETE

require_once 'DB.php';
include_once 'config-db.php';
$db = DB::Connect(DB_DSN);
$db->setFetchMode(DB_FETCHMODE_ASSOC);
$db->query("SET NAMES utf8");
setlocale(LC_ALL, 'hu_HU.UTF-8');

$allData = $db->getAll("SELECT * FROM grid_test");

echo json_encode(["data" => $allData], JSON_PRETTY_PRINT);