package requisito;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.Motorista;
import repositorio.Repositorio;
import repositorio.RepositorioMotorista;
import util.Util;

public class FachadaMotorista {
    private FachadaMotorista() {}

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
    
    
	public static void alterarFoto(String cnh, byte[] foto) throws Exception {
		try {
			Repositorio.conectar();
			Repositorio.begin();
			Motorista m = repMotorista.localizar(cnh); // usando chave primaria
			if (m == null)
				throw new Exception("alterar foto - cnh inexistente:" + cnh);

			m.setFoto(foto);
			
			repMotorista.atualizar(m); 		
			Repositorio.commit();

		} catch (Exception e) {
			Repositorio.rollback();
			throw e;
		} finally {
			Repositorio.desconectar();
		}
	}

	
	
    
}