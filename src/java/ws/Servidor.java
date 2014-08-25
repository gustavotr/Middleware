/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import db.DataBase;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author a1097075
 */
public class Servidor implements Runnable {

    private ArrayList<Solicitacao> solicitacoesCompra;
    private ArrayList<Solicitacao> solicitacoesVenda;
    private ArrayList<String> empresas;
    private ArrayList<Monitor> monitoramento;
    private JTable mercadoDeVendas;
    private JTable mercadoDeCompras;
    private DataBase db;

    public static void main(String[] args) {
        new Servidor();
    }

    public Servidor() {
        db = new DataBase();
        db.clear();
        initComponents();
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                //System.out.print(".");
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }

            //verifica as solicitações cadastradas no banco e remove as que
            // estão com tempo expirado
            db.removerSolicitacoesExpiradas();
            //recupera as solicitações de compra cadastradas
            solicitacoesCompra = db.getSolicitacoesDeCompra();
            //recupera as solicitações de venda cadastradas
            solicitacoesVenda = db.getSolicitacoesDeVenda();
            //recupera as empresas cadastradas
            empresas = db.getEmpresas();
            //recupera as solicitações de monitoramento cadastradas
            monitoramento = db.getMonitoramento();

            //monta uma tabela para exibir as solicitações de compra
            DefaultTableModel model = (DefaultTableModel) mercadoDeCompras.getModel();
            model.setNumRows(0);
            for (Solicitacao sol : solicitacoesCompra) {
                String empresa = sol.getNome();
                int quantidade = sol.getQuantidade();
                double preco = sol.getPreco();
                model.addRow(new Object[]{empresa, "Compra", quantidade, preco});
            }

            for (Monitor m : monitoramento) {
                try {
                    String notificacao = "monitorar";
                    String cliente = m.getCliente();
                    ArrayList<String> empresas = m.getEmpresas();
                    JSONObject json = new JSONObject();
                    int i = 0;
                    for (String emp : empresas) {
                        //System.out.println(emp);
                        ArrayList<Object> status = db.getStatusEmpresa(emp);
                        for (Object obj : status) {
                            //obj = {id, interessa, quantidade, preco}
                            Object[] obj2 = (Object[]) obj;
                            JSONArray array = new JSONArray(obj2);
                            json.put("row" + i, array);
                            i++;
                        }
                    }
                    String jsonString = json.toString();
                    if(!jsonString.equals("{}")){
                        Post post = new Post();
                        ArrayList<String> retorno = post.newPost("notificacao=monitorar&json=" + jsonString, cliente + "/notificar.php");
//                        for (String str : retorno) {
//                            System.out.println(str);
//                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            //monta uma tabela para exibir as solicitações de venda
            model = (DefaultTableModel) mercadoDeVendas.getModel();
            model.setNumRows(0);
            for (Solicitacao sol : solicitacoesVenda) {
                String empresa = sol.getNome();
                int quantidade = sol.getQuantidade();
                double preco = sol.getPreco();
                model.addRow(new Object[]{empresa, "Venda", quantidade, preco});
            }

            //verifica se existe uma solicitação de compra para cada solicitação
            // de venda cadastrada, caso exista uma solicitação de compra de uma
            // empresa que esteja com ação a venda e caso o valor de compra
            // for maior que o valor de venda, o servidor calcula o valor médio
            // e informa os donos das ações que as solicitações foram atendidas
            int auxCompra = 0;
            for (Solicitacao compra : solicitacoesCompra) {
                int auxVenda = 0;
                for (Solicitacao venda : solicitacoesVenda) {

                    String nomeAcaoCompra = compra.getNome();
                    String nomeAcaoVenda = venda.getNome();
                    double precoCompra = compra.getPreco();
                    double precoVenda = venda.getPreco();
                    if (nomeAcaoCompra.equals(nomeAcaoVenda) && precoCompra >= precoVenda) {
                        try {
                            System.out.println("Houve uma venda");
                            double valorMedio = (compra.getPreco() + venda.getPreco()) / 2;
                            String compraStr = "notificacao=compra_efetuada";
                            String obj = "&empresa=" + venda.getNome()
                                    + "&preco=" + valorMedio
                                    + "&quantidade=" + 1;
                            Post post = new Post();
                            ArrayList<String> retorno = post.newPost(compraStr + obj, compra.getSolicitante() + "/notificar.php");
                            for (String str : retorno) {
                                System.out.println(str);
                            }
                            //compra.getSolicitante().notificar(compraStr, obj);
                            String vendaStr = "notificacao=venda_efetuada";
                            //venda.getSolicitante().notificar(vendaStr, obj);
                            retorno = post.newPost(vendaStr + obj, venda.getSolicitante() + "/notificar.php");
                            for (String str : retorno) {
                                System.out.println(str);
                            }
                            System.out.println("Avisar Comprador");
                            solicitacoesCompra.remove(compra);
                            solicitacoesVenda.remove(venda);
                            DefaultTableModel modeloDeCompra = (DefaultTableModel) mercadoDeCompras.getModel();
                            modeloDeCompra.removeRow(auxCompra);
                            auxCompra++;
                            DefaultTableModel modeloDeVenda = (DefaultTableModel) mercadoDeVendas.getModel();
                            modeloDeVenda.removeRow(auxVenda);
                            auxVenda++;
                            return;
                        } catch (IOException ex) {
                            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
    }

    private void initComponents() {

        JFrame j = new JFrame("Servidor");
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.setBounds(0, 0, 800, 600);
        j.setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));

        String[] nomeDasColunas = {"Empresa", "Interesse", "Quantidade", "Valor"};
        TableModel model = new DefaultTableModel(nomeDasColunas, 0);

        mercadoDeVendas = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(mercadoDeVendas);
        mercadoDeVendas.setFillsViewportHeight(true);

        mercadoDeCompras = new JTable(new DefaultTableModel(nomeDasColunas, 0));
        JScrollPane scrollPane2 = new JScrollPane(mercadoDeCompras);
        mercadoDeCompras.setFillsViewportHeight(true);

        panel.add(scrollPane);
        panel.add(scrollPane2);

        j.add(panel);
        j.pack();

    }

    private Object[] getInfo(ArrayList empresas) {
        ArrayList<Object> transacoes = new ArrayList<>();

        for (Object empresa : empresas) {
            for (Solicitacao solicitacao : solicitacoesVenda) {
                if (solicitacao.getNome().equals(empresa)) {
                    Object[] obj = new Object[]{empresa, "Venda", solicitacao.getPreco(), solicitacao.getQuantidade()};
                    transacoes.add(obj);
                }
            }
            for (Solicitacao solicitacao : solicitacoesCompra) {
                if (solicitacao.getNome().equals(empresa)) {
                    Object[] obj = new Object[]{empresa, "Compra", solicitacao.getPreco(), solicitacao.getQuantidade()};
                    transacoes.add(obj);
                }
            }
        }

        Object[] trans = new Object[transacoes.size()];
        for (int i = 0; i < transacoes.size(); i++) {
            trans[i] = transacoes.get(i);
        }

        return trans;
    }

    /**
     * Procura por uma cliente na lista de monitoramento e retorna seu indice.
     * Caso nao encontre retorna -1.
     *
     * @param interfaceCliente
     * @param monitoramento
     * @return
     */
    private int acharCliente(String cliente, ArrayList<Monitor> monitoramento) {
        for (int i = 0; i < monitoramento.size(); i++) {
            Monitor m = monitoramento.get(i);
            if (m.getCliente().equals(cliente)) {
                return i;
            }
        }
        return -1;
    }
}
