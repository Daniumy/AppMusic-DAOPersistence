package um.tds.dao;



import java.util.List;

import um.tds.dominio.Cancion;

public interface IAdaptadorCancionesDAO {
	
	//public void registrarCancion (Cancion cancion);
	public void persistirCanciones(List<Cancion> canciones);
	public void modificarCancion (Cancion cancion);
	//public Cancion recuperarCancion (String interprete,String titulo, String estilo);
	public List<Cancion> recuperarTodasCanciones();
}
