/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import ws.Monitor;
import ws.Solicitacao;

/**
 *
 * @author Gustavo
 */
public class DataBase {

    //define o caminho onde esta localizado o banco
    private final String db = "jdbc:sqlite:C:\\Users\\Gustavo\\Documents\\NetBeansProjects\\Middleware-servidor\\middlewareServer.db";

    public DataBase() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(db);

            //cria tabela de solicitações no banco
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS solicitacoes ("
                    + " id          INTEGER     PRIMARY KEY   AUTOINCREMENT,"
                    + " cliente     TEXT        NOT NULL,"
                    + " acao        TEXT        NOT NULL,"
                    + " empresa     TEXT        NOT NULL,"
                    + " quantidade  INT         NOT NULL,"
                    + " tempo       TEXT        NOT NULL,"
                    + " preco       DOUBLE      NOT NULL);";

            stmt.executeUpdate(sql);          
            
            //cria tabela de monitoramento no banco
            sql = "CREATE TABLE IF NOT EXISTS monitorando ("
                    + " id            INTEGER     PRIMARY KEY   AUTOINCREMENT,"
                    + " cliente       TEXT        NOT NULL,"
                    + " empresas      TEXT        NOT NULL);";

            stmt.executeUpdate(sql);

            //cria tabela de empresas no banco
            sql = "CREATE TABLE IF NOT EXISTS empresas ("
                    + " id          INTEGER    PRIMARY KEY  AUTOINCREMENT,"
                    + " empresa     TEXT       NOT NULL);";

            stmt.executeUpdate(sql);

            stmt.close();
            c.close();

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        //System.out.println("Opened database successfully");
    }

    //cadastra uma nova solicitação de compra ou venda no banco
    public void inserirSolicitacao(String cliente, String acao, String empresa, long tempo, int quantidade, double preco) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(db);
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");
            
            stmt = c.createStatement();
            String sql = "INSERT INTO solicitacoes (empresa,quantidade,preco,cliente,acao,tempo) "
                    + "VALUES ('" + empresa + "','" + quantidade + "','" + preco + "','" + cliente + "','" + acao + "','" + tempo + "');";
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Solicitação de " + acao + " da empresa " + empresa + " cadastrada! Cliente: " + cliente);
    }

    //cadastra uma nova empresa no banco
    public void inserirEmpresa(String empresa) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(db);
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "INSERT INTO empresas (empresa) VALUES ('" + empresa + "');";
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println(empresa + " cadastrada!");
    }
    
    //cadastra uma nova empresa no banco
    public void inserirMonitoramento(String cliente, String empresa) {
        Connection c = null;
        Statement stmt = null;
        String sql;
        String empresas = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(db);
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM monitorando WHERE cliente = '" + cliente + "';");
            if(rs.next()){
                int id = rs.getInt("id");
                empresas = rs.getString("empresas");
                empresas = empresas + ";" + empresa;
                sql = "UPDATE monitorando SET cliente = '" + cliente + "', empresas = '" + empresas + "' WHERE id = '" + id + "';";
            }else{
                empresas = empresa;
                sql = "INSERT INTO monitorando (cliente, empresas) VALUES ('" + cliente + "','" + empresas + "');";
            }
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println(cliente + " está monitorando " + empresas);
    }

    //recupera as solicitações de compra que foram inseridas no banco
    public ArrayList<Solicitacao> getSolicitacoesDeCompra() {
        ArrayList<Solicitacao> solicitacoes = new ArrayList<>();

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(db);
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM solicitacoes WHERE acao = 'compra';");
            while (rs.next()) {                
                String cliente = rs.getString("cliente");
                String empresa = rs.getString("empresa");
                int quantidade = rs.getInt("quantidade");
                int id = rs.getInt("id");
                long tempo = Long.parseLong(rs.getString("tempo"));
                double preco = rs.getDouble("preco");
                Solicitacao sol = new Solicitacao(id, cliente, empresa, quantidade, tempo, preco);
                solicitacoes.add(sol);                
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        //System.out.println("Operation done successfully");
                
        return solicitacoes;
    }
    
    //recupera todas as solicitacoes de uma empresa especifica
    public ArrayList<Object> getStatusEmpresa(String empresa) {
        ArrayList<Object> status = new ArrayList<>();

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(db);
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM solicitacoes WHERE empresa = '" + empresa + "';");
            while (rs.next()) { 
                String interesse = rs.getString("acao");
                int quantidade = rs.getInt("quantidade");
                double preco = rs.getDouble("preco");
                int id = rs.getInt("id");
                Object [] obj = {id,empresa,interesse,quantidade, preco};
                status.add(obj);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        //System.out.println("Operation done successfully");
                
        return status;
    }

    //recupera as solicitações de venda que foram inseridas no banco
    public ArrayList<Solicitacao> getSolicitacoesDeVenda() {
        ArrayList<Solicitacao> solicitacoes = new ArrayList<>();

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(db);
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM solicitacoes WHERE acao = 'venda';");
            while (rs.next()) {                
                String cliente = rs.getString("cliente");
                String empresa = rs.getString("empresa");
                int quantidade = rs.getInt("quantidade");
                long tempo = Long.parseLong(rs.getString("tempo"));
                double preco = rs.getDouble("preco");
                int id = rs.getInt("id");
                Solicitacao sol = new Solicitacao(id, cliente, empresa, quantidade, tempo, preco);
                solicitacoes.add(sol);                
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        //System.out.println("Operation done successfully");

        return solicitacoes;
    }

    ////recupera as empresas que foram cadastradas no banco
    public ArrayList<String> getEmpresas() {
        ArrayList<String> empresas = new ArrayList<>();

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(db);
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM empresas;");
            while (rs.next()) {                                
                String empresa = rs.getString("empresa");                
                empresas.add(empresa);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        //System.out.println("Operation done successfully");

        return empresas;
    }

    //recupera as solicitações de monitoramento que foram inseridas no banco
    public ArrayList<Monitor> getMonitoramento() {
        ArrayList<Monitor> monitoramento = new ArrayList<>();

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(db);
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM monitorando;");
            while (rs.next()) {   
                ArrayList<String> emps = new ArrayList<>();
                String cliente = rs.getString("cliente");
                String empresas = rs.getString("empresas");
                String[] emp = empresas.split(";");
                for(String str : emp){
                    emps.add(str);
                }
                Monitor monitor = new Monitor(cliente,emps);
                monitoramento.add(monitor);                
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        //System.out.println("Operation done successfully");

        return monitoramento;
    }
    
    //exclui uma solicitação que foi cadastrada no banco
    public void excluirSolicitacao(int id){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(db);
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();
            stmt.executeUpdate("DELETE FROM solicitacoes WHERE id = '" + id + "';");
            c.commit();            
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    //verificar se a solicitação já ultrapassou o tempo que foi programado para 
    // ela e caso tenha expirado o tempo remove a solicitação
    public void removerSolicitacoesExpiradas() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(db);
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM solicitacoes;");
            while (rs.next()) {   
                int id = rs.getInt("id");
                long tempo = Long.parseLong(rs.getString("tempo"));
                //System.out.println(tempo + " -> " + System.currentTimeMillis());
                if(tempo <= System.currentTimeMillis()){                    
                    Statement stmt2 = c.createStatement();
                    stmt2.executeUpdate("DELETE FROM solicitacoes WHERE id = '" + id + "';");
                    c.commit();
                    //System.out.println("Removido solicitacao");
                }
                 
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        //System.out.println("Operation done successfully");
    }
    
    //Limpa o banco de dados para uma nova execucao
    public void clear() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(db);
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();
            stmt.executeUpdate("DELETE FROM monitorando;");
            c.commit();            
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

}
