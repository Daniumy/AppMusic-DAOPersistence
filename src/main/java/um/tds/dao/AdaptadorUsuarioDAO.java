package um.tds.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import um.tds.dominio.Cancion;
import um.tds.dominio.ListaCanciones;
import um.tds.dominio.Usuario;
import beans.Entidad;
import beans.Propiedad;

/**
 * 
 * Clase que implementa el Adaptador DAO concreto de Usuario para el tipo H2.
 * 
 */
public final class AdaptadorUsuarioDAO implements IAdaptadorUsuarioDAO {

	private static final String USUARIO = "Usuario";

	private static final String NOMBRE = "nombre";
	private static final String APELLIDOS = "apellidos";
	private static final String EMAIL = "email";
	private static final String LOGIN = "login";
	private static final String PASSWORD = "password";
	private static final String FECHA_NACIMIENTO = "fechaNacimiento";

	private ServicioPersistencia servPersistencia;

	private static AdaptadorUsuarioDAO unicaInstancia;

	public static AdaptadorUsuarioDAO getUnicaInstancia() { // patron singleton
		if (unicaInstancia == null)
			return new AdaptadorUsuarioDAO();
		else
			return unicaInstancia;
	}

	public AdaptadorUsuarioDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	private Usuario entidadToUsuario(Entidad eUsuario) {
		// añadiré aquí un comentario acerca de como se persiste el hashmap ya que es lo
		// único que se sale de la monotonía de la persistencia de todo el resto de
		// clases y atributos, se obtiene la lista de canciones en "reproducidas" a
		// partir de los codigos de estas, y los numeros de reproducciones de cada
		// canción correspondiente a partir de sus "codigos". Y con estas dos listas de
		// cancion - nreproducciones se crea el hashmap
		List<ListaCanciones> playlists = new LinkedList<ListaCanciones>();
		List<Cancion> recientes = new ArrayList<Cancion>();
		List<Cancion> reproducidas = new ArrayList<Cancion>();
		List<Integer> nreproducciones = new ArrayList<Integer>();
		HashMap<Cancion, Integer> cancionesReproducidas = new HashMap<Cancion, Integer>();
		String premium = servPersistencia.recuperarPropiedadEntidad(eUsuario, "premium");
		String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, NOMBRE);
		String apellidos = servPersistencia.recuperarPropiedadEntidad(eUsuario, APELLIDOS);
		String email = servPersistencia.recuperarPropiedadEntidad(eUsuario, EMAIL);
		String login = servPersistencia.recuperarPropiedadEntidad(eUsuario, LOGIN);
		String password = servPersistencia.recuperarPropiedadEntidad(eUsuario, PASSWORD);
		String fechaNacimiento = servPersistencia.recuperarPropiedadEntidad(eUsuario, FECHA_NACIMIENTO);
		recientes = obtenerCancionesDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, "recientes"));
		playlists = obtenerPlaylistsDesdeCodigos(
				servPersistencia.recuperarPropiedadEntidad(eUsuario, "listaCanciones"));
		reproducidas = obtenerCancionesDesdeCodigos(
				servPersistencia.recuperarPropiedadEntidad(eUsuario, "reproducidas"));
		nreproducciones = obtenerReproduccionesDesdeCodigos(
				servPersistencia.recuperarPropiedadEntidad(eUsuario, "nreproducciones"));
		Usuario usuario = new Usuario(nombre, apellidos, email, login, password, fechaNacimiento);
		usuario.setPremium(premium.equals("1"));
		if (playlists != null)
			for (ListaCanciones playlist : playlists) {
				usuario.addPlaylist(playlist);
			}
		if (recientes != null)
			for (Cancion cancion : recientes) {
				usuario.addReciente(cancion);
			}
		if (reproducidas != null) {
			for (int i = 0; i < reproducidas.size(); i++) {
				cancionesReproducidas.put(reproducidas.get(i), nreproducciones.get(i));
			}
			usuario.setCancionesReproducidas(cancionesReproducidas);
		}
		usuario.setId(eUsuario.getId());
		PoolDAO.getUnicaInstancia().addObjeto(usuario.getId(), usuario);
		return usuario;
	}

	private Entidad usuarioToEntidad(Usuario usuario) {
		//como vemos se guarda el hashmap como una lista de canciones reproduciadas y de numer de reproduccionees.
		Entidad eUsuario = new Entidad();
		ArrayList<Cancion> cancionesReproducidas = usuario.getListCancionesReproducidas();
		String esPremium = "0";
		if (usuario.isPremium())
			esPremium = "1";
		eUsuario.setNombre(USUARIO);

		eUsuario.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(new Propiedad(NOMBRE, usuario.getNombre()),
				new Propiedad(APELLIDOS, usuario.getApellidos()), new Propiedad(EMAIL, usuario.getEmail()),
				new Propiedad(LOGIN, usuario.getLogin()), new Propiedad(PASSWORD, usuario.getPassword()),
				new Propiedad(FECHA_NACIMIENTO, usuario.getFechaNacimiento()), new Propiedad("premium", esPremium),
				new Propiedad("recientes", obtenerCodigosCanciones(usuario.getRecientes())),
				new Propiedad("listaCanciones", obtenerCodigosListas(usuario.getPlaylists())),
				new Propiedad("reproducidas", obtenerCodigosCanciones(cancionesReproducidas)),
				new Propiedad("nreproducciones", obtenerNumeroReproducciones(usuario)))));

		return eUsuario;
	}

	public void create(Usuario usuario) {
		Entidad eUsuario = this.usuarioToEntidad(usuario);
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		usuario.setId(eUsuario.getId());
	}

	public boolean delete(Usuario usuario) {
		Entidad eUsuario;
		eUsuario = servPersistencia.recuperarEntidad(usuario.getId());

		return servPersistencia.borrarEntidad(eUsuario);
	}

	public void updatePerfil(Usuario usuario) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getId());
		servPersistencia.eliminarPropiedadEntidad(eUsuario, "recientes");
		String lineas = obtenerCodigosCanciones(usuario.getRecientes());
		servPersistencia.anadirPropiedadEntidad(eUsuario, "recientes", lineas);
		servPersistencia.eliminarPropiedadEntidad(eUsuario, "listaCanciones");
		String lineass = obtenerCodigosListas(usuario.getPlaylists());
		servPersistencia.anadirPropiedadEntidad(eUsuario, "listaCanciones", lineass);
		servPersistencia.eliminarPropiedadEntidad(eUsuario, "reproducidas");
		String lineasss = obtenerCodigosCanciones(usuario.getListCancionesReproducidas());
		servPersistencia.anadirPropiedadEntidad(eUsuario, "reproducidas", lineasss);
		servPersistencia.eliminarPropiedadEntidad(eUsuario, "nreproducciones");
		String lineassss = obtenerNumeroReproducciones(usuario);
		servPersistencia.anadirPropiedadEntidad(eUsuario, "nreproducciones", lineassss);
	}

	/*
	 * he hecho metodo aparte del update perfil para no tener que hacer todos esos
	 * registros en base de datos solo por hacerse premium, se podría haber hecho
	 * con más metodos con los diferentes atributos que se cambian en el
	 * updatePerfil.
	 */
	@Override
	public void updatePerfilPremium(Usuario usuarioActual) {
		String esPremium = "0";
		if (usuarioActual.isPremium())
			esPremium = "1";
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuarioActual.getId());
		servPersistencia.eliminarPropiedadEntidad(eUsuario, "premium");
		servPersistencia.anadirPropiedadEntidad(eUsuario, "premium", esPremium);
	}

	public Usuario get(int id) {
		if (PoolDAO.getUnicaInstancia().contiene(id))
			return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(id);
		Entidad eUsuario = servPersistencia.recuperarEntidad(id);// tabien

		return entidadToUsuario(eUsuario);
	}

	public List<Usuario> getAll() {
		List<Entidad> entidades = servPersistencia.recuperarEntidades(USUARIO);

		List<Usuario> usuarios = new LinkedList<Usuario>();
		for (Entidad eUsuario : entidades) {
			usuarios.add(get(eUsuario.getId()));// tabien
		}

		return usuarios;
	}

	// -------------------Funciones auxiliares-----------------------------
	private String obtenerCodigosListas(List<ListaCanciones> lista) {
		String aux = "";
		for (ListaCanciones listaux : lista) {
			aux += listaux.getCodigo() + " ";
		}
		return aux.trim();
	}

	private String obtenerCodigosCanciones(ArrayList<Cancion> recientes) {
		String aux = "";
		for (Cancion cancion : recientes) {
			aux += cancion.getId() + " ";
		}
		return aux.trim();
	}

	private String obtenerNumeroReproducciones(Usuario usuario2) {
		String aux = "";
		for (int i : usuario2.getCancionesReproducidas().values()) {
			aux += i + " ";
		}
		return aux.trim();
	}

	private List<ListaCanciones> obtenerPlaylistsDesdeCodigos(String lineas) {
		if (lineas == null)
			return null;
		List<ListaCanciones> listas = new LinkedList<ListaCanciones>();
		StringTokenizer strTok = new StringTokenizer(lineas, " ");
		AdaptadorPlaylistsDAO adaptadorPlaylists = AdaptadorPlaylistsDAO.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			listas.add(adaptadorPlaylists.recuperarListaCanciones(Integer.valueOf((String) strTok.nextElement())));
		}
		return listas;
	}

	private List<Cancion> obtenerCancionesDesdeCodigos(String lineas) {
		if (lineas == null)
			return null;
		List<Cancion> listas = new LinkedList<Cancion>();
		StringTokenizer strTok = new StringTokenizer(lineas, " ");
		AdaptadorCancionesDAO adaptadorCanciones = AdaptadorCancionesDAO.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			listas.add(adaptadorCanciones.get(Integer.valueOf((String) strTok.nextElement())));
		}
		return listas;
	}

	private List<Integer> obtenerReproduccionesDesdeCodigos(String lineas) {
		if (lineas == null)
			return null;
		List<Integer> nreproducciones = new LinkedList<Integer>();
		StringTokenizer strTok = new StringTokenizer(lineas, " ");
		while (strTok.hasMoreTokens()) {
			nreproducciones.add(Integer.parseInt((String) strTok.nextElement()));
		}
		return nreproducciones;
	}

}
