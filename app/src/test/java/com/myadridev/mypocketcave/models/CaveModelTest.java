package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.enums.CaveTypeEnum;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CaveModelTest {

    @Test
    public void createVoidCaveModel() {
        CaveModel cave = new CaveModel();

        assertEquals(0, cave.Id);
        assertNull(cave.Name);
        assertNull(cave.CaveType);
        assertNotNull(cave.CaveArrangement);
    }

    @Test
    public void createCaveModelFromExisting() {
        CaveModel expectedCave = new CaveModel();
        expectedCave.Id = 42;
        expectedCave.Name = "cave";
        expectedCave.CaveType = CaveTypeEnum.BULK;
        expectedCave.CaveArrangement.TotalCapacity = 17;
        expectedCave.CaveArrangement.TotalUsed = 3;

        CaveModel cave = new CaveModel(expectedCave);

        assertEquals(expectedCave.Id, cave.Id);
        assertEquals(expectedCave.Name, cave.Name);
        assertEquals(expectedCave.CaveType, cave.CaveType);
        assertEquals(expectedCave.CaveArrangement.TotalCapacity, cave.CaveArrangement.TotalCapacity);
        assertEquals(expectedCave.CaveArrangement.TotalUsed, cave.CaveArrangement.TotalUsed);
    }

    @Test
    public void getId() {
        CaveModel cave = new CaveModel();
        cave.Id = 42;

        assertEquals(cave.Id, cave.getId());
    }

    @Test
    public void isValid() {
        CaveModel cave = new CaveModel();
        assertFalse(cave.isValid());

        cave.CaveType = CaveTypeEnum.BOX;
        assertFalse(cave.isValid());

        cave.Name = "name";
        cave.CaveArrangement = null;
        assertFalse(cave.isValid());

        cave.CaveArrangement = new CaveArrangementModel();
        assertTrue(cave.isValid());
    }

    @Test
    public void compare() {
        CaveModel cave1 = new CaveModel();
        cave1.CaveType = CaveTypeEnum.BOX;
        cave1.Name = "name";
        CaveModel cave2 = new CaveModel();
        cave2.CaveType = CaveTypeEnum.BULK;
        cave2.Name = "name";
        CaveModel cave3 = new CaveModel();
        cave3.CaveType = CaveTypeEnum.FRIDGE;
        cave3.Name = "name";
        CaveModel cave4 = new CaveModel();
        cave4.CaveType = CaveTypeEnum.BOX;
        cave4.Name = "aname";
        CaveModel cave5 = new CaveModel();
        cave5.CaveType = CaveTypeEnum.BOX;
        cave5.Name = "zname";
        CaveModel cave6 = new CaveModel();
        cave6.CaveType = CaveTypeEnum.BOX;
        cave6.Name = "name";

        assertEquals(1, cave1.compareTo(cave2));
        assertEquals(-1, cave1.compareTo(cave3));
        assertEquals(1, cave1.compareTo(cave4));
        assertEquals(-1, cave1.compareTo(cave5));
        assertEquals(0, cave1.compareTo(cave6));
    }
}