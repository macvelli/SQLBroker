package org.sqlbroker.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.sqlbroker.annotation.Txn;

import root.adt.MapHashed;
import root.jdbc.TransactionIsolationLevel;
import root.jdbc.Transaction;
import root.log.Log;

/**
 * TODO:
 * 		+ In the future where there are more options on the @Txn annotation, need
 * 		  to track the @Txn annotation itself to the method it applies to
 *
 * @author esmith
 */
public final class TransactionProxy implements InvocationHandler {

	// <><><><><><><><><><><><><>< Static Artifacts ><><><><><><><><><><><><><>

	private static final Log log = new Log(TransactionProxy.class);

	@SuppressWarnings("unchecked")
	public static final <T> T create(final T obj) {
		return (T) Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new TransactionProxy(obj));
	}

	// <><><><><><><><><><><><><>< Class Attributes ><><><><><><><><><><><><><>

	/** The transaction-managed object													*/
	private final Object delegate;

	/**	The method names and associated isolation levels to run under a transaction		*/
	private final MapHashed<String, TransactionIsolationLevel> transactionMap;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	private TransactionProxy(final Object obj) {
		log.debug("Configuring transaction management for class {P}", obj.getClass());
		delegate = obj;
		transactionMap = new MapHashed<>();

		Txn cLevel, mLevel;
		for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			cLevel = clazz.getAnnotation(Txn.class);

			for (Method m : clazz.getDeclaredMethods()) {
				mLevel = m.getAnnotation(Txn.class);
				if (mLevel != null) {
					log.debug("Creating method-level transaction entry for {P}", m);
					transactionMap.put(m.getName(), mLevel.value());
				} else if (cLevel != null) {
					log.debug("Creating class-level transaction entry for {P}", m);
					transactionMap.put(m.getName(), cLevel.value());
				}
			}
		}
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final Object invoke(final Object proxy, final Method m, final Object[] args) throws Throwable {
		final TransactionIsolationLevel isoLevel = transactionMap.get(m.getName());

		if (isoLevel == null || Transaction.isActive()) {
			// No transaction entry found or transaction already in progress, invoke method normally
			return m.invoke(delegate, args);
		}

		Transaction.begin(isoLevel);
		try {
			final Object retVal = m.invoke(delegate, args);
			Transaction.commit();
			return retVal;
		} catch (Throwable t) {
			Transaction.rollback();
			throw t;
		}
	}

}
