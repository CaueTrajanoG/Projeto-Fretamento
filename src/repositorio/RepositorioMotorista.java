package repositorio;

import java.util.List;
import jakarta.persistence.TypedQuery;
import model.Motorista;
import util.Util;

public class RepositorioMotorista extends Repositorio<Motorista> {
	
	@Override
	public Motorista localizar(Object chave) {
	    String cnh = (String) chave;        
	    TypedQuery<Motorista> q = Util.getManager().createQuery("""
	            select m from Motorista m 
	            left join fetch m.listaViagem 
	            where m.cnh = :c""", Motorista.class);     
	    q.setParameter("c", cnh);
	    return q.getSingleResultOrNull();
	}
	
	@Override
	public List<Motorista> listar() {
		TypedQuery<Motorista> q = Util.getManager().createQuery("""
				select p from Motorista p
				order by p.nome
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

	public List<Motorista> consultarMotoristasPorQtdViagensEDestino(int n, String destino) {
		// Alterado de m.viagens para m.listaViagem para bater com o mapeamento da Entidade
		TypedQuery<Motorista> q = Util.getManager().createQuery(
				"select m from Motorista m join m.listaViagem v " +
				"where v.destino = :dest " +
				"group by m " +
				"having count(v) > :qtd", Motorista.class);
		q.setParameter("dest", destino);
		q.setParameter("qtd", (long) n); // o count() retorna Long no JPA
		
		return q.getResultList();
	}
}