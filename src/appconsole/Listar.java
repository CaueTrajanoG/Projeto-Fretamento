package appconsole;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Motorista;
import model.Veiculo;
import model.Viagem;

import org.hibernate.internal.build.AllowSysOut;
import util.Util;

public class Listar {

	public Listar() {
		try {
			Util.conectar();
			EntityManager manager = Util.getManager();
			
			System.out.println("\nListagem de viagens");
			TypedQuery<Viagem> query1 = manager.createQuery("select v from Viagem v", Viagem.class); // order by p.nome
			List<Viagem> resultados1 = query1.getResultList();
			for (Viagem v : resultados1)
				System.out.println(v);

			System.out.println("\nListagem de motoristas");
			TypedQuery<Motorista> query2 = manager.createQuery("select m from Motorista m", Motorista.class); 
			List<Motorista> resultados2 = query2.getResultList();
			for (Motorista m : resultados2)
				System.out.println(m);


			System.out.println("\nListagem de veículos");
			TypedQuery<Veiculo> query3 = manager.createQuery("select v from Veiculo v", Veiculo.class); 
			List<Veiculo> resultados3 = query3.getResultList();
			for (Veiculo v : resultados3)
				System.out.println(v);

 
			System.out.println("\n Listagem das viagens de cada veiculo");
			TypedQuery<Viagem> query4 = manager.createQuery("select v from Viagem v", Viagem.class); // order by p.nome
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
			
		Util.desconectar();
		System.out.println("fim do programa");
	}

	// =================================================
	public static void main(String[] args) {
		new Listar();
	}

}
