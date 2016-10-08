package com.myadridev.mypocketcave.managers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by adrie on 08/10/2016.
 */
public class DependencyManagerTest {

    private interface MyInterface {
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
            Assert.assertTrue(true);
            return;
        }
        Assert.fail();
    }

    @Test
    public void WhenRegisterSingleton_ThenTheSingletonIsRegistered() {
        try {
            DependencyManager.init();
            DependencyManager.registerSingleton(MyInterface.class, new MyImplA());
        } catch (Exception ex) {
            Assert.fail();
        }
        Assert.assertTrue(true);
    }

    @Test
    public void WhenRegisterSingletonTwiceTheSingleton_ThenIsRegisteredAndThenItThrows() {
        try {
            DependencyManager.init();
            DependencyManager.registerSingleton(MyInterface.class, new MyImplA());
            DependencyManager.registerSingleton(MyInterface.class, new MyImplB());
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(true);
            return;
        }
        Assert.fail();
    }

    @Test
    public void WhenRegisterSingletonTwiceWithOverride_ThenTheSingletonIsRegistered() {
        try {
            DependencyManager.init();
            DependencyManager.registerSingleton(MyInterface.class, new MyImplA());
            DependencyManager.registerSingleton(MyInterface.class, new MyImplB(), true);
        } catch (Exception ex) {
            Assert.fail();
        }
        Assert.assertTrue(true);
    }

    @Test
    public void WhenGetSingletonWithoutInit_ThenItThrows() {
        try {
            MyInterface singl = DependencyManager.getSingleton(MyInterface.class);
        } catch (IllegalStateException ex) {
            Assert.assertTrue(true);
            return;
        }
        Assert.fail();
    }

    @Test
    public void WhenGetSingletonNotRegistered_ThenItThrows() {
        try {
            DependencyManager.init();
            MyInterface singl = DependencyManager.getSingleton(MyInterface.class);
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(true);
            return;
        }
        Assert.fail();
    }

    @Test
    public void WhenGetSingletonRegistered_ThenTheSingletonIsGet() {
        try {
            DependencyManager.init();
            DependencyManager.registerSingleton(MyInterface.class, new MyImplA());
            MyInterface singl = DependencyManager.getSingleton(MyInterface.class);
            Assert.assertEquals(nameImplA, singl.getName());
        } catch (Exception ex) {
            Assert.fail();
        }
    }

    @Test
    public void WhenGetSingletonRegisteredTwiceWithOverride_ThenTheSingletonIsGet() {
        try {
            DependencyManager.init();
            DependencyManager.registerSingleton(MyInterface.class, new MyImplA());
            DependencyManager.registerSingleton(MyInterface.class, new MyImplB(), true);
            MyInterface singl = DependencyManager.getSingleton(MyInterface.class);
            Assert.assertEquals(nameImplB, singl.getName());
        } catch (Exception ex) {
            Assert.fail();
        }
    }
}