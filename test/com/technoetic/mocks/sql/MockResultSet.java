package com.technoetic.mocks.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockResultSet implements ResultSet {
    public ArrayList returnedRows;
    public HashMap columnMap;
    private List currentRow;

    public boolean nextCalled;
    public int nextCalledCount;
    public Boolean nextReturn;
    public java.sql.SQLException nextSQLException;

    public boolean next() throws java.sql.SQLException {
        nextCalled = true;
        nextCalledCount++;
        if (nextSQLException != null) {
            throw nextSQLException;
        }
        if (nextReturn != null || returnedRows == null) {
            return nextReturn.booleanValue();
        } else {
            if (nextCalledCount <= returnedRows.size()) {
                currentRow = (List)returnedRows.get(nextCalledCount - 1);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean wasNull() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method wasNull() not yet implemented.");
    }

    public String getString(int columnIndex) throws SQLException {
        return (String)currentRow.get(columnIndex-1);
    }

    public boolean getBoolean(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBoolean() not yet implemented.");
    }

    public byte getByte(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getByte() not yet implemented.");
    }

    public short getShort(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getShort() not yet implemented.");
    }


    public int getInt(int columnIndex) throws SQLException {
        return ((Integer)currentRow.get(columnIndex - 1)).intValue();
    }

    public long getLong(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getLong() not yet implemented.");
    }

    public float getFloat(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getFloat() not yet implemented.");
    }

    public double getDouble(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getDouble() not yet implemented.");
    }

    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBigDecimal() not yet implemented.");
    }

    public byte[] getBytes(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBytes() not yet implemented.");
    }

    public Date getDate(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getDate() not yet implemented.");
    }

    public Time getTime(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getTime() not yet implemented.");
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getTimestamp() not yet implemented.");
    }

    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getAsciiStream() not yet implemented.");
    }

    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getUnicodeStream() not yet implemented.");
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBinaryStream() not yet implemented.");
    }

    public String getString(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getString() not yet implemented.");
    }

    public boolean getBoolean(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBoolean() not yet implemented.");
    }

    public byte getByte(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getByte() not yet implemented.");
    }

    public short getShort(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getShort() not yet implemented.");
    }

    private Object getColumnValue(String columnName) {
        return currentRow.get(((Integer)columnMap.get(columnName)).intValue() - 1);
    }

    public int getInt(String columnName) throws SQLException {
        return ((Integer)getColumnValue(columnName)).intValue();
    }

    public long getLong(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getLong() not yet implemented.");
    }

    public float getFloat(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getFloat() not yet implemented.");
    }

    public double getDouble(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getDouble() not yet implemented.");
    }

    public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBigDecimal() not yet implemented.");
    }

    public byte[] getBytes(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBytes() not yet implemented.");
    }

    public Date getDate(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getDate() not yet implemented.");
    }

    public Time getTime(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getTime() not yet implemented.");
    }

    public Timestamp getTimestamp(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getTimestamp() not yet implemented.");
    }

    public InputStream getAsciiStream(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getAsciiStream() not yet implemented.");
    }

    public InputStream getUnicodeStream(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getUnicodeStream() not yet implemented.");
    }

    public InputStream getBinaryStream(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBinaryStream() not yet implemented.");
    }

    public SQLWarning getWarnings() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getWarnings() not yet implemented.");
    }

    public void clearWarnings() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method clearWarnings() not yet implemented.");
    }

    public String getCursorName() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getCursorName() not yet implemented.");
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getMetaData() not yet implemented.");
    }

    public Object getObject(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getObject() not yet implemented.");
    }

    public Object getObject(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getObject() not yet implemented.");
    }

    public int findColumn(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method findColumn() not yet implemented.");
    }

    public Reader getCharacterStream(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getCharacterStream() not yet implemented.");
    }

    public Reader getCharacterStream(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getCharacterStream() not yet implemented.");
    }

    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBigDecimal() not yet implemented.");
    }

    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBigDecimal() not yet implemented.");
    }

    public boolean isBeforeFirst() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method isBeforeFirst() not yet implemented.");
    }

    public boolean isAfterLast() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method isAfterLast() not yet implemented.");
    }

    public boolean isFirst() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method isFirst() not yet implemented.");
    }

    public boolean isLast() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method isLast() not yet implemented.");
    }

    public void beforeFirst() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method beforeFirst() not yet implemented.");
    }

    public void afterLast() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method afterLast() not yet implemented.");
    }

    public boolean first() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method first() not yet implemented.");
    }

    public boolean last() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method last() not yet implemented.");
    }

    public int getRow() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getRow() not yet implemented.");
    }

    public boolean absolute(int row) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method absolute() not yet implemented.");
    }

    public boolean relative(int rows) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method relative() not yet implemented.");
    }

    public boolean previous() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method previous() not yet implemented.");
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

    public int getType() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getType() not yet implemented.");
    }

    public int getConcurrency() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getConcurrency() not yet implemented.");
    }

    public boolean rowUpdated() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method rowUpdated() not yet implemented.");
    }

    public boolean rowInserted() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method rowInserted() not yet implemented.");
    }

    public boolean rowDeleted() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method rowDeleted() not yet implemented.");
    }

    public void updateNull(int columnIndex) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateNull() not yet implemented.");
    }

    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateBoolean() not yet implemented.");
    }

    public void updateByte(int columnIndex, byte x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateByte() not yet implemented.");
    }

    public void updateShort(int columnIndex, short x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateShort() not yet implemented.");
    }

    public void updateInt(int columnIndex, int x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateInt() not yet implemented.");
    }

    public void updateLong(int columnIndex, long x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateLong() not yet implemented.");
    }

    public void updateFloat(int columnIndex, float x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateFloat() not yet implemented.");
    }

    public void updateDouble(int columnIndex, double x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateDouble() not yet implemented.");
    }

    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateBigDecimal() not yet implemented.");
    }

    public void updateString(int columnIndex, String x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateString() not yet implemented.");
    }

    public void updateDate(int columnIndex, Date x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateDate() not yet implemented.");
    }

    public void updateTime(int columnIndex, Time x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateTime() not yet implemented.");
    }

    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateTimestamp() not yet implemented.");
    }

    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateAsciiStream() not yet implemented.");
    }

    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateBinaryStream() not yet implemented.");
    }

    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateCharacterStream() not yet implemented.");
    }

    public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateObject() not yet implemented.");
    }

    public void updateObject(int columnIndex, Object x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateObject() not yet implemented.");
    }

    public void updateNull(String columnName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateNull() not yet implemented.");
    }

    public void updateBoolean(String columnName, boolean x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateBoolean() not yet implemented.");
    }

    public void updateByte(String columnName, byte x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateByte() not yet implemented.");
    }

    public void updateShort(String columnName, short x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateShort() not yet implemented.");
    }

    public void updateInt(String columnName, int x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateInt() not yet implemented.");
    }

    public void updateLong(String columnName, long x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateLong() not yet implemented.");
    }

    public void updateFloat(String columnName, float x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateFloat() not yet implemented.");
    }

    public void updateDouble(String columnName, double x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateDouble() not yet implemented.");
    }

    public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateBigDecimal() not yet implemented.");
    }

    public void updateString(String columnName, String x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateString() not yet implemented.");
    }

    public void updateDate(String columnName, Date x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateDate() not yet implemented.");
    }

    public void updateTime(String columnName, Time x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateTime() not yet implemented.");
    }

    public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateTimestamp() not yet implemented.");
    }

    public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateAsciiStream() not yet implemented.");
    }

    public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateBinaryStream() not yet implemented.");
    }

    public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateCharacterStream() not yet implemented.");
    }

    public void updateObject(String columnName, Object x, int scale) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateObject() not yet implemented.");
    }

    public void updateObject(String columnName, Object x) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateObject() not yet implemented.");
    }

    public void insertRow() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method insertRow() not yet implemented.");
    }

    public void updateRow() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method updateRow() not yet implemented.");
    }

    public void deleteRow() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method deleteRow() not yet implemented.");
    }

    public void refreshRow() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method refreshRow() not yet implemented.");
    }

    public void cancelRowUpdates() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method cancelRowUpdates() not yet implemented.");
    }

    public void moveToInsertRow() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method moveToInsertRow() not yet implemented.");
    }

    public void moveToCurrentRow() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method moveToCurrentRow() not yet implemented.");
    }

    public Statement getStatement() throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getStatement() not yet implemented.");
    }

    public Object getObject(int i, Map map) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getObject() not yet implemented.");
    }

    public Ref getRef(int i) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getRef() not yet implemented.");
    }

    public Blob getBlob(int i) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBlob() not yet implemented.");
    }

    public Clob getClob(int i) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getClob() not yet implemented.");
    }

    public Array getArray(int i) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getArray() not yet implemented.");
    }

    public Object getObject(String colName, Map map) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getObject() not yet implemented.");
    }

    public Ref getRef(String colName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getRef() not yet implemented.");
    }

    public Blob getBlob(String colName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getBlob() not yet implemented.");
    }

    public Clob getClob(String colName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getClob() not yet implemented.");
    }

    public Array getArray(String colName) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getArray() not yet implemented.");
    }

    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getDate() not yet implemented.");
    }

    public Date getDate(String columnName, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getDate() not yet implemented.");
    }

    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getTime() not yet implemented.");
    }

    public Time getTime(String columnName, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getTime() not yet implemented.");
    }

    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getTimestamp() not yet implemented.");
    }

    public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
        throw new java.lang.UnsupportedOperationException("Method getTimestamp() not yet implemented.");
    }

    public boolean closeCalled;
    public java.sql.SQLException closeSQLException;

    public void close() throws java.sql.SQLException {
        closeCalled = true;
        if (closeSQLException != null) {
            throw closeSQLException;
        }
    }

    public boolean updateBytesCalled;
    public java.sql.SQLException updateBytesSQLException;
    public int updateBytesColumnIndex;
    public byte[] updateBytesX;

    public void updateBytes(int columnIndex, byte[] x) throws java.sql.SQLException {
        updateBytesCalled = true;
        updateBytesColumnIndex = columnIndex;
        updateBytesX = x;
        if (updateBytesSQLException != null) {
            throw updateBytesSQLException;
        }
    }

    public boolean updateBytes2Called;
    public java.sql.SQLException updateBytes2SQLException;
    public String updateBytes2ColumnName;
    public byte[] updateBytes2X;

    public void updateBytes(java.lang.String columnName, byte[] x) throws java.sql.SQLException {
        updateBytes2Called = true;
        updateBytes2ColumnName = columnName;
        updateBytes2X = x;
        if (updateBytes2SQLException != null) {
            throw updateBytes2SQLException;
        }
    }

    public boolean getURLCalled;
    public java.net.URL getURLReturn;
    public java.sql.SQLException getURLSQLException;
    public int getURLColumnIndex;

    public java.net.URL getURL(int columnIndex) throws java.sql.SQLException {
        getURLCalled = true;
        getURLColumnIndex = columnIndex;
        if (getURLSQLException != null) {
            throw getURLSQLException;
        }
        return getURLReturn;
    }

    public java.lang.String getURLColumnName;

    public java.net.URL getURL(java.lang.String columnName) throws java.sql.SQLException {
        getURLCalled = true;
        getURLColumnName = columnName;
        if (getURLSQLException != null) {
            throw getURLSQLException;
        }
        return getURLReturn;
    }

    public boolean updateRefCalled;
    public java.sql.SQLException updateRefSQLException;
    public int updateRefColumnIndex;
    public java.sql.Ref updateRefX;

    public void updateRef(int columnIndex, java.sql.Ref x) throws java.sql.SQLException {
        updateRefCalled = true;
        updateRefColumnIndex = columnIndex;
        updateRefX = x;
        if (updateRefSQLException != null) {
            throw updateRefSQLException;
        }
    }

    public java.lang.String updateRefColumnName;

    public void updateRef(java.lang.String columnName, java.sql.Ref x) throws java.sql.SQLException {
        updateRefCalled = true;
        updateRefColumnName = columnName;
        updateRefX = x;
        if (updateRefSQLException != null) {
            throw updateRefSQLException;
        }
    }

    public boolean updateBlobCalled;
    public java.sql.SQLException updateBlobSQLException;
    public int updateBlobColumnIndex;
    public java.sql.Blob updateBlobX;

    public void updateBlob(int columnIndex, java.sql.Blob x) throws java.sql.SQLException {
        updateBlobCalled = true;
        updateBlobColumnIndex = columnIndex;
        updateBlobX = x;
        if (updateBlobSQLException != null) {
            throw updateBlobSQLException;
        }
    }

    public java.lang.String updateBlobColumnName;

    public void updateBlob(java.lang.String columnName, java.sql.Blob x) throws java.sql.SQLException {
        updateBlobCalled = true;
        updateBlobColumnName = columnName;
        updateBlobX = x;
        if (updateBlobSQLException != null) {
            throw updateBlobSQLException;
        }
    }

    public boolean updateClobCalled;
    public java.sql.SQLException updateClobSQLException;
    public int updateClobColumnIndex;
    public java.sql.Clob updateClobX;

    public void updateClob(int columnIndex, java.sql.Clob x) throws java.sql.SQLException {
        updateClobCalled = true;
        updateClobColumnIndex = columnIndex;
        updateClobX = x;
        if (updateClobSQLException != null) {
            throw updateClobSQLException;
        }
    }

    public java.lang.String updateClobColumnName;

    public void updateClob(java.lang.String columnName, java.sql.Clob x) throws java.sql.SQLException {
        updateClobCalled = true;
        updateClobColumnName = columnName;
        updateClobX = x;
        if (updateClobSQLException != null) {
            throw updateClobSQLException;
        }
    }

    public boolean updateArrayCalled;
    public java.sql.SQLException updateArraySQLException;
    public int updateArrayColumnIndex;
    public java.sql.Array updateArrayX;

    public void updateArray(int columnIndex, java.sql.Array x) throws java.sql.SQLException {
        updateArrayCalled = true;
        updateArrayColumnIndex = columnIndex;
        updateArrayX = x;
        if (updateArraySQLException != null) {
            throw updateArraySQLException;
        }
    }

    public java.lang.String updateArrayColumnName;

    public void updateArray(java.lang.String columnName, java.sql.Array x) throws java.sql.SQLException {
        updateArrayCalled = true;
        updateArrayColumnName = columnName;
        updateArrayX = x;
        if (updateArraySQLException != null) {
            throw updateArraySQLException;
        }
    }

    public RowId getRowId(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public RowId getRowId(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getHoldability() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isClosed() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateNString(int columnIndex, String nString) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateNString(String columnLabel, String nString) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NClob getNClob(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NClob getNClob(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getNString(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getNString(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}