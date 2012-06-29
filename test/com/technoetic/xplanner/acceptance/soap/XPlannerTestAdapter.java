package com.technoetic.xplanner.acceptance.soap;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.RemoteException;

import org.apache.commons.beanutils.BeanUtils;

import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.soap.XPlanner;
import com.technoetic.xplanner.soap.domain.DomainData;

/**
 * This class allows us to run the SOAP tests within the IDE. Another option is to run the XPlanner
 * web application in the IDE debugger and use the remote soap test.
 */
public class XPlannerTestAdapter implements InvocationHandler {
    private XPlanner xplanner = new XPlanner();

    public static com.technoetic.xplanner.soap.XPlanner create() {
        return (com.technoetic.xplanner.soap.XPlanner)Proxy.newProxyInstance(
            XPlannerTestAdapter.class.getClassLoader(),
            new Class[] { com.technoetic.xplanner.soap.XPlanner.class },
            new XPlannerTestAdapter());
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        try {
            if (args == null) {
                args = new Object[0];
            }
            ThreadSession.set(GlobalSessionFactory.get().openSession());
            try {
                translateArguments(args);
                Object result = getDelegateMethod(method, args).invoke(xplanner, args);
                if (result != null) {
                    if (result.getClass().isArray()) {
                        if (((Object[])result).length > 0) {
                            Object[] resultArray = (Object[])result;
                            Object[] clientResultArray = (Object[])Array.newInstance(
                                    getClientResultClass(resultArray[0]), resultArray.length);
                            for (int i = 0; i < resultArray.length; i++) {
                                clientResultArray[i] = translateResult(resultArray[i]);
                            }
                            result = clientResultArray;
                        } else {
                            result = java.lang.reflect.Array.newInstance(
                                    Class.forName(result.getClass().getComponentType().getName().
                                    replaceAll("soap.domain", "soap.client")), 0);
                        }
                    } else {
                        result = translateResult(result);
                    }
                }
                return result;
            } catch (Throwable e) {
                while (e.getCause() != null) {
                    e = e.getCause();
                }
                throw new RemoteException(e.toString(), e);
            }
        } finally {
            ThreadSession.get().close();
            ThreadSession.set(null);
        }
    }

    private Object translateResult(Object result)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, InvocationTargetException {
        Object clientObject = result;
        Package packageInfo = result.getClass().getPackage();
        if (packageInfo != null && packageInfo.equals(DomainData.class.getPackage())) {
            clientObject = getClientResultClass(result).newInstance();
            BeanUtils.copyProperties(clientObject, result);
        }
        return clientObject;
    }

    private Class getClientResultClass(Object result) throws ClassNotFoundException {
        return Class.forName(result.getClass().getName().
                            replaceFirst("soap.domain", "soap.client"));
    }

    private void translateArguments(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg.getClass().getPackage().equals("")) {////XPlannerService.class.getPackage()
                try {
                    Object domainObject = Class.forName(arg.getClass().getName().
                            replaceFirst("soap.client", "soap.domain")).newInstance();
                    BeanUtils.copyProperties(domainObject, arg);
                    args[i] = domainObject;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Method getDelegateMethod(Method method, Object[] args) throws NoSuchMethodException {
        Class[] methodParameterTypes = method.getParameterTypes();
        for (int i = 0; i < args.length; i++) {
            if (!methodParameterTypes[i].isPrimitive()) {
                methodParameterTypes[i] = args[i].getClass();
            }
        }
        return xplanner.getClass().getMethod(method.getName(), methodParameterTypes);
    }
}
