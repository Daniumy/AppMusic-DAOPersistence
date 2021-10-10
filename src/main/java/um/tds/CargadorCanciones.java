package um.tds;

import java.io.File;
import java.util.LinkedList;
import um.tds.dominio.Cancion;

public final class CargadorCanciones {
	
	public static LinkedList<Cancion> parsearCanciones(String rutaFichero){
		LinkedList<Cancion> canciones = new LinkedList<Cancion>();
		File directorio = new File(rutaFichero);
		for (File file : directorio.listFiles()) {
			if(file.isDirectory()) {
				String estilo = file.getName();
				for (File fileaux : file.listFiles()) {
					String cancion = fileaux.getName();
					if (cancion.substring(cancion.length()-4).equals(".mp3")) {
						int division = cancion.indexOf("-");
						Cancion cancionaux = new Cancion(cancion.substring(division + 1,cancion.length()-4),fileaux.getAbsolutePath(),0, cancion.substring(0, division), estilo);
						canciones.add(cancionaux);
					}	
				}
			}
		}
        return canciones;
    }
}
