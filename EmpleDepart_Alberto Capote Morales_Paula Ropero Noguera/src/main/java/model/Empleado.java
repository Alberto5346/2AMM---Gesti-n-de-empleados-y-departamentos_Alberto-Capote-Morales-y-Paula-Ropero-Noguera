package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Empleado {
	private Integer id;
	private String nombre;
	private Double salario;
	private Departamento departamento;

	public Empleado(Integer id, String nombre, Double salario) {
		this.id = id;
		this.nombre = nombre;
		this.salario = salario;
	}

	public Empleado(String nombre, double salario, Departamento departamento) {
		this.nombre = nombre;
		this.salario = salario;
		this.departamento=departamento;
	}
}
