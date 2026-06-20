/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * POO
 * Prof. Fausto Maranh�o Ayres
 **********************************/
package repositorio;

import java.util.List;

import jakarta.persistence.TypedQuery;
import model.Motorista;
import util.Util;

public class RepositorioMotorista extends Repositorio<Motorista> {
	
	public Motorista localizar(Object chave) {
		String nome = (String) chave;
		TypedQuery<Motorista> q = Util.getManager().createQuery("""
				select * from Motorista""", Motorista.class);
		q.setParameter("n", nome);

		return q.getSingleResultOrNull();
	}
	
	public List<Motorista> listar() {
		TypedQuery<Motorista> q = Util.getManager().createQuery("""
				select p from Motorista p
				order by p.id
				""", Motorista.class);
		return q.getResultList();
	}
	public List<Motorista> listarPorNome(String nome) {
        // Criamos a consulta JPQL buscando pelo atributo 'nome' da entidade Motorista
        // O LOWER garante que a busca funcione ignorando maiúsculas e minúsculas (Case-Insensitive)
        TypedQuery<Motorista> query = Util.getManager().createQuery(
                "SELECT m FROM Motorista m WHERE LOWER(m.nome) LIKE LOWER(:nome)", 
                Motorista.class
        );
        
        // O caractere '%' serve como coringa antes e depois do termo pesquisado
        query.setParameter("nome", "%" + nome + "%");
        
        return query.getResultList();
    }

}
