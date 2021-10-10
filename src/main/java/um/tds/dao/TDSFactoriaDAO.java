package um.tds.dao;

/** 
 * Factoria concreta DAO para el Servidor de Persistencia de la asignatura TDS.
 * 
 */

public final class TDSFactoriaDAO extends FactoriaDAO {
	
	public TDSFactoriaDAO() {	}
	
	@Override
	public IAdaptadorUsuarioDAO getUsuarioDAO() {	
		return AdaptadorUsuarioDAO.getUnicaInstancia();
	}
	
	@Override
	public IAdaptadorCancionesDAO getCancionesDAO() {	
		return AdaptadorCancionesDAO.getUnicaInstancia();
	}
	
	@Override
	public IAdaptadorPlaylistsDAO getPlaylistsDAO() {	
		return new AdaptadorPlaylistsDAO();
	}

}
