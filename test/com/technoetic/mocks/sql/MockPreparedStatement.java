package com.technoetic.mocks.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;

public class MockPreparedStatement implements PreparedStatement {
    public boolean executeQueryCalled;
    public ResultSet executeQueryReturn;

    public ResultSet executeQuery() throws SQLException {
        executeQueryCalled = true;
        return executeQueryReturn;
    }

    public boolean executeUpdateCalled;
    public Integer executeUpdateReturn;

    public int executeUpdate() throws SQLException {
        executeUpdateCalled = true;
        return executeUpdateReturn.intValue();
    }

    public Object[] boundVariables;

    public void setBoundVariableCount(int i) {
        boundVariables = new Object[i + 1];
    }

    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        boundVariables[parameterIndex] = null;
    }

    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        boundVariables[parameterIndex] = new Boolean(x);
    }

    public void setByte(int parameterIndex, byte x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setByte() not yet implemented.");
    }

    public void setShort(int parameterIndex, short x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setShort() not yet implemented.");
    }

    public void setInt(int parameterIndex, int x) throws SQLException {
        boundVariables[parameterIndex] = new Integer(x);
    }

    public void setLong(int parameterIndex, long x) throws SQLException {
        boundVariables[parameterIndex] = new Long(x);
    }

    public void setFloat(int parameterIndex, float x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setFloat() not yet implemented.");
    }

    public void setDouble(int parameterIndex, double x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setDouble() not yet implemented.");
    }

    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setBigDecimal() not yet implemented.");
    }

    public void setString(int parameterIndex, String x) throws SQLException {
        boundVariables[parameterIndex] = x;
    }

    public void setDate(int parameterIndex, Date x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setDate() not yet implemented.");
    }

    public void setTime(int parameterIndex, Time x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setTime() not yet implemented.");
    }

    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setTimestamp() not yet implemented.");
    }

    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setAsciiStream() not yet implemented.");
    }

    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setUnicodeStream() not yet implemented.");
    }

    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setBinaryStream() not yet implemented.");
    }

    public void clearParameters() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method clearParameters() not yet implemented.");
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setObject() not yet implemented.");
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setObject() not yet implemented.");
    }

    public void setObject(int parameterIndex, Object value) throws SQLException {
        boundVariables[parameterIndex] = value;
    }

    public boolean execute() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method execute() not yet implemented.");
    }

    public void addBatch() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method addBatch() not yet implemented.");
    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setCharacterStream() not yet implemented.");
    }

    public void setRef(int i, Ref x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setRef() not yet implemented.");
    }

    public void setBlob(int i, Blob x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setBlob() not yet implemented.");
    }

    public void setClob(int i, Clob x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setClob() not yet implemented.");
    }

    public void setArray(int i, Array x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setArray() not yet implemented.");
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getMetaData() not yet implemented.");
    }

    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setDate() not yet implemented.");
    }

    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setTime() not yet implemented.");
    }

    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setTimestamp() not yet implemented.");
    }

    public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setNull() not yet implemented.");
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method executeQuery() not yet implemented.");
    }

    public int executeUpdate(String sql) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method executeUpdate() not yet implemented.");
    }

    public boolean closeCalled;
    public void close() throws SQLException {
        closeCalled = true;
    }

    public int getMaxFieldSize() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getMaxFieldSize() not yet implemented.");
    }

    public void setMaxFieldSize(int max) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setMaxFieldSize() not yet implemented.");
    }

    public int getMaxRows() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getMaxRows() not yet implemented.");
    }

    public void setMaxRows(int max) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setMaxRows() not yet implemented.");
    }

    public void setEscapeProcessing(boolean enable) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setEscapeProcessing() not yet implemented.");
    }

    public int getQueryTimeout() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getQueryTimeout() not yet implemented.");
    }

    public void setQueryTimeout(int seconds) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setQueryTimeout() not yet implemented.");
    }

    public void cancel() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method cancel() not yet implemented.");
    }

    public SQLWarning getWarnings() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getWarnings() not yet implemented.");
    }

    public void clearWarnings() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method clearWarnings() not yet implemented.");
    }

    public void setCursorName(String name) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setCursorName() not yet implemented.");
    }

    public boolean execute(String sql) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method execute() not yet implemented.");
    }

    public ResultSet getResultSet() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getResultSet() not yet implemented.");
    }

    public int getUpdateCount() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getUpdateCount() not yet implemented.");
    }

    public boolean getMoreResults() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getMoreResults() not yet implemented.");
    }

    public void setFetchDirection(int direction) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setFetchDirection() not yet implemented.");
    }

    public int getFetchDirection() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getFetchDirection() not yet implemented.");
    }

    public void setFetchSize(int rows) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method setFetchSize() not yet implemented.");
    }

    public int getFetchSize() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getFetchSize() not yet implemented.");
    }

    public int getResultSetConcurrency() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getResultSetConcurrency() not yet implemented.");
    }

    public int getResultSetType() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getResultSetType() not yet implemented.");
    }

    public void addBatch(String sql) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method addBatch() not yet implemented.");
    }

    public void clearBatch() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method clearBatch() not yet implemented.");
    }

    public int[] executeBatch() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method executeBatch() not yet implemented.");
    }

    public Connection getConnection() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getConnection() not yet implemented.");
    }

    public boolean setBytesCalled;
    public java.sql.SQLException setBytesSQLException;
    public int setBytesParameterIndex;
    public byte[] setBytesX;

    public void setBytes(int parameterIndex, byte[] x) throws java.sql.SQLException {
        setBytesCalled = true;
        setBytesParameterIndex = parameterIndex;
        setBytesX = x;
        if (setBytesSQLException != null) {
            throw setBytesSQLException;
        }
    }

    public boolean setURLCalled;
    public java.sql.SQLException setURLSQLException;
    public int setURLParameterIndex;
    public java.net.URL setURLX;

    public void setURL(int parameterIndex, java.net.URL x) throws java.sql.SQLException {
        setURLCalled = true;
        setURLParameterIndex = parameterIndex;
        setURLX = x;
        if (setURLSQLException != null) {
            throw setURLSQLException;
        }
    }

    public boolean getParameterMetaDataCalled;
    public java.sql.ParameterMetaData getParameterMetaDataReturn;
    public java.sql.SQLException getParameterMetaDataSQLException;

    public java.sql.ParameterMetaData getParameterMetaData() throws java.sql.SQLException {
        getParameterMetaDataCalled = true;
        if (getParameterMetaDataSQLException != null) {
            throw getParameterMetaDataSQLException;
        }
        return getParameterMetaDataReturn;
    }

    public boolean getMoreResultsCalled;
    public Boolean getMoreResultsReturn;
    public java.sql.SQLException getMoreResultsSQLException;
    public int getMoreResultsCurrent;

    public boolean getMoreResults(int current) throws java.sql.SQLException {
        getMoreResultsCalled = true;
        getMoreResultsCurrent = current;
        if (getMoreResultsSQLException != null) {
            throw getMoreResultsSQLException;
        }
        return getMoreResultsReturn.booleanValue();
    }

    public boolean getGeneratedKeysCalled;
    public java.sql.ResultSet getGeneratedKeysReturn;
    public java.sql.SQLException getGeneratedKeysSQLException;

    public java.sql.ResultSet getGeneratedKeys() throws java.sql.SQLException {
        getGeneratedKeysCalled = true;
        if (getGeneratedKeysSQLException != null) {
            throw getGeneratedKeysSQLException;
        }
        return getGeneratedKeysReturn;
    }

    public java.sql.SQLException executeUpdateSQLException;
    public java.lang.String executeUpdateSql;
    public int executeUpdateAutoGeneratedKeys;

    public int executeUpdate(java.lang.String sql, int autoGeneratedKeys) throws java.sql.SQLException {
        executeUpdateCalled = true;
        executeUpdateSql = sql;
        executeUpdateAutoGeneratedKeys = autoGeneratedKeys;
        if (executeUpdateSQLException != null) {
            throw executeUpdateSQLException;
        }
        return executeUpdateReturn.intValue();
    }

    public int[] executeUpdateColumnIndexes;

    public int executeUpdate(java.lang.String sql, int[] columnIndexes) throws java.sql.SQLException {
        executeUpdateCalled = true;
        executeUpdateSql = sql;
        executeUpdateColumnIndexes = columnIndexes;
        if (executeUpdateSQLException != null) {
            throw executeUpdateSQLException;
        }
        return executeUpdateReturn.intValue();
    }

    public java.lang.String[] executeUpdateColumnNames;

    public int executeUpdate(java.lang.String sql, java.lang.String[] columnNames) throws java.sql.SQLException {
        executeUpdateCalled = true;
        executeUpdateSql = sql;
        executeUpdateColumnNames = columnNames;
        if (executeUpdateSQLException != null) {
            throw executeUpdateSQLException;
        }
        return executeUpdateReturn.intValue();
    }

    public boolean executeCalled;
    public Boolean executeReturn;
    public java.sql.SQLException executeSQLException;
    public java.lang.String executeSql;
    public int executeAutoGeneratedKeys;

    public boolean execute(java.lang.String sql, int autoGeneratedKeys) throws java.sql.SQLException {
        executeCalled = true;
        executeSql = sql;
        executeAutoGeneratedKeys = autoGeneratedKeys;
        if (executeSQLException != null) {
            throw executeSQLException;
        }
        return executeReturn.booleanValue();
    }

    public int[] executeColumnIndexes;

    public boolean execute(java.lang.String sql, int[] columnIndexes) throws java.sql.SQLException {
        executeCalled = true;
        executeSql = sql;
        executeColumnIndexes = columnIndexes;
        if (executeSQLException != null) {
            throw executeSQLException;
        }
        return executeReturn.booleanValue();
    }

    public java.lang.String[] executeColumnNames;

    public boolean execute(java.lang.String sql, java.lang.String[] columnNames) throws java.sql.SQLException {
        executeCalled = true;
        executeSql = sql;
        executeColumnNames = columnNames;
        if (executeSQLException != null) {
            throw executeSQLException;
        }
        return executeReturn.booleanValue();
    }

    public boolean getResultSetHoldabilityCalled;
    public Integer getResultSetHoldabilityReturn;
    public java.sql.SQLException getResultSetHoldabilitySQLException;

    public int getResultSetHoldability() throws java.sql.SQLException {
        getResultSetHoldabilityCalled = true;
        if (getResultSetHoldabilitySQLException != null) {
            throw getResultSetHoldabilitySQLException;
        }
        return getResultSetHoldabilityReturn.intValue();
    }

    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNString(int parameterIndex, String value) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isClosed() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setPoolable(boolean poolable) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isPoolable() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}