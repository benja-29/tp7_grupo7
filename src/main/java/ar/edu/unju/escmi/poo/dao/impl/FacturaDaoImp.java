package ar.edu.unju.escmi.poo.dao.impl;

import java.util.List;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.poo.dao.IFacturaDao;
import ar.edu.unju.escmi.poo.dominio.Cliente;
import ar.edu.unju.escmi.poo.dominio.DetalleFactura;
import ar.edu.unju.escmi.poo.dominio.Factura;
import ar.edu.unju.escmi.poo.dominio.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.LockModeType;

public class FacturaDaoImp implements IFacturaDao {


@Override
public void guardar(Factura factura) {

    EntityManager manager =
            EmfSingleton.getInstance().createEntityManager();

    try {
        manager.getTransaction().begin();

        if (factura == null
                || factura.getCliente() == null
                || factura.getCliente().getId() == null) {
            throw new IllegalArgumentException(
                    "La factura debe tener un cliente");
        }

        Cliente cliente = manager.find(
                Cliente.class,
                factura.getCliente().getId());

        if (cliente == null || !cliente.isEstado()) {
            throw new IllegalArgumentException(
                    "El cliente no existe o esta inactivo");
        }

        if (factura.getDetalles() == null
                || factura.getDetalles().isEmpty()) {
            throw new IllegalArgumentException(
                    "La factura debe tener detalles");
        }

        factura.setCliente(cliente);

        double total = 0;

        for (DetalleFactura detalle : factura.getDetalles()) {

            if (detalle == null
                    || detalle.getProducto() == null
                    || detalle.getProducto().getId() == null) {
                throw new IllegalArgumentException(
                        "Detalle sin producto valido");
            }

            int cantidad = detalle.getCantidad();

            if (cantidad <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad debe ser mayor a cero");
            }

            Producto producto = manager.find(
                    Producto.class,
                    detalle.getProducto().getId(),
                    LockModeType.PESSIMISTIC_WRITE);

            if (producto == null || !producto.isEstado()) {
                throw new IllegalArgumentException(
                        "Producto inexistente o inactivo");
            }

            if (producto.getStock() < cantidad) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para "
                        + producto.getDescripcion());
            }

            detalle.setProducto(producto);

            producto.setStock(
                    producto.getStock() - cantidad);

            total += detalle.getSubtotal();
        }

        factura.setTotal(total);

        manager.persist(factura);

        manager.getTransaction().commit();

    } catch (Exception e) {

        if (manager.getTransaction().isActive()) {
            manager.getTransaction().rollback();
        }

        throw new RuntimeException(
                "No se pudo registrar la venta: "
                + e.getMessage(), e);

    } finally {
        manager.close();
    }
}

    @Override
    public Factura buscarPorId(Long id) {

        EntityManager manager =
                EmfSingleton.getInstance()
                        .createEntityManager();

        try {

            return manager.find(
                    Factura.class,
                    id);

        } finally {
            manager.close();
        }
    }

    @Override
    public void modificar(Factura factura) {

        EntityManager manager =
                EmfSingleton.getInstance()
                        .createEntityManager();

        try {

            manager.getTransaction().begin();
            manager.merge(factura);
            manager.getTransaction().commit();

        } catch (Exception e) {

            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }

            throw new RuntimeException(
                    "No se pudo modificar la factura",
                    e);

        } finally {
            manager.close();
        }
    }

    @Override
    public void eliminar(Factura factura) {

        EntityManager manager =
                EmfSingleton.getInstance()
                        .createEntityManager();

        try {

            manager.getTransaction().begin();

            Factura encontrada =
                    manager.find(
                            Factura.class,
                            factura.getId());

            if (encontrada != null) {
                encontrada.setEstado(false);
            }

            manager.getTransaction().commit();

        } catch (Exception e) {

            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }

            throw new RuntimeException(
                    "No se pudo eliminar la factura",
                    e);

        } finally {
            manager.close();
        }
    }

    @Override
    public List<Factura> listarTodos() {

        EntityManager manager =
                EmfSingleton.getInstance()
                        .createEntityManager();

        try {

            TypedQuery<Factura> query =
                    manager.createQuery(
                            "SELECT DISTINCT f "
                            + "FROM Factura f "
                            + "WHERE f.estado = true",
                            Factura.class);

            return query.getResultList();

        } finally {
            manager.close();
        }
    }

    @Override
    public List<Factura> listarConTotalMayorA(
            double monto) {

        EntityManager manager =
                EmfSingleton.getInstance()
                        .createEntityManager();

        try {

            TypedQuery<Factura> query =
                    manager.createQuery(
                            "SELECT DISTINCT f "
                            + "FROM Factura f "
                            + "WHERE f.estado = true "
                            + "AND f.total > :monto",
                            Factura.class);

            query.setParameter(
                    "monto",
                    monto);

            return query.getResultList();

        } finally {
            manager.close();
        }
    }
}