/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ws;

import java.util.ArrayList;

/**
 *
 * @author Saranghae
 */
public class Monitor {
    
    private String cliente;
    private ArrayList<String> empresas;

    public Monitor(String cliente, ArrayList<String> empresas) {
        this.cliente = cliente;
        this.empresas = empresas;
    }

    public String getCliente() {
        return cliente;
    }

    public ArrayList<String> getEmpresas() {
        return empresas;
    }
}
