package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DependencyManagerTest {

    private interface MyInterface {
        String getName();
    }

    private interface MyInterface2 {
        String getName();
    }

    private static final String nameImplA = "nameA";
    private static final String nameImplB = "nameB";

    private class MyImplA implements MyInterface {
        @Override
        public String getName() {
            return nameImplA;
        }
    }

    private class MyImplB implements MyInterface {
        @Override
        public String getName() {
            return nameImplB;
        }
    }

    @Before
    public void before() {
        DependencyManager.cleanUp();
    }

    @Test
    public void WhenRegisterWithoutInit_ThenItThrows() {
        try {
            DependencyManager.registerSingleton(MyInterface.class, new MyImplA());
        } catch (IllegalStateException ex) {
            assertTrue(true);
            return;
        }
        fail();
    }

    @Test
    public void WhenRegisterSingleton_ThenTheSingletonIsRegistered() {
        try {
            DependencyManager.init();
            DependencyManager.registerSingleton(MyInterface.class, new MyImplA());
        } catch (Exception ex) {
            fail();
        }
        assertTrue(true);
    }

    @Test
    public void WhenRegisterSingletonTwiceTheSingleton_ThenIsRegisteredAndThenItThrows() {
        try {
            DependencyManager.init();
            DependencyManager.registerSingleton(MyInterface.class, new MyImplA());
            DependencyManager.registerSingleton(MyInterface.class, new MyImplB());
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
            return;
        }
        fail();
    }

    @Test
    public void WhenRegisterSingletonTwiceWithOverride_ThenTheSingletonIsRegistered() {
        try {
            DependencyManager.init();
            DependencyManager.registerSingleton(MyInterface.class, new MyImplA());
            DependencyManager.registerSingleton(MyInterface.class, new MyImplB(), true);
        } catch (Exception ex) {
            fail();
        }
        assertTrue(true);
    }

    @Test
    public void WhenGetSingletonWithoutInit_ThenItThrows() {
        try {
            MyInterface singl = DependencyManager.getSingleton(MyInterface.class, null);
        } catch (IllegalStateException ex) {
            assertTrue(true);
            return;
        }
        fail();
    }

    @Test
    public void WhenGetSingletonNotRegistered_ThenItThrows() {
        try {
            DependencyManager.init();
            MyInterface singl = DependencyManager.getSingleton(MyInterface.class, null);
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
            return;
        }
        fail();
    }

    @Test
    public void WhenGetSingletonRegistered_ThenTheSingletonIsGet() {
        try {
            DependencyManager.init();
            DependencyManager.registerSingleton(MyInterface.class, new MyImplA());
            MyInterface singl = DependencyManager.getSingleton(MyInterface.class, null);
            assertEquals(nameImplA, singl.getName());
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void WhenGetSingletonRegisteredTwiceWithOverride_ThenTheSingletonIsGet() {
        try {
            DependencyManager.init();
            DependencyManager.registerSingleton(MyInterface.class, new MyImplA());
            DependencyManager.registerSingleton(MyInterface.class, new MyImplB(), true);
            MyInterface singl = DependencyManager.getSingleton(MyInterface.class, null);
            assertEquals(nameImplB, singl.getName());
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void WhenGetSingletonRegisteredWithListenerAndRegisterAgain_ThenTheListenerIsFired() {
        try {
            DependencyManager.init();
            DependencyManager.registerSingleton(MyInterface.class, new MyImplA());

            final MyInterface[] singlArray = new MyInterface[1];

            singlArray[0] = DependencyManager.getSingleton(MyInterface.class, () -> singlArray[0] = null);
            assertEquals(nameImplA, singlArray[0].getName());
            DependencyManager.registerSingleton(MyInterface.class, new MyImplB(), true);
            assertNull(singlArray[0]);
            singlArray[0] = DependencyManager.getSingleton(MyInterface.class, null);
            assertEquals(nameImplB, singlArray[0].getName());
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void WhenCleanUp_ThenAllListenersAreFired() {
        try {
            final boolean[] listenersFired = new boolean[2];
            DependencyManager.init();
            DependencyManager.registerSingleton(MyInterface.class, new MyImplA());
            DependencyManager.registerSingleton(MyInterface2.class, () -> null);

            DependencyManager.getSingleton(MyInterface.class, () -> listenersFired[0] = true);
            DependencyManager.getSingleton(MyInterface2.class, () -> listenersFired[1] = true);

            assertFalse(listenersFired[0]);
            assertFalse(listenersFired[1]);

            DependencyManager.cleanUp();
            assertTrue(listenersFired[0]);
            assertTrue(listenersFired[1]);
        } catch (Exception ex) {
            fail();
        }
    }
}