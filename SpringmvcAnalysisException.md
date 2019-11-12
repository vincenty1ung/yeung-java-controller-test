# spring mvc 异常处理分析
`1.ApplicationFilterChain.internalDoFilter() 执行到L：231 servlet.service(request, response); 进入过滤链`
```java
class ApplicationFilterChain{
    private void internalDoFilter(ServletRequest request,
                                  ServletResponse response)
        throws IOException, ServletException {

        // Call the next filter if there is one
        if (pos < n) {
            ApplicationFilterConfig filterConfig = filters[pos++];
            try {
                Filter filter = filterConfig.getFilter();

                if (request.isAsyncSupported() && "false".equalsIgnoreCase(
                        filterConfig.getFilterDef().getAsyncSupported())) {
                    request.setAttribute(Globals.ASYNC_SUPPORTED_ATTR, Boolean.FALSE);
                }
                if( Globals.IS_SECURITY_ENABLED ) {
                    final ServletRequest req = request;
                    final ServletResponse res = response;
                    Principal principal =
                        ((HttpServletRequest) req).getUserPrincipal();

                    Object[] args = new Object[]{req, res, this};
                    SecurityUtil.doAsPrivilege ("doFilter", filter, classType, args, principal);
                } else {
                    filter.doFilter(request, response, this);
                }
            } catch (IOException | ServletException | RuntimeException e) {
                throw e;
            } catch (Throwable e) {
                e = ExceptionUtils.unwrapInvocationTargetException(e);
                ExceptionUtils.handleThrowable(e);
                throw new ServletException(sm.getString("filterChain.filter"), e);
            }
            return;
        }

        // We fell off the end of the chain -- call the servlet instance
        try {
            if (ApplicationDispatcher.WRAP_SAME_OBJECT) {
                lastServicedRequest.set(request);
                lastServicedResponse.set(response);
            }

            if (request.isAsyncSupported() && !servletSupportsAsync) {
                request.setAttribute(Globals.ASYNC_SUPPORTED_ATTR,
                        Boolean.FALSE);
            }
            // Use potentially wrapped request from this point
            if ((request instanceof HttpServletRequest) &&
                    (response instanceof HttpServletResponse) &&
                    Globals.IS_SECURITY_ENABLED ) {
                final ServletRequest req = request;
                final ServletResponse res = response;
                Principal principal =
                    ((HttpServletRequest) req).getUserPrincipal();
                Object[] args = new Object[]{req, res};
                SecurityUtil.doAsPrivilege("service",
                                           servlet,
                                           classTypeUsedInService,
                                           args,
                                           principal);
            } else {
                //关键点！！！！
                servlet.service(request, response);
            }
        } catch (IOException | ServletException | RuntimeException e) {
            //请求过程中的响应在这里最后抛出！！！！
            throw e;
        } catch (Throwable e) {
            e = ExceptionUtils.unwrapInvocationTargetException(e);
            ExceptionUtils.handleThrowable(e);
            throw new ServletException(sm.getString("filterChain.servlet"), e);
        } finally {
            if (ApplicationDispatcher.WRAP_SAME_OBJECT) {
                lastServicedRequest.set(null);
                lastServicedResponse.set(null);
            }
        }
    }
}
```
---

``2.HttpServlet.service() 执行到L:742 service(request, response);``
```java
class HttpServlet{
    /**
     * Dispatches client requests to the protected
     */
    @Override
    public void service(ServletRequest req, ServletResponse res)
        throws ServletException, IOException {

        HttpServletRequest  request;
        HttpServletResponse response;

        try {
            request = (HttpServletRequest) req;
            response = (HttpServletResponse) res;
        } catch (ClassCastException e) {
            throw new ServletException("non-HTTP request or response");
        }
        //继续执行！！！
        service(request, response);
    }
}
```

---
``3.FrameworkServlet.service() 执行到L：846 super.service(request, response);``
```java

```

---
``4.HttpServlet.service() 执行到L：635 doGet(req, resp);``
```java

```

---
``5.FrameworkServlet.doGet() 执行到L：861 processRequest(request, response);``
```java

```

---
``6.FrameworkServlet.processRequest() 执行到L：970 doService(request, response);``
```java

```

---
``7.DispatcherServlet.doService() 执行到L：901 doDispatch(request, response);``
```java

```

---
``8.DispatcherServlet.doDispatch()  执行到L：967 ha.handle(processedRequest, response, mappedHandler.getHandler()); 真正的执行的处理器``
```java

```
---
``9.当真正执行的类出现异常时，会被包装成new NestedServletException("Handler dispatch failed", err)抛出，DispatcherServlet L:982``
```java

```








