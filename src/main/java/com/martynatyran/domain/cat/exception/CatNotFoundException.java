package com.martynatyran.domain.cat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception class indicating that a searched cat
 * does not exist.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Cat does not exist")
public class CatNotFoundException extends RuntimeException { }