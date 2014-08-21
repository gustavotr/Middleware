<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <meta charset="UTF-8">
        <title></title>
    </head>
    <body>
        <?php
            $client = new SoapClient("http://localhost:8081/Middleware/Middleware?WSDL");
            $something =  $client->listarEmpresas();
            echo ($something);
        ?>
    </body>
</html>
