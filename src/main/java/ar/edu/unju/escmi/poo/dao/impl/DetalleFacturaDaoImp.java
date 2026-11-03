package ar.edu.unju.escmi.poo.dao.impl;

import java.util.List;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.poo.dao.IDetalleFacturaDao;
import ar.edu.unju.escmi.poo.dominio.DetalleFactura;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class DetalleFacturaDaoImp
        implements IDetalleFacturaDao {

    @Override
    public void guardar(
            DetalleFactura detalle) {

        EntityManager manager =
                EmfSingleton.getInstance()
                        .createEntityManager();

        try {

            manager.getTransaction().begin();
            manager.persist(detalle);
            manager.getTransaction().commit();

        } catch (Exception e) {

            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }

            throw new RuntimeException(
                    "No se pudo guardar el detalle",
                    e);

        } finally {
            manager.close();
        }
    }

    @Override
    public DetalleFactura buscarPorId(Long id) {

        EntityManager manager =
                EmfSingleton.getInstance()
                        .createEntityManager();

        try {

            return manager.find(
                    DetalleFactura.class,
                    id);

        } finally {
            manager.close();
        }
    }

    @Override
    public void modificar(
            DetalleFactura detalle) {

        EntityManager manager =
                EmfSingleton.getInstance()
                        .createEntityManager();

        try {

            manager.getTransaction().begin();
            manager.merge(detalle);
            manager.getTransaction().commit();

        } catch (Exception e) {

            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }

            throw new RuntimeException(
                    "No se pudo modificar el detalle",
                    e);

        } finally {
            manager.close();
        }
    }

    @Override
    public void eliminar(
            DetalleFactura detalle) {

        EntityManager manager =
                EmfSingleton.getInstance()
                        .createEntityManager();

        try {

            manager.getTransaction().begin();

            DetalleFactura encontrado =
                    manager.find(
                            DetalleFactura.class,
                            detalle.getId());

            if (encontrado != null) {
                manager.remove(encontrado);
            }

            manager.getTransaction().commit();

        } catch (Exception e) {

            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }

            throw new RuntimeException(
                    "No se pudo eliminar el detalle",
                    e);

        } finally {
            manager.close();
        }
    }

    @Override
    public List<DetalleFactura> listarTodos() {

        EntityManager manager =
                EmfSingleton.getInstance()
                        .createEntityManager();

        try {

            TypedQuery<DetalleFactura> query =
                    manager.createQuery(
                            "SELECT d FROM DetalleFactura d",
                            DetalleFactura.class);

            return query.getResultList();

        } finally {
            manager.close();
        }
    }
}