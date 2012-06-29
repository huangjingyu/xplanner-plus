/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.thoughtworks.proxy.toys.echo;

import java.io.PrintWriter;
import java.lang.reflect.Method;

import com.thoughtworks.proxy.Invoker;
import com.thoughtworks.proxy.ProxyFactory;
import com.thoughtworks.proxy.factory.InvokerReference;
import com.thoughtworks.proxy.kit.SimpleInvoker;
import com.thoughtworks.proxy.toys.decorate.Decorating;
import com.thoughtworks.proxy.toys.decorate.DecoratingInvoker;
import com.thoughtworks.proxy.toys.decorate.InvocationDecoratorSupport;


/**
 * A {@link com.thoughtworks.proxy.toys.decorate.InvocationDecoratorSupport} implementation that echoes any invocation to a {@link java.io.PrintWriter}.
 * <p>
 * The implementation will try to create new proxies for every return value, that can be proxied by the
 * {@link com.thoughtworks.proxy.ProxyFactory} in use.
 * </p>
 * 
 * @author <a href="mailto:dan.north@thoughtworks.com">Dan North</a>
 * @author J&ouml;rg Schaible
 * @since 0.1
 */
public class EchoDecorator extends InvocationDecoratorSupport {
    private static final long serialVersionUID = 1L;
    private final PrintWriter out;
    private final ProxyFactory factory;

    /**
     * Construct an EchoingDecorator.
     * 
     * @param out the {@link java.io.PrintWriter} receving the logs
     * @param factory the {@link com.thoughtworks.proxy.ProxyFactory} to use
     * @since 0.2, different arguments in 0.1
     */
    public EchoDecorator(final PrintWriter out, final ProxyFactory factory) {
        this.out = out;
        this.factory = factory;
    }

    public Object[] beforeMethodStarts(final Object proxy, final Method method, final Object[] args) {
        if (isIgnored(method)) return args;
        printMethodCall(proxy, method, args);
        return super.beforeMethodStarts(proxy, method, args);
    }

    public Object decorateResult(final Object proxy, final Method method, final Object[] args, Object result) {
        if (isIgnored(method)) return result;
        printMethodResult(result);
        final Class returnType = method.getReturnType();
        if (returnType != Object.class && factory.canProxy(returnType)) {
            result = Decorating.object(new Class[]{returnType}, result, this, factory);
        } else if (result != null && returnType == Object.class && factory.canProxy(result.getClass())) {
            result = Decorating.object(new Class[]{result.getClass()}, result, this, factory);
        }
        return result;
    }

    public Throwable decorateTargetException(
            final Object proxy, final Method method, final Object[] args, final Throwable cause) {
        if (isIgnored(method)) return cause;
        printTargetException(cause);
        return super.decorateTargetException(proxy, method, args, cause);
    }

    public Exception decorateInvocationException(
            final Object proxy, final Method method, final Object[] args, final Exception cause) {
        if (isIgnored(method)) return cause;
        printInvocationException(cause);
        return super.decorateInvocationException(proxy, method, args, cause);
    }

    private void printMethodCall(Object proxy, Method method, Object[] args) {
        final StringBuffer buf = new StringBuffer();
//        buf.append("[");
//        buf.append(Thread.currentThread().getName());
//        buf.append("] ");
//        buf.append(proxy);
       Object target = getTarget(proxy);
       if (target != null)
          buf.append(target.getClass().getName() + "@" + Integer.toHexString(target.hashCode()));
       else
          buf.append(method.getDeclaringClass().getName());
        buf.append(".").append(method.getName());

        if (args == null) {
            args = new Object[0];
        }
        buf.append("(");
        for (int i = 0; i < args.length; i++) {
            buf.append(i == 0 ? "<" : ", <").append(args[i].toString()).append(">");
        }
        buf.append(") ");
        out.print(buf);
        out.flush();
    }

   private Object getTarget(Object proxy) {
      Object target = null;
      Invoker invoker = ((InvokerReference) proxy).getInvoker();
      if (invoker instanceof DecoratingInvoker) invoker = ((DecoratingInvoker) invoker).getDecorated();
      if (invoker instanceof SimpleInvoker) target = ((SimpleInvoker) invoker).getTarget();
      return target;
   }

   private boolean isIgnored(Method method) {return method.getName().equals("toString");}

   private void printMethodResult(final Object result) {
       final StringBuffer buf = new StringBuffer("--> <");
       buf.append(result == null ? "NULL" : result.toString());
       buf.append(">");
       out.println(buf);
       out.flush();
   }

    private void printTargetException(final Throwable throwable) {
        final StringBuffer buf = new StringBuffer("throws ");
        buf.append(throwable.getClass().getName());
        buf.append(": ");
        buf.append(throwable.getMessage());
        out.println(buf);
        out.flush();
    }

    private void printInvocationException(final Throwable throwable) {
        out.print("INTERNAL ERROR, ");
        printTargetException(throwable);
    }
}
