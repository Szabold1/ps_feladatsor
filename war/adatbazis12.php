<?php

require_once 'DB.php';
include_once 'config-db.php';
$db = DB::Connect(DB_DSN);
$db->setFetchMode(DB_FETCHMODE_ASSOC);
$db->query("SET NAMES utf8");
setlocale(LC_ALL, 'hu_HU.UTF-8');

$method = $_SERVER['REQUEST_METHOD'];

if ($method === "GET") {
    $allData = $db->getAll("SELECT * FROM grid_test");
    echo json_encode(["data" => $allData], JSON_PRETTY_PRINT);
    exit;
}

if ($method === "POST") {
    $name = $_POST['name'];
    $nickname = $_POST['nickname'];
    $email = $_POST['email'];
    $birthdate = $_POST['birthdate'];
    $favNum = $_POST['favNum'];

    // check if user exists in database
    $user = $db->getRow("SELECT * FROM grid_test WHERE email = ?", [$email]);

    // if user exists, update the data
    if ($user) {
        $queryData = [$name, $nickname, $birthdate, $favNum, $email];
        $db->query("UPDATE grid_test 
                    SET name = ?, nickname = ?, birthdate = ?, favourite_number = ? 
                    WHERE email = ?", $queryData);

        exit;
    }

    // if user does not exist, insert the data
    $queryData = [$name, $nickname, $email, $birthdate, $favNum];
    $db->query("INSERT INTO grid_test (name, nickname, email, birthdate, favourite_number)
                VALUES (?, ?, ?, ?, ?)", $queryData);
}
