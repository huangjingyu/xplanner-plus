/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Dec 21, 2005
 * Time: 2:24:07 AM
 */
package com.thoughtworks.proxy.toys.decorate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.thoughtworks.proxy.Invoker;
import com.thoughtworks.proxy.kit.PrivateInvoker;

/**
 * Invoker implementation for the decorating proxy. The implementation may decorate an object or another {@link com.thoughtworks.proxy.Invoker}.
 *
 * @author <a href="mailto:dan.north@thoughtworks.com">Dan North</a>
 * @author Aslak Helles&oslash;y
 * @author J&ouml;rg Schaible
 * @since 0.1
 */
public class DecoratingInvoker implements Invoker {
    private static final long serialVersionUID = 8293471912861497447L;
    private Invoker decorated;
    private InvocationDecorator decorator;

    /**
     * Construct a DecoratingInvoker decorating another Invoker.
     *
     * @param decorated the decorated {@link com.thoughtworks.proxy.Invoker}.
     * @param decorator the decorating instance.
     * @since 0.1
     */
    public DecoratingInvoker(final Invoker decorated, final InvocationDecorator decorator) {
        this.decorated = decorated;
        this.decorator = decorator;
    }

    /**
     * Construct a DecoratingInvoker decorating another object.
     *
     * @param delegate the decorated object.
     * @param decorator the decorating instance.
     * @since 0.1
     */
    public DecoratingInvoker(final Object delegate, final InvocationDecorator decorator) {
        this(new PrivateInvoker(delegate), decorator);
    }

    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final Object[] decoratedArgs = decorator.beforeMethodStarts(proxy, method, args);
        try {
            final Object result = decorated.invoke(proxy, method, decoratedArgs);
            return decorator.decorateResult(proxy, method, decoratedArgs, result);
        } catch (InvocationTargetException e) {
          Throwable decoratedException = decorator.decorateTargetException(proxy, method, decoratedArgs, e.getTargetException());
          if (decoratedException != null) throw decoratedException;
        } catch (Exception e) {
          Exception decoratedException = decorator.decorateInvocationException(proxy, method, decoratedArgs, e);
          if (decoratedException != null) throw decoratedException;
        }
      return null;
    }

   public Invoker getDecorated() {
      return decorated;
   }
}