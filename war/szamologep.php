<?php

$method = $_SERVER['REQUEST_METHOD'];

if ($method === "GET" && isset($_GET['num1']) && isset($_GET['num2']) && isset($_GET['operation'])) {
    $num1 = $_GET['num1'];
    $num2 = $_GET['num2'];
    $operation = $_GET['operation'];

    print_r(calculate($num1, $num2, $operation));
    exit;
}

if ($method === "POST" && isset($_POST['num1']) && isset($_POST['num2']) && isset($_POST['operation'])) {
    $num1 = $_POST['num1'];
    $num2 = $_POST['num2'];
    $operation = $_POST['operation'];

    print_r(calculate($num1, $num2, $operation));
    exit;
}

function calculate($num1, $num2, $operation)
{
    $operationPairs = [
        "" => $num1 + $num2,
        "+" => $num1 + $num2,
        "-" => $num1 - $num2,
        "*" => $num1 * $num2,
        "%" => $num2 == 0 ? "Division by zero" : $num1 / $num2,
    ];

    if (array_key_exists($operation, $operationPairs)) {
        return $operationPairs[$operation];
    } else {
        return "Invalid operation";
    }
}