package com.superescuadronalfa.restaurant.dbEntities.control;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IControlEntidad<T> {
    boolean agregar(T entidad);

    boolean editar(T entidad);

    boolean eliminar(T entidad);

    List<T> getLista();

    T fromResultSet(ResultSet result) throws SQLException;
}
