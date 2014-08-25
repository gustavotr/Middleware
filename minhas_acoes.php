<table>
    <thead>
        <tr><th>Monitorando</th></tr>
        <tr><th>Empresa</th><th>Interesse</th><th>Quantidade</th><th>Valor</th></tr>
    </thead>
    <tbody>
        <?php
        require_once "DataBase.php";
        $db = new DataBase();
        $sql = "SELECT * FROM acoes";
        $ret = $db->query($sql);

        if (!$ret) {
            sleep(1);
            $ret = $db->exec($sql);
            if (!$ret) {
                echo $db->lastErrorMsg();
            }
        } else {
            while ($row = $ret->fetchArray(SQLITE3_ASSOC)) {
                echo "<tr><td>{$row['empresa']}</td><td>{$row['quantidade']}</td><td>{$row['preco']}</td></tr>";
            }
        }
        $db->close();
        ?>   
    </tbody>
</table>