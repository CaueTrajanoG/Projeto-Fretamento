/**********************************
 * IFPB - SI
 * POB - Persistencia de Objetos
 * Prof. Fausto Ayres
 **********************************/

package appconsole;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Motorista;
import model.Viagem;

import java.time.LocalDate; 
import java.util.List;

import util.Util;

public class Consultar {

	public Consultar() {
		try{
			Util.conectar();
			EntityManager manager = Util.getManager();

			List<Viagem> viagens;
			TypedQuery<Viagem> v1;
			List<Motorista> motoristas;
			TypedQuery<Motorista> m1;
			
			System.out.print("\n--- Listar viagens na data x ---");
			v1 = manager.createQuery ("""
			select v from Viagem v where v.data = :data""",
			Viagem.class);
			LocalDate data = LocalDate.of (2026,02,20);
			v1.setParameter("data",data);
			viagens = v1.getResultList();
			for (Viagem v : viagens) System.out.println(v);

			System.out.println("\n--- Listar viagens com veículo de placa x ---");
			v1 = manager.createQuery("""
			select vi from Viagem vi
			join vi.veiculo v where v.placa like :placa""", Viagem.class);
			v1.setParameter("placa","CDE-1234"); 
			viagens = v1.getResultList();
			for (Viagem v : viagens) System.out.println(v);

			System.out.println("\n--- Quais motoristas com mais de N viagens com Destino X ---");
			m1 = manager.createQuery("""
			select m from Motorista m join m.listaViagem vi 
			where vi.destino like :destinoX
			group by m
			having count(vi) > :n""",Motorista.class);
			
			m1.setParameter("destinoX","Campina Grande"); 
			m1.setParameter("n",1);
			motoristas = m1.getResultList();
			for (Motorista m : motoristas) System.out.println(m);


		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		Util.desconectar();
		System.out.println("\nFim do programa");
	}
		// =================================================
	public static void main(String[] args) {
		new Consultar();
	}
}

