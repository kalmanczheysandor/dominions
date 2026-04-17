package hu.kalmancheysandor.applications.dominions.servers.admin.configuration.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * This interceptor copies the HTTP session cookie (JSESSIONID)
 * from the current request and sends it with Feign calls.
 *
 * Feign does not send cookies automatically, so without this
 * the other microservice will not know the user session.
 */
@Component
public class FeignSessionInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            System.out.println("❌ No RequestAttributes!");
            return;
        }

        String cookie = attributes.getRequest().getHeader("Cookie");

        System.out.println("Forwarding cookie: " + cookie);

        if (cookie != null) {
            template.header("Cookie", cookie);
        }
    }
}
