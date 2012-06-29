package com.technoetic.xplanner.actions;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.dao.impl.CommonDao;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.events.EventManager;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.domain.Identifiable;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.history.HistorySupport;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.tags.DomainContext;
import com.technoetic.xplanner.tx.CheckedExceptionHandlingTransactionTemplate;
import com.technoetic.xplanner.util.Callable;
import com.technoetic.xplanner.util.LogUtil;
import com.technoetic.xplanner.util.RequestUtils;

public abstract class AbstractAction<T extends Identifiable> extends Action {
	static Logger LOG = LogUtil.getLogger();

	public static final String TYPE_KEY = "@type";
	static final String TARGET_OBJECT = "targetObject";
	private String type;
	private EventManager eventBus;
	protected HistorySupport historySupport;

	protected CheckedExceptionHandlingTransactionTemplate transactionTemplate;

	protected final Class<T> domainClass;
	private CommonDao<T> commonDao;

	public AbstractAction() {
		Type genericSuperclass = getClass().getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			Object object = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];	
			if (object instanceof Class<?>) {
				domainClass = (Class<T>) object;
			}else {
				domainClass = null; //(Class<T>) ((TypeVariable) object).getClass();
			}
		}else {
			domainClass = null; //(Class<T>) ((TypeVariable) object).getClass();
		}
	}
	
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		if (LOG.isDebugEnabled()) {
			String name = mapping.getName();
			String requestParams = RequestUtils.toString(request);
			String action = "\naction=\"" + name + "\" requestParams=" + requestParams;
			LOG.debug(action);
		}
		try {
			ActionForward forward = (ActionForward) transactionTemplate.execute(new Callable() {
				public Object run() throws Exception {
					ActionForward forward = doExecute(mapping, form, request, response);
					// DEBT JM DEBT: Should be a instance member. Try converting
					// to SpringAction to have a stateful action with members
					T object = getTargetObject(request);
					if (object != null) {
						beforeObjectCommit(object, mapping, form, request, response);
					}
					return forward;

				}
			});
			afterObjectCommit(mapping, form, request, response);
			return forward;
		} catch (Throwable e) {
			LOG.error(e);
			if (e instanceof RuntimeException)
				throw (RuntimeException) e;
			if (e instanceof Error)
				throw (Error) e;
			if (e instanceof Exception)
				throw (Exception) e;
			return null; // never reached
		}
	}

	// DEBT(SPRING) Remove access to session. Access to the db should always go
	// through repositories or queries that are injected
	protected void beforeObjectCommit(T object, ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse reply) throws Exception {
	}

	protected void afterObjectCommit(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse reply) throws Exception {
	}

	protected abstract ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	protected Class getObjectType(ActionMapping actionMapping, HttpServletRequest request)
			throws ClassNotFoundException, ServletException {
		String className = type;
		if (className == null) {
			className = getObjectTypeFromForward(actionMapping, className);
		}
		if (className == null) {
			className = request.getParameter(TYPE_KEY);
		}
		if (className != null) {
			return Class.forName(className);
		} else {
			throw new ServletException("no object type is specified in mapping or request");
		}
	}

	private String getObjectTypeFromForward(ActionMapping actionMapping, String className) {
		ActionForward forward = actionMapping.findForward(TYPE_KEY);
		if (forward != null) {
			className = forward.getPath();
		}
		return className;
	}

	protected DomainContext setDomainContext(HttpServletRequest request, Object object, ActionMapping actionMapping)
			throws Exception {
		DomainContext domainContext = DomainContext.get(request);
		if (domainContext != null) {
			return domainContext;
		}
		domainContext = new DomainContext();
		domainContext.populate(object);
		domainContext.setActionMapping(actionMapping);
		String projectIdParam = request.getParameter("projectId");
		if (domainContext.getProjectId() == 0 && StringUtils.isNotEmpty(projectIdParam) && !projectIdParam.equals("0")) {
			Project project = (Project) getCommonDao().getById(Project.class, Integer.parseInt((request.getParameter("projectId"))));
			domainContext.populate(project);
		}
		domainContext.save(request);
		return domainContext;
	}

	public void addError(HttpServletRequest request, String errorKey) {
		addError(request, new ActionError(errorKey));
	}

	public void addError(HttpServletRequest request, String errorKey, Object arg) {
		addError(request, new ActionError(errorKey, arg));
	}

	private void addError(HttpServletRequest request, ActionError error) {
		ActionErrors errors = getActionErrors(request);
		errors.add(ActionErrors.GLOBAL_ERROR, error);
	}

	private ActionErrors getActionErrors(HttpServletRequest request) {
		ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
		if (errors == null) {
			errors = new ActionErrors();
			request.setAttribute(Globals.ERROR_KEY, errors);
		}
		return errors;
	}

	public ActionForward getGeneralErrorForward(ActionMapping mapping, HttpServletRequest request, String errorKey) {
		addError(request, errorKey);
		return mapping.findForward("error");
	}

	public ActionForward getGeneralErrorForward(ActionMapping mapping, HttpServletRequest request, String errorKey,
			Object arg) {
		addError(request, errorKey, arg);
		return mapping.findForward("error");

	}

	@SuppressWarnings("unchecked")
	protected T getTargetObject(HttpServletRequest request) {
		return (T) request.getAttribute(TARGET_OBJECT);
	}

	protected void setTargetObject(HttpServletRequest request, Object target) {
		request.setAttribute(TARGET_OBJECT, target);
	}

	protected Session getSession(HttpServletRequest request) {
		return HibernateHelper.getSession(request);
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTransactionTemplate(CheckedExceptionHandlingTransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public Iteration getIteration(int id) throws RepositoryException {
		return getCommonDao().getById(Iteration.class, id);
	}

	public void setEventBus(EventManager eventBus) {
		this.eventBus = eventBus;
	}

	public EventManager getEventBus() {
		return eventBus;
	}

	protected final Person getLoggedInUser(HttpServletRequest request) {
		try {
			int remoteUserId = SecurityHelper.getRemoteUserId(request);
			return getCommonDao().getById(Person.class, remoteUserId);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		return null;

	}

	public CommonDao<T> getCommonDao() {
		return commonDao;
	}

	@Autowired
	@Required
	public void setCommonDao(CommonDao<T> commonDao) {
		this.commonDao = commonDao;
	}

	@Autowired
	@Required
	public void setHistorySupport(HistorySupport historySupport) {
		this.historySupport = historySupport;
	}

}
