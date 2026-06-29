package requisito;

import java.util.List;

import model.Motorista;
import model.Veiculo;
import repositorio.Repositorio;
import repositorio.RepositorioVeiculo;

public class FachadaVeiculo {
	private FachadaVeiculo() {}

    private static RepositorioVeiculo repVeiculo = new RepositorioVeiculo();

    // ==========================================
    // LOCALIZAR VEÍCULO
    // ==========================================
    public static Veiculo localizarVeiculo(String placa) throws Exception {
        try {
            Repositorio.conectar();
            Veiculo v = repVeiculo.localizar(placa); // Busca pela Placa (Chave Primária)
            return v;
        } catch (Exception e) {
            throw e;
        } finally {
            Repositorio.desconectar();
        }
    }

    // ==========================================
    // CRIAR VEÍCULO
    // ==========================================
    public static void criarVeiculo(String placa, int capacidade) throws Exception {
        try {
            Repositorio.conectar();
            Repositorio.begin();

            // 1. Validação básica da placa
            if (placa == null || placa.isBlank())
                throw new Exception("Criar veículo - A placa é obrigatória.");
            
            // 2. Validação da capacidade (não pode ser zero ou negativa)
            if (capacidade <= 0)
                throw new Exception("Criar veículo - A capacidade deve ser maior que zero.");

            // 3. Validação de Regra de Negócio: Evitar Placa duplicada
            Veiculo v = repVeiculo.localizar(placa);
            if (v != null) 
                throw new Exception("Criar veículo - Já existe um veículo cadastrado com esta placa: " + placa);
            
            // 4. Instanciação e persistência do objeto
            v = new Veiculo();
            v.setPlaca(placa);
            v.setCapacidade(capacidade);
            
            repVeiculo.criar(v);
            Repositorio.commit();

        } catch (Exception e) {
            Repositorio.rollback();
            throw e;
        } finally {
            Repositorio.desconectar();
        }
    }

    // ==========================================
    // ALTERAR VEÍCULO
    // ==========================================
    public static void alterarVeiculo(String placa, int novaCapacidade) throws Exception {
        try {
            Repositorio.conectar();
            Repositorio.begin();
            
            // Localiza o veículo existente
            Veiculo v = repVeiculo.localizar(placa);
            if (v == null)
                throw new Exception("Alterar veículo - Veículo não encontrado com a placa: " + placa);

            // Valida a nova capacidade fornecida antes de alterar
            if (novaCapacidade <= 0)
                throw new Exception("Alterar veículo - A nova capacidade deve ser maior que zero.");

            v.setCapacidade(novaCapacidade);

            repVeiculo.atualizar(v); 
            Repositorio.commit();

        } catch (Exception e) {
            Repositorio.rollback();
            throw e;
        } finally {
            Repositorio.desconectar();
        }
    }

    // ==========================================
    // APAGAR VEÍCULO
    // ==========================================
    public static void apagarVeiculo(String placa) throws Exception {
        try {
            Repositorio.conectar();
            Repositorio.begin();
            
            Veiculo v = repVeiculo.localizar(placa);
            if (v == null)
                throw new Exception("Excluir veículo - Veículo inexistente com a placa: " + placa);

            // ⚠️ ALERTA DE REGRA DE NEGÓCIO: 
            // Se o veículo estiver associado a uma viagem ativa, o banco impedirá a exclusão.
            
            repVeiculo.deletar(v);   
            Repositorio.commit();
            
        } catch (Exception e) {
            Repositorio.rollback();
            throw e;
        } finally {
            Repositorio.desconectar();
        }
    }

    // ==========================================
    // LISTAGENS E FILTROS
    // ==========================================
    public static List<Veiculo> listarVeiculos() {
        Repositorio.conectar();
        List<Veiculo> lista = repVeiculo.listar();
        Repositorio.desconectar();
        return lista;
    }
    
    // Buscar um único veiculo pela placa
    public static Veiculo buscarVeiculoPorPlaca(String placa) {
        Repositorio.conectar();
        List<Veiculo> lista = repVeiculo.listar();
        Repositorio.desconectar();

        // Varre a lista procurando o motorista
        for (Veiculo v : lista) {
            
            if (v.getPlaca().equalsIgnoreCase(placa)) {
                return v; // Encontrou! Retorna o objeto e para o método
            }
        }
        return null; // Se percorreu a lista toda e não achou, retorna null
    }
}
