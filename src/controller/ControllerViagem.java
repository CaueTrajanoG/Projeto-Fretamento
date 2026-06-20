package controller;

/**********************************
 * IFPB - SI
 * POB - Persistencia de Objetos
 * Prof. Fausto Ayres
 **********************************/

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import model.Motorista;
import model.Veiculo;
import model.Viagem;
import repositorio.Repositorio;
import repositorio.RepositorioViagem;
import repositorio.RepositorioMotorista; // Supondo que existam estes repositórios
import repositorio.RepositorioVeiculo;

public class ControllerViagem {
	private ControllerViagem() {
	}

	private static RepositorioViagem repViagem = new RepositorioViagem();
	private static RepositorioMotorista repMotorista = new RepositorioMotorista();
	private static RepositorioVeiculo repVeiculo = new RepositorioVeiculo();

	// ==========================================
	// LOCALIZAR VIAGEM
	// ==========================================
	public static Viagem localizarViagem(int id) throws Exception {
		try {
			Repositorio.conectar();
			Viagem v = repViagem.localizar(id); // Busca pela Chave Primária (ID)
			return v;
		} catch (Exception e) {
			throw e;
		} finally {
			Repositorio.desconectar();
		}
	}

	// ==========================================
	// CRIAR VIAGEM
	// ==========================================
	public static void criarViagem(String dataStr, String destino, String cnhMotorista, String placaVeiculo,
			List<String> passageiros) throws Exception {
		try {
			Repositorio.conectar();
			Repositorio.begin();

			// 1. Validação do formato da data recebida da Tela/Console
			LocalDate dataFormatada;
			try {
				dataFormatada = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			} catch (DateTimeParseException e) {
				throw new Exception("Criar viagem - formato de data inválido (use dd/MM/yyyy): " + dataStr);
			}

			// 2. Validação se o Motorista existe no banco
			Motorista m = repMotorista.localizar(cnhMotorista);
			if (m == null)
				throw new Exception("Criar viagem - Motorista não encontrado com a CNH: " + cnhMotorista);

			// 3. Validação se o Veículo existe no banco
			Veiculo vec = repVeiculo.localizar(placaVeiculo);
			if (vec == null)
				throw new Exception("Criar viagem - Veículo não encontrado com a Placa: " + placaVeiculo);

			// 4. Instanciação e persistência do objeto
			Viagem v = new Viagem(dataFormatada, destino, vec, m);
			v.setNomePas(passageiros);

			repViagem.criar(v);
			Repositorio.commit();

		} catch (Exception e) {
			Repositorio.rollback();
			throw e;
		} finally {
			Repositorio.desconectar();
		}
	}

	// ==========================================
	// ALTERAR VIAGEM
	// ==========================================
	public static void alterarViagem(int id, String dataStr, String destino, List<String> passageiros)
			throws Exception {
		try {
			Repositorio.conectar();
			Repositorio.begin();

			Viagem v = repViagem.localizar(id);
			if (v == null)
				throw new Exception("Alterar viagem - Viagem inexistente com o ID: " + id);

			// Atualiza a lista de passageiros se enviada
			if (passageiros != null) {
				v.setNomePas(passageiros);
			}

			// Atualiza o destino se enviado
			if (destino != null && !destino.isBlank()) {
				v.setDestino(destino);
			}

			// Valida e atualiza a data se enviada
			if (dataStr != null) {
				try {
					LocalDate dataFormatada = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
					v.setData(dataFormatada);
				} catch (DateTimeParseException e) {
					throw new Exception("Alterar viagem - formato de data inválido: " + dataStr);
				}
			}

			repViagem.atualizar(v);
			Repositorio.commit();

		} catch (Exception e) {
			Repositorio.rollback();
			throw e;
		} finally {
			Repositorio.desconectar();
		}
	}

	// ==========================================
	// APAGAR VIAGEM
	// ==========================================
	public static void apagarViagem(int id) throws Exception {
		try {
			Repositorio.conectar();
			Repositorio.begin();

			Viagem v = repViagem.localizar(id);
			if (v == null)
				throw new Exception("Excluir viagem - Viagem inexistente com o ID: " + id);

			// Desvincular relacionamentos para evitar quebras de integridade
			v.setMotorista(null);
			v.setVeiculo(null);
			v.getNomePas().clear(); // Limpa a tabela de ElementCollection de passageiros

			repViagem.deletar(v);
			Repositorio.commit();

		} catch (Exception e) {
			Repositorio.rollback();
			throw e;
		} finally {
			Repositorio.desconectar();
		}
	}

	// ==========================================
	// LISTAGENS
	// ==========================================
	public static List<Viagem> listarViagens() {
		Repositorio.conectar();
		List<Viagem> lista = repViagem.listar();
		Repositorio.desconectar();
		System.out.println("Listando viagens...");
		return lista;
	}

	// Listagem por filtro de texto (ex: buscar viagens por destino)
	public static List<Viagem> listarViagensPorDestino(String destino) {
		Repositorio.conectar();
		List<Viagem> lista = repViagem.listarPorDestino(destino);
		Repositorio.desconectar();
		return lista;
	}

	// ==========================================
	// CONSULTAS CUSTOMIZADAS (Exemplos)
	// ==========================================

	public static List<Viagem> consultarViagensDoMotorista(String cnh) {
		Repositorio.conectar();
		List<Viagem> result = repViagem.listarPorMotorista(cnh);
		Repositorio.desconectar();
		return result;
	}

	public static List<Viagem> consultarViagensNaData(String dataStr) throws Exception {
		try {
			LocalDate dataFormatada = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			Repositorio.conectar();
			List<Viagem> result = repViagem.listarPorData(dataFormatada);
			Repositorio.desconectar();
			return result;
		} catch (DateTimeParseException e) {
			throw new Exception("Consulta por data - formato inválido: " + dataStr);
		}
	}
}