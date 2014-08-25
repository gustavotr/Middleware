/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

/**
 *
 * @author Casa
 */
public class Solicitacao {
    
    private String solicitante;
    private int id;
    private String nome;
    private long tempo;
    private int quantidade;
    private double preco;

    public Solicitacao(int id, String solicitante, String nome, int quantidade, long tempo, double preco) {
        this.solicitante = solicitante;
        this.id = id;
        this.nome = nome;
        this.tempo = tempo;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public String getNome() {
        return nome;
    }

    public long getTempo() {
        return tempo;
    }

    public double getPreco() {
        return preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }
    
    
}
