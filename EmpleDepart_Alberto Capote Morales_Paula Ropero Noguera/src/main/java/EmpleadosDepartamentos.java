import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import dao.BD;
import model.Departamento;
import model.Empleado;

public class EmpleadosDepartamentos {

	private Connection conn = null;

	/**
	 * Constructor
	 */
	public EmpleadosDepartamentos() {
		conn = BD.getConnection();
		createTables();
	}

	public boolean addDepartamento(Departamento d) {
		String sql = """
				INSERT INTO departamento (id, nombre, jefe)
				VALUES (?, ?, ?)
				""";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, d.getId());
			ps.setString(2, d.getNombre());
			//ps.setEmpleado(3, d.getJefe());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
		}
		return false;
	}

	public boolean addEmpleado(Empleado e) {
		String sql = """
				INSERT INTO empleado (id, nombre,salario, departamento)
				VALUES (?, ?, ?, ?)
				""";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, e.getId());
			ps.setString(2, e.getNombre());
			ps.setDouble(3, e.getSalario());
			//ps.setString(4, e.getDepartamento());
			return ps.executeUpdate() > 0;
		} catch (SQLException e1) {
		}
		return false;
	}

	private void createTables() {
		String sql = null;
		if (BD.typeDB.equals("sqlite")) {
			sql = """
						CREATE TABLE IF NOT EXISTS departamento (
							id INTEGER PRIMARY KEY,
							nombre TEXT NOT NULL,
							jefe INTEGER,
							FOREIGN KEY (jefe)
						)
					""";

			sql = """
						CREATE TABLE IF NOT EXISTS empleado (
							id INTEGER PRIMARY KEY,
							nombre TEXT NOT NULL,
							salario REAL DEFAULT 0.0,
							departamento INTEGER,
							FOREIGN KEY (departamento)
						)
					""";
		}
		if (BD.typeDB.equals("mariadb")) {
			sql = """
					CREATE TABLE IF NOT EXISTS departamento(
						id INT PRIMARY KEY AUTOINCREMENT,
						nombre VARCHAR(255) NOT NULL,
						jefe INT
						)
					""";
			
			sql = """
					CREATE TABLE IF NOT EXISTS empleado (
						id INT PRIMARY KEY AUTOINCREMENT,
						nombre VARCHAR(255) NOT NULL,
						salario DECIMAL(10,2) DEFAULT 0.0,
						departamento INT
					)
				""";
		}
		try {
			conn.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
		}
	}

	/**
	 * Cierra la tabla
	 */
	public void close() {
		BD.close();
	}

}
