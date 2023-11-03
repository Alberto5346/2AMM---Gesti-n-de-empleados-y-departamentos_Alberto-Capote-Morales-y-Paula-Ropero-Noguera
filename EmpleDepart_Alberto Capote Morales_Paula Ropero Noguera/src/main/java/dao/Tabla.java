package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Departamento;
import model.Empleado;

public class Tabla {

	private Connection conn = null;

	public Tabla() {
		conn = BD.getConnection();
		createTables();

	}

	public void close() {
		BD.close();
	}

	public String showDepartamento() {
		String sql = "SELECT id, nombre, jefe FROM departamento";
		try {
			StringBuffer sb = new StringBuffer();
			ResultSet rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				Departamento c = readDepartamento(rs);
				sb.append(c.toString());
				sb.append("\n");
			}
			return sb.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String showEmpleado() {
		String sql = "SELECT id, nombre, salario, departamento FROM empleado";
		try {
			StringBuffer sb = new StringBuffer();
			ResultSet rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				Empleado e = readEmpleado(rs);
				sb.append(e.toString());
				sb.append("\n");
			}
			return sb.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	public Departamento buscarDepartamentoPorCodigo(int id) {
		String sql = "SELECT id, nombre, jefe FROM departamento WHERE id = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return readDepartamento(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Empleado buscarEmpleadoPorCodigo(int id) {
		String sql = "SELECT id, nombre, salario, departamento FROM empleado WHERE id = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return readEmpleado(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Departamento> buscarPorNombreDepartamento(String inicio) {
		String sql = "SELECT id, nombre, jefe FROM departamento WHERE nombre LIKE ?";
		List<Departamento> departamentos = new ArrayList<Departamento>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, inicio + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				departamentos.add(readDepartamento(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return departamentos;
	}

	public List<Empleado> buscarPorNombreEmpleado(String inicio) {
		String sql = "SELECT id, nombre, salario, departamento FROM empleado WHERE nombre LIKE ?";
		List<Empleado> empleados = new ArrayList<Empleado>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, inicio + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				empleados.add(readEmpleado(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return empleados;
	}

	public boolean addDepartamento(Departamento c) {
	    // Verificar si ya existe un Departamento con el mismo ID
	    if (buscarDepartamentoPorCodigo(c.getId()) != null) {
	        System.out.println("Ya existe un Departamento con el mismo ID.");
	        return false;
	    }

	    String sql = "INSERT INTO departamento (id, nombre, jefe) VALUES (?, ?, ?)";
	    try {
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1, c.getId());
	        ps.setString(2, c.getNombre());
	        if (c.getJefe() != null) {
	            ps.setInt(3, c.getJefe().getId());
	        } else {
	            ps.setNull(3, java.sql.Types.INTEGER);
	        }

	        int rowsAffected = ps.executeUpdate();

	        if (rowsAffected > 0) {
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	public boolean addEmpleado(Empleado c) {
	    // Verificar si ya existe un Empleado con el mismo ID
	    if (buscarEmpleadoPorCodigo(c.getId()) != null) {
	        System.out.println("Ya existe un Empleado con el mismo ID.");
	        return false;
	    }

	    String sql = "INSERT INTO empleado (id, nombre, salario, departamento) VALUES (?, ?, ?, ?)";
	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, c.getId());
	        ps.setString(2, c.getNombre());
	        ps.setDouble(3, c.getSalario());
	        if (c.getDepartamento() != null) {
	            ps.setInt(4, c.getDepartamento().getId());
	        } else {
	            ps.setNull(4, java.sql.Types.INTEGER);
	        }

	        int rowsAffected = ps.executeUpdate();

	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}




	public boolean deleteDepartamento(int id) {
	    // Obtener el departamento antes de eliminarlo
	    Departamento departamento = buscarDepartamentoPorCodigo(id);
	    
	    // Si se encontró el departamento, obtener su jefe antes de eliminarlo
	    Empleado jefe = null;
	    if (departamento != null) {
	        jefe = departamento.getJefe();
	    }
	    
	    // Eliminar el departamento
	    String sql = "DELETE FROM departamento WHERE id = ?";
	    try {
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1, id);
	        int rowsAffected = ps.executeUpdate();
	        
	        // Si se eliminó el departamento, actualizar los empleados que pertenecían a este departamento
	        if (rowsAffected > 0) {
	            updateEmpleadosDepartamento(id, null);
	            // Si se encontró un jefe, actualizar el departamento para que tenga un jefe nulo
	            if (jefe != null) {
	                updateDepartamentoJefe(id, null);
	            }
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	public boolean deleteEmpleado(int id) {
	    // Obtener el empleado antes de eliminarlo
	    Empleado empleado = buscarEmpleadoPorCodigo(id);
	    
	    // Si se encontró el empleado, obtener el departamento al que pertenece antes de eliminarlo
	    Departamento departamento = null;
	    if (empleado != null) {
	        departamento = empleado.getDepartamento();
	    }
	    
	    // Si el empleado es jefe de un departamento, actualizar el departamento para que tenga un jefe nulo
	    if (departamento != null && departamento.getJefe() != null && departamento.getJefe().getId() == id) {
	        updateDepartamentoJefe(departamento.getId(), null);
	    }

	    // Eliminar el empleado
	    String sql = "DELETE FROM empleado WHERE id = ?";
	    try {
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1, id);
	        int rowsAffected = ps.executeUpdate();
	        
	        // Si se eliminó el empleado, actualizar el departamento al que pertenecía para tener un empleado nulo
	        if (rowsAffected > 0) {
	            updateEmpleadosDepartamento(departamento != null ? departamento.getId() : 0, null);
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	// ...

	private void updateEmpleadosDepartamento(int departamentoId, Empleado newEmpleado) {
	    String sql = "UPDATE empleado SET departamento = ? WHERE departamento = ?";
	    try {
	        PreparedStatement ps = conn.prepareStatement(sql);
	        if (newEmpleado != null) {
	            ps.setInt(1, newEmpleado.getId());
	        } else {
	            ps.setNull(1, java.sql.Types.INTEGER);
	        }
	        ps.setInt(2, departamentoId);
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	public void dropDepartamento() {
		String sql = "DELETE FROM departamento";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void dropEmpleado() {
		String sql = "DELETE FROM empleado";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Departamento readDepartamento(ResultSet rs) {
		try {
			int id = rs.getInt("id");
			String nombre = rs.getString("nombre");
			int jefeId = rs.getInt("jefe");
			Empleado jefe = buscarEmpleadoPorCodigo(jefeId);
			return new Departamento(id, nombre, jefe);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Empleado readEmpleado(ResultSet rs) {
		try {
			int id = rs.getInt("id");
			String nombre = rs.getString("nombre");
			double salario = rs.getDouble("salario");
			int departamentoId = rs.getInt("departamento");

			Departamento departamento = null;
			if (!rs.wasNull()) {
				departamento = buscarDepartamentoPorCodigo(departamentoId);
			}

			Empleado empleado = new Empleado(id, nombre, salario, departamento);
			return empleado;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void updateEmpleadoDepartamento(String empleadoId, Departamento newDepartamento) {
		String sql = "UPDATE empleado SET departamento = ? WHERE id = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			if (newDepartamento != null) {
				ps.setInt(1, newDepartamento.getId());
			} else {
				ps.setNull(1, java.sql.Types.INTEGER);
			}
			ps.setString(2, empleadoId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void updateDepartamentoJefe(int departamentoId, Empleado newJefe) {
		String sql = "UPDATE departamento SET jefe = ? WHERE id = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			if (newJefe != null) {
				ps.setInt(1, newJefe.getId());
			} else {
				ps.setNull(1, java.sql.Types.INTEGER);
			}
			ps.setInt(2, departamentoId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createTables() {
		String sql = null;
		if (BD.typeDB.equals("sqlite")) {
			sql = "CREATE TABLE IF NOT EXISTS departamento (id INTEGER PRIMARY KEY, nombre TEXT NOT NULL, jefe INTEGER)";
		}
		if (BD.typeDB.equals("mariadb")) {
			sql = "CREATE TABLE IF NOT EXISTS departamento (id INT PRIMARY KEY AUTO_INCREMENT, nombre VARCHAR(255) NOT NULL, jefe INT)";
		}
		try {
			conn.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (BD.typeDB.equals("sqlite")) {
			sql = "CREATE TABLE IF NOT EXISTS empleado (id INTEGER PRIMARY KEY, nombre TEXT NOT NULL, salario REAL DEFAULT 0.0, departamento INTEGER)";
		}
		if (BD.typeDB.equals("mariadb")) {
			sql = "CREATE TABLE IF NOT EXISTS empleado (id INT PRIMARY KEY AUTO_INCREMENT, nombre VARCHAR(255) NOT NULL, salario DECIMAL(10,2) DEFAULT 0.0, departamento INT)";
		}
		try {
			conn.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
