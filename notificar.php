<?php
require "DataBase.php";

$db = new DataBase();

if ($_POST != null) {
    foreach ($_POST as $key => $value) {
        $value = filter_input(INPUT_POST, $key);
        $post[$key] = $value;
    }

    if ($post['notificacao'] == "compra_efetuada") {
        $quantidade = $post['quantidade'];
        $preco = $post['preco'];
        $empresa = $post['empresa'];

        $sql = "UPDATE acoes SET quantidade = quantidade + $quantidade, preco = '$preco' WHERE empresa = '$empresa';";
        $ret = $db->exec($sql);

        if (!$ret) {
            echo $db->lastErrorMsg();
        } else {
        }


        $sql = "INSERT INTO notificacoes (texto) VALUES ('Comprado $quantidade ações da $empresa por R$$preco')";
        $ret = $db->exec($sql);

        if (!$ret) {
            echo $db->lastErrorMsg();
        } else {
            echo "Notificacao de compra bem sucedida!";
        }
    } elseif ($post['notificacao'] == "venda_efetuada") {
        $quantidade = $post['quantidade'];
        $preco = $post['preco'];
        $empresa = $post['empresa'];
        
        $sql = "UPDATE acoes SET quantidade = quantidade - $quantidade, preco = '$preco' WHERE empresa = '$empresa';";
        $ret = $db->exec($sql);

        if (!$ret) {
            echo $db->lastErrorMsg();
        } else {
            
        }


        $sql = "INSERT INTO notificacoes (texto) VALUES ('Vendido $quantidade ações da $empresa por R$$preco')";
        $ret = $db->exec($sql);

        if (!$ret) {
            echo $db->lastErrorMsg();
        } else {
            echo "Notificacao de venda bem sucedida!";
        }
    } elseif ($post['notificacao'] == "monitorar") {
        $notificacao = $post['json'];
        $json = json_decode($notificacao,true);
        $sql = "DELETE FROM monitorando;";
        for($i = 0; $i < count($json); $i++){
            $row = $json["row".$i];
            $empresa = $row[1];
            $interesse = $row[2];
            $quantidade = $row[3];
            $preco = $row[4];
            
            $sql = $sql . "INSERT INTO monitorando (empresa, interesse, quantidade, preco) VALUES ('$empresa','$interesse','$quantidade','$preco');";
            
        }              
        
        $ret = $db->exec($sql);

        if (!$ret) {
            sleep(1);
            $ret = $db->exec($sql);
            if (!$ret) {
                echo $db->lastErrorMsg();
            }
        } else {
            echo $notificacao;
        }
    }
}
