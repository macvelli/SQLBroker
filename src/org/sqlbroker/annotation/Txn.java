package org.sqlbroker.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import root.jdbc.TransactionIsolationLevel;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Txn {

	TransactionIsolationLevel value() default TransactionIsolationLevel.DEFAULT;

} // End Txn
