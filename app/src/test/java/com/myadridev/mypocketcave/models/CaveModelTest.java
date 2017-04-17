package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.enums.v2.CaveTypeEnumV2;
import com.myadridev.mypocketcave.models.v2.CaveArrangementModelV2;
import com.myadridev.mypocketcave.models.v2.CaveModelV2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CaveModelTest {

    @Test
    public void createVoidCaveModel() {
        CaveModelV2 cave = new CaveModelV2();

        assertEquals(0, cave.Id);
        assertEquals("", cave.Name);
        assertNull(cave.CaveType);
        assertNotNull(cave.CaveArrangement);
    }

    @Test
    public void createCaveModelFromExisting() {
        CaveModelV2 expectedCave = new CaveModelV2();
        expectedCave.Id = 42;
        expectedCave.Name = "cave";
        expectedCave.CaveType = CaveTypeEnumV2.bu;
        expectedCave.CaveArrangement.TotalCapacity = 17;
        expectedCave.CaveArrangement.TotalUsed = 3;

        CaveModelV2 cave = new CaveModelV2(expectedCave);

        assertEquals(expectedCave.Id, cave.Id);
        assertEquals(expectedCave.Name, cave.Name);
        assertEquals(expectedCave.CaveType, cave.CaveType);
        assertEquals(expectedCave.CaveArrangement.TotalCapacity, cave.CaveArrangement.TotalCapacity);
        assertEquals(expectedCave.CaveArrangement.TotalUsed, cave.CaveArrangement.TotalUsed);
    }

    @Test
    public void getId() {
        CaveModelV2 cave = new CaveModelV2();
        cave.Id = 42;

        assertEquals(cave.Id, cave.getId());
    }

    @Test
    public void isValid() {
        CaveModelV2 cave = new CaveModelV2();
        cave.CaveArrangement = null;
        assertFalse(cave.isValid());

        cave.CaveType = CaveTypeEnumV2.bo;
        assertFalse(cave.isValid());

        cave.CaveArrangement = new CaveArrangementModelV2();
        assertTrue(cave.isValid());
    }

    @Test
    public void compare() {
        CaveModelV2 cave1 = new CaveModelV2();
        cave1.CaveType = CaveTypeEnumV2.bo;
        cave1.Name = "name";
        CaveModelV2 cave2 = new CaveModelV2();
        cave2.CaveType = CaveTypeEnumV2.bu;
        cave2.Name = "name";
        CaveModelV2 cave3 = new CaveModelV2();
        cave3.CaveType = CaveTypeEnumV2.f;
        cave3.Name = "name";
        CaveModelV2 cave4 = new CaveModelV2();
        cave4.CaveType = CaveTypeEnumV2.bo;
        cave4.Name = "aname";
        CaveModelV2 cave5 = new CaveModelV2();
        cave5.CaveType = CaveTypeEnumV2.bo;
        cave5.Name = "zname";
        CaveModelV2 cave6 = new CaveModelV2();
        cave6.CaveType = CaveTypeEnumV2.bo;
        cave6.Name = "name";

        assertEquals(1, cave1.compareTo(cave2));
        assertEquals(-1, cave1.compareTo(cave3));
        assertEquals(1, cave1.compareTo(cave4));
        assertEquals(-1, cave1.compareTo(cave5));
        assertEquals(0, cave1.compareTo(cave6));
    }

    @Test
    public void trimAll() {
        CaveModelV2 cave = new CaveModelV2();
        cave.Id = 42;
        cave.Name = " cave  ";
        cave.CaveType = CaveTypeEnumV2.bu;
        cave.CaveArrangement.TotalCapacity = 17;
        cave.CaveArrangement.TotalUsed = 3;

        cave.trimAll();

        assertEquals("cave", cave.Name);
    }
}