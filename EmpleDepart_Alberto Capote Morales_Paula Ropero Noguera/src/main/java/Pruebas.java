import dao.Tabla;

public class Pruebas {
	public static void main(String[] args) {
		Tabla tabla = new Tabla();
		

		System.out.println("Departamentos:");
		System.out.println(tabla.showDepartamento());

		System.out.println("Empleados:");
		System.out.println(tabla.showEmpleado());

		tabla.close(); // Make sure to close the connection when you're done.
	}

}
