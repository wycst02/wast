package io.github.wycst.wast.jdbc.commands;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author wangy
 * @since 2021/2/14 18:21
 */
public interface SqlExecuteCall<T> {

    /**
     * @param connection
     * @return
     * @throws SQLException
     */
    public T execute(Connection connection) throws SQLException;

}
