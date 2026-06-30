package util;

import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Util {
	private static EntityManager manager;
	private static EntityManagerFactory factory;
	private static final Logger logger = LogManager.getLogger(Util.class);

	public static void conectar() {
		if (manager == null) {
			Properties propriedades = new Properties();
			try {
				propriedades.load(Util.class.getResourceAsStream("/util/ip.properties"));
			} catch (IOException e) {
				logger.error("Arquivo /util/ip.properties inexistente");
				throw new RuntimeException("arquivo /util/ip.properties inexistente");
			}
			
			try {
				logger.info("----conectando ao banco de dados (pob)");
				factory = Persistence.createEntityManagerFactory("pob");
				manager = factory.createEntityManager();
				logger.info("-------- conectou banco de dados");
			} catch (Exception e) {
				logger.error("Erro ao criar EntityManagerFactory: " + e.getMessage());
				throw new RuntimeException("Erro de conexão: " + e.getMessage());
			}
		}
	}

	public static void desconectar() {
		if (manager != null && manager.isOpen()) {
			manager.close();
			factory.close();
			manager = null;
			logger.info("-------- desconectou banco de dados");
		}
	}

	public static EntityManager getManager() {
		if (manager == null) {
			conectar();
		}
		return manager;
	}
}
