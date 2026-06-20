/**********************************
 * IFPB - SI
 * POB - Persistencia de Objetos
 * Prof. Fausto Ayres
 **********************************/

package appconsole;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Veiculo;
import model.Viagem;
import util.Util;

public class Alterar {

	public Alterar() {
		try {
			Util.conectar();
			EntityManager manager = Util.getManager();

			manager.getTransaction().begin();
			TypedQuery<Viagem> q = manager.createQuery(
					"select v from Viagem v join v.veiculo vc where vc.placa = :p", Viagem.class);
			q.setParameter("p", "ABC-1234");
			List<Viagem> viagens = q.getResultList();
			
			if (viagens.size() > 0) {
				Viagem viagem = viagens.get(0);
				Veiculo veiculo = viagem.getVeiculo();
				
				viagem.setVeiculo(null);
				if (veiculo != null) {
					veiculo.remover(viagem);
				}
				
				manager.getTransaction().commit();
				
				System.out.println("Veiculo removido da viagem: " + viagem.getDestino());
				
			} else {
				System.out.println("Veiculo nao encontrado");
			}
			
		} 
	
		catch (Exception e) {
			System.out.println(e.getMessage());
		}

		Util.desconectar();
		System.out.println("fim do programa");
	}

	// =================================================
	public static void main(String[] args) {
		new Alterar();
	}

}