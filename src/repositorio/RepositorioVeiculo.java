package repositorio;

import java.util.List;
import jakarta.persistence.TypedQuery;
import model.Veiculo;
import util.Util;

public class RepositorioVeiculo extends Repositorio<Veiculo> {

	// ==========================================================
	// 1. IMPLEMENTAÇÃO DOS MÉTODOS ABSTRATOS DA CLASSE MÃE
	// ==========================================================

	/**
	 * Localiza um veículo no banco de dados.
	 * 
	 * @param chave Pode ser o ID (int) ou a Placa (String), dependendo de como você
	 *              definiu a chave primária (@Id) na classe Veiculo.
	 */
	@Override
	public Veiculo localizar(Object chave) {
		// Se a sua chave primária (@Id) na classe Veiculo for a PLACA (String):
		if (chave instanceof String) {
			return Util.getManager().find(Veiculo.class, (String) chave);
		}

		// Se a sua chave primária (@Id) na classe Veiculo for um ID numérico (int):
		if (chave instanceof Integer) {
			return Util.getManager().find(Veiculo.class, (Integer) chave);
		}

		return null;
	}

	/**
	 * Lista todos os veículos cadastrados na tabela do banco.
	 */
	@Override
	public List<Veiculo> listar() {
		TypedQuery<Veiculo> query = Util.getManager().createQuery("SELECT v FROM Veiculo v", Veiculo.class);
		return query.getResultList();
	}

	// ==========================================================
	// 2. CONSULTAS CUSTOMIZADAS (JPQL)
	// ==========================================================

	/**
	 * Caso sua chave primária seja numérica, esse método serve para buscar
	 * especificamente pela Placa do veículo (útil para validações na Fachada).
	 */
	public Veiculo buscarPorPlaca(String placa) {
		try {
			TypedQuery<Veiculo> query = Util.getManager().createQuery("SELECT v FROM Veiculo v WHERE v.placa = :placa",
					Veiculo.class);
			query.setParameter("placa", placa);
			return query.getSingleResult();
		} catch (Exception e) {
			// Retorna nulo se não encontrar nenhum veículo com essa placa
			return null;
		}
	}
}