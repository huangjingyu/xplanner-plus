package com.technoetic.xplanner.actions;

import java.util.ArrayList;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Note;

import com.technoetic.mocks.hibernate.MockSession;

public class TestDownloadAttachmentAction extends TestCase {
    private DownloadAttachmentAction action;
    private Note testNote;

    private MockSession mockSession;

    private String fileContent;
    private byte[] file;
    private String contentType;
    private int fileSize;
    private String filename;

    public TestDownloadAttachmentAction(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        super.setUp();
        action = new DownloadAttachmentAction();
        testNote = new Note();
        fileContent = "XXXXXX";
        file = fileContent.getBytes();
        contentType = "text/plain";
        fileSize = 6;
        filename = "testFile.txt";

        mockSession = new MockSession();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testLocateNote() throws Exception {
        int testId = 5;
        ArrayList notes = new ArrayList();
        notes.add(testNote);
        mockSession.findReturn = notes;
        String expectedQuery = "from object in class net.sf.xplanner.domain.Note"
                + " where id=" + testId;

        Note actualNote = action.locateNote(mockSession, testId);

        assertEquals("Session did not receive the correct query.", expectedQuery, mockSession.findQuery);
        assertEquals("Did not receive the correct note.", testNote, actualNote);
    }

    /**
     * The <code>testExecute</code> method is waiting to be implemented
     * when signoff is obtained for importing the org.mockobjects libaries
     *
     * @exception Exception if an error occurs
     */
    public void testExecute() throws Exception {
    }
}
