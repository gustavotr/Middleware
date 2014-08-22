<?php
    ini_set("soap.wsdl_cache_enabled", "0"); // disabling WSDL cache
    $client = new SoapClient("http://localhost/middlewareCliente/soap/inventory.wsdl");
    $return = $client->notificar('Olha isso');
    print_r($return);
?>