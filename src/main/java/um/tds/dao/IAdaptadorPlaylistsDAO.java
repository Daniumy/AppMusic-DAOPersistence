package um.tds.dao;

import um.tds.dominio.ListaCanciones;


public interface IAdaptadorPlaylistsDAO {
	public void registrarListaCanciones(ListaCanciones lista);
	public void borrarListaCanciones(ListaCanciones lista);
	public void modificarListaCanciones(ListaCanciones lista);
	public ListaCanciones recuperarListaCanciones(int codigo);
	
}
