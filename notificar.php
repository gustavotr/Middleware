<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
if($_POST != null){
    foreach ($_POST as $key => $value) {
        $value = filter_input(INPUT_POST, $key);
        $post[$key] = $value;        
    }
    
    if($post['notificacao'] == "compra efetuada"){
        
    }elseif($post['notificacao'] == "venda efetuada"){
        
    }elseif($post['notificacao'] == "monitor"){
        
    }
}
