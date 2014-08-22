<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

   class DataBase extends SQLite3
   {
      function __construct()
      {
         $this->open('middleware.db');
      }
   }
   $db = new DataBase();
   if(!$db){
      echo $db->lastErrorMsg();
   } else {
      //echo "Opened database successfully\n";
   }
   
   $sql =<<<EOF
      CREATE TABLE IF NOT EXISTS acoes (
           id           INTEGER     PRIMARY KEY   AUTOINCREMENT,
           empresa      TEXT        NOT NULL,
           quantidade   INT         NOT NULL,
           preco        DOUBLE      NOT NULL
        );
           
       CREATE TABLE IF NOT EXISTS monitorando (
           id           INTEGER     PRIMARY KEY   AUTOINCREMENT,
           empresa      TEXT        NOT NULL,
           interesse    TEXT        NOT NULL,
           quantidade   INT         NOT NULL,
           preco        DOUBLE      NOT NULL
        );
          
        CREATE TABLE IF NOT EXISTS notificacoes (
           id           INTEGER     PRIMARY KEY   AUTOINCREMENT,
           texto        TEXT        NOT NULL
        );           
EOF;

   $ret = $db->exec($sql);
   if(!$ret){
      echo $db->lastErrorMsg();
   } else {
      //echo "Table created successfully\n";
   }
   $db->close();
?>