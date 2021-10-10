package um.tds.dao;

public abstract class FactoriaDAO {
	private static FactoriaDAO unicaInstancia = null;
	public static final String DAO_TDS = "um.tds.dao.TDSFactoriaDAO";

	public static FactoriaDAO getInstancia(String tipo) throws DAOException {
		if (unicaInstancia == null)
			try {
				unicaInstancia = (FactoriaDAO) Class.forName(tipo).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				throw new DAOException(e.getMessage());
			}
		return unicaInstancia;
	}

	public static FactoriaDAO getInstancia() throws DAOException {
		if (unicaInstancia == null)
			return getInstancia(FactoriaDAO.DAO_TDS);
		else
			return unicaInstancia;
	}

	protected FactoriaDAO() {
	}

	// Metodos factoria para obtener adaptadores
	public abstract IAdaptadorUsuarioDAO getUsuarioDAO();
	public abstract IAdaptadorCancionesDAO getCancionesDAO();
	public abstract IAdaptadorPlaylistsDAO getPlaylistsDAO();
}
