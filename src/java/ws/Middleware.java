/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ws;

import db.DataBase;
import java.util.ArrayList;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author Gustavo
 */
@WebService(serviceName = "Middleware")
@Stateless()
public class Middleware{
    
    protected DataBase db;

    public Middleware() {
        db = new DataBase(); 
    }
          
       
    /**
     * Operação de Web service cadastra solicitação de compra
     */
    @WebMethod(operationName = "comprar")
    public String comprar(@WebParam(name = "cliente") String cliente, @WebParam(name = "empresa") String empresa, @WebParam(name = "quantidade") int quantidade, @WebParam(name = "preco") double preco, @WebParam(name = "tempo") int tempo) {        
        long time = System.currentTimeMillis() + tempo*1000;
        db.inserirSolicitacao(cliente, "compra", empresa, time, quantidade, preco);        
        return "Solicitação registrada";
    }
    
    /**
     * Operação de Web service cadastra solicitação de venda
     */
    @WebMethod(operationName = "vender")
    public String vender(@WebParam(name = "cliente") String cliente, @WebParam(name = "empresa") String empresa, @WebParam(name = "quantidade") int quantidade, @WebParam(name = "preco") double preco, @WebParam(name = "tempo") int tempo) {
        long time = System.currentTimeMillis() + tempo*1000;
        db.inserirSolicitacao(cliente, "venda", empresa, time, quantidade, preco);
         return "Solicitação registrada";
    }

    /**
     * Operação de Web service cadastra solicitação para monitorar
     */
    @WebMethod(operationName = "monitorar")
    public String monitorar(@WebParam(name = "cliente") String cliente, @WebParam(name = "empresa") String empresa) {
        db.inserirMonitoramento(cliente, empresa);
        return "Cadastrado monitoramento";
    }

    /**
     * Operação de Web service para listar as empresas cadastradas no servidor
     */
    @WebMethod(operationName = "listarEmpresas")
    public ArrayList<String> listarEmpresas() {
        ArrayList<String> empresas = db.getEmpresas();
        return empresas;
    }

    /**
     * Operação de Web service para cadastrar empresas no servidor
     */
    @WebMethod(operationName = "cadastrarEmpresa")
    public String cadastrarEmpresa(@WebParam(name = "empresa") String empresa) {
        ArrayList<String> empresas = db.getEmpresas();
        for (String emp : empresas) {
            if (emp.equals(empresa)) {
                return "Empresa já cadastrada";
            }
        }
        db.inserirEmpresa(empresa);        
        return empresa + " cadastrada";
    }
    
    /**
     * Procura por uma cliente na lista de monitoramento e retorna seu indice. Caso nao encontre retorna -1.
     * @param cliente
     * @param monitoramento
     * @return 
     */
    private int acharCliente(String cliente, ArrayList<Monitor> monitoramento) {
        for(int i = 0; i < monitoramento.size(); i++){
            Monitor m = monitoramento.get(i);
            if(m.getCliente().equals(cliente)){
                return i;
            }
        }
        return -1;
    }

}
