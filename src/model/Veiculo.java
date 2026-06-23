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
@Table(name="veiculo20251234567")
public class Veiculo {
    @Id
    private String placa;
    private int capacidade;

    @OneToMany(mappedBy="veiculo", cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Viagem> listaViagem = new ArrayList<>();

    public Veiculo() {}

    public Veiculo(String placa, int capacidade) {
        this.placa = placa;
        this.capacidade = capacidade;
    }

    // Getters e Setters
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public int getCapacidade() { return capacidade; }
    public void setCapacidade(int capacidade) { this.capacidade = capacidade; }

    public List<Viagem> getListaViagem() { return listaViagem; }
    public void setListaViagem(List<Viagem> listaViagem) { this.listaViagem = listaViagem; }

    public String history(){
        String iti = "";
        for (Viagem v : listaViagem){
            iti +="\n"+ v + " ,";
        }
        return iti;
    }
    @Override
    public String toString() {
        String historico  = history();
        return "Placa: " + placa + " - capacidade: " + capacidade;
    }

	public void remover(Viagem viagem) {
		this.listaViagem.remove(viagem);
	}
}