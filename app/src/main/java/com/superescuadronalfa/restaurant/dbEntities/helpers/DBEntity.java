package com.superescuadronalfa.restaurant.dbEntities.helpers;

import java.util.List;

public class DBEntity {
    public static <T> boolean agregar(T entidad) {
        throw new RuntimeException("Method must be overrided");
    }

    public static <T> boolean editar(T entidad) {
        throw new RuntimeException("Method must be overrided");
    }

    public static <T> boolean remover(T entidad) {
        throw new RuntimeException("Method must be overrided");
    }

    public static <T> List<T> lista(T entidad) {
        throw new RuntimeException("Method must be overrided");
    }

    public static <T> T busca(int id) {
        throw new RuntimeException("Method must be overrided");
    }
}
