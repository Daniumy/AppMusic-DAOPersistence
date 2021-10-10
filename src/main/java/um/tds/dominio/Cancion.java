package um.tds.dominio;

import java.util.LinkedList;
import java.util.List;


public class Cancion {
	private int id;
	private String titulo;
	private String rutaFichero;
	private int numReproducciones;
	private String interprete;
	private String estilo;

	public Cancion(String titulo, String rutaFichero, int numReproducciones, String interprete,
			String estilo) {
		this.id = 0;
		this.titulo = titulo;
		this.rutaFichero = rutaFichero;
		this.numReproducciones = numReproducciones;
		this.interprete = interprete;
		this.estilo = estilo;
	}

	public String getTitulo() {
		return titulo;
	}
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getRutaFichero() {
		return rutaFichero;
	}

	public void setRutaFichero(String rutaFichero) {
		this.rutaFichero = rutaFichero;
	}

	public int getNumReproducciones() {
		return numReproducciones;
	}

	public void sumaReproducción(){
		this.numReproducciones += 1;
	}

	public String getInterprete() {
		return interprete;
	}

	public void setInterprete(String interprete) {
		this.interprete = interprete;
	}

	public String getEstilo() {
		return estilo;
	}

	public void setEstilo(String estilo) {
		this.estilo = estilo;
	}

	public void nuevaReproduccion() {
		this.numReproducciones += 1;
	}
	
	
}
