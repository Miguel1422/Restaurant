package com.superescuadronalfa.restaurant.app;

public class AppConfig {

    public static String SERVER_ADRESS;
    private static AppConfig instance;
    private String URL_ADD_ORDEN = "http://%s/restaurantwrapper/api/add_orden.php";
    private String URL_ADD_PEDIDO = "http://%s/restaurantwrapper/api/add_pedido.php";
    private String URL_LOGIN = "http://%s/restaurantwrapper/api/login.php";
    private String URL_GET_MESAS = "http://%s/restaurantwrapper/api/get_mesas.php";
    private String URL_GET_ORDEN = "http://%s/restaurantwrapper/api/get_orden.php";
    private String URL_GET_TRABAJADOR_IMAGE = "http://%s/restaurantwrapper/api/get_trabajador_image.php";
    private String URL_DELETE_PEDIDO = "http://%s/restaurantwrapper/api/delete_pedido.php";
    private String URL_EDITAR_PEDIDO = "http://%s/restaurantwrapper/api/edit_pedido.php";
    private String URL_EDITAR_PEDIDO_STATUS = "http://%s/restaurantwrapper/api/edit_pedido_state.php";
    private String URL_GET_TIPOS = "http://%s/restaurantwrapper/api/get_lista_tipos.php";
    private String URL_GET_PRODUCTOS = "http://%s/restaurantwrapper/api/get_productos.php";



    public static final boolean USE_CONNECTOR = false;
    private String URL_GET_PRODUCTO_IMAGE = "http://%s/restaurantwrapper/api/get_producto_image.php";

    private AppConfig() {
    }

    public static AppConfig getInstance() {
        if (instance == null) instance = new AppConfig();
        return instance;
    }

    public static String getServerAdress() {
        return SERVER_ADRESS;
    }

    public String getURLAddOrden() {
        return String.format(URL_ADD_ORDEN, SERVER_ADRESS);
    }

    public String getUrlAddPedido() {
        return String.format(URL_ADD_PEDIDO, SERVER_ADRESS);
    }

    public String getUrlLogin() {
        return String.format(URL_LOGIN, SERVER_ADRESS);
    }

    public String getUrlGetMesas() {
        return String.format(URL_GET_MESAS, SERVER_ADRESS);
    }

    public String getUrlGetOrden() {
        return String.format(URL_GET_ORDEN, SERVER_ADRESS);
    }

    public String getUrlGetTrabajadorImage() {
        return String.format(URL_GET_TRABAJADOR_IMAGE, SERVER_ADRESS);
    }

    public String getUrlDeletePedido() {
        return String.format(URL_DELETE_PEDIDO, SERVER_ADRESS);
    }

    public String getUrlEditarPedido() {
        return String.format(URL_EDITAR_PEDIDO, SERVER_ADRESS);
    }

    public String getUrlEditarPedidoStatus() {
        return String.format(URL_EDITAR_PEDIDO_STATUS, SERVER_ADRESS);
    }

    public String getUrlGetTipos() {
        return String.format(URL_GET_TIPOS, SERVER_ADRESS);
    }

    public String getUrlGetProductos() {
        return String.format(URL_GET_PRODUCTOS, SERVER_ADRESS);
    }

    public String getUrlGetProductoImage() {
        return String.format(URL_GET_PRODUCTO_IMAGE, SERVER_ADRESS);
    }
}
