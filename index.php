<?php
require "DataBase.php";
$empresas = array("Vivo", "Claro", "Tim", "GVT");
$client = new SoapClient("http://localhost:8081/Middleware/Middleware?WSDL");

$url = "http://$_SERVER[HTTP_HOST]$_SERVER[REQUEST_URI]";

if($_POST != null){
    foreach ($_POST as $key => $value) {
        $value = filter_input(INPUT_POST, $key);
        $post[$key] = $value;        
    }
    
    if($post['acao'] == "compra_ou_venda"){
        if($post['interesse'] == "comprar"){
            $res = $client->comprar(array('ciente' => $url,'empresa' => $post['empresa'],'quantidade'=>$post['quantidade'],'preco'=>$post['preco'],'tempo'=>$post['tempo']));
        }elseif($post['interesse'] == "vender"){
            $res = $client->vender(array('ciente' => $url,'empresa' => $post['empresa'],'quantidade'=>$post['quantidade'],'preco'=>$post['preco'],'tempo'=>$post['tempo']));
        }        
    }elseif($post['acao'] == "monitorar"){
        $res = $client->monitorar(array('ciente' => $url,'empresa' => $post['empresa']));        
    }
    
}

//cadastra empresa no Servidor
foreach ($empresas as $key => $value) {
    $res = $client->cadastrarEmpresa(array('empresa' => $value));
    //print_r($res);
}


$db = new DataBase();
$rows = $db->query("SELECT COUNT(*) as count FROM acoes");
$row = $rows->fetchArray();
$numRows = $row['count'];
if ($numRows == 0) {
    // zero rows
    for ($i = 0; $i < 4; $i++) {
        $quantidade = rand(1, 100);
        $preco = rand(1, 1000) / 100;
        $sql = "INSERT INTO acoes (empresa, quantidade, preco) VALUES ('$empresas[$i]','$quantidade','$preco')";
        $ret = $db->exec($sql);

        if (!$ret) {
            echo $db->lastErrorMsg();
        } else {
            //echo "Acao criada";
        }
    }
}
?>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <meta charset="UTF-8">
        <title>CLiente</title>
        <style>            
            #container { width: 800px; margin: auto;}
            table { margin-top: 20px; text-align: center; border-style: dotted; padding: 5px;}
            input, select {margin-top: 5px;}
            form { margin-top: 50px; border-style: dotted; padding: 5px;}
        </style>
    </head>
    <body>
        <div id="container">
            <table>
                <thead>
                    <tr><th>Minhas Açoes</th></tr>
                    <tr><th>Empresa</th><th>Quantidade</th><th>Valor</th></tr>
                </thead>
                <tbody>
                    <?php
                    $sql = "SELECT * FROM acoes";
                    $ret = $db->query($sql);

                    while ($row = $ret->fetchArray(SQLITE3_ASSOC)) {
                        echo "<tr><td>{$row['empresa']}</td><td>{$row['quantidade']}</td><td>{$row['preco']}</td></tr>";
                    }
                    ?>                   
                </tbody>
            </table>
            <table>
                <thead>
                    <tr><th>Açoes no Servidor</th></tr>                
                </thead>
                <tbody>
                    <?php
                    $obj = $client->listarEmpresas();
                    $return = $obj->return;
                    foreach ($return as $key => $value) {
                        echo "<tr><td>$value</td></tr>";
                    }
                    ?>
                </tbody>
            </table>
            <table>
                <thead>
                    <tr><th>Monitorando</th></tr>
                    <tr><th>Empresa</th><th>Interesse</th><th>Quantidade</th><th>Valor</th></tr>
                </thead>
                <tbody>
                    <?php
                    $sql = "SELECT * FROM monitorando";
                    $ret = $db->query($sql);

                    while ($row = $ret->fetchArray(SQLITE3_ASSOC)) {
                        echo "<tr><td>{$row['empresa']}</td><td>{$row['interesse']}</td><td>{$row['quantidade']}</td><td>{$row['preco']}</td></tr>";
                    }
                    ?>   
                </tbody>
            </table>
            <form method="post">
                <h3>Comprar ou Vender</h3>
                <input type="hidden" name="acao" value="compra_ou_venda"/>
                <label for="empresa">Empresa</label>
                <input id="empresa" name="empresa" type="text"/>
                <select name="interesse">
                    <option value="comprar">Comprar</option>
                    <option value="vender">Vender</option>
                </select>
                <label for="preco">Preço</label>
                <input id="preco" name="preco" type="text"/>
                <label for="quantidade">Quantidade</label>
                <input id="quantidade" name="quantidade" type="text"/></br>
                <label for="tempo">Tempo em segundos</label>
                <input id="tempo" name="tempo" type="text"/>
                <input type="submit" value="Enviar"/>
            </form>
            <form method="post">
                <h3>Monitorar</h3>
                <input type="hidden" name="acao" value="monitorar"/>
                <label for="empresa">Empresa</label>
                <input id="empresa" name="empresa" type="text"/>                
                <input type="submit" value="Monitorar"/>
            </form>
        </div>
        <?php
        $sql = "SELECT * FROM notificacoes";
        $ret = $db->query($sql);

        while ($row = $ret->fetchArray(SQLITE3_ASSOC)) {
            echo "<script language=\"javascript\" type=\"text/javascript\">
                    alert('".$row['texto']."');
                </script>";
        }     
        
        $db->close();
        ?>
    </body>
</html>
