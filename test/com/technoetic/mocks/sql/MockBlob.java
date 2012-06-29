package com.technoetic.mocks.sql;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class MockBlob implements Blob {
    public InputStream getBinaryStreamReturn;
    public InputStream getBinaryStream() throws SQLException {
        return getBinaryStreamReturn;
    }

    public byte[] getBytes(long pos, int length) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public long length() throws SQLException {
        throw new UnsupportedOperationException();
    }

    public long position(Blob pattern, long start) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public long position(byte pattern[], long start) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public OutputStream setBinaryStream(long pos) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int setBytes(long pos, byte[] bytes) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public void truncate(long len) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public void free() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
