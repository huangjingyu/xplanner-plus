package com.technoetic.mocks.sql;

import java.sql.*;
import java.util.HashMap;
import java.util.Properties;

public class MockConnection implements Connection {
    public boolean createStatementCalled;
    public Statement createStatementReturn;
    public SQLException createStatementSQLException;

    public Statement createStatement() throws SQLException {
        createStatementCalled = true;
        if (createStatementSQLException != null) {
            throw createStatementSQLException;
        }
        return createStatementReturn;
    }

    public boolean prepareStatementCalled;
    public PreparedStatement prepareStatementReturn;
    public HashMap prepareStatementReturnMap;
    public SQLException prepareStatementSQLException;
    public java.lang.String prepareStatementSql;

    public PreparedStatement prepareStatement(java.lang.String sql) throws SQLException {
        prepareStatementCalled = true;
        prepareStatementSql = sql;
        if (prepareStatementSQLException != null) {
            throw prepareStatementSQLException;
        }
        if (prepareStatementReturnMap != null) {
            return (PreparedStatement)prepareStatementReturnMap.get(sql);
        } else {
            return prepareStatementReturn;
        }
    }

    public boolean prepareCallCalled;
    public CallableStatement prepareCallReturn;
    public SQLException prepareCallSQLException;
    public java.lang.String prepareCallSql;

    public CallableStatement prepareCall(java.lang.String sql) throws SQLException {
        prepareCallCalled = true;
        prepareCallSql = sql;
        if (prepareCallSQLException != null) {
            throw prepareCallSQLException;
        }
        return prepareCallReturn;
    }

    public boolean nativeSQLCalled;
    public java.lang.String nativeSQLReturn;
    public SQLException nativeSQLSQLException;
    public java.lang.String nativeSQLSql;

    public java.lang.String nativeSQL(java.lang.String sql) throws SQLException {
        nativeSQLCalled = true;
        nativeSQLSql = sql;
        if (nativeSQLSQLException != null) {
            throw nativeSQLSQLException;
        }
        return nativeSQLReturn;
    }

    public boolean setAutoCommitCalled;
    public SQLException setAutoCommitSQLException;
    public boolean setAutoCommitAutoCommit;

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        setAutoCommitCalled = true;
        setAutoCommitAutoCommit = autoCommit;
        if (setAutoCommitSQLException != null) {
            throw setAutoCommitSQLException;
        }
    }

    public boolean getAutoCommitCalled;
    public Boolean getAutoCommitReturn;
    public SQLException getAutoCommitSQLException;

    public boolean getAutoCommit() throws SQLException {
        getAutoCommitCalled = true;
        if (getAutoCommitSQLException != null) {
            throw getAutoCommitSQLException;
        }
        return getAutoCommitReturn.booleanValue();
    }

    public boolean commitCalled;
    public SQLException commitSQLException;

    public void commit() throws SQLException {
        commitCalled = true;
        if (commitSQLException != null) {
            throw commitSQLException;
        }
    }

    public boolean rollbackCalled;
    public SQLException rollbackSQLException;

    public void rollback() throws SQLException {
        rollbackCalled = true;
        if (rollbackSQLException != null) {
            throw rollbackSQLException;
        }
    }

    public boolean closeCalled;
    public SQLException closeSQLException;

    public void close() throws SQLException {
        closeCalled = true;
        if (closeSQLException != null) {
            throw closeSQLException;
        }
    }

    public boolean isClosedCalled;
    public Boolean isClosedReturn;
    public SQLException isClosedSQLException;

    public boolean isClosed() throws SQLException {
        isClosedCalled = true;
        if (isClosedSQLException != null) {
            throw isClosedSQLException;
        }
        return isClosedReturn.booleanValue();
    }

    public boolean getMetaDataCalled;
    public DatabaseMetaData getMetaDataReturn;
    public SQLException getMetaDataSQLException;

    public DatabaseMetaData getMetaData() throws SQLException {
        getMetaDataCalled = true;
        if (getMetaDataSQLException != null) {
            throw getMetaDataSQLException;
        }
        return getMetaDataReturn;
    }

    public boolean setReadOnlyCalled;
    public SQLException setReadOnlySQLException;
    public boolean setReadOnlyReadOnly;

    public void setReadOnly(boolean readOnly) throws SQLException {
        setReadOnlyCalled = true;
        setReadOnlyReadOnly = readOnly;
        if (setReadOnlySQLException != null) {
            throw setReadOnlySQLException;
        }
    }

    public boolean isReadOnlyCalled;
    public Boolean isReadOnlyReturn;
    public SQLException isReadOnlySQLException;

    public boolean isReadOnly() throws SQLException {
        isReadOnlyCalled = true;
        if (isReadOnlySQLException != null) {
            throw isReadOnlySQLException;
        }
        return isReadOnlyReturn.booleanValue();
    }

    public boolean setCatalogCalled;
    public SQLException setCatalogSQLException;
    public java.lang.String setCatalogCatalog;

    public void setCatalog(java.lang.String catalog) throws SQLException {
        setCatalogCalled = true;
        setCatalogCatalog = catalog;
        if (setCatalogSQLException != null) {
            throw setCatalogSQLException;
        }
    }

    public boolean getCatalogCalled;
    public java.lang.String getCatalogReturn;
    public SQLException getCatalogSQLException;

    public java.lang.String getCatalog() throws SQLException {
        getCatalogCalled = true;
        if (getCatalogSQLException != null) {
            throw getCatalogSQLException;
        }
        return getCatalogReturn;
    }

    public boolean setTransactionIsolationCalled;
    public SQLException setTransactionIsolationSQLException;
    public int setTransactionIsolationLevel;

    public void setTransactionIsolation(int level) throws SQLException {
        setTransactionIsolationCalled = true;
        setTransactionIsolationLevel = level;
        if (setTransactionIsolationSQLException != null) {
            throw setTransactionIsolationSQLException;
        }
    }

    public boolean getTransactionIsolationCalled;
    public Integer getTransactionIsolationReturn;
    public SQLException getTransactionIsolationSQLException;

    public int getTransactionIsolation() throws SQLException {
        getTransactionIsolationCalled = true;
        if (getTransactionIsolationSQLException != null) {
            throw getTransactionIsolationSQLException;
        }
        return getTransactionIsolationReturn.intValue();
    }

    public boolean getWarningsCalled;
    public SQLWarning getWarningsReturn;
    public SQLException getWarningsSQLException;

    public SQLWarning getWarnings() throws SQLException {
        getWarningsCalled = true;
        if (getWarningsSQLException != null) {
            throw getWarningsSQLException;
        }
        return getWarningsReturn;
    }

    public boolean clearWarningsCalled;
    public SQLException clearWarningsSQLException;

    public void clearWarnings() throws SQLException {
        clearWarningsCalled = true;
        if (clearWarningsSQLException != null) {
            throw clearWarningsSQLException;
        }
    }

    public int createStatementResultSetType;
    public int createStatementResultSetConcurrency;

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        createStatementCalled = true;
        createStatementResultSetType = resultSetType;
        createStatementResultSetConcurrency = resultSetConcurrency;
        if (createStatementSQLException != null) {
            throw createStatementSQLException;
        }
        return createStatementReturn;
    }

    public int prepareStatement2ResultSetType;
    public int prepareStatement2ResultSetConcurrency;

    public PreparedStatement prepareStatement(java.lang.String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new UnsupportedOperationException();
//        prepareStatementCalled = true;
//        prepareStatementSql = sql;
//        prepareStatementResultSetType = resultSetType;
//        prepareStatementResultSetConcurrency = resultSetConcurrency;
//        if (prepareStatementSQLException != null) {
//            throw prepareStatementSQLException;
//        }
//        return prepareStatementReturn;
    }

    public int prepareCallResultSetType;
    public int prepareCallResultSetConcurrency;

    public CallableStatement prepareCall(java.lang.String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        prepareCallCalled = true;
        prepareCallSql = sql;
        prepareCallResultSetType = resultSetType;
        prepareCallResultSetConcurrency = resultSetConcurrency;
        if (prepareCallSQLException != null) {
            throw prepareCallSQLException;
        }
        return prepareCallReturn;
    }

    public boolean getTypeMapCalled;
    public java.util.Map getTypeMapReturn;
    public SQLException getTypeMapSQLException;

    public java.util.Map getTypeMap() throws SQLException {
        getTypeMapCalled = true;
        if (getTypeMapSQLException != null) {
            throw getTypeMapSQLException;
        }
        return getTypeMapReturn;
    }

    public boolean setTypeMapCalled;
    public SQLException setTypeMapSQLException;
    public java.util.Map setTypeMapMap;

    public void setTypeMap(java.util.Map map) throws SQLException {
        setTypeMapCalled = true;
        setTypeMapMap = map;
        if (setTypeMapSQLException != null) {
            throw setTypeMapSQLException;
        }
    }

    public boolean setHoldabilityCalled;
    public java.sql.SQLException setHoldabilitySQLException;
    public int setHoldabilityHoldability;

    public void setHoldability(int holdability) throws java.sql.SQLException {
        setHoldabilityCalled = true;
        setHoldabilityHoldability = holdability;
        if (setHoldabilitySQLException != null) {
            throw setHoldabilitySQLException;
        }
    }

    public boolean getHoldabilityCalled;
    public Integer getHoldabilityReturn;
    public java.sql.SQLException getHoldabilitySQLException;

    public int getHoldability() throws java.sql.SQLException {
        getHoldabilityCalled = true;
        if (getHoldabilitySQLException != null) {
            throw getHoldabilitySQLException;
        }
        return getHoldabilityReturn.intValue();
    }

    public boolean setSavepointCalled;
    public java.sql.Savepoint setSavepointReturn;
    public java.sql.SQLException setSavepointSQLException;

    public java.sql.Savepoint setSavepoint() throws java.sql.SQLException {
        setSavepointCalled = true;
        if (setSavepointSQLException != null) {
            throw setSavepointSQLException;
        }
        return setSavepointReturn;
    }

    public java.lang.String setSavepointName;

    public java.sql.Savepoint setSavepoint(java.lang.String name) throws java.sql.SQLException {
        setSavepointCalled = true;
        setSavepointName = name;
        if (setSavepointSQLException != null) {
            throw setSavepointSQLException;
        }
        return setSavepointReturn;
    }

    public java.sql.Savepoint rollbackSavepoint;

    public void rollback(java.sql.Savepoint savepoint) throws java.sql.SQLException {
        rollbackCalled = true;
        rollbackSavepoint = savepoint;
        if (rollbackSQLException != null) {
            throw rollbackSQLException;
        }
    }

    public boolean releaseSavepointCalled;
    public java.sql.SQLException releaseSavepointSQLException;
    public java.sql.Savepoint releaseSavepointSavepoint;

    public void releaseSavepoint(java.sql.Savepoint savepoint) throws java.sql.SQLException {
        releaseSavepointCalled = true;
        releaseSavepointSavepoint = savepoint;
        if (releaseSavepointSQLException != null) {
            throw releaseSavepointSQLException;
        }
    }

    public int createStatementResultSetHoldability;

    public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws java.sql.SQLException {
        createStatementCalled = true;
        createStatementResultSetType = resultSetType;
        createStatementResultSetConcurrency = resultSetConcurrency;
        createStatementResultSetHoldability = resultSetHoldability;
        if (createStatementSQLException != null) {
            throw createStatementSQLException;
        }
        return createStatementReturn;
    }

    public int prepareStatementResultSetType;
    public int prepareStatementResultSetConcurrency;
    public int prepareStatementResultSetHoldability;

    public java.sql.PreparedStatement prepareStatement(java.lang.String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws java.sql.SQLException {
        prepareStatementCalled = true;
        prepareStatementSql = sql;
        prepareStatementResultSetType = resultSetType;
        prepareStatementResultSetConcurrency = resultSetConcurrency;
        prepareStatementResultSetHoldability = resultSetHoldability;
        if (prepareStatementSQLException != null) {
            throw prepareStatementSQLException;
        }
        return prepareStatementReturn;
    }

    public int prepareCallResultSetHoldability;

    public java.sql.CallableStatement prepareCall(java.lang.String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws java.sql.SQLException {
        prepareCallCalled = true;
        prepareCallSql = sql;
        prepareCallResultSetType = resultSetType;
        prepareCallResultSetConcurrency = resultSetConcurrency;
        prepareCallResultSetHoldability = resultSetHoldability;
        if (prepareCallSQLException != null) {
            throw prepareCallSQLException;
        }
        return prepareCallReturn;
    }

    public int prepareStatementAutoGeneratedKeys;

    public java.sql.PreparedStatement prepareStatement(java.lang.String sql, int autoGeneratedKeys) throws java.sql.SQLException {
        prepareStatementCalled = true;
        prepareStatementSql = sql;
        prepareStatementAutoGeneratedKeys = autoGeneratedKeys;
        if (prepareStatementSQLException != null) {
            throw prepareStatementSQLException;
        }
        return prepareStatementReturn;
    }

    public int[] prepareStatementColumnIndexes;

    public java.sql.PreparedStatement prepareStatement(java.lang.String sql, int[] columnIndexes) throws java.sql.SQLException {
        prepareStatementCalled = true;
        prepareStatementSql = sql;
        prepareStatementColumnIndexes = columnIndexes;
        if (prepareStatementSQLException != null) {
            throw prepareStatementSQLException;
        }
        return prepareStatementReturn;
    }

    public java.lang.String[] prepareStatementColumnNames;

    public java.sql.PreparedStatement prepareStatement(java.lang.String sql, java.lang.String[] columnNames) throws java.sql.SQLException {
        prepareStatementCalled = true;
        prepareStatementSql = sql;
        prepareStatementColumnNames = columnNames;
        if (prepareStatementSQLException != null) {
            throw prepareStatementSQLException;
        }
        return prepareStatementReturn;
    }

    public Clob createClob() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Blob createBlob() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NClob createNClob() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SQLXML createSQLXML() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isValid(int timeout) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getClientInfo(String name) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Properties getClientInfo() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
