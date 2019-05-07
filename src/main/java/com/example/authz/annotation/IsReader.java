package com.example.authz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("(#oauth2.hasScope('read') or #oauth2.hasScope('READ')) and hasRole('ROLE_USER')")
public @interface IsReader {
	// Empty
}
