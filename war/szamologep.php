<?php

if (isset($_GET['num1']) && isset($_GET['num2'])) {
    if (isset($_GET['operation'])) {
        
    }

    $num1 = $_GET['num1'];
    $num2 = $_GET['num2'];
    $sum = intval($num1) + intval($num2);
    print_r($sum);
    exit;
}