import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// CGLIB imports. This requires the CGLIB library (e.g., cglib-nodep-2.2.jar) on the classpath.
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Defines a simple service interface.
 * JDK Dynamic Proxies can only proxy interfaces.
 */
interface MyService {
    void doSomething();
    String getData(String input);
}

/**
 * An implementation of the MyService interface.
 * CGLIB can proxy concrete classes like this one, even without an interface.
 */
class MyServiceImpl implements MyService {
    @Override
    public void doSomething() {
        System.out.println("  MyServiceImpl: Doing something real.");
    }

    @Override
    public String getData(String input) {
        return "  MyServiceImpl: Data for " + input;
    }

    /**
     * A method not part of the MyService interface.
     * This method can only be intercepted by CGLIB proxies if the proxy is cast to MyServiceImpl.
     * JDK Dynamic Proxies, being interface-based, cannot directly intercept this.
     */
    public void doAnotherThing() {
        System.out.println("  MyServiceImpl: Doing another thing (not in interface).");
    }
}

/**
 * InvocationHandler for JDK Dynamic Proxies.
 * This class intercepts method calls on the proxy object.
 */
class JdkDynamicProxyHandler implements InvocationHandler {
    private final Object target;

    public JdkDynamicProxyHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // This is where cross-cutting concerns (e.g., logging, security, transactions) are applied
        System.out.println("  JDK Proxy: Before calling '" + method.getName() + "'");
        Object result = method.invoke(target, args); // Call the original method on the target object
        System.out.println("  JDK Proxy: After calling '" + method.getName() + "'");
        return result;
    }
}

/**
 * MethodInterceptor for CGLIB Proxies.
 * This class intercepts method calls on the proxy object.
 */
class CglibProxyInterceptor implements MethodInterceptor {
    private final Object target;

    public CglibProxyInterceptor(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        // This is where cross-cutting concerns are applied for CGLIB proxies
        System.out.println("  CGLIB Proxy: Before calling '" + method.getName() + "'");
        Object result = method.invoke(target, args); // Call the original method on the target object
        System.out.println("  CGLIB Proxy: After calling '" + method.getName() + "'");
        return result;
    }
}

/**
 * Main class to demonstrate both JDK Dynamic Proxy and CGLIB Proxy.
 */
public class ProxyComparison {
    public static void main(String[] args) {
        System.out.println("--- Demonstrating JDK Dynamic Proxy ---");
        // JDK Dynamic Proxy requires an interface to proxy.
        // It creates a proxy object that implements the specified interface(s).
        MyService targetService = new MyServiceImpl();
        MyService jdkProxy = (MyService) Proxy.newProxyInstance(
            MyService.class.getClassLoader(), // Class loader to define the proxy class
            new Class<?>[]{MyService.class}, // Interfaces the proxy class should implement
            new JdkDynamicProxyHandler(targetService) // The invocation handler
        );

        jdkProxy.doSomething(); // Call method on JDK proxy
        System.out.println(jdkProxy.getData("world")); // Call method on JDK proxy
        // Note: You cannot call doAnotherThing() on jdkProxy because it's cast to MyService
        // and doAnotherThing is not part of the MyService interface.
        // If you try to cast jdkProxy to MyServiceImpl, it will throw a ClassCastException.

        System.out.println("\n--- Demonstrating CGLIB Proxy ---");
        // CGLIB can proxy concrete classes (no interface required).
        // It works by creating a subclass at runtime that overrides the target class's methods.
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(MyServiceImpl.class); // CGLIB proxies by subclassing the target class
        enhancer.setCallback(new CglibProxyInterceptor(new MyServiceImpl())); // The method interceptor

        MyServiceImpl cglibProxy = (MyServiceImpl) enhancer.create(); // Create the CGLIB proxy instance

        cglibProxy.doSomething(); // Call method on CGLIB proxy
        System.out.println(cglibProxy.getData("CGLIB")); // Call method on CGLIB proxy
        cglibProxy.doAnotherThing(); // CGLIB proxy can call and intercept methods not in an interface
                                     // because it's a subclass of MyServiceImpl.
    }
}
