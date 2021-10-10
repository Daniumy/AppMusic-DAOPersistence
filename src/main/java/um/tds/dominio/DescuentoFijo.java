package um.tds.dominio;

public class DescuentoFijo implements Descuento {
	private String nombre;
	public DescuentoFijo() {
		this.nombre = "DescuentoFijo";
	}

	@Override
	public double calcDescuento() {
		return 20;
	}

	@Override
	public String getNombre() {
		return nombre;
	}

	
}
