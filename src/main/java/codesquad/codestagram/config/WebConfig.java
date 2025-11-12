package codesquad.codestagram.config;

import codesquad.codestagram.argumentresolver.RequestIpArgumentResolver;
import codesquad.codestagram.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RequestIpArgumentResolver requestIpArgumentResolver;


    public WebConfig(RequestIpArgumentResolver requestIpArgumentResolver) {
        this.requestIpArgumentResolver = requestIpArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(requestIpArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico", "/fonts/**", "/error",
                        "/", "/users/**");
    }

}
