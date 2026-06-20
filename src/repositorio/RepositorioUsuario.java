/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * POO
 * Prof. Fausto Maranh�o Ayres
 **********************************/
package repositorio;

import java.util.List;

import jakarta.persistence.TypedQuery;
import model.Usuario;
import util.Util;

public class RepositorioUsuario extends Repositorio<Usuario> {

	public Usuario localizar(Object chave) {
		String nome = (String) chave;
		TypedQuery<Usuario> q = Util.getManager().createQuery("""
				select u from Usuario u
				where u.nome=:n
				 """, Usuario.class);
		q.setParameter("n", nome);

		return q.getSingleResultOrNull();
	}

	public Usuario localizarComSenha(String nome, String senha) {
		TypedQuery<Usuario> q = Util.getManager().createQuery("""
				select u from Usuario u
				where u.nome=:n and u.senha=:s
				 """, Usuario.class);
		q.setParameter("n", nome);
		q.setParameter("s", senha);

		return q.getSingleResultOrNull();
	}

	public List<Usuario> listar() {
		TypedQuery<Usuario> q = Util.getManager().createQuery("""
				select  u from Usuario u
				""", Usuario.class);

		return q.getResultList();
	}

	
}