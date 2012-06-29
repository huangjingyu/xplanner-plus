package com.technoetic.xplanner.actions;

import org.apache.commons.collections.Predicate;

import com.technoetic.xplanner.domain.SearchResult;
import com.technoetic.xplanner.filters.ThreadServletRequest;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;
import com.technoetic.xplanner.tags.DomainContext;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: Dec 14, 2004
 * Time: 2:02:13 PM
 */
public class SearchResultAuthorizationPredicate implements Predicate {
    private final int remoteUserId;

    public SearchResultAuthorizationPredicate(int remoteUserId) {
        this.remoteUserId = remoteUserId;
    }

    public boolean evaluate(Object o) {
        try {
            return isResultReadableByUser((SearchResult) o, remoteUserId);
        } catch (AuthenticationException e) {
            return false;
        }
    }

    //debt  This code looks a bit like some code in RepositorySecurityAdapter.checkAuthorization
    protected boolean isResultReadableByUser(SearchResult searchResult, int remoteUserId)
        throws AuthenticationException {
        Object object = searchResult.getMatchingObject();
        boolean result;
        DomainContext context = new DomainContext();

        try {
            context.populate(object);
            result = SystemAuthorizer.get().hasPermission(context.getProjectId(),
                                SecurityHelper.getRemoteUserId(ThreadServletRequest.get()), object, "read");
        } catch (Exception e) {
            throw new AuthenticationException(e);
        }
        return result;
    }

}
