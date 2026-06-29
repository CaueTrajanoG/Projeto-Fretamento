package model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="motorista20251234567")
public class Motorista {
    @Id
    private String cnh;
    private String nome;
	private byte[] foto;

    @OneToMany(mappedBy="motorista", cascade=CascadeType.ALL, orphanRemoval=true,fetch = FetchType.EAGER)
    private List<Viagem> listaViagem = new ArrayList<>();

    
    public Motorista() {}

    public Motorista(String cnh, String nome) {
        this.cnh = cnh;
        this.nome = nome;
    }
    
	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
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
        
        return "Motorista: " + nome + "- CNH: "+cnh ;
    }
}