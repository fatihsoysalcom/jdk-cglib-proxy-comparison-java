# JDK CGLIB Proxy Comparison Java

This example demonstrates the core differences between JDK Dynamic Proxy and CGLIB proxy mechanisms in Java. It shows how JDK Dynamic Proxy works by creating proxies for interfaces, while CGLIB can create proxies for concrete classes by subclassing them at runtime. Both examples illustrate how method calls can be intercepted to apply cross-cutting concerns.

## Language

`java`

## How to Run

1. Save the code as `ProxyComparison.java`.
2. Download the CGLIB library (e.g., `cglib-nodep-2.2.jar` from Maven Central).
3. Compile: `javac -cp cglib-nodep-2.2.jar ProxyComparison.java`
4. Run: `java -cp .:cglib-nodep-2.2.jar ProxyComparison` (Linux/macOS) or `java -cp .;cglib-nodep-2.2.jar ProxyComparison` (Windows)

## Original Article

This example accompanies the Turkish article: [Spring Proxy'leri: JDK Dinamik Proxy ve CGLIB Karşılaştırması](https://fatihsoysal.com/blog/spring-proxyleri-jdk-dinamik-proxy-ve-cglib-karsilastirmasi/).

## License

MIT — see [LICENSE](LICENSE).
