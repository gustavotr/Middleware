<?php
function notificar($texto){
    $sql = "INSERT INTO notificacoes (texto) VALUES ('$texto')";
    $ret = $db->exec($sql);

    if (!$ret) {
        echo $db->lastErrorMsg();
    } else {
    }
    return $texto;
}

function notificarCompra($empresa, $preco, $quantidade){
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
    }
    return true;
}

function notificarVenda($empresa, $preco, $quantidade){
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
    }
    return true;
}