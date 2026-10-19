package ar.edu.unju.escmi.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EmfSingleton {
    private static final String PERSISTENCE_UNIT_NAME = "tp7_grupo7";
    private static EntityManagerFactory emf;

    private EmfSingleton() {}

    public static EntityManagerFactory getInstance() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        return emf;
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}