/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ws;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author Gustavo
 */
@WebService(serviceName = "Cliente")
@Stateless()
public class Cliente {

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "notificar")
    public String notificar(@WebParam(name = "texto") String texto) {
        //TODO write your implementation code here:
        return null;
    }

    
}
