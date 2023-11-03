package view;


import java.util.InputMismatchException;
import java.util.Scanner;

import dao.Tabla;
import model.Departamento;
import model.Empleado;

public class Menu {
	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Tabla tabla = new Tabla();
        Empleado emple = new Empleado(1, "Javier", 500.5);
        Departamento d = new Departamento(1, "Ventas", emple);
        tabla.addDepartamento(d);
        tabla.addEmpleado(emple);

        int opcion;
        do {
            System.out.println("\nMenú:");
            System.out.println("1. Agregar Departamento");
            System.out.println("2. Agregar Empleado");
            System.out.println("3. Mostrar Departamentos");
            System.out.println("4. Mostrar Empleados");
            System.out.println("5. Buscar Departamento por ID");
            System.out.println("6. Buscar Empleado por ID");
            System.out.println("7. Eliminar Departamento");
            System.out.println("8. Eliminar Empleado");
            System.out.println("9. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer de entrada

                switch (opcion) {
                    case 1:
                        agregarDepartamento(tabla, scanner);
                        break;
                    case 2:
                        agregarEmpleado(tabla, scanner);
                        break;
                    case 3:
                        mostrarDepartamentos(tabla);
                        break;
                    case 4:
                        mostrarEmpleados(tabla);
                        break;
                    case 5:
                        buscarDepartamentoPorID(tabla, scanner);
                        break;
                    case 6:
                        buscarEmpleadoPorID(tabla, scanner);
                        break;
                    case 7:
                        eliminarDepartamento(tabla, scanner);
                        break;
                    case 8:
                        eliminarEmpleado(tabla, scanner);
                        break;
                    case 9:
                        System.out.println("Saliendo del programa.");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, introduzca un número.");
                scanner.nextLine(); // Limpiar el buffer de entrada
                opcion = 0;
            }
        } while (opcion != 9);

        scanner.close();
        tabla.close();
    }

    private static void eliminarDepartamento(Tabla tabla, Scanner scanner) {
        System.out.print("Ingrese el ID del Departamento a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer de entrada
        if (tabla.deleteDepartamento(id)) {
            System.out.println("Departamento eliminado con éxito.");
        } else {
            System.out.println("Error al eliminar el Departamento.");
        }
    }

    private static void eliminarEmpleado(Tabla tabla, Scanner scanner) {
        System.out.print("Ingrese el ID del Empleado a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer de entrada
        if (tabla.deleteEmpleado(id)) {
            System.out.println("Empleado eliminado con éxito.");
        } else {
            System.out.println("Error al eliminar el Empleado.");
        }
    }

   

    private static Empleado buscarJefe(Tabla tabla, Scanner scanner) {
        Empleado jefe = null;
        System.out.print("ID del Empleado jefe (deje en blanco si no hay jefe): ");
        String jefeID = scanner.nextLine();
        if (!jefeID.isEmpty()) {
            int id = Integer.parseInt(jefeID);
            jefe = tabla.buscarEmpleadoPorCodigo(id);
        }
        return jefe;
    }
    private static void agregarDepartamento(Tabla tabla, Scanner scanner) {
        System.out.print("ID del Departamento: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer de entrada
        System.out.print("Nombre del Departamento: ");
        String nombre = scanner.nextLine();
        
        Empleado jefe = null;
        System.out.print("¿El departamento tiene un jefe? (S/N): ");
        String respuesta = scanner.nextLine();
        if (respuesta.equalsIgnoreCase("S")) {
            jefe = buscarJefe(tabla, scanner);
        }

        Departamento departamento = new Departamento(id, nombre, jefe);

        if (tabla.addDepartamento(departamento)) {
            System.out.println("Departamento agregado con éxito.");
        } else {
            System.out.println("Error al agregar el Departamento.");
        }
    }


    private static void agregarEmpleado(Tabla tabla, Scanner scanner) {
        System.out.print("ID del Empleado: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer de entrada
        System.out.print("Nombre del Empleado: ");
        String nombre = scanner.nextLine();
        System.out.print("Salario del Empleado: ");
        double salario = scanner.nextDouble();
        scanner.nextLine(); // Limpiar el buffer de entrada

        Departamento departamento = null;
        System.out.print("¿El empleado tiene un departamento? (S/N): ");
        String respuesta = scanner.nextLine();
        if (respuesta.equalsIgnoreCase("S")) {
            departamento = buscarDepartamento(tabla, scanner);
        }

        Empleado empleado = new Empleado(id, nombre, salario, departamento);

        if (tabla.addEmpleado(empleado)) {
            System.out.println("Empleado agregado con éxito.");
        } else {
            System.out.println("Error al agregar el Empleado.");
        }
    }


    private static void mostrarDepartamentos(Tabla tabla) {
        String departamentos = tabla.showDepartamento();
        if (!departamentos.isEmpty()) {
            System.out.println("Lista de Departamentos:");
            System.out.println(departamentos);
        } else {
            System.out.println("No hay Departamentos registrados.");
        }
    }

    private static void mostrarEmpleados(Tabla tabla) {
        String empleados = tabla.showEmpleado();
        if (!empleados.isEmpty()) {
            System.out.println("Lista de Empleados:");
            System.out.println(empleados);
        } else {
            System.out.println("No hay Empleados registrados.");
        }
    }

    private static Departamento buscarDepartamento(Tabla tabla, Scanner scanner) {
        Departamento jefe = null;
        System.out.print("ID del Departamento jefe (deje en blanco si no hay jefe): ");
        String jefeID = scanner.nextLine();
        if (!jefeID.isEmpty()) {
            int id = Integer.parseInt(jefeID);
            jefe = tabla.buscarDepartamentoPorCodigo(id);
        }
        return jefe;
    }

    private static void buscarDepartamentoPorID(Tabla tabla, Scanner scanner) {
        System.out.print("Ingrese el ID del Departamento a buscar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer de entrada
        Departamento departamento = tabla.buscarDepartamentoPorCodigo(id);
        if (departamento != null) {
            System.out.println("Departamento encontrado:");
            System.out.println(departamento);
        } else {
            System.out.println("Departamento no encontrado.");
        }
    }

    private static void buscarEmpleadoPorID(Tabla tabla, Scanner scanner) {
        System.out.print("Ingrese el ID del Empleado a buscar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer de entrada
        Empleado empleado = tabla.buscarEmpleadoPorCodigo(id);
        if (empleado != null) {
            System.out.println("Empleado encontrado:");
            System.out.println(empleado);
        } else {
            System.out.println("Empleado no encontrado.");
        }
    }
}
