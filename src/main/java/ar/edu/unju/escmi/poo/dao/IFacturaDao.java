package ar.edu.unju.escmi.poo.dao;

import java.util.List;

import ar.edu.unju.escmi.poo.dominio.Factura;

public interface IFacturaDao {

    void guardar(Factura factura);

    Factura buscarPorId(Long id);

    void modificar(Factura factura);

    void eliminar(Factura factura);

    List<Factura> listarTodos();

    List<Factura> listarConTotalMayorA(
            double monto);
}