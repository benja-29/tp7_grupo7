package ar.edu.unju.escmi.poo.dao.impl;

import java.util.List;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.poo.dao.IClienteDao;
import ar.edu.unju.escmi.poo.dominio.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class ClienteDaoImp implements IClienteDao {

    @Override
    public void guardar(Cliente cliente) {

        EntityManager manager =
                EmfSingleton.getInstance().createEntityManager();

        try {
            manager.getTransaction().begin();

            manager.persist(cliente);

            manager.getTransaction().commit();

        } catch (Exception e) {

            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }

            throw new RuntimeException(
                    "No se pudo guardar el cliente", e);

        } finally {
            manager.close();
        }
    }

    @Override
    public Cliente buscarPorId(Long id) {

        EntityManager manager =
                EmfSingleton.getInstance().createEntityManager();

        try {
            return manager.find(Cliente.class, id);

        } finally {
            manager.close();
        }
    }

    @Override
    public void modificar(Cliente cliente) {

        EntityManager manager =
                EmfSingleton.getInstance().createEntityManager();

        try {
            manager.getTransaction().begin();

            manager.merge(cliente);

            manager.getTransaction().commit();

        } catch (Exception e) {

            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }

            throw new RuntimeException(
                    "No se pudo modificar el cliente", e);

        } finally {
            manager.close();
        }
    }

    @Override
    public void eliminar(Cliente cliente) {

        EntityManager manager =
                EmfSingleton.getInstance().createEntityManager();

        try {
            manager.getTransaction().begin();

            Cliente clienteEncontrado =
                    manager.find(Cliente.class, cliente.getId());

            if (clienteEncontrado != null) {
                manager.remove(clienteEncontrado);
            }

            manager.getTransaction().commit();

        } catch (Exception e) {

            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }

            throw new RuntimeException(
                    "No se pudo eliminar el cliente", e);

        } finally {
            manager.close();
        }
    }

    @Override
    public List<Cliente> listarTodos() {

        EntityManager manager =
                EmfSingleton.getInstance().createEntityManager();

        try {

            TypedQuery<Cliente> query =
                    manager.createQuery(
                            "SELECT c FROM Cliente c",
                            Cliente.class);

            return query.getResultList();

        } finally {
            manager.close();
        }
    }
}