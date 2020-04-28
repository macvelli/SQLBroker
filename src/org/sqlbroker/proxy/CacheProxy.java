package org.sqlbroker.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.sqlbroker.annotation.Cached;

//import root.cache.LRUMultiKeyCache;
import root.adt.MapHashed;
import root.log.Log;

/**
 * TODO:
 * 		+ Ok so this only caches at the method level. Pass in the same arguments and get the same result.
 * 		+ So how would one invalidate this cache when a change is made?
 *
 * @author esmith
 */
public final class CacheProxy implements InvocationHandler {

	// <><><><><><><><><><><><><>< Static Artifacts ><><><><><><><><><><><><><>

	private static final Log log = new Log(CacheProxy.class);

	@SuppressWarnings("unchecked")
	public static final <T> T create(final T obj) {
		return (T) Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new CacheProxy(obj));
	}

	// <><><><><><><><><><><><><>< Class Attributes ><><><><><><><><><><><><><>

	/** The cache-managed object		*/
	private final Object delegate;

	/**	The method names to cache		*/
//	private final MapHashed<Method, LRUMultiKeyCache<Object, Object>> cacheMap;
	private final MapHashed<Method, Object> cacheMap;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	private CacheProxy(final Object obj) {
		log.debug("Configuring cache for class {P}", obj.getClass());
		delegate = obj;
		cacheMap = new MapHashed<>();

		Cached c;
		for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			for (Method m : clazz.getDeclaredMethods()) {
				c = m.getAnnotation(Cached.class);
				if (c != null) {
					log.debug("Creating a {P} element cache for {P}", c.size(), m);
//					cacheMap.put(m, new LRUMultiKeyCache<>(c.size()));
				}
			}
		}
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final Object invoke(final Object proxy, final Method m, final Object[] args) throws Throwable {
//		final LRUMultiKeyCache<Object, Object> cache = cacheMap.get(m);
		final Object cache = cacheMap.get(m);

		if (cache == null) {
			log.debug("Invoking method {P}: no cache found", m);
			return m.invoke(delegate, args);
		}

//		Object val = cache.get(args);
		Object val = null;

		if (val == null) {
			val = m.invoke(delegate, args);
//			cache.put(args, val);
			log.debug("{P}: Put {P} into the cache under {P}", m, val, args);
		} else {
			log.debug("{P}: Cache returned {P} for {P}", m, val, args);
		}

		return val;
	}

} // End CacheProxy
