package repositorio;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.TypedQuery;
import model.Viagem;
import util.Util; // Importando a classe util que gerencia o manager

public class RepositorioViagem extends Repositorio<Viagem> {

	// Implementando o método abstrato da mãe (busca por ID)
	@Override
	public Viagem localizar(Object chave) {
		// Como a chave vem como Object, transformamos para int (ID da Viagem)
		int id = (Integer) chave; 
		return Util.getManager().find(Viagem.class, id);
	}

	// Implementando o método abstrato da mãe (listar todos)
	@Override
	public List<Viagem> listar() {
		TypedQuery<Viagem> query = Util.getManager().createQuery("SELECT v FROM Viagem v", Viagem.class);
		return query.getResultList();
	}

	public List<Viagem> listarPorDestino(String destino) {
		TypedQuery<Viagem> query = Util.getManager().createQuery(
				"SELECT v FROM Viagem v WHERE v.destino LIKE :dest", Viagem.class);
		query.setParameter("dest", "%" + destino + "%");
		return query.getResultList();
	}
	public List<Viagem> listarPorMotorista(String nome) {
		TypedQuery<Viagem> query = Util.getManager().createQuery(
				"SELECT v FROM Viagem v WHERE v.motorista LIKE :moto", Viagem.class);
		query.setParameter("moto", "%" + nome + "%");
		return query.getResultList();
	}
    public Viagem localizarViagemComMotorista(int idViagem) {
        return Util.getManager()
            .createQuery("select v from Viagem v join fetch v.motorista where v.id = :id", Viagem.class)
            .setParameter("id", idViagem)
            .getSingleResult();
    }
	
	/**
     * Busca todas as viagens cadastradas em uma data específica.
     * @param data Objeto LocalDate contendo a data da busca.
     * @return Lista de viagens encontradas naquela data.
     */
    public List<Viagem> listarPorData(LocalDate data) {
        // Criamos a consulta JPQL buscando pelo atributo 'data' da entidade Viagem
        TypedQuery<Viagem> query = Util.getManager().createQuery(
                "SELECT v FROM Viagem v WHERE v.data = :data", 
                Viagem.class
        );
        
        // Passamos o objeto LocalDate diretamente como parâmetro
        query.setParameter("data", data);
        
        return query.getResultList();
    }
    

}