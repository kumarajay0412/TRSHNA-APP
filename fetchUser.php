<?php
    $con = mysqli_connect("localhost", "id11495159_pis_tishna", "Internet_22", "id11495159_tishna");
    
    $username = $_POST["username"];
    $password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM water WHERE Username = ? AND Password = ?");
    mysqli_stmt_bind_param($statement, "ss", $username, $password);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $name, $age, $username, $password,$DailyLimit);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;  
        $response["name"] = $name;
        $response["age"] = $age;
        $response["username"] = $username;
        $response["password"] = $password;
        $response["daily_limit"] = $DailyLimit;
        
    }
    
    echo json_encode($response);
?>