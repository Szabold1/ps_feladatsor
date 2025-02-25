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
    // save the query parameters
    $sortField = isset($_GET['sortField']) ? $_GET['sortField'] : null; // field to sort by, e.g. name
    strcmp($sortField, 'favNum') === 0 ? $sortField = 'favourite_number' : null;
    $sortDir = isset($_GET['sortDir']) ? $_GET['sortDir'] : "NONE"; // ASC, DESC, NONE
    $limit = isset($_GET['limit']) ? intval($_GET['limit']) : 5; // number of rows to return
    $offset = isset($_GET['offset']) ? intval($_GET['offset']) : 0; // starting row
    $filter = isset($_GET['filter']) ? $_GET['filter'] : ""; // filter string, e.g. "name:John,nickname:Johnny"

    // puzzle together the query
    $queryBegin = "SELECT * FROM grid_test";
    $filtersQuery = buildUpFiltersQuery(splitUpFilter($filter));
    $queryFilter = $filtersQuery === "" ? "" : "WHERE $filtersQuery";
    $querySort = !isValidSort($sortField, $sortDir) ? "" : "ORDER BY $sortField $sortDir";
    $queryEnd = "LIMIT ? OFFSET ?";
    $query = "$queryBegin $queryFilter $querySort $queryEnd";

    // execute the query
    $queryData = [$limit, $offset];
    $data = $db->getAll($query, $queryData);

    $totalCount = $db->getOne("SELECT COUNT(*) FROM grid_test $queryFilter");

    $dataToSend = [
        "totalCount" => intval($totalCount),
        "data" => $data
    ];

    echo json_encode($dataToSend);

    exit;
}

// POST request
if ($method === "POST" && strcmp(strtolower($_POST['action']), "post") === 0) {
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

    exit;
}

// DELETE request
if ($method === "POST" && strcmp(strtolower($_POST['action']), "delete") === 0) {
    $email = $_POST['email'];
    $birthdate = $_POST['birthdate'];

    $queryData = [$email, $birthdate];

    // check if user exists in database before deleting
    $user = $db->getRow("SELECT * FROM grid_test WHERE email = ? AND birthdate = ?", $queryData);
    if ($user) {
        $db->query("DELETE FROM grid_test WHERE email = ? AND birthdate = ?", $queryData);
    }

    exit;
}


// ----- Helper functions ------------------------------------------------------------ //
// ----------------------------------------------------------------------------------- //

function isValidSort($sortField, $sortDir)
{
    $validSortFields = ['name', 'nickname', 'email', 'birthdate', 'favourite_number'];
    $validSortDirs = ['ASC', 'DESC'];

    return in_array($sortField, $validSortFields) && in_array($sortDir, $validSortDirs);
}

// $filter will look like this: "name:John,birthdate:before=2020-01-01"
// return an associative array with the filters, e.g. ["name" => "John", "birthdate" => "before=2020-01-01"]
function splitUpFilter($filter)
{
    if (strlen($filter) === 0) {
        return [];
    }

    $filterPairs = explode(",", $filter);
    $filters = [];
    foreach ($filterPairs as $filterPair) {
        list($key, $value) = explode(":", $filterPair, 2);
        strcmp($key, 'favNum') === 0 ? $key = 'favourite_number' : null;
        $filters[$key] = $value;
    }

    return $filters;
}

// $filters will look like this: ["name" => "John", birthdate => "before=2020-01-01"]
// return a string that can be used in a SQL query, e.g. "name LIKE '%John%' AND birthdate < '2020-01-01'"
function buildUpFiltersQuery($filters)
{
    if (count($filters) === 0) {
        return "";
    }

    $queryParts = [];

    foreach ($filters as $key => $value) {
        // if the value contains an equal sign, we have a comparison
        if (strpos($value, "=") !== false) {
            $comparisons = explode("_", $value);
            $comparisons[0] = substr($comparisons[0], strpos($comparisons[0], ":"));

            foreach ($comparisons as $comparison) {
                list($comp, $actualValue) = explode("=", $comparison);

                switch ($comp) {
                    case "before":
                    case "lt":
                        $queryParts[] = "$key < '$actualValue'";
                        break;
                    case "after":
                    case "gt":
                        $queryParts[] = "$key > '$actualValue'";
                        break;
                    case 'on':
                    case 'eq':
                        $queryParts[] = "$key = '$actualValue'";
                        break;
                    default:
                        $queryParts[] = "$key LIKE '%$value%'";
                        break;
                }
            }
        } else {
            $queryParts[] = "$key LIKE '%$value%'";
        }
    }

    $query = implode(" AND ", $queryParts);

    return $query;
}
