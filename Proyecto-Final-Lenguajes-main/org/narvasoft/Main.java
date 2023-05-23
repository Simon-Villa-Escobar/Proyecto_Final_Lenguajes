package org.narvasoft;

import controller.Server;
import model.User;
import model.SyntaxParser;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SyntaxParser parser = new SyntaxParser();
        Server request = new Server();
        
        Scanner scanner = new Scanner(System.in);
        Scanner scannerId = new Scanner(System.in);
        String cabecera;
        String opcion;
        String correo;
        String telefono;
        int id;
        String nombre;
        String body;
        String linea;

        label:
        while (true) {
            System.out.println("\n");
            System.out.println("Seleccione una opción: ");
            System.out.println("1. Leer usuarios existentes");
            System.out.println("2. Encontrar un usuario");
            System.out.println("3. Crear un usuario");
            System.out.println("4. Editar la información de un usuario existente");
            System.out.println("5. Eliminar un usuario");
            System.out.println("6. Salir");

            opcion = scanner.nextLine();
            
            System.out.println("\n");

            switch (opcion) {
                case "1":
                    linea = "GET http://example.com/api/users HTTP/1.1\r\n";
                    cabecera = "Content-Type: application/json\r\nAuthorization-Bearer: abc123\r\n";
                    body = "";

                    parser.ApiRest(linea, cabecera, body, 0);
                    break;
                case "2":
                    System.out.println("Cual es el id del usuario que desea buscar: ");
                    id = scannerId.nextInt();

                    linea = "GET http://example.com/api/users HTTP/1.1\r\n";
                    cabecera = "Content-Type: application/json\r\nAuthorization-Bearer: abc123\r\n";
                    body = "";

                    parser.ApiRest(linea, cabecera, body, id);
                    break;
                case "3":
                    System.out.println("Nombre del usuario que desea crear: ");
                    nombre = scanner.nextLine();

                    System.out.println("Correo del usuario que desea crear: ");
                    correo = scanner.nextLine();

                    System.out.println("Telefono del usuario que desea crear: ");
                    telefono = scanner.nextLine();

                    linea = "POST http://example.com/api/users HTTP/1.1\r\n";
                    cabecera = "Content-Type: application/json\r\nAuthorization-Bearer: abc123\r\n";
                    body = "{\"nombres\": \"" + nombre + "\", \"email\": \"" + correo + "\", \"phone\": \"" + telefono + "\"}\r\n";

                    parser.ApiRest(linea, cabecera, body, 0);
                    break;
                case "4":
                    System.out.println("Cual es el id del usuario que desea editar: ");
                    id = scannerId.nextInt();

                    System.out.println("Nuevo nombre del usuario con id " + id + ":");
                    nombre = scanner.nextLine();

                    System.out.println("Nuevo correo del usuario con id " + id + ":");
                    correo = scanner.nextLine();

                    System.out.println("Nuevo telefono del usuario con id " + id + ":");
                    telefono = scanner.nextLine();

                    linea = "PUT http://example.com/api/users HTTP/1.1\r\n";
                    cabecera = "Content-Type: application/json\r\nAuthorization-Bearer: abc123\r\n";
                    body = "{\"nombres\": \"" + nombre + "\", \"email\": \"" + correo + "\", \"phone\": \"" + telefono + "\"}\r\n";

                    parser.ApiRest(linea, cabecera, body, id);
                    break;
                case "5":
                    System.out.println("Cual es el id del usuario que desea eliminar: ");
                    id = scannerId.nextInt();

                    linea = "DELETE http://example.com/api/users HTTP/1.1\r\n";
                    cabecera = "Content-Type: application/json\r\nAuthorization-Bearer: abc123\r\n";
                    body = "";

                    parser.ApiRest(linea, cabecera, body, id);
                    break;
                case "6":
                    System.out.println("Gracias por usar el programa!");

                    break label;
                default:
                    System.out.println("Opción invalida. Inténtelo de nuevo.");
                    break;
            }
        }
    }
}

