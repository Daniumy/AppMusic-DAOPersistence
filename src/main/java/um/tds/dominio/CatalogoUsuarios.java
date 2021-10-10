package um.tds.dominio;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import um.tds.dao.IAdaptadorUsuarioDAO;
import um.tds.dao.DAOException;
import um.tds.dao.FactoriaDAO;

public class CatalogoUsuarios {
	private HashMap<String, Usuario> usuarios;
	private static CatalogoUsuarios unicaInstancia;
	private FactoriaDAO factoria;
	private IAdaptadorUsuarioDAO adaptadorUsuario;

	private CatalogoUsuarios (){
		try {
			factoria = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
			adaptadorUsuario = factoria.getUsuarioDAO();
			usuarios = new HashMap<String, Usuario>();
			this.cargarCatalogo();
			}
		 catch (DAOException eDAO) {
			   eDAO.printStackTrace();
		}
	}


	public static CatalogoUsuarios getUnicaInstancia() {
		if (unicaInstancia == null) unicaInstancia = new CatalogoUsuarios();
		return unicaInstancia;
	}
	
	public List<Usuario> getUsuarios() throws DAOException {
		return new ArrayList<Usuario>(usuarios.values());
	}
	
	public Usuario getUsuario(String login) {
		return usuarios.get(login);
	}

	
	public void addUsuario(Usuario usuario) {
		usuarios.put(usuario.getLogin(), usuario);
	}
	
	public void removeUsuario(Usuario usuario) {
		usuarios.remove(usuario.getLogin());
	}
	
	/*Recupera todos los usuarios para trabajar con ellos en memoria*/
	private void cargarCatalogo() throws DAOException {
		 List<Usuario> usuariosBD = adaptadorUsuario.getAll();
		 for (Usuario usuario: usuariosBD) 
			     usuarios.put(usuario.getLogin(),usuario);
	}

}
