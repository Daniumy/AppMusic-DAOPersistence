package um.tds.dominio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import um.tds.dao.IAdaptadorCancionesDAO;
import um.tds.dao.DAOException;
import um.tds.dao.FactoriaDAO;

public class CatalogoCanciones {
	private List<Cancion> canciones;
	private static CatalogoCanciones unicaInstancia;
	private FactoriaDAO factoria;
	private IAdaptadorCancionesDAO adaptadorCanciones;

	private CatalogoCanciones() {
		try {
			factoria = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
			adaptadorCanciones = factoria.getCancionesDAO();
			canciones = new LinkedList<Cancion>();
			this.cargarCatalogo();
		} catch (DAOException eDAO) {
			eDAO.printStackTrace();
		}
	}

	public static CatalogoCanciones getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new CatalogoCanciones();
		return unicaInstancia;
	}

	public List<Cancion> getCanciones() {
		return new ArrayList<Cancion>(canciones);
	}

	public List<Cancion> filtrarCanciones(String interprete, String titulo, String estilo) {
		//uso de streams para el filtrado de las canciones buscadas.
		List<Cancion> canciones = new ArrayList<Cancion>();
		canciones = getCanciones().stream()
				.filter(c -> c.getTitulo().contains(titulo) && c.getInterprete().contains(interprete) && c.getEstilo().equalsIgnoreCase(estilo))
				.collect(Collectors.toList());
		return canciones;
	}

	public void addCancion(Cancion cancion) {
		canciones.add(cancion);
	}

	public void removeCancion(Cancion cancion) {
		canciones.remove(cancion);
	}

	private void cargarCatalogo() throws DAOException {
		List<Cancion> cancionesBD = adaptadorCanciones.recuperarTodasCanciones();
		for (Cancion cancion : cancionesBD)
			canciones.add(cancion);
	}

	public boolean comprobarRepetida(String titulo, String interprete) throws DAOException {
		for (Cancion cancion : this.getCanciones()) {
			if ((cancion.getTitulo().equals(titulo)) && (cancion.getInterprete().equals(interprete))) {
				return true;
			}
		}
		return false;
	}

	public List<Cancion> getMasReproducidas() throws DAOException {
		//un buen ejemplo de uso de streams en el que se obtienen las mas reproducidas de toda la aplicación.
		List<Cancion> canciones = new ArrayList<Cancion>();
		canciones = this.getCanciones().stream()
				.sorted(Comparator.comparingInt(Cancion::getNumReproducciones).reversed()).limit(10)
				.filter(c -> c.getNumReproducciones() > 0)
				.collect(Collectors.toList());
		return canciones;
	}

}
