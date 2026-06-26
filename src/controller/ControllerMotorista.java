package controller;

import java.util.List;

import model.Motorista;
import repositorio.Repositorio;
import repositorio.RepositorioMotorista;
import util.Util;
import util.Util.*;

public class ControllerMotorista {
    private ControllerMotorista() {}

    private static RepositorioMotorista repMotorista = new RepositorioMotorista();

    // ==========================================
    // LOCALIZAR MOTORISTA
    // ==========================================
    public static Motorista localizarMotorista(String cnh) throws Exception {
        try {
            Repositorio.conectar();
            Motorista m = repMotorista.localizar(cnh); // Busca pela CNH (ou ID correspondente)
            return m;
        } catch (Exception e) {
            throw e;
        } finally {
            Repositorio.desconectar();
        }
    }

    // ==========================================
    // CRIAR MOTORISTA
    // ==========================================
    public static void criarMotorista(String cnh, String nome) throws Exception {
        try {
            Repositorio.conectar();
            Repositorio.begin();

            // 1. Validação básica de campos vazios
            if (cnh == null || cnh.isBlank())
                throw new Exception("Criar motorista - CNH é obrigatória.");
            if (nome == null || nome.isBlank())
                throw new Exception("Criar motorista - Nome é obrigatório.");

            // 2. Validação de Regra de Negócio: Evitar CNH duplicada
            Motorista m = repMotorista.localizar(cnh);
            if (m != null) {
            	System.out.println("CNH duplicada");
                throw new Exception("Criar motorista - Já existe um motorista cadastrado com esta CNH: " + cnh);
                
            }
            // 3. Instanciação e persistência do objeto
            m = new Motorista();
            m.setCnh(cnh);
            m.setNome(nome);
            
            
            repMotorista.criar(m);
            Repositorio.commit();

        } catch (Exception e) {
            Repositorio.rollback();
            throw e;
        } finally {
            Repositorio.desconectar();
        }
    }

    // ==========================================
    // ALTERAR MOTORISTA
    // ==========================================
    public static void alterarMotorista(String cnh, String novoNome) throws Exception {
        try {
            Repositorio.conectar();
            Repositorio.begin();
            
            // Localiza o motorista existente
            Motorista m = repMotorista.localizar(cnh);
            if (m == null)
                throw new Exception("Alterar motorista - Motorista não encontrado com a CNH: " + cnh);

            // Atualiza os dados se eles forem enviados válidos
            if (novoNome != null && !novoNome.isBlank()) {
                m.setNome(novoNome);
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

    // ==========================================
    // APAGAR MOTORISTA
    // ==========================================
    public static void apagarMotorista(String cnh) throws Exception {
        try {
            Repositorio.conectar();
            Repositorio.begin();
            
            Motorista m = repMotorista.localizar(cnh);
            if (m == null)
                throw new Exception("Excluir motorista - Motorista inexistente com a CNH: " + cnh);

            // ⚠️ ALERTA DE REGRA DE NEGÓCIO: 
            // Se o motorista estiver vinculado a alguma Viagem, o Hibernate pode lançar um erro 
            // de chave estrangeira (Constraint Violation). Idealmente, você deve validar isso antes.
            
            repMotorista.deletar(m);   
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
    public static List<Motorista> listarMotoristas() {
        Repositorio.conectar();
        List<Motorista> lista = repMotorista.listar();
        Repositorio.desconectar();
        return lista;
    }

 // Buscar um único motorista pelo nome
    public static Motorista buscarMotoristaPorNome(String nome) {
        Repositorio.conectar();
        List<Motorista> lista = repMotorista.listarPorNome(nome);
        Repositorio.desconectar();

        // Varre a lista procurando o motorista
        for (Motorista m : lista) {
            // equalsIgnoreCase garante que "caue" ou "Caue" funcionem igual
            if (m.getNome().equalsIgnoreCase(nome)) {
                return m; // Encontrou! Retorna o objeto e para o método
            }
        }
        return null; // Se percorreu a lista toda e não achou, retorna null
    }
    
    public static void salvarFoto(String cnh, byte[] bytesFoto) throws Exception {
        if (cnh == null || cnh.trim().isEmpty()) {
            throw new Exception("A CNH do motorista não foi informada.");
        }
        if (bytesFoto == null || bytesFoto.length == 0) {
            throw new Exception("Nenhum dado de imagem válido foi detectado.");
        }
        try {
            if (!Util.getManager().getTransaction().isActive()) {
                Util.getManager().getTransaction().begin();
            }
            RepositorioMotorista repo = new RepositorioMotorista();
            System.out.println("passei aqui " + cnh);
            repo.salvarFoto(cnh, bytesFoto);

            // 4. Salva permanentemente no banco de dados
            Util.getManager().getTransaction().commit();
            
        } catch (Exception e) {
            // Se algo der errado (banco fora do ar, erro de conversão, etc), desfaz as alterações
            if (Util.getManager().getTransaction().isActive()) {
                Util.getManager().getTransaction().rollback();
            }
            throw new Exception("Erro no controlador ao salvar foto: " + e.getMessage());
        }
    }
    
}