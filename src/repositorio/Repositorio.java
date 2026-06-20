/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistência de Objetos
 * Prof. Fausto Maranhão Ayres
 **********************************/
package repositorio;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;

import util.Util;

public abstract class Repositorio<T> {

	public static void conectar() {
		Util.conectar();
	}

	public static void desconectar() {
		Util.desconectar();
	}

	// operações CRUD genéricas

	public void criar(T objeto) {
		Util.getManager().persist(objeto);
	}

	public void atualizar(T objeto) {
		Util.getManager().merge(objeto);
	}

	public void deletar(T objeto) {
		Util.getManager().remove(objeto);
	}

	// MÉTODOS ABSTRATOS (ver subclasses)
	public abstract T localizar(Object chave);

	public abstract List<T> listar();

	public static void begin() {
		if (!Util.getManager().getTransaction().isActive())
			Util.getManager().getTransaction().begin();
	}

	public static void commit() {
		if (Util.getManager().getTransaction().isActive()) {
			Util.getManager().getTransaction().commit();
			Util.getManager().clear(); // limpar cache de memoria
		}
	}

	public static void rollback() {
		if (Util.getManager().getTransaction().isActive())
			Util.getManager().getTransaction().rollback();
	}

	public void resetID() {
		// reinicializar id em 1 no banco
		// deve ser chamado de dentro de uma transação, antes de um commit
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		String classe = type.getSimpleName().toLowerCase();
		try {
			String nomesgbd = "";
			Connection con = Repositorio.getConnection();
			if (con == null)
				throw new SQLException("resetar id - falha ao obter conexao");

			// sql para resetar depende do SGBD
			nomesgbd = con.getMetaData().getDatabaseProductName();
			if (nomesgbd.equalsIgnoreCase("postgresql"))
				Util.getManager().createNativeQuery("ALTER SEQUENCE " + classe + "_id_seq RESTART WITH 1")
						.executeUpdate();
			else if (nomesgbd.equalsIgnoreCase("mysql"))
				Util.getManager().createNativeQuery("ALTER TABLE " + classe + " AUTO_INCREMENT = 1").executeUpdate();
			else
				throw new SQLException("resetar id - sgbd desconhecido " + nomesgbd);

		} catch (SQLException ex) {
			throw new RuntimeException("resetar id -  " + ex.getMessage());
		}

	}

	public static Connection getConnection() {
		// obter conexao jdbc (classe Connection) atraves do hibernate
		try {
			Session session = Util.getManager().unwrap(Session.class);
			SessionFactoryImplementor sfi = (SessionFactoryImplementor) session.getSessionFactory();
			Connection conn = sfi.getJdbcServices().getBootstrapJdbcConnectionAccess().obtainConnection();
			return conn;
		} catch (Exception ex) {
			return null;
		}
	}
}
