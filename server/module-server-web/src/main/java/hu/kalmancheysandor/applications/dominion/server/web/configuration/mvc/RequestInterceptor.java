package hu.kalmancheysandor.applications.dominion.server.web.configuration.mvc;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class RequestInterceptor implements HandlerInterceptor {

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        if (handler instanceof HandlerMethod) {
//            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            BlockAjaxRequest annotation = handlerMethod.getMethodAnnotation(BlockAjaxRequest.class);
//            if (annotation != null) {
//                String requestedWithHeader = request.getHeader("X-Requested-With");
//                if (!"XMLHttpRequest".equals(requestedWithHeader)) {
//                    // If the request is AJAX and the method is annotated with @BlockAjaxRequest, then rejects it
//                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                    return false;
//                }
//            }
//        }
//        return true;
//    }


//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("INTERCEPTOR:"+handler);
//
//
//        if (handler instanceof HandlerMethod) {
//            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            System.out.println("INTERCEPTOR-A");
//            if (hasAnnotation(handlerMethod, BlockAjaxRequest.class)) {
//                String requestedWithHeader = request.getHeader("X-Requested-With");
//                System.out.println("INTERCEPTOR-B:"+requestedWithHeader);
//                if ("XMLHttpRequest".equals(requestedWithHeader)) {
//                    System.out.println("INTERCEPTOR-C:Yes");
//                    // If the request is AJAX and the method is annotated with @BlockAjaxRequest, then rejects it
//                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                    return false;
//                }
//            }
//
//            if (hasAnnotation(handlerMethod, BlockStandardRequest.class)) {
//                String requestedWithHeader = request.getHeader("X-Requested-With");
//                if (!"XMLHttpRequest".equals(requestedWithHeader)) {
//                    // If the request is nonAJAX and the method is annotated with @BlockNonAjaxRequest, then rejects it
//                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                    return false;
//                }
//            }
//        }
//        return true;
//    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            // Accessing controller class
            Class<?> classObj = handlerMethod.getBeanType();
            Method methodObj = handlerMethod.getMethod();

            if (classObj.isAnnotationPresent(BlockAllRequestByDefault.class)) {
                if (isAjaxRequest(request) && !methodObj.isAnnotationPresent(AllowAjaxRequest.class)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Ajax request is forbidden!");
                    return false;
                }

                if (isStandardRequest(request) && !methodObj.isAnnotationPresent(AllowStandardRequest.class)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Standard request is forbidden!");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasAnnotation(HandlerMethod handlerMethod, Class<? extends Annotation> annotationClass) {
        return handlerMethod.getMethod().isAnnotationPresent(annotationClass) || handlerMethod.getBeanType().isAnnotationPresent(annotationClass);
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        System.out.println("isAjaxRequest ?");
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            System.out.println("isAjaxRequest:Yes");
            return true;
        }
        System.out.println("isAjaxRequest:No");
        return false;
    }

    private boolean isStandardRequest(HttpServletRequest request) {
        System.out.println("isStandardRequest ?");
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            System.out.println("isStandardRequest:No");
            return false;
        }
        System.out.println("isStandardRequest:Yes");
        return true;
    }
}
