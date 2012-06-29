package com.technoetic.xplanner.domain.repository;

import static org.easymock.EasyMock.expect;
import net.sf.xplanner.domain.NamedObject;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.DummyDomainObject;

public class TestHibernateObjectRepository extends AbstractUnitTestCase {
   private HibernateObjectRepository repository;
   private HibernateTemplate mockHibernateTemplate;
   private NamedObject domainObject;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setUpThreadSession(false);
		mockHibernateTemplate = createLocalMock(HibernateTemplate.class);
		repository = new HibernateObjectRepository(Object.class) {
			@Override
			protected Session getSession2() {
				return mockSession;
			}
		};
		repository.setHibernateTemplate(mockHibernateTemplate);

		domainObject = new DummyDomainObject();

	}

   @Override
protected void tearDown() throws Exception {
      ThreadSession.set(null);
      super.tearDown();
   }

   public void testDelete() throws Exception {
	  expect(mockHibernateTemplate.isAllowCreate()).andReturn(false).anyTimes();
	  SessionFactory sessionFactory = createLocalMock(SessionFactory.class);
	  expect(sessionFactory.openSession()).andReturn(mockSession);
	  expect(mockSession.getSessionFactory()).andReturn(sessionFactory);
	  expect(mockHibernateTemplate.getSessionFactory()).andReturn(sessionFactory).anyTimes();
      expect(mockHibernateTemplate.bulkUpdate("delete "+Object.class.getName() +" where id = ?",
                                                            new Integer(10))).andReturn(1);
      replay();

      repository.delete(10);

      verify();
   }

   public void testLoad() throws Exception {
      Object object = new Object();
      expect(mockHibernateTemplate.load(Object.class, new Integer(1))).andReturn(object);
      replay();

      Object loadedObject = repository.load(1);

      verify();
      assertSame(object, loadedObject);
   }

	public void testInsert() throws Exception {
		expect(mockSession.save(domainObject)).andReturn(
				new Integer(44));

		replay();

		int id = repository.insert(domainObject);

		verify();
		assertEquals("wrong id", 44, id);
	}

   public void testUpdate() throws Exception {
      replay();

      repository.update(domainObject);

      verify();
   }

}
