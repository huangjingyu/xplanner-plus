package com.technoetic.mocks.sql;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MockStatement implements Statement {

    public boolean executeQueryCalled;
    public java.sql.ResultSet executeQueryReturn;
    public java.sql.SQLException executeQuerySQLException;
    public java.lang.String executeQuerySql;

    public java.sql.ResultSet executeQuery(java.lang.String sql) throws java.sql.SQLException {
        executeQueryCalled = true;
        executeQuerySql = sql;
        if (executeQuerySQLException != null) {
            throw executeQuerySQLException;
        }
        return executeQueryReturn;
    }

    public boolean executeUpdateCalled;
    public Integer executeUpdateReturn;
    public java.sql.SQLException executeUpdateSQLException;
    public java.lang.String executeUpdateSql;

    public int executeUpdate(java.lang.String sql) throws java.sql.SQLException {
        executeUpdateCalled = true;
        executeUpdateSql = sql;
        if (executeUpdateSQLException != null) {
            throw executeUpdateSQLException;
        }
        return executeUpdateReturn.intValue();
    }

    public boolean closeCalled;
    public java.sql.SQLException closeSQLException;

    public void close() throws java.sql.SQLException {
        closeCalled = true;
        if (closeSQLException != null) {
            throw closeSQLException;
        }
    }

    public boolean getMaxFieldSizeCalled;
    public Integer getMaxFieldSizeReturn;
    public java.sql.SQLException getMaxFieldSizeSQLException;

    public int getMaxFieldSize() throws java.sql.SQLException {
        getMaxFieldSizeCalled = true;
        if (getMaxFieldSizeSQLException != null) {
            throw getMaxFieldSizeSQLException;
        }
        return getMaxFieldSizeReturn.intValue();
    }

    public boolean setMaxFieldSizeCalled;
    public java.sql.SQLException setMaxFieldSizeSQLException;
    public int setMaxFieldSizeMax;

    public void setMaxFieldSize(int max) throws java.sql.SQLException {
        setMaxFieldSizeCalled = true;
        setMaxFieldSizeMax = max;
        if (setMaxFieldSizeSQLException != null) {
            throw setMaxFieldSizeSQLException;
        }
    }

    public boolean getMaxRowsCalled;
    public Integer getMaxRowsReturn;
    public java.sql.SQLException getMaxRowsSQLException;

    public int getMaxRows() throws java.sql.SQLException {
        getMaxRowsCalled = true;
        if (getMaxRowsSQLException != null) {
            throw getMaxRowsSQLException;
        }
        return getMaxRowsReturn.intValue();
    }

    public boolean setMaxRowsCalled;
    public java.sql.SQLException setMaxRowsSQLException;
    public int setMaxRowsMax;

    public void setMaxRows(int max) throws java.sql.SQLException {
        setMaxRowsCalled = true;
        setMaxRowsMax = max;
        if (setMaxRowsSQLException != null) {
            throw setMaxRowsSQLException;
        }
    }

    public boolean setEscapeProcessingCalled;
    public java.sql.SQLException setEscapeProcessingSQLException;
    public boolean setEscapeProcessingEnable;

    public void setEscapeProcessing(boolean enable) throws java.sql.SQLException {
        setEscapeProcessingCalled = true;
        setEscapeProcessingEnable = enable;
        if (setEscapeProcessingSQLException != null) {
            throw setEscapeProcessingSQLException;
        }
    }

    public boolean getQueryTimeoutCalled;
    public Integer getQueryTimeoutReturn;
    public java.sql.SQLException getQueryTimeoutSQLException;

    public int getQueryTimeout() throws java.sql.SQLException {
        getQueryTimeoutCalled = true;
        if (getQueryTimeoutSQLException != null) {
            throw getQueryTimeoutSQLException;
        }
        return getQueryTimeoutReturn.intValue();
    }

    public boolean setQueryTimeoutCalled;
    public java.sql.SQLException setQueryTimeoutSQLException;
    public int setQueryTimeoutSeconds;

    public void setQueryTimeout(int seconds) throws java.sql.SQLException {
        setQueryTimeoutCalled = true;
        setQueryTimeoutSeconds = seconds;
        if (setQueryTimeoutSQLException != null) {
            throw setQueryTimeoutSQLException;
        }
    }

    public boolean cancelCalled;
    public java.sql.SQLException cancelSQLException;

    public void cancel() throws java.sql.SQLException {
        cancelCalled = true;
        if (cancelSQLException != null) {
            throw cancelSQLException;
        }
    }

    public boolean getWarningsCalled;
    public java.sql.SQLWarning getWarningsReturn;
    public java.sql.SQLException getWarningsSQLException;

    public java.sql.SQLWarning getWarnings() throws java.sql.SQLException {
        getWarningsCalled = true;
        if (getWarningsSQLException != null) {
            throw getWarningsSQLException;
        }
        return getWarningsReturn;
    }

    public boolean clearWarningsCalled;
    public java.sql.SQLException clearWarningsSQLException;

    public void clearWarnings() throws java.sql.SQLException {
        clearWarningsCalled = true;
        if (clearWarningsSQLException != null) {
            throw clearWarningsSQLException;
        }
    }

    public boolean setCursorNameCalled;
    public java.sql.SQLException setCursorNameSQLException;
    public java.lang.String setCursorNameName;

    public void setCursorName(java.lang.String name) throws java.sql.SQLException {
        setCursorNameCalled = true;
        setCursorNameName = name;
        if (setCursorNameSQLException != null) {
            throw setCursorNameSQLException;
        }
    }

    public boolean executeCalled;
    public Boolean executeReturn;
    public java.sql.SQLException executeSQLException;
    public java.lang.String executeSql;
    public ArrayList executeSqlList = new ArrayList();

    public boolean execute(java.lang.String sql) throws java.sql.SQLException {
        executeCalled = true;
        executeSql = sql;
        executeSqlList.add(sql);
        if (executeSQLException != null) {
            throw executeSQLException;
        }
        return executeReturn.booleanValue();
    }

    public boolean getResultSetCalled;
    public java.sql.ResultSet getResultSetReturn;
    public java.sql.SQLException getResultSetSQLException;

    public java.sql.ResultSet getResultSet() throws java.sql.SQLException {
        getResultSetCalled = true;
        if (getResultSetSQLException != null) {
            throw getResultSetSQLException;
        }
        return getResultSetReturn;
    }

    public boolean getUpdateCountCalled;
    public Integer getUpdateCountReturn;
    public java.sql.SQLException getUpdateCountSQLException;

    public int getUpdateCount() throws java.sql.SQLException {
        getUpdateCountCalled = true;
        if (getUpdateCountSQLException != null) {
            throw getUpdateCountSQLException;
        }
        return getUpdateCountReturn.intValue();
    }

    public boolean getMoreResultsCalled;
    public Boolean getMoreResultsReturn;
    public java.sql.SQLException getMoreResultsSQLException;

    public boolean getMoreResults() throws java.sql.SQLException {
        getMoreResultsCalled = true;
        if (getMoreResultsSQLException != null) {
            throw getMoreResultsSQLException;
        }
        return getMoreResultsReturn.booleanValue();
    }

    public boolean setFetchDirectionCalled;
    public java.sql.SQLException setFetchDirectionSQLException;
    public int setFetchDirectionDirection;

    public void setFetchDirection(int direction) throws java.sql.SQLException {
        setFetchDirectionCalled = true;
        setFetchDirectionDirection = direction;
        if (setFetchDirectionSQLException != null) {
            throw setFetchDirectionSQLException;
        }
    }

    public boolean getFetchDirectionCalled;
    public Integer getFetchDirectionReturn;
    public java.sql.SQLException getFetchDirectionSQLException;

    public int getFetchDirection() throws java.sql.SQLException {
        getFetchDirectionCalled = true;
        if (getFetchDirectionSQLException != null) {
            throw getFetchDirectionSQLException;
        }
        return getFetchDirectionReturn.intValue();
    }

    public boolean setFetchSizeCalled;
    public java.sql.SQLException setFetchSizeSQLException;
    public int setFetchSizeRows;

    public void setFetchSize(int rows) throws java.sql.SQLException {
        setFetchSizeCalled = true;
        setFetchSizeRows = rows;
        if (setFetchSizeSQLException != null) {
            throw setFetchSizeSQLException;
        }
    }

    public boolean getFetchSizeCalled;
    public Integer getFetchSizeReturn;
    public java.sql.SQLException getFetchSizeSQLException;

    public int getFetchSize() throws java.sql.SQLException {
        getFetchSizeCalled = true;
        if (getFetchSizeSQLException != null) {
            throw getFetchSizeSQLException;
        }
        return getFetchSizeReturn.intValue();
    }

    public boolean getResultSetConcurrencyCalled;
    public Integer getResultSetConcurrencyReturn;
    public java.sql.SQLException getResultSetConcurrencySQLException;

    public int getResultSetConcurrency() throws java.sql.SQLException {
        getResultSetConcurrencyCalled = true;
        if (getResultSetConcurrencySQLException != null) {
            throw getResultSetConcurrencySQLException;
        }
        return getResultSetConcurrencyReturn.intValue();
    }

    public boolean getResultSetTypeCalled;
    public Integer getResultSetTypeReturn;
    public java.sql.SQLException getResultSetTypeSQLException;

    public int getResultSetType() throws java.sql.SQLException {
        getResultSetTypeCalled = true;
        if (getResultSetTypeSQLException != null) {
            throw getResultSetTypeSQLException;
        }
        return getResultSetTypeReturn.intValue();
    }

    public boolean addBatchCalled;
    public java.sql.SQLException addBatchSQLException;
    public java.lang.String addBatchSql;

    public void addBatch(java.lang.String sql) throws java.sql.SQLException {
        addBatchCalled = true;
        addBatchSql = sql;
        if (addBatchSQLException != null) {
            throw addBatchSQLException;
        }
    }

    public boolean clearBatchCalled;
    public java.sql.SQLException clearBatchSQLException;

    public void clearBatch() throws java.sql.SQLException {
        clearBatchCalled = true;
        if (clearBatchSQLException != null) {
            throw clearBatchSQLException;
        }
    }

    public boolean executeBatchCalled;
    public int[] executeBatchReturn;
    public java.sql.SQLException executeBatchSQLException;

    public int[] executeBatch() throws java.sql.SQLException {
        executeBatchCalled = true;
        if (executeBatchSQLException != null) {
            throw executeBatchSQLException;
        }
        return executeBatchReturn;
    }

    public boolean getConnectionCalled;
    public java.sql.Connection getConnectionReturn;
    public java.sql.SQLException getConnectionSQLException;

    public java.sql.Connection getConnection() throws java.sql.SQLException {
        getConnectionCalled = true;
        if (getConnectionSQLException != null) {
            throw getConnectionSQLException;
        }
        return getConnectionReturn;
    }

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