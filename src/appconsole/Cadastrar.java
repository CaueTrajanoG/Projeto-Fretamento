package appconsole;

import java.time.LocalDate;
import jakarta.persistence.EntityManager;
import model.Motorista;
import model.Veiculo;
import model.Viagem;
import util.Util;

public class Cadastrar {
	
	public Cadastrar() {
		try {
			Util.conectar();
			EntityManager manager = Util.getManager();
			
			manager.getTransaction().begin();
			
			System.out.println("Cadastrando veículo, motorista e viagens...");
			
			// Criando veiculos
			Veiculo vc1 = new Veiculo("ABC-1234", 4);
			Veiculo vc2 = new Veiculo("CDE-1234", 6);
			Veiculo vc3 = new Veiculo("FGH-1234", 8);
			manager.persist(vc1);
			manager.persist(vc2);
			manager.persist(vc3);
			
			// Criando Motoristas
			Motorista m1 = new Motorista("111", "Victor");
			Motorista m2 = new Motorista("222", "Ludmilla");
			Motorista m3 = new Motorista("333", "Caue");
			manager.persist(m1);
			manager.persist(m2);
			manager.persist(m3);
			
			// Criando Viagens e Adicionando passageiros
			Viagem vg1 = new Viagem(LocalDate.of(2026, 2, 5), "Campina Grande", vc1, m1);
			vg1.getNomePas().add("João Pedro");
			vg1.getNomePas().add("Maria de Fatima");
			manager.persist(vg1);
			
			Viagem vg2 = new Viagem(LocalDate.of(2026, 2, 10), "Cajazeiras", vc2, m2);
			vg2.getNomePas().add("Claudia Raia");
			vg2.getNomePas().add("Carla Perez");
			manager.persist(vg2);
			
			Viagem vg3 = new Viagem(LocalDate.of(2026, 2, 20), "Natal", vc3, m3);
			vg3.getNomePas().add("Pedro Arthur");
			vg3.getNomePas().add("Joana Maria");
			manager.persist(vg3);
			
			Viagem vg4 = new Viagem(LocalDate.of(2026, 3, 2), "Patos", vc1, m2);
			vg4.getNomePas().add("João");
			vg4.getNomePas().add("Maria");
			manager.persist(vg4);
			
			Viagem vg5 = new Viagem(LocalDate.of(2026, 3, 5), "Fortaleza", vc1, m3);
			vg5.getNomePas().add("João Guilherme");
			vg5.getNomePas().add("Felipe Sousa");
			manager.persist(vg5);
			
			Viagem vg6 = new Viagem(LocalDate.of(2026, 3, 10), "Campina Grande", vc1, m1);
			vg6.getNomePas().add("Melquisedeque Vital");
			vg6.getNomePas().add("Murilo Maciel");
			manager.persist(vg6);
			
			Viagem vg7 = new Viagem(LocalDate.of(2026, 4, 5), "Salvador", vc2, m1);
			vg7.getNomePas().add("Jonas Sarmento");
			vg7.getNomePas().add("Paulo Antonio");
			manager.persist(vg7);
			
			Viagem vg8 = new Viagem(LocalDate.of(2026, 4, 8), "São Paulo", vc2, m3);
			vg8.getNomePas().add("Davi Leite");
			vg8.getNomePas().add("Arthur Aguiar");
			manager.persist(vg8);
			
			Viagem vg9 = new Viagem(LocalDate.of(2026, 4, 12), "Rio de Janeiro", vc3, m1);
			vg9.getNomePas().add("Maria Clara");
			vg9.getNomePas().add("Mariana Santos");
			manager.persist(vg9);
			
			Viagem vg10 = new Viagem(LocalDate.of(2026, 4, 16), "Curitiba", vc3, m2);
			vg10.getNomePas().add("Gabriel Medeiros");
			vg10.getNomePas().add("Pedro Assunção");
			manager.persist(vg10);
			
			
			
			manager.getTransaction().commit();
			System.out.println("Dados gravados!");
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}


		Util.desconectar();
		System.out.println("fim do programa");
	}


	// =================================================
	public static void main(String[] args) {
		new Cadastrar();
	}

}
