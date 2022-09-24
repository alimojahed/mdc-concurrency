package dev.alimojahed.mdc.springdemo.filter;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Priority;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Ali Mojahed on 8/13/2022
 * @project notinou-local
 **/

//@Component
//@Order(2000)
@WebFilter
@Priority(10000)
@Component
public class LoggerReferenceIdFilter extends OncePerRequestFilter implements Filter {

    public static final String REF_ID_LOG_TOKEN_KEY = "REF_ID_TOKEN_KEY";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String referenceId = UUID.randomUUID().toString();

            MDC.put(REF_ID_LOG_TOKEN_KEY, referenceId);

            filterChain.doFilter(httpServletRequest, httpServletResponse);

        } finally {
            MDC.remove(REF_ID_LOG_TOKEN_KEY);
        }
    }
}
