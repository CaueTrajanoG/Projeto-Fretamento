package model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * POB - Persistencia de Objetos
 * Prof. Fausto Ayres
 *
 */

@Entity
@Table(name="motorista20251234567")
public class Motorista {
    @Id
    private String cnh;
    private String nome;

    @OneToMany(mappedBy="motorista", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Viagem> listaViagem = new ArrayList<>();

    
    public Motorista() {}

    public Motorista(String cnh, String nome) {
        this.cnh = cnh;
        this.nome = nome;
    }

    
    public String getCnh() { return cnh; }
    public void setCnh(String cnh) { this.cnh = cnh; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public List<Viagem> getListaViagem() { return listaViagem; }
    public void setListaViagem(List<Viagem> listaViagem) { this.listaViagem = listaViagem; }

    public String itinerario(){
        String iti = "";
        for (Viagem v : listaViagem){
            iti += "\n"+v + " ,";
        }
        return iti;
    }

    @Override
    public String toString() {
        String historico  = itinerario();
        return "--------------------------------------\n Motorista CNH = " + cnh + ", nome = " + nome + historico;
    }
}