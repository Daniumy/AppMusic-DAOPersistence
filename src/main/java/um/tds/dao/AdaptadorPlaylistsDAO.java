package um.tds.dao;

import um.tds.dominio.Cancion;
import um.tds.dominio.ListaCanciones;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;



public class AdaptadorPlaylistsDAO implements IAdaptadorPlaylistsDAO{
	
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorPlaylistsDAO unicaInstancia = null;
	
	public static AdaptadorPlaylistsDAO getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null) {
			return new AdaptadorPlaylistsDAO();
		} else
			return unicaInstancia;
	}
	
	public AdaptadorPlaylistsDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	private Entidad listaToEntidad(ListaCanciones lista) {
		Entidad eLista = new Entidad();
		eLista.setNombre("listaCanciones");
		eLista.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad("nombre", lista.getNombre()),
				new Propiedad("canciones", obtenerCodigosCanciones(lista.getCanciones())))));
		return eLista;
	}
	

	public void registrarListaCanciones(ListaCanciones lista) 
	{
		Entidad eLista;
		boolean existe = true; 
		try {
			eLista = servPersistencia.recuperarEntidad(lista.getCodigo());
		} catch (NullPointerException e) {
			existe = false;
		}
		if (existe) return;
		
		// registrar primero los atributos que son objetos
		eLista = listaToEntidad(lista);
		eLista = servPersistencia.registrarEntidad(eLista);
		lista.setCodigo(eLista.getId());
		
	}
	
	@Override
	public void modificarListaCanciones(ListaCanciones lista) {
		Entidad ePlaylist = servPersistencia.recuperarEntidad(lista.getCodigo());
		String lineas = obtenerCodigosCanciones(lista.getCanciones());
		servPersistencia.eliminarPropiedadEntidad(ePlaylist,"canciones");
		servPersistencia.anadirPropiedadEntidad(ePlaylist, "canciones", lineas);
	}
	
	
	public void borrarListaCanciones(ListaCanciones lista) {
		Entidad eLista = servPersistencia.recuperarEntidad(lista.getCodigo());
		servPersistencia.borrarEntidad(eLista);
	}
	
	
	
	
	public ListaCanciones recuperarListaCanciones(int codigo) {
		if (PoolDAO.getUnicaInstancia().contiene(codigo))
			return (ListaCanciones) PoolDAO.getUnicaInstancia().getObjeto(codigo);
		Entidad eListaCanciones;
		String nombre;
		List<Cancion> canciones = new LinkedList<Cancion>(); 
		eListaCanciones = servPersistencia.recuperarEntidad(codigo);
		nombre = servPersistencia.recuperarPropiedadEntidad(eListaCanciones, "nombre");

		canciones = obtenerCancionesDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eListaCanciones, "canciones"));
		ListaCanciones lista = new ListaCanciones(nombre,canciones);
		lista.setCodigo(codigo);
		PoolDAO.getUnicaInstancia().addObjeto(lista.getCodigo(), lista);
		return lista;
	}
	
	
	
	

	// -------------------Funciones auxiliares-----------------------------
	private String obtenerCodigosCanciones(List<Cancion> canciones) {
		String aux = "";
		for (Cancion cancion : canciones) {
			aux += cancion.getId() + " ";
		}
		return aux.trim();
	}
	
	private List<Cancion> obtenerCancionesDesdeCodigos(String canciones) {
		List<Cancion> listaCanciones = new LinkedList<Cancion>();
		StringTokenizer strTok = new StringTokenizer(canciones, " ");
		AdaptadorCancionesDAO adaptadorCanciones = AdaptadorCancionesDAO.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			listaCanciones.add(adaptadorCanciones.get(Integer.valueOf((String) strTok.nextElement())));
		}
		return listaCanciones;
	}
	
	
	
}
