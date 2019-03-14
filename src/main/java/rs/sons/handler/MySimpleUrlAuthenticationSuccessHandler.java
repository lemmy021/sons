package rs.sons.handler;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
	
		handle(request, response, authentication);
        clearAuthenticationAttributes(request);

	}
	
	protected void handle(HttpServletRequest request, 
		      HttpServletResponse response, Authentication authentication)
		      throws IOException {
		  
		        String targetUrl = determineTargetUrl(authentication);
		 
		        if (response.isCommitted()) {
		            return;
		        }
		 
		        redirectStrategy.sendRedirect(request, response, targetUrl);
		    }
		 
		    protected String determineTargetUrl(Authentication authentication) {
		        //ovo mi za sada ne treba
		    	/*boolean isUser = false;
		        boolean isAdmin = false;
		        Collection<? extends GrantedAuthority> authorities
		         = authentication.getAuthorities();
		        for (GrantedAuthority grantedAuthority : authorities) {
		        	System.out.println(grantedAuthority.getAuthority().toString());
		            if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
		                isUser = true;
		                break;
		            } else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
		                isAdmin = true;
		                break;
		            }
		        }
		 
		        if (isUser) {
		            return "/adduser";
		        } else if (isAdmin) {
		            return "/";
		        } else {
		            throw new IllegalStateException();
		        }*/
		    	
		    	return "/";
		    }
		 
		    protected void clearAuthenticationAttributes(HttpServletRequest request) {
		        HttpSession session = request.getSession(false);
		        if (session == null) {
		            return;
		        }
		        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		    }
		 
		    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		        this.redirectStrategy = redirectStrategy;
		    }
		    protected RedirectStrategy getRedirectStrategy() {
		        return redirectStrategy;
		    }

}
