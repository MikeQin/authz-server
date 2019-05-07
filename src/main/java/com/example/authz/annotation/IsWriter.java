package com.example.authz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("(#oauth2.hasScope('write') or #oauth2.hasScope('WRITE')) and hasRole('ROLE_ADMIN')")
public @interface IsWriter {
	// Empty
}
