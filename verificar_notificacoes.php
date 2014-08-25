<?php

require_once "DataBase.php";

$db = new DataBase();

$sql = "SELECT * FROM notificacoes";
$ret = $db->query($sql);

while ($row = $ret->fetchArray(SQLITE3_ASSOC)) {
    echo "<script language=\"javascript\" type=\"text/javascript\">
                    alert('" . $row['texto'] . "');
                </script>";
}

$sql = "DELETE FROM notificacoes";
$ret = $db->query($sql);

$db->close();