package hu.kalmancheysandor.applications.dominions.servers.admin.exceptionhandling.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(1)
@RestControllerAdvice
public class AuthenticationGlobalExceptionHandler {}
