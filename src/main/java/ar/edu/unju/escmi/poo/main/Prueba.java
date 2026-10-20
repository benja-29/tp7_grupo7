package ar.edu.unju.escmi.poo.main;

import ar.edu.unju.escmi.config.EmfSingleton;

public class Prueba {
    public static void main(String[] args) {
        try {
            EmfSingleton.getInstance();
            System.out.println("¡Conexión exitosa y tablas generadas correctamente!");
        } catch (Exception e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            EmfSingleton.close();
        }
    }
}