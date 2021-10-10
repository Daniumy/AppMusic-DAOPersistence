package um.tds.controlador;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import um.tds.dominio.Cancion;
import um.tds.dominio.ListaCanciones;

public class ControladorTest {

	@Test
	public void testRegistrarUsuario() {
		//será falso tras la primera prueba, ya que ya estaría registrado
		String nombre = "a";
		String apellidos = "a";
		String email = "a@gmail.com";
		String login = "userNotRegisteredYet";
		String password = "a";
		String fechaNacimiento = "20/10/2000";
		boolean shouldBe = true; //si ya lo hemos registrado shouldBe debería de ser false para que fuese satisfactorio la prueba unitaria
		assertEquals(shouldBe, Controlador.getUnicaInstancia().registrarUsuario(nombre, apellidos, email, login,password, fechaNacimiento));
	}
	
 
	@Test
	public void testLoginErroneo() {
		String login = "whatever";
		String pass = "whatever";
 
		boolean shouldBe = false;
		assertEquals(shouldBe, Controlador.getUnicaInstancia().loginUsuario(login, pass));
	}
	

	// check if es usuario registrado se le pasa el login
	@Test
	public void testIfUserIsRegistered() {
		String login = "userNotRegisteredYet";
		boolean shouldBe = true;
		assertEquals(shouldBe, Controlador.getUnicaInstancia().esUsuarioRegistrado(login));
	}

	@Test
	public void testRegistrarYRecuperarPlaylist() {
		Controlador.getUnicaInstancia().loginUsuario("userNotRegisteredYet","a");
		List<Cancion> canciones = new ArrayList<Cancion>();
		Cancion cancion = new Cancion("a", "a", 0, "a","a");
		canciones.add(cancion);
		Controlador.getUnicaInstancia().registrarPlaylist(canciones, "nombreListaQueNoExiste");
		ListaCanciones dichaLista = Controlador.getUnicaInstancia().getPlaylistConcretaUsuario("nombreListaQueNoExiste");
		assertNotNull(dichaLista);		
		
	}
	
}
