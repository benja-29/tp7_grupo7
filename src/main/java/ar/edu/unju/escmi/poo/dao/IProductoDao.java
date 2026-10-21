package ar.edu.unju.escmi.poo.dao;

import java.util.List;

import ar.edu.unju.escmi.poo.dominio.Producto;

public interface IProductoDao {

    void guardar(Producto producto);

    Producto buscarPorId(Long id);

    void modificar(Producto producto);

    void eliminar(Producto producto);

    List<Producto> listarTodos();
}