package com.example.demo.defaultapp.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NamedParamStatement {
    private final PreparedStatement prepStmt;
    private final List<String> fields = new ArrayList<String>();

    public NamedParamStatement(Connection conn, String sql) throws SQLException {
        int pos;
        while((pos = sql.indexOf(":")) != -1) {
            int end = sql.substring(pos).indexOf(" ");
            if (end == -1)
                end = sql.length();
            else
                end += pos;
            fields.add(sql.substring(pos+1,end));
            sql = sql.substring(0, pos) + "?" + sql.substring(end);
        }
        prepStmt = conn.prepareStatement(sql);
    }

    public PreparedStatement getPreparedStatement() {
        return prepStmt;
    }
    public ResultSet executeQuery() throws SQLException {
        return prepStmt.executeQuery();
    }
    public void executeBatch() throws SQLException {
        prepStmt.executeBatch();
    }

    public void close() throws SQLException {
        prepStmt.close();
    }

    public NamedParamStatement setInt(String name, int value) throws SQLException {
        prepStmt.setInt(getIndex(name), value);
        return this;
    }

    public NamedParamStatement setString(String name, String value) throws SQLException {
        prepStmt.setString(getIndex(name), value);
        return this;
    }

    public NamedParamStatement addBatch() throws SQLException {
        prepStmt.addBatch();
        return this;
    }

    private int getIndex(String name) {
        return fields.indexOf(name)+1;
    }
}
