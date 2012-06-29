/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Dec 21, 2005
 * Time: 2:27:19 AM
 */
package com.thoughtworks.proxy.kit;

import java.lang.reflect.Method;

import com.thoughtworks.proxy.Invoker;

/**
 * A simple {@link com.thoughtworks.proxy.Invoker} implementation, that routes any call to a target object. A <code>null</code> value as
 * target can be handled, the invocation result will alway be <code>null</code>.
 *
 * @author Aslak Helles&oslash;y
 * @since 0.2, 0.1 in package com.thoughtworks.proxy.toy.decorate
 */
public class SimpleInvoker implements Invoker {
    private static final long serialVersionUID = 1L;

    private Object target;

    /**
     * Construct a SimpleInvoker.
     *
     * @param target the invocation target.
     * @since 0.2, 0.1 in package com.thoughtworks.proxy.toy.decorate
     */
    public SimpleInvoker(final Object target) {
        this.target = target;
    }

    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if (method.getName().equals("equals") && proxy == args[0]) return Boolean.TRUE; 
        return (target == null ? null : method.invoke(target, args));
    }

    /**
     * Retrieve the target of the invocations.
     *
     * @return the target object
     * @since 0.2
     */
    public Object getTarget() {
        return target;
    }
}