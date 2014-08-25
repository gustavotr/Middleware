/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 *
 * @author Gustavo
 */
public class Post {
    
    public ArrayList<String> newPost(String urlParameters, String address) throws MalformedURLException, IOException{                             
        ArrayList<String> retorno = new ArrayList<>();
        URL url = new URL(address);
        URLConnection conn = url.openConnection();

        conn.setDoOutput(true);

        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

        writer.write(urlParameters);
        writer.flush();

        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                
        while ((line = reader.readLine()) != null) {
            retorno.add(line);
        }
        writer.close();
        reader.close(); 
        
        return retorno;
    }
    
}
