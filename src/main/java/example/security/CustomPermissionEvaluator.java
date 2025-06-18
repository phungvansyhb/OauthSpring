package example.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private static final Logger logger = LoggerFactory.getLogger(CustomPermissionEvaluator.class);

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || permission == null) {
            logger.debug("Authentication or permission is null");
            return false;
        }

        String permissionString = permission.toString();

        logger.debug("Checking permission: {}", permissionString);
        logger.debug("User authorities: {}", authentication.getAuthorities());

        boolean hasPermission = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> {
                    logger.debug("Comparing authority: {} with permission: {}", authority, permissionString);
                    return authority.equals(permissionString);
                });

        logger.debug("Permission check result: {}", hasPermission);
        return hasPermission;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        logger.debug("Called second hasPermission method with permission: {}", permission);
        return hasPermission(authentication, null, permission);
    }
}