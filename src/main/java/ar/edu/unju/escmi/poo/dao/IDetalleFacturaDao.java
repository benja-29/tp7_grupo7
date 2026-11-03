package ar.edu.unju.escmi.poo.dao;

import java.util.List;

import ar.edu.unju.escmi.poo.dominio.DetalleFactura;

public interface IDetalleFacturaDao {

    void guardar(DetalleFactura detalle);

    DetalleFactura buscarPorId(Long id);

    void modificar(DetalleFactura detalle);

    void eliminar(DetalleFactura detalle);

    List<DetalleFactura> listarTodos();
}