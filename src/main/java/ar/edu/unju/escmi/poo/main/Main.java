package ar.edu.unju.escmi.poo.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.poo.dao.IClienteDao;
import ar.edu.unju.escmi.poo.dao.IFacturaDao;
import ar.edu.unju.escmi.poo.dao.IProductoDao;
import ar.edu.unju.escmi.poo.dao.impl.ClienteDaoImp;
import ar.edu.unju.escmi.poo.dao.impl.FacturaDaoImp;
import ar.edu.unju.escmi.poo.dao.impl.ProductoDaoImp;
import ar.edu.unju.escmi.poo.dominio.Cliente;
import ar.edu.unju.escmi.poo.dominio.DetalleFactura;
import ar.edu.unju.escmi.poo.dominio.Factura;
import ar.edu.unju.escmi.poo.dominio.Producto;

public class Main {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final IClienteDao clienteDao =
            new ClienteDaoImp();

    private static final IProductoDao productoDao =
            new ProductoDaoImp();

    private static final IFacturaDao facturaDao =
            new FacturaDaoImp();

    public static void main(String[] args) {

        int opcion;

        try {

            do {

                mostrarMenu();

                opcion =
                        leerEntero(
                                "Ingrese una opcion: ");

                try {

                    switch (opcion) {

                    case 1:
                        altaCliente();
                        break;

                    case 2:
                        altaProducto();
                        break;

                    case 3:
                        realizarVenta();
                        break;

                    case 4:
                        buscarFactura();
                        break;

                    case 5:
                        eliminarFactura();
                        break;

                    case 6:
                        eliminarProducto();
                        break;

                    case 7:
                        modificarCliente();
                        break;

                    case 8:
                        modificarPrecioProducto();
                        break;

                    case 9:
                        eliminarCliente();
                        break;

                    case 10:
                        mostrarFacturas();
                        break;

                    case 11:
                        mostrarClientes();
                        break;

                    case 12:
                        mostrarFacturasMayoresA900000();
                        break;

                    case 0:
                        System.out.println(
                                "\nPrograma finalizado.");
                        break;

                    default:
                        System.out.println(
                                "\nOpcion incorrecta.");
                    }

                } catch (Exception e) {

                    System.out.println(
                            "\nSe produjo un error: "
                            + e.getMessage());
                }

            } while (opcion != 0);

        } finally {

            scanner.close();
            EmfSingleton.close();
        }
    }

    private static void mostrarMenu() {

        System.out.println(
                "\n========== TP7 - MENU ==========");

        System.out.println("1- Alta de cliente");
        System.out.println("2- Alta de producto");
        System.out.println("3- Realizar venta");
        System.out.println("4- Buscar factura");
        System.out.println("5- Eliminar factura");
        System.out.println("6- Eliminar producto");
        System.out.println("7- Modificar cliente");
        System.out.println("8- Modificar precio producto");
        System.out.println("9- Eliminar cliente");
        System.out.println("10- Mostrar facturas");
        System.out.println("11- Mostrar clientes");
        System.out.println(
                "12- Facturas mayores a $900000");
        System.out.println("0- Salir");
    }

    private static void altaCliente() {

        System.out.println(
                "\n===== ALTA CLIENTE =====");

        String nombre =
                leerTexto("Nombre: ");

        String apellido =
                leerTexto("Apellido: ");

        int dni =
                leerEntero("DNI: ");

        String domicilio =
                leerTexto("Domicilio: ");

        Cliente cliente =
                new Cliente(
                        nombre,
                        apellido,
                        dni,
                        domicilio,
                        true);

        clienteDao.guardar(cliente);

        System.out.println(
                "\nCliente guardado. ID: "
                + cliente.getId());
    }

    private static void altaProducto() {

        System.out.println(
                "\n===== ALTA PRODUCTO =====");

        String descripcion =
                leerTexto("Descripcion: ");

        double precio =
                leerDouble(
                        "Precio unitario: ");

        int stock =
                leerEntero("Stock: ");

        if (precio <= 0 || stock < 0) {

            System.out.println(
                    "\nPrecio o stock incorrecto.");

            return;
        }

        Producto producto =
                new Producto(
                        descripcion,
                        precio,
                        stock,
                        true);

        productoDao.guardar(producto);

        System.out.println(
                "\nProducto guardado. ID: "
                + producto.getId());
    }

    private static void realizarVenta() {

        System.out.println(
                "\n===== NUEVA FACTURA =====");

        Long idCliente =
                leerLong(
                        "ID del cliente: ");

        Cliente cliente =
                clienteDao.buscarPorId(
                        idCliente);

        if (cliente == null
                || !cliente.isEstado()) {

            System.out.println(
                    "\nCliente inexistente o eliminado.");

            return;
        }

        List<DetalleFactura> detalles =
                new ArrayList<>();

        Map<Long, Producto> productosActualizados =
                new LinkedHashMap<>();

        double total = 0;

        int continuar;

        do {

            Long idProducto =
                    leerLong(
                            "\nID del producto: ");

            Producto producto =
                    productosActualizados
                            .get(idProducto);

            if (producto == null) {

                producto =
                        productoDao
                                .buscarPorId(
                                        idProducto);
            }

            if (producto == null
                    || !producto.isEstado()) {

                System.out.println(
                        "Producto inexistente o eliminado.");

            } else {

                int cantidad =
                        leerEntero(
                                "Cantidad: ");

                if (cantidad <= 0) {

                    System.out.println(
                            "La cantidad debe ser mayor a 0.");

                } else if (
                        cantidad
                        > producto.getStock()) {

                    System.out.println(
                            "Stock insuficiente.");

                } else {

                    DetalleFactura detalle =
                            new DetalleFactura();

                    detalle.setProducto(
                            producto);

                    detalle.setCantidad(
                            cantidad);

                    detalles.add(
                            detalle);

                    total +=
                            detalle.getSubtotal();

                    producto.setStock(
                            producto.getStock()
                            - cantidad);

                    productosActualizados.put(
                            producto.getId(),
                            producto);

                    System.out.println(
                            "Subtotal: $"
                            + detalle.getSubtotal());
                }
            }

            continuar =
                    leerEntero(
                            "Agregar otro producto? "
                            + "1=Si 0=No: ");

        } while (continuar == 1);

        if (detalles.isEmpty()) {

            System.out.println(
                    "\nLa factura no tiene productos.");

            return;
        }

        Factura factura =
                new Factura();

        factura.setFecha(
                LocalDate.now());

        factura.setCliente(
                cliente);

        factura.setDomicilio(
                cliente.getDomicilio());

        factura.setDetalles(
                detalles);

        factura.setTotal(
                total);

        factura.setEstado(
                true);

        facturaDao.guardar(factura);

        System.out.println(
                "\nVenta realizada.");

        System.out.println(
                "Factura numero: "
                + factura.getId());

        System.out.println(
                "Total: $"
                + factura.getTotal());
    }

    private static void buscarFactura() {

        Long id =
                leerLong(
                        "\nNumero de factura: ");

        Factura factura =
                facturaDao.buscarPorId(id);

        if (factura == null
                || !factura.isEstado()) {

            System.out.println(
                    "\nFactura no encontrada.");

            return;
        }

        mostrarFactura(
                factura);
    }

    private static void eliminarFactura() {

        Long id =
                leerLong(
                        "\nNumero de factura: ");

        Factura factura =
                facturaDao.buscarPorId(id);

        if (factura == null
                || !factura.isEstado()) {

            System.out.println(
                    "\nFactura no encontrada.");

            return;
        }

        facturaDao.eliminar(
                factura);

        System.out.println(
                "\nFactura eliminada logicamente.");
    }

    private static void eliminarProducto() {

        Long id =
                leerLong(
                        "\nID del producto: ");

        Producto producto =
                productoDao.buscarPorId(id);

        if (producto == null
                || !producto.isEstado()) {

            System.out.println(
                    "\nProducto no encontrado.");

            return;
        }

        productoDao.eliminar(
                producto);

        System.out.println(
                "\nProducto eliminado logicamente.");
    }

    private static void modificarCliente() {

        Long id =
                leerLong(
                        "\nID del cliente: ");

        Cliente cliente =
                clienteDao.buscarPorId(id);

        if (cliente == null
                || !cliente.isEstado()) {

            System.out.println(
                    "\nCliente no encontrado.");

            return;
        }

        cliente.setNombre(
                leerTexto(
                        "Nuevo nombre: "));

        cliente.setApellido(
                leerTexto(
                        "Nuevo apellido: "));

        cliente.setDni(
                leerEntero(
                        "Nuevo DNI: "));

        cliente.setDomicilio(
                leerTexto(
                        "Nuevo domicilio: "));

        clienteDao.modificar(
                cliente);

        System.out.println(
                "\nCliente modificado.");
    }

    private static void modificarPrecioProducto() {

        Long id =
                leerLong(
                        "\nID del producto: ");

        Producto producto =
                productoDao.buscarPorId(id);

        if (producto == null
                || !producto.isEstado()) {

            System.out.println(
                    "\nProducto no encontrado.");

            return;
        }

        double precio =
                leerDouble(
                        "Nuevo precio: ");

        if (precio <= 0) {

            System.out.println(
                    "\nPrecio incorrecto.");

            return;
        }

        producto.setPrecioUnitario(
                precio);

        productoDao.modificar(
                producto);

        System.out.println(
                "\nPrecio modificado.");
    }

    private static void eliminarCliente() {

        Long id =
                leerLong(
                        "\nID del cliente: ");

        Cliente cliente =
                clienteDao.buscarPorId(id);

        if (cliente == null
                || !cliente.isEstado()) {

            System.out.println(
                    "\nCliente no encontrado.");

            return;
        }

        clienteDao.eliminar(
                cliente);

        System.out.println(
                "\nCliente eliminado logicamente.");
    }

    private static void mostrarFacturas() {

        List<Factura> facturas =
                facturaDao.listarTodos();

        if (facturas.isEmpty()) {

            System.out.println(
                    "\nNo hay facturas.");

            return;
        }

        for (Factura factura :
                facturas) {

            mostrarFactura(
                    factura);
        }
    }

    private static void mostrarClientes() {

        List<Cliente> clientes =
                clienteDao.listarTodos();

        if (clientes.isEmpty()) {

            System.out.println(
                    "\nNo hay clientes.");

            return;
        }

        for (Cliente cliente :
                clientes) {

            System.out.println(
                    "\nID: "
                    + cliente.getId()
                    + " | DNI: "
                    + cliente.getDni()
                    + " | Nombre: "
                    + cliente.getNombre()
                    + " "
                    + cliente.getApellido()
                    + " | Domicilio: "
                    + cliente.getDomicilio());
        }
    }

    private static void mostrarFacturasMayoresA900000() {

        List<Factura> facturas =
                facturaDao
                        .listarConTotalMayorA(
                                900000);

        if (facturas.isEmpty()) {

            System.out.println(
                    "\nNo existen facturas "
                    + "mayores a $900000.");

            return;
        }

        for (Factura factura :
                facturas) {

            mostrarFactura(
                    factura);
        }
    }

    private static void mostrarFactura(
            Factura factura) {

        System.out.println(
                "\n========== FACTURA ==========");

        System.out.println(
                "Numero: "
                + factura.getId());

        System.out.println(
                "Fecha: "
                + factura.getFecha());

        System.out.println(
                "Cliente: "
                + factura.getCliente()
                        .getNombre()
                + " "
                + factura.getCliente()
                        .getApellido());

        System.out.println(
                "Domicilio: "
                + factura.getDomicilio());

        System.out.println(
                "Detalles:");

        for (DetalleFactura detalle :
                factura.getDetalles()) {

            System.out.println(
                    "- "
                    + detalle.getProducto()
                            .getDescripcion()
                    + " | Cantidad: "
                    + detalle.getCantidad()
                    + " | Subtotal: $"
                    + detalle.getSubtotal());
        }

        System.out.println(
                "TOTAL: $"
                + factura.getTotal());
    }

    private static int leerEntero(
            String mensaje) {

        while (true) {

            try {

                System.out.print(
                        mensaje);

                return Integer.parseInt(
                        scanner.nextLine());

            } catch (NumberFormatException e) {

                System.out.println(
                        "Debe ingresar un entero.");
            }
        }
    }

    private static Long leerLong(
            String mensaje) {

        while (true) {

            try {

                System.out.print(
                        mensaje);

                return Long.parseLong(
                        scanner.nextLine());

            } catch (NumberFormatException e) {

                System.out.println(
                        "Debe ingresar un numero valido.");
            }
        }
    }

    private static double leerDouble(
            String mensaje) {

        while (true) {

            try {

                System.out.print(
                        mensaje);

                String valor =
                        scanner.nextLine()
                                .replace(",", ".");

                return Double.parseDouble(
                        valor);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Debe ingresar un numero valido.");
            }
        }
    }

    private static String leerTexto(
            String mensaje) {

        while (true) {

            System.out.print(
                    mensaje);

            String texto =
                    scanner.nextLine()
                            .trim();

            if (!texto.isEmpty()) {
                return texto;
            }

            System.out.println(
                    "El dato no puede estar vacio.");
        }
    }
}