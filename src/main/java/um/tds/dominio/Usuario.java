package um.tds.dominio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Usuario {

	private int id;
	private String nombre;
	private String apellidos;
	private String email;
	private String login;
	private String password;
	private String fechaNacimiento;
	private List<Cancion> recientes;
	private List<ListaCanciones> listaCanciones;
	private boolean premium;
	private Descuento descuento;
	private HashMap<Cancion, Integer> cancionesReproducidas;

	public Usuario(String nombre, String apellidos, String email, String login, String password,
			String fechaNacimiento) {
		this.id = 0;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.login = login;
		this.password = password;
		this.fechaNacimiento = fechaNacimiento;
		this.premium = false;
		this.descuento = null;
		this.recientes = new ArrayList<Cancion>();
		this.listaCanciones = new ArrayList<ListaCanciones>();
		this.cancionesReproducidas = new HashMap<Cancion, Integer>();
	}

	public HashMap<Cancion, Integer> getCancionesReproducidas() {
		return new HashMap<Cancion, Integer>(cancionesReproducidas);
	}

	public void setCancionesReproducidas(HashMap<Cancion, Integer> cancionesReproducidass) {
		this.cancionesReproducidas = new HashMap<Cancion, Integer>(cancionesReproducidass);
	}

	public void añadirCancionReproducida(Cancion cancion) {
		// si esa cancion ya estaba añadida, unicaemente se le suma una reproducción con
		// ese simple "+1", else, si no lo estaba se añade, con una unica reproduccion
		if (cancionesReproducidas.containsKey(cancion)) {
			cancionesReproducidas.replace(cancion, cancionesReproducidas.get(cancion) + 1);
		} else
			cancionesReproducidas.put(cancion, 1);
	}

	public HashMap<Cancion, Integer> recuperarMasReproducidas() {
		//muy buen ejemplo de uso de streams en el que recuperamos las canciones que más a reproducido cierto usuario concreto
		HashMap<Cancion, Integer> cancionesyrepros = new HashMap<Cancion, Integer>();
		ArrayList<Cancion> canciones = new ArrayList<Cancion>();
		canciones = (ArrayList<Cancion>) cancionesReproducidas.keySet().stream()
				.sorted(Comparator.comparingInt(c -> cancionesReproducidas.get(c)).reversed()).limit(10)
				.collect(Collectors.toList());
		for (Cancion cancion : canciones) {
			cancionesyrepros.put(cancion, cancionesReproducidas.get(cancion));
		}
		return cancionesyrepros;
	}

	public void setDescuento(Descuento descuento) {
		this.descuento = descuento; // en el main se hará lo de Uusario.setDescuento(new DescuentoFijo);
	}

	public double calcularDescuento() {
		return descuento.calcDescuento();
	}

	public boolean addReciente(Cancion song) {
		//si ya esta añadida, pues devolvemos true para indicar al que llame que ya estaba añadida, si no, se añade(eliminando la primera cancion reciente que habia si ya eran 10)
		if (recientes.contains(song))
			return true;
		recientes.add(song);
		if (recientes.size() > 10) {
			recientes.remove(0);
		}
		return false;
	}

	public ArrayList<Cancion> getRecientes() {
		return new ArrayList<Cancion>(this.recientes);
	}

	public void addPlaylist(ListaCanciones playlist) {
		listaCanciones.add(playlist);
	}

	public ListaCanciones crearPlaylist(List<Cancion> canciones, String nombre) {
		// el usuario es el responsable de crear las playlist, no se viola el patrón
		// creador
		ListaCanciones lista = new ListaCanciones(nombre, canciones);
		addPlaylist(lista);
		return lista;
	}

	public ListaCanciones modificarPlaylist(List<Cancion> canciones, String nombre) {
		ListaCanciones lista = getPlaylistConcreta(nombre);
		lista.setCanciones(canciones);
		return lista;
	}

	public ArrayList<ListaCanciones> getPlaylists() {
		return new ArrayList<ListaCanciones>(listaCanciones); // cambiar a return lista canciones a ver q pasa
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public boolean isPremium() {
		return premium;
	}

	public void setPremium(boolean aux) {
		this.premium = aux;
	}

	public boolean checkNombreListaIgual(String nombreLista) {
		for (ListaCanciones playlist : this.listaCanciones) {
			if (playlist.getNombre().equals(nombreLista))
				return true;
		}
		return false;
	}

	public void setPlaylists(List<ListaCanciones> userPlaylist) {
		this.listaCanciones = new ArrayList<ListaCanciones>(userPlaylist);

	}

	public ListaCanciones getPlaylistConcreta(String nombreLista) {
		ListaCanciones lista = getPlaylists().stream().filter(l -> l.getNombre().equals(nombreLista)).findAny().get();
		return lista;
	}

	public ArrayList<Cancion> getListCancionesReproducidas() {
		ArrayList<Cancion> canciones = new ArrayList<Cancion>(this.getCancionesReproducidas().keySet());
		return canciones;
	}

	public List<Descuento> getDescuentosAplicables() throws ParseException {
		// respeta patrón creador puesto que es el usuario el creador de descuentos
		List<Descuento> descuentos = new ArrayList<Descuento>();
		Descuento descuentoFijo = new DescuentoFijo();
		descuentos.add(descuentoFijo);
		if (calcularEdad() < 18) {
			Descuento descuentoJovenes = new DescuentoJovenes();
			descuentos.add(descuentoJovenes);
		}
		return descuentos;
	}

	private float calcularEdad() throws ParseException {
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
		Date date1 = formatter1.parse(fechaNacimiento);
		LocalDateTime now = LocalDateTime.now();
		Date dateaux = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
		Date date2 = formatter1.parse(formatter1.format(dateaux));
		long diff = date2.getTime() - date1.getTime();
		float days = (diff / (1000 * 60 * 60 * 24));
		return days / 365;
	}

}
