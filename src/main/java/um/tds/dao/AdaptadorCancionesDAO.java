package um.tds.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import um.tds.dominio.Cancion;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import beans.Entidad;
import beans.Propiedad;

public class AdaptadorCancionesDAO implements IAdaptadorCancionesDAO {
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorCancionesDAO unicaInstancia;

	public static AdaptadorCancionesDAO getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			return new AdaptadorCancionesDAO();
		else
			return unicaInstancia;
	}

	public AdaptadorCancionesDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	private Cancion entidadToCancion(Entidad eCancion) {

		String titulo = servPersistencia.recuperarPropiedadEntidad(eCancion, "titulo");
		String rutaFichero = servPersistencia.recuperarPropiedadEntidad(eCancion, "rutaFichero");
		int numReproducciones = Integer
				.parseInt(servPersistencia.recuperarPropiedadEntidad(eCancion, "numReproducciones"));
		String interprete = servPersistencia.recuperarPropiedadEntidad(eCancion, "interprete");
		String estilo = servPersistencia.recuperarPropiedadEntidad(eCancion, "estilo");

		Cancion cancion = new Cancion(titulo, rutaFichero, numReproducciones, interprete, estilo);
		cancion.setId(eCancion.getId());
		PoolDAO.getUnicaInstancia().addObjeto(cancion.getId(), cancion);
		return cancion;
	}

	private Entidad cancionToEntidad(Cancion cancion) {
		Entidad eCancion = new Entidad();
		eCancion.setNombre("Cancion");
		eCancion.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad("titulo", cancion.getTitulo()),
				new Propiedad("rutaFichero", cancion.getRutaFichero()),
				new Propiedad("numReproducciones", String.valueOf(cancion.getNumReproducciones())),
				new Propiedad("interprete", cancion.getInterprete()), new Propiedad("estilo", cancion.getEstilo()))));
		return eCancion;
	}

	public void modificarCancion(Cancion cancion) {
		Entidad eCancion = servPersistencia.recuperarEntidad(cancion.getId());
		servPersistencia.eliminarPropiedadEntidad(eCancion, "numReproducciones");
		servPersistencia.anadirPropiedadEntidad(eCancion, "numReproducciones",
				String.valueOf(cancion.getNumReproducciones()));
	}

	public void persistirCanciones(List<Cancion> canciones) {
		Entidad eSong = null;
		boolean existe = true;

		for (Cancion cancion : canciones) {
			try {
				eSong = servPersistencia.recuperarEntidad(cancion.getId());
			} catch (NullPointerException e) {
				existe = false;
			}
			if (eSong == null) {
				existe = false;
			}
			if (existe) {
				return;
			}

			Entidad eCancion = this.cancionToEntidad(cancion);
			eCancion = servPersistencia.registrarEntidad(eCancion);
			cancion.setId(eCancion.getId());
		}
	}

	public Cancion get(int id) {
		if (PoolDAO.getUnicaInstancia().contiene(id))
			return (Cancion) PoolDAO.getUnicaInstancia().getObjeto(id);
		
		Entidad eCancion = servPersistencia.recuperarEntidad(id);
		return entidadToCancion(eCancion);
	}

	public List<Cancion> recuperarTodasCanciones() {
		List<Entidad> entidades = servPersistencia.recuperarEntidades("Cancion");
		List<Cancion> canciones = new LinkedList<Cancion>();
		for (Entidad eCancion : entidades) {
			canciones.add(get(eCancion.getId()));
		}
		return canciones;
	}
}
