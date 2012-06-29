package com.technoetic.mocks.sql;

import java.util.ArrayList;

public class JdbcTestSupport {
    public MockConnection mockConnection;
    public MockPreparedStatement mockPreparedStatement;
    public MockStatement mockStatement;
    public MockResultSet mockResultSet;

    public JdbcTestSupport() {
        mockConnection = new MockConnection();
        mockPreparedStatement = new MockPreparedStatement();
        mockConnection.prepareStatementReturn = mockPreparedStatement;
        mockStatement = new MockStatement();
        mockConnection.createStatementReturn = mockStatement;
        mockResultSet = new MockResultSet();
        mockResultSet.returnedRows = new ArrayList();
        mockPreparedStatement.executeQueryReturn = mockResultSet;
        mockStatement.executeQueryReturn = mockResultSet;
    }

}
