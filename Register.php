<?php
    $con=mysqli_connect("localhost","id11495159_pis_tishna","Internet_22","id11495159_tishna");
    
    $Name = $_POST["name"];
    $Age = $_POST["age"];
    $Username = $_POST["username"];
    $Password = $_POST["password"];
    $DailyLimit=$_POST["daily_limit"];

    
    $statement = mysqli_prepare($con, "INSERT INTO water (Name, Age, Username, Password,DailyLimit) VALUES (?, ?, ?,?,?)");
    
    mysqli_stmt_bind_param($statement, "sissi", $Name, $Age, $Username, $Password,$DailyLimit);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_close($statement);

    $response=array();
    $response["success"]=true;
    echo json_encode($response);
    
    mysqli_close($con);
?>