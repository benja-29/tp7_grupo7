package ar.edu.unju.escmi.poo.dao;

import java.util.List;

import ar.edu.unju.escmi.poo.dominio.Cliente;

public interface IClienteDao {

    void guardar(Cliente cliente);

    Cliente buscarPorId(Long id);

    void modificar(Cliente cliente);

    void eliminar(Cliente cliente);

    List<Cliente> listarTodos();
}