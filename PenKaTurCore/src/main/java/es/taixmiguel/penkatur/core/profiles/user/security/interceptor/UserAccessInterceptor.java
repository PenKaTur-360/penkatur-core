package es.taixmiguel.penkatur.core.profiles.user.security.interceptor;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import es.taixmiguel.penkatur.core.profiles.user.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserAccessInterceptor implements HandlerInterceptor {

	private static final Pattern USER_ID_PATTERN = Pattern.compile("^\\/api\\/(athlete)\\/(\\d+)");

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Optional<Long> userId = getUserId(request);
		Optional<Long> authenticatedUserId = getUserIdAuthenticated();

		if (!canAccess(authenticatedUserId, userId)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
			return false;
		}

		return true;
	}

	private Optional<Long> getUserIdAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
			return Optional.of(principal.getId());
		}
		return Optional.empty();
	}

	private Optional<Long> getUserId(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		Matcher matcher = USER_ID_PATTERN.matcher(requestURI);

		if (matcher.find())
			return Optional.of(Long.parseLong(matcher.group(2)));
		return Optional.empty();
	}

	private boolean canAccess(Optional<Long> authenticatedUserId, Optional<Long> userId) {
		return authenticatedUserId.orElse((long) -1) == userId.orElse((long) -2);
	}
}
