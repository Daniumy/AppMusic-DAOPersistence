package um.tds.dominio;
import java.util.ArrayList;
import java.util.List;


public class ListaCanciones {
	private int codigo;
	private String nombre;
	private List<Cancion> canciones;
	
	public ListaCanciones(String nombre,List<Cancion> canciones) {
		this.codigo = 0;
		this.nombre = nombre;
		this.canciones = new ArrayList<Cancion>(canciones);
	}
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setCanciones(List<Cancion> canciones) {
		this.canciones = new ArrayList<Cancion>(canciones);
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Cancion> getCanciones() {
		return new ArrayList<Cancion>(canciones);
	}
	
	public void addCancion(Cancion cancion) {
		canciones.add(cancion);
	}
	
	public void eliminarCancion (Cancion cancion) {
		canciones.remove(cancion);
	}
	
}
