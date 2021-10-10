package um.tds.dominio;

public class DescuentoJovenes implements Descuento {
	private String nombre;
	public DescuentoJovenes() {
		this.nombre = "DescuentoJovenes";
	}

	@Override
	public double calcDescuento() {
		return 40;
	}

	@Override
	public String getNombre() {
		return nombre;
	}

}
