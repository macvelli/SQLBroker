package org.sqlbroker.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import root.jdbc.IsolationLevel;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Txn {

	IsolationLevel value() default IsolationLevel.DEFAULT;

}
