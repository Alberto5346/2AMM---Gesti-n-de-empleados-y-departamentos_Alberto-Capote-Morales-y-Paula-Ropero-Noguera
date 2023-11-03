package model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Departamento {
	
	private Integer id;
	private String nombre;
	private Empleado jefe;

	public Departamento(Integer id,String nombre) {
		this.id=id;
		this.nombre=nombre;
	}
	public Departamento(String nombre, Empleado jefe) {
		this.nombre=nombre;
		this.jefe=jefe;
	}
}
