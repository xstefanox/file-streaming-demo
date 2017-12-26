package io.github.xstefanox.demo.filestreaming.processor;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

public class ProcessorThreadFactoryTest {

    private static final String POOL_NAME_1 = "POOL_NAME_1";
    private static final String POOL_NAME_2 = "POOL_NAME_2";
    private static final Runnable RUNNABLE = () -> {};

    private ProcessorThreadFactory processorThreadFactory1 = new ProcessorThreadFactory(POOL_NAME_1);
    private ProcessorThreadFactory processorThreadFactory2 = new ProcessorThreadFactory(POOL_NAME_1);
    private ProcessorThreadFactory processorThreadFactory3 = new ProcessorThreadFactory(POOL_NAME_2);

    @Test
    public void threadsShouldHaveAName() {

        final Thread thread = processorThreadFactory1.newThread(RUNNABLE);

        assertThat("a Thread should be returned", thread, notNullValue());
        assertThat("the thread should have a name", thread.getName(), not(isEmptyOrNullString()));
        assertThat("the thread name should start with the given prefix", thread.getName(), startsWith(POOL_NAME_1));
    }

    @Test
    public void threadsShouldBeCountedByPrefix() {

        final Thread thread1 = processorThreadFactory1.newThread(RUNNABLE);
        final Thread thread2 = processorThreadFactory1.newThread(RUNNABLE);

        assertThat("threads should be sortable by name", thread2.getName(), greaterThan(thread1.getName()));
        assertThat("threads should countain their counter in name", thread1.getName(), equalTo("POOL_NAME_1-thread-1"));
        assertThat("threads should countain their counter in name", thread2.getName(), equalTo("POOL_NAME_1-thread-2"));
    }

    @Test
    public void factoriesHavingDifferentNamesShouldNotShareTheCounter() {

        final Thread thread1 = processorThreadFactory1.newThread(RUNNABLE);
        final Thread thread2 = processorThreadFactory3.newThread(RUNNABLE);

        assertThat("threads should be sortable by name", thread2.getName(), greaterThan(thread1.getName()));
        assertThat("threads should countain their counter in name", thread1.getName(), equalTo("POOL_NAME_1-thread-4"));
        assertThat("threads should countain their counter in name", thread2.getName(), equalTo("POOL_NAME_2-thread-1"));
    }

    @Test
    public void factoriesHavingTheSameNameShouldShareTheCounter() {

        final Thread thread1 = processorThreadFactory1.newThread(RUNNABLE);
        final Thread thread2 = processorThreadFactory2.newThread(RUNNABLE);

        assertThat("threads should be sortable by name", thread2.getName(), greaterThan(thread1.getName()));
        assertThat("threads should countain their counter in name", thread1.getName(), equalTo("POOL_NAME_1-thread-5"));
        assertThat("threads should countain their counter in name", thread2.getName(), equalTo("POOL_NAME_1-thread-6"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyPoolNameShouldBeRejected() {

        new ProcessorThreadFactory("");
    }
}
