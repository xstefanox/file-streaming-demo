package io.github.xstefanox.demo.filestreaming.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.NORM_PRIORITY;
import static java.util.Collections.synchronizedMap;
import static java.util.Objects.requireNonNull;

public class ProcessorThreadFactory implements ThreadFactory {

    private static final String NAME_FORMAT = "%s-thread-%d";
    private static final Map<String, AtomicInteger> POOL_COUNTERS = synchronizedMap(new HashMap<>());

    private final String name;
    private final ThreadGroup group;

    public ProcessorThreadFactory(final String name) {

        requireNonNull(name, "name must not be null");

        if (name.isEmpty()) {
            throw new IllegalArgumentException("name must not be empty");
        }

        this.name = name;

        final SecurityManager securityManager = System.getSecurityManager();
        group = (securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();

        POOL_COUNTERS.putIfAbsent(name, new AtomicInteger());
    }

    @Override
    public Thread newThread(final Runnable r) {

        final String threadName = String.format(NAME_FORMAT, name, POOL_COUNTERS.get(name).incrementAndGet());

        Thread thread = new Thread(group, r, threadName);
        thread.setDaemon(false);
        thread.setPriority(NORM_PRIORITY);

        return thread;
    }
}
