<?php

header('Content-Type: application/json');

$oneYearInSeconds = 365 * 24 * 60 * 60;

function generateRandomName() {
    $namePartOne = ["Al", "Be", "Cha", "Da", "El", "Fi", "Go", "Ha"];
    $namePartTwo = ["bar", "car", "dar", "far", "gar", "har", "jar", "kar"];
    
    return $namePartOne[array_rand($namePartOne)] . $namePartTwo[array_rand($namePartTwo)];
}

$data = [];
for ($i = 0; $i < 20; $i++) {
    $name = generateRandomName() . " " . generateRandomName();
    $nickname = substr($name, 0, 5);
    $age = $i + 10;
    $birthdate = date('Y-m-d', time() - $age * $oneYearInSeconds);
    $favNum = rand(1, 100);

    $data[] = [
        "name" => $name,
        "nickname" => $nickname,
        "birthdate" => $birthdate,
        "age" => $age,
        "favNum" => $favNum
    ];
}

echo json_encode($data, JSON_PRETTY_PRINT);