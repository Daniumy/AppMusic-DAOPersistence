package um.tds.controlador;

import um.tds.dao.DAOException;
import um.tds.dao.FactoriaDAO;
import um.tds.dominio.Usuario;
import um.tds.dominio.Descuento;
import umu.tds.componente.CancionesCargador;
import umu.tds.componente.CancionesEvent;
import umu.tds.componente.CancionesListener;
import um.tds.dominio.CatalogoUsuarios;
import um.tds.dominio.ListaCanciones;
import um.tds.dominio.Cancion;
import um.tds.dominio.CatalogoCanciones;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import um.tds.CargadorCanciones;
import um.tds.dao.*;

public final class Controlador implements CancionesListener {
	private static Controlador unicaInstancia;
	private CatalogoCanciones catalogoCanciones;
	private CatalogoUsuarios catalogoUsuarios;
	private Usuario usuarioActual;
	private IAdaptadorCancionesDAO adaptadorCanciones;
	private IAdaptadorUsuarioDAO adaptadorUsuario;
	private IAdaptadorPlaylistsDAO adaptadorPlaylists;
	private FactoriaDAO factoria;
	private ArrayList<Cancion> ultimaListaCanciones = new ArrayList<Cancion>();
	private CancionesCargador cargadorCanciones = new CancionesCargador();
	private MediaPlayer mediaPlayer = null;
	private String binPath;
	private String tempPath;
	private int ultimoIndice;
	private boolean ultimaFueRecientes;

	private Controlador() {
		inicializarAdaptadores();
		inicializarCatalogos();
		cargadorCanciones.addOyente(this);
		usuarioActual = null;
		try {
			factoria = FactoriaDAO.getInstancia();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	public static Controlador getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new Controlador();
		return unicaInstancia;
	}

	public Usuario getUsuarioActual() {
		return usuarioActual;
	}

	public boolean esUsuarioRegistrado(String login) {
		return catalogoUsuarios.getUsuario(login) != null;
	}

	public boolean loginUsuario(String nombre, String password) {
		Usuario usuario = catalogoUsuarios.getUsuario(nombre);
		if (usuario != null && usuario.getPassword().equals(password)) {
			this.usuarioActual = usuario;
			return true;
		}
		return false;
	}

	public void cargarMusica(String ruta_directorio) {
		// es la clase cargadora la que creará las canciones, la cual considero más
		// propia para ser la responsable de cargar y CREAR las canciones,
		// preferiblemente antes que el controlador.
		LinkedList<Cancion> canciones = CargadorCanciones.parsearCanciones(ruta_directorio);
		// persisto las canciones creadas y las añado al catálogo de canciones
		adaptadorCanciones.persistirCanciones(canciones);
		for (Cancion cancion : canciones) {
			catalogoCanciones.addCancion(cancion);
		}
	}

	public List<Cancion> filtrarCanciones(String interprete, String titulo, String estilo) {
		List<Cancion> canciones = new ArrayList<Cancion>();
		// busco en el catalogo canciones que coincidan con el filtro
		canciones = catalogoCanciones.filtrarCanciones(interprete, titulo, estilo);
		return canciones;
	}

	public boolean registrarUsuario(String nombre, String apellidos, String email, String login, String password,
			String fechaNacimiento) {

		if (esUsuarioRegistrado(login))
			return false;
		// creamos el usuario con el controlador ya que los catálogos nunca son los
		// responsables de crear objetos
		Usuario usuario = new Usuario(nombre, apellidos, email, login, password, fechaNacimiento);
		// creamos el usuario también en la base de datos
		adaptadorUsuario.create(usuario);
		// lo añadimos al catalogo
		catalogoUsuarios.addUsuario(usuario);
		return true;
	}

	public boolean borrarUsuario(Usuario usuario) {
		if (!esUsuarioRegistrado(usuario.getLogin()))
			return false;
		adaptadorUsuario.delete(usuario);
		CatalogoUsuarios.getUnicaInstancia().removeUsuario(usuario);
		return true;
	}

	public void registrarPlaylist(List<Cancion> canciones, String nombre) {
		// creamos la playlist en el usuario, luego la registramos en el DAO de las
		// playlists y actualizamos en persistencia el usuario que contiene dicha
		// playlist
		ListaCanciones lista = usuarioActual.crearPlaylist(canciones, nombre);
		adaptadorPlaylists.registrarListaCanciones(lista);
		adaptadorUsuario.updatePerfil(usuarioActual);

	}

	public void modificarPlaylist(List<Cancion> canciones, String nombre) {
		// lo mismo que al registrar solo que modificando una ya existente
		ListaCanciones lista = usuarioActual.modificarPlaylist(canciones, nombre);
		adaptadorPlaylists.modificarListaCanciones(lista);
		adaptadorUsuario.updatePerfil(usuarioActual);
	}

	public void cargarCanciones(String fichero) throws DAOException {
		// metodo para cargar las canciones xml
		cargadorCanciones.setArchivoCanciones(fichero);
		CatalogoCanciones.getUnicaInstancia();
	}

	public ArrayList<ListaCanciones> getPlaylistsUsuario() {
		return usuarioActual.getPlaylists();
	}

	public ListaCanciones getPlaylistConcretaUsuario(String nombreLista) {
		return this.usuarioActual.getPlaylistConcreta(nombreLista);
	}

	private void inicializarCatalogos() {
		catalogoCanciones = CatalogoCanciones.getUnicaInstancia();
		catalogoUsuarios = CatalogoUsuarios.getUnicaInstancia();
	}

	private void inicializarAdaptadores() {
		FactoriaDAO factoria = null;
		try {
			factoria = FactoriaDAO.getInstancia(FactoriaDAO.DAO_TDS);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		adaptadorCanciones = factoria.getCancionesDAO();
		adaptadorUsuario = factoria.getUsuarioDAO();
		adaptadorPlaylists = factoria.getPlaylistsDAO();
	}

	public boolean checkNombreListaIgual(String nombreLista) {
		return usuarioActual.checkNombreListaIgual(nombreLista);
	}

	public void play(Cancion cancion, ArrayList<Cancion> canciones, int indiceCancion) {
		//metodo que trata cada vez que se presiona el botón del play
		binPath = Controlador.class.getClassLoader().getResource(".").getPath();
		binPath = binPath.replaceFirst("/", "");
		tempPath = binPath.replace("/bin", "/temp");
		tempPath = tempPath.replace("%20", " ");
		URL uri = null;
		// compruebo si habia una ejecutandose al darle al play, en ese caso paro su
		// reproduccion ya que se va a reproducir otra
		if ((mediaPlayer != null) && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING && cancion != null) {
			stop(cancion);
		}
		//si habia una pausada y se le ha dado al play, se continua por donde se quedó pausada.
		if ((mediaPlayer != null) && (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) && (cancion == null)) {
			mediaPlayer.play();

		} else {
			//si no pues reproduce la canción tal y como se enseñó en clase.
			String ruta = cancion.getRutaFichero();
			try {
				com.sun.javafx.application.PlatformImpl.startup(() -> {
				});
				if (ruta.contains("C:\\")) {
					File f = new File(ruta);
					mediaPlayer = new MediaPlayer(new Media(f.toURI().toString()));
				} else {
					uri = new URL(ruta);

					System.setProperty("java.io.tmpdir", tempPath);
					Path mp3 = Files.createTempFile("now-playing", ".mp3");

					try (InputStream stream = uri.openStream()) {
						Files.copy(stream, mp3, StandardCopyOption.REPLACE_EXISTING);
					}

					Media media = new Media(mp3.toFile().toURI().toString());
					mediaPlayer = new MediaPlayer(media);
				}
				mediaPlayer.play();
				//actualizo ciertos valores que me serán de utilidad, especialmente el de la lista "ultimaListaCanciones" crucial para la reproduccion de "siguientes" y "anteriores"
				this.ultimaListaCanciones = canciones;
				this.ultimoIndice = indiceCancion;
				//sumamos la reproduccion de dicha cancion
				cancion.nuevaReproduccion();
				//actualizamos dicha cancion en persistencia
				AdaptadorCancionesDAO.getUnicaInstancia().modificarCancion(cancion);
				//la añadimos a las canciones recientes de dicho usuario, y avisamos de que tiene una nueva cancion reproducida y actualizamos la persistencia con estos nuevos cambios
				cancionReciente(cancion);
				cancionReproducidaPorUsuario(cancion);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public Cancion playNext() {
		//si se estaba reproduciendo una cancion, con las variables que actualizamos anteriormente, reproducimos la siguiente cancion de la lista.
		if (mediaPlayer != null) {
			if (ultimoIndice == ultimaListaCanciones.size() - 1)
				ultimoIndice = -1;
			ultimoIndice += 1;
			Cancion cancion = this.ultimaListaCanciones.get(ultimoIndice);
			play(cancion, ultimaListaCanciones, ultimoIndice);
			return cancion;
		}
		return null;
	}

	public Cancion playPrevious() {
		if (mediaPlayer != null) {
			if (ultimoIndice == 0)
				ultimoIndice = ultimaListaCanciones.size();
			ultimoIndice -= 1;
			Cancion cancion = this.ultimaListaCanciones.get(ultimoIndice);
			play(cancion, ultimaListaCanciones, ultimoIndice);
			return cancion;
		}
		return null;
	}

	public void pause() {
		//se pausa
		if (mediaPlayer != null)
			mediaPlayer.pause();
	}

	public void cancionReciente(Cancion cancion) {
		boolean isRepetida = usuarioActual.addReciente(cancion);
		if (!isRepetida)
			adaptadorUsuario.updatePerfil(usuarioActual);
	}

	private void cancionReproducidaPorUsuario(Cancion cancion) {
		usuarioActual.añadirCancionReproducida(cancion);
		adaptadorUsuario.updatePerfil(usuarioActual);
	}

	public void setUserPremium() {
		//se cambia la variable premium del usuario y se persiste
		usuarioActual.setPremium(true);
		adaptadorUsuario.updatePerfilPremium(usuarioActual);
	}

	public void stop(Cancion cancion) {
		if (mediaPlayer != null)
			mediaPlayer.stop();
		File directorio = new File(tempPath);
		String[] files = directorio.list();
		for (String archivo : files) {
			File fichero = new File(tempPath + File.separator + archivo);
			fichero.delete();
		}
	}

	@Override
	public void nuevasCanciones(CancionesEvent ev) {
		//este método es el encargado de recibir la notificación de eventos cancion xml y de tratar dichas canciones como deba hacerlo para su posterior reproduccion
		boolean existe = true;
		List<Cancion> songsaux = new LinkedList<>();
		List<umu.tds.componente.Cancion> canciones = ev.getCancionesNuevas().getCancion();
		for (umu.tds.componente.Cancion c : canciones) {
			
			Cancion ca = new Cancion(c.getTitulo(), c.getURL(), 0, c.getInterprete(), c.getEstilo());
			if (!ca.getRutaFichero().startsWith("http")) {
				File file = new File(ca.getRutaFichero());
				if (!file.exists()) {
					existe = false;
				}
			}
			try {
				if (!CatalogoCanciones.getUnicaInstancia().comprobarRepetida(c.getTitulo(), c.getInterprete())
						&& existe) {
					songsaux.add(ca);
					catalogoCanciones.addCancion(ca);
				}
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!existe)
				existe = true;
		}
		adaptadorCanciones.persistirCanciones(songsaux);
	}

	public ArrayList<Cancion> getRecientes() {
		return usuarioActual.getRecientes();

	}

	public boolean isCancionPausada() {
		if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED)
			return true;
		return false;
	}

	public HashMap<Cancion, Integer> getMasReproducidasUsuario() {
		return this.usuarioActual.recuperarMasReproducidas();
	}

	public List<Cancion> getMasReproducidasSistema() throws DAOException {
		return this.catalogoCanciones.getMasReproducidas();
	}

	public boolean isUsuarioPremium() {
		return usuarioActual.isPremium();
	}

	public List<Descuento> getDescuentosAplicablesUsuario() throws ParseException {
		return this.usuarioActual.getDescuentosAplicables();

	}

	public void generarPdf(String rutaPDF) throws FileNotFoundException, DocumentException {
		//metodo para generar el pdf donde se haya indicado en "rutaPDF" como argumento.
		binPath = Controlador.class.getClassLoader().getResource(".").getPath();
		FileOutputStream archivo = new FileOutputStream(rutaPDF + "\\DocumentoPDFAppMusic.pdf");
		Document documento = new Document();
		PdfWriter.getInstance(documento, archivo);
		documento.open();
		for (ListaCanciones playlist : usuarioActual.getPlaylists()) {
			documento.add(new Paragraph("Nombre playlist: " + playlist.getNombre()));
			List<Cancion> canciones = playlist.getCanciones();
			for (Cancion cancion : canciones) {
				documento.add(new Paragraph(
						cancion.getTitulo() + " - " + cancion.getInterprete() + " - " + cancion.getEstilo()));
			}
			documento.add(new Paragraph("******************************************"));
		}
		documento.close();
	}

	public String getNombreUsuario() {
		return this.usuarioActual.getNombre();
	}

}
