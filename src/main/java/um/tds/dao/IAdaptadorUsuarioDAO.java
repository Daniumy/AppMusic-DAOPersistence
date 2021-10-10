package um.tds.dao;

import java.util.List;

import um.tds.dominio.Usuario;

public interface IAdaptadorUsuarioDAO {
	
	void create(Usuario asistente);
	boolean delete(Usuario asistente);
	void updatePerfil(Usuario asistente);
	Usuario get(int id);
	List<Usuario> getAll();
	void updatePerfilPremium(Usuario usuarioActual);
	
}

