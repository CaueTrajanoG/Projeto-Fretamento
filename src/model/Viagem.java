package model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="viagem20251234567")
public class Viagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private LocalDate data;
    private String destino;

    @ManyToOne
    private Veiculo veiculo;

    @ManyToOne
    private Motorista motorista;

    @ElementCollection
    @CollectionTable(name="viagem_passageiros20251234567")
    private List<String> nomePas = new ArrayList<>();

    public Viagem() {}

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
    public void setMotorista(Motorista motorista) { this.motorista = motorista; }

    public List<String> getNomePas() { return nomePas; }
    public void setNomePas(List<String> nomePas) { this.nomePas = nomePas; }

    @Override
    public String toString() {
        return "Viagem [id = " + id + ", data = " + data + ", destino = " + destino +
               ", motorista = " + (motorista != null ? motorista.getNome() : "N/A") + ", Veiculo:  "+ veiculo.getPlaca() +"]";
    }
}