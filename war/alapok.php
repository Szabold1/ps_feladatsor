<?php

$arr = [3, 7, 1, 9, 5, 65, 12, 99, 61, 8, 7, 129, 72, 333, 42];

// min - max
$minValue = min($arr);
$maxValue = max($arr);

print_r($arr);
echo "A legkisebb érték: $minValue<br>";
echo "A legnagyobb érték: $maxValue<br>";
echo "<br>";

// searching
$toSearch = 99;
if ($res = array_search($toSearch, $arr)) {
    echo "{$toSearch} indexe: {$res}<br>";
} else {
    echo "{$toSearch} nem található a tömbben<br>";
}
echo "<br>";

// find x number of largest elements
function findXLargest($arr, $x)
{
    rsort($arr);
    return array_slice($arr, 0, $x);
}

$x = 5;
$largest = findXLargest($arr, $x);

echo "A legnagyobb $x elem:<br>";
echo "<pre>";
print_r($largest);
echo "</pre>";
echo "<br>";

// factorial
function factorial($n)
{
    if ($n == 0 || $n == 1) {
        return 1;
    }
    return $n * factorial($n - 1);
}

$n = 4;
echo "A(z) $n faktoriálisa: " . factorial($n) . "<br>";
echo "<br>";

// Euclidean Sequence - Greatest Common Divisor
function greatestCommonDivisor($a, $b)
{
    $num = $a < $b ? $a : $b;
    while ($num > 0) {
        if ($a % $num == 0 && $b % $num == 0) {
            return $num;
        }
        $num--;
    }
}

$a = 128;
$b = 64;
echo "Legnagyobb közös osztó: {$a}, {$b} ==> " . greatestCommonDivisor($a, $b) . "<br>";
echo "<br>";

// search in 2D array

function searchIn2DArr($arr, $toSearch)
{
    foreach ($arr as $index => $value) {
        $res = array_search($toSearch, $value);
        if ($res !== false) {
            return [$index, $res];
        }
    }

    return false;
}

$arr2D = [
    [11, 2, 97],
    [43, 75, 6],
    [7, 8, 91]
];
$searchedValue = 6;

echo "<pre>";
print_r($arr2D);
echo "</pre>";

if ($res = searchIn2DArr($arr2D, $searchedValue)) {
    echo "{$searchedValue} megtalálható a tömbben, indexe: [{$res[0]}, {$res[1]}]<br>";
} else {
    echo "{$searchedValue} nem található a tömbben<br>";
}
echo "<br>";

// sort 2d array by column
$newArr = [
    ["name" => "John", "age" => 50],
    ["name" => "Doe", "age" => 30],
    ["name" => "Jane", "age" => 40],
    ["name"=> "Jack", "age" => 20]
];

uasort($newArr, function ($a, $b) {
    return $a["age"] - $b["age"];
});

echo "Tömb rendezve életkor szerint:<br>";
echo "<pre>";
print_r($newArr);
echo "</pre>";
echo "<br>";

// json
$json = json_encode($newArr);

echo "Tömb JSON formátumban:<br>";
print_r($json);
echo "<br><br>";

$json = json_decode($json, true);

echo "JSON visszaalakítva tömbbé:<br>";
echo "<pre>";
print_r($json);
echo "</pre>";
echo "<br>";