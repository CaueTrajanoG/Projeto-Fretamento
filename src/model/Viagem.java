package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="viagem20231370016")
public class Viagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;    
    private LocalDate data;
    private String destino;

    @ManyToOne
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY) // LAZY
    @JoinColumn(name = "motorista_cnh")
    private Motorista motorista;

    @ElementCollection
    @CollectionTable(name="viagem_passageiros20231370016")
    private List<String> nomePas = new ArrayList<>();

    public Viagem() {
    	
    }

    public Viagem(LocalDate data, String destino, Veiculo veiculo, Motorista motorista) {
        this.data = data;
        this.destino = destino;
        this.veiculo = veiculo;
        this.motorista = motorista;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public Veiculo getVeiculo() { return veiculo; }
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }

    public Motorista getMotorista() { return motorista; }
    
    public void setMotorista(Motorista motorista) { 
    	this.motorista = motorista; 
    }

    public List<String> getNomePas() { return nomePas; }
    public void setNomePas(List<String> nomePas) { this.nomePas = nomePas; }

    @Override
    public String toString() {
        return "Viagem [id = " + id + ", data = " + data + ", destino = " + destino ;
    }
}