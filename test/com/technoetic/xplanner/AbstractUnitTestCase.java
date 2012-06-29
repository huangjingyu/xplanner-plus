package com.technoetic.xplanner;

import static org.easymock.EasyMock.expect;
import junit.framework.Assert;
import junit.framework.TestCase;
import net.sf.xplanner.events.EventManager;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.ObjectMother;
import com.technoetic.xplanner.domain.repository.MetaRepository;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.easymock.EasyMockController;
import com.technoetic.xplanner.easymock.EasyMockHelper;

public abstract class AbstractUnitTestCase extends TestCase implements EasyMockController {

    EasyMockHelper easymockHelper;
    protected MetaRepository mockMetaRepository;
    protected ObjectRepository mockObjectRepository;
    public Session mockSession;
    public HibernateTemplate hibernateTemplate;
    protected Transaction mockTransaction;
    protected XPlannerTestSupport support;
    protected ObjectMother mom;
	protected EventManager eventBus;
	protected SessionFactory mockSessionFactory;

    @Override
	protected void setUp() throws Exception {
        super.setUp();
        easymockHelper = new EasyMockHelper();
        support = new XPlannerTestSupport();
        mom = new ObjectMother();
    }

    protected void setUpThreadSession() throws HibernateException {
        setUpThreadSession(true);
    }

    protected void setUpThreadSession(boolean expectCommit) throws HibernateException {
    	mockSession = easymockHelper.createGlobalMock(Session.class);
    	mockSessionFactory = easymockHelper.createGlobalMock(SessionFactory.class);
    	hibernateTemplate = easymockHelper.createGlobalMock(HibernateTemplate.class);
        mockTransaction = easymockHelper.createNiceGlobalMock(Transaction.class);
        if (expectCommit) {
        	expect(mockSession.beginTransaction()).andReturn(mockTransaction);
        }
        ThreadSession.set(mockSession);
    }

    @Override
	protected void tearDown() throws Exception {
        super.tearDown();
        ThreadSession.set(null);
    }

    protected final <T> T createLocalMock(Class<T> class1) {
    	return easymockHelper.createLocalMock(class1);
	}

    protected final <T> T createGlobalMock(Class<T> clazz) {
        return easymockHelper.createGlobalMock(clazz);
    }

    public void replay() {
        assertHelperPresent();
        easymockHelper.replayMocks();
    }

    private void assertHelperPresent() {
        Assert.assertNotNull("no EasyMock helper: was super.setUp() called?", easymockHelper);
    }

    public void verify() {
        assertHelperPresent();
        easymockHelper.verifyMocks();
    }

    public void reset() {
        assertHelperPresent();
        easymockHelper.resetMocks();
    }

    @SuppressWarnings("unchecked")
	protected void expectObjectRepositoryAccess(Class objectClass) {
        if (mockMetaRepository == null) {
            setUpRepositories();
        }
        expect(mockMetaRepository.getRepository(objectClass)).andReturn(mockObjectRepository).atLeastOnce();
    }

    protected void setUpRepositories() {
        mockMetaRepository = easymockHelper.createGlobalMock(MetaRepository.class);
        mockObjectRepository = easymockHelper.createGlobalMock(ObjectRepository.class);
        eventBus = easymockHelper.createGlobalMock(EventManager.class);
    }
}
