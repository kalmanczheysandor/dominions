package hu.kalmancheysandor.applications.dominions.servers.site.configuration.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
