package ar.edu.unju.escmi.poo.dao.impl;

import java.util.List;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.poo.dao.IProductoDao;
import ar.edu.unju.escmi.poo.dominio.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class ProductoDaoImp implements IProductoDao {

    @Override
    public void guardar(Producto producto) {

        EntityManager manager =
                EmfSingleton.getInstance().createEntityManager();

        try {
            manager.getTransaction().begin();

            manager.persist(producto);

            manager.getTransaction().commit();

        } catch (Exception e) {

            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }

            throw new RuntimeException(
                    "No se pudo guardar el producto", e);

        } finally {
            manager.close();
        }
    }

    @Override
    public Producto buscarPorId(Long id) {

        EntityManager manager =
                EmfSingleton.getInstance().createEntityManager();

        try {
            return manager.find(Producto.class, id);

        } finally {
            manager.close();
        }
    }

    @Override
    public void modificar(Producto producto) {

        EntityManager manager =
                EmfSingleton.getInstance().createEntityManager();

        try {
            manager.getTransaction().begin();

            manager.merge(producto);

            manager.getTransaction().commit();

        } catch (Exception e) {

            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }

            throw new RuntimeException(
                    "No se pudo modificar el producto", e);

        } finally {
            manager.close();
        }
    }

    @Override
    public void eliminar(Producto producto) {

        EntityManager manager =
                EmfSingleton.getInstance().createEntityManager();

        try {
            manager.getTransaction().begin();

            Producto productoEncontrado =
                    manager.find(Producto.class, producto.getId());

            if (productoEncontrado != null) {
                manager.remove(productoEncontrado);
            }

            manager.getTransaction().commit();

        } catch (Exception e) {

            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }

            throw new RuntimeException(
                    "No se pudo eliminar el producto", e);

        } finally {
            manager.close();
        }
    }

    @Override
    public List<Producto> listarTodos() {

        EntityManager manager =
                EmfSingleton.getInstance().createEntityManager();

        try {

            TypedQuery<Producto> query =
                    manager.createQuery(
                            "SELECT p FROM Producto p",
                            Producto.class);

            return query.getResultList();

        } finally {
            manager.close();
        }
    }
}