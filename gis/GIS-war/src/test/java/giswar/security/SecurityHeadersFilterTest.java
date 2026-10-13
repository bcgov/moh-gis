package giswar.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.mockito.InOrder;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class SecurityHeadersFilterTest {

    @Test
    public void setsHeadersBeforeContinuingChain() throws Exception {
        ServletRequest request = mock(ServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        new SecurityHeadersFilter().doFilter(request, response, chain);

        InOrder order = inOrder(response, chain);
        order.verify(response).setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        order.verify(response).setHeader("X-Content-Type-Options", "nosniff");
        order.verify(response).setHeader("Permissions-Policy", "geolocation=(), camera=(), microphone=(), payment=()");
        order.verify(response).setHeader("Cross-Origin-Opener-Policy", "same-origin");
        order.verify(response).setHeader("Cross-Origin-Resource-Policy", "same-origin");
        order.verify(response).setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        order.verify(chain).doFilter(request, response);
    }
}