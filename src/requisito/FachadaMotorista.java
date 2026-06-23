package requisito;

import java.util.List;
import model.Motorista;
import repositorio.Repositorio;
import repositorio.RepositorioMotorista;

public class FachadaMotorista {
	
	// Construtor privado para impedir instanciação (Padrão Singleton/Static)
	private FachadaMotorista() {}
	
	private static RepositorioMotorista repMotorista = new RepositorioMotorista();

	/*
	 *
	 * Localiza um motorista pela CNH.
	 */
	public static Motorista localizarMotorista(String cnh) throws Exception {
		try {
			Repositorio.conectar();
			Motorista m = repMotorista.localizar(cnh);
			return m;
		} catch (Exception e) {
			throw e;
		} finally {
			Repositorio.desconectar();
		}
	}

	/**
	 * Cria um novo motorista com validações de negócio.
	 * Inclui o parâmetro byte[] foto para cumprir a exigência do roteiro.
	 */
	public static void criarMotorista(String cnh, String nome, byte[] foto) throws Exception {
		try {
			// Validações básicas de preenchimento
			if (cnh == null || cnh.trim().isEmpty())
				throw new Exception("criar motorista - CNH obrigatoria.");
			if (nome == null || nome.trim().isEmpty())
				throw new Exception("criar motorista - nome obrigatorio.");

			Repositorio.conectar();
			Repositorio.begin();

			// Validação de duplicidade: não podem existir dois motoristas com a mesma CNH
			Motorista m = repMotorista.localizar(cnh);
			if (m != null)
				throw new Exception("criar motorista - CNH ja cadastrada: " + cnh);

			// Instancia e configura o objeto de modelo
			Motorista novoMotorista = new Motorista(cnh, nome);
			novoMotorista.setFoto(foto); // Atributo exigido pelo roteiro

			// Salva no banco de dados através do repositório
			repMotorista.criar(novoMotorista);
			Repositorio.commit();

		} catch (Exception e) {
			Repositorio.rollback();
			throw e;
		} finally {
			Repositorio.desconectar();
		}
	}

	/**
	 * Altera os dados de um motorista existente.
	 */
	public static void alterarMotorista(String cnh, String novoNome, byte[] novaFoto) throws Exception {
		try {
			if (novoNome == null || novoNome.trim().isEmpty())
				throw new Exception("alterar motorista - nome nao pode ser vazio.");

			Repositorio.conectar();
			Repositorio.begin();

			// Verifica se o motorista realmente existe antes de alterar
			Motorista m = repMotorista.localizar(cnh);
			if (m == null)
				throw new Exception("alterar motorista - CNH inexistente: " + cnh);

			// Atualiza os atributos
			m.setNome(novoNome);
			if (novaFoto != null) {
				m.setFoto(novaFoto);
			}

			repMotorista.atualizar(m);
			Repositorio.commit();

		} catch (Exception e) {
			Repositorio.rollback();
			throw e;
		} finally {
			Repositorio.desconectar();
		}
	}

	/**
	 * Remove um motorista do sistema (opcional, mas recomendado para o CRUD completo).
	 */
	public static void excluirMotorista(String cnh) throws Exception {
		try {
			Repositorio.conectar();
			Repositorio.begin();

			Motorista m = repMotorista.localizar(cnh);
			if (m == null)
				throw new Exception("excluir motorista - CNH inexistente: " + cnh);

			// Regra de negócio implícita: Verificar se o motorista possui viagens vinculadas antes de excluir
			if (m.getListaViagem() != null && !m.getListaViagem().isEmpty()) {
				throw new Exception("excluir motorista - nao eh possivel excluir um motorista com viagens vinculadas.");
			}

			repMotorista.deletar(m);
			Repositorio.commit();

		} catch (Exception e) {
			Repositorio.rollback();
			throw e;
		} finally {
			Repositorio.desconectar();
		}
	}

	/**
	 * Lista todos os motoristas cadastrados.
	 */
	public static List<Motorista> listarMotoristas() {
		Repositorio.conectar();
		List<Motorista> result = repMotorista.listar();
		Repositorio.desconectar();
		return result;
	}

	/**********************************************************
	 * * CONSULTAS IMPLEMENTADAS NOS REPOSITORIOS
	 * **********************************************************/

	/**
	 * Consulta 3: quais os motoristas que tem mais de N viagens com destino X
	 */
	public static List<Motorista> consultarMotoristasPorQtdViagensEDestino(int n, String destino) throws Exception {
		try {
			if (n < 0)
				throw new Exception("consulta - a quantidade N de viagens deve ser maior ou igual a zero.");
			if (destino == null || destino.trim().isEmpty())
				throw new Exception("consulta - o destino deve ser informado.");

			Repositorio.conectar();
			// Chama a consulta customizada por JPQL que você vai criar dentro do RepositorioMotorista
			List<Motorista> result = repMotorista.consultarMotoristasPorQtdViagensEDestino(n, destino);
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			Repositorio.desconectar();
		}
	}
}