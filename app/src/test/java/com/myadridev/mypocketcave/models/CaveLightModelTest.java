package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.enums.v2.CaveTypeEnumV2;
import com.myadridev.mypocketcave.models.v2.CaveLightModelV2;
import com.myadridev.mypocketcave.models.v2.CaveModelV2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CaveLightModelTest {

    @Test
    public void createVoidCaveLightModel() {
        CaveLightModelV2 caveLight = new CaveLightModelV2();

        assertEquals(0, caveLight.Id);
        assertEquals("", caveLight.Name);
        assertNull(caveLight.CaveType);
        assertEquals(0, caveLight.TotalCapacity);
        assertEquals(0, caveLight.TotalUsed);
    }

    @Test
    public void createCaveLightModelFromExisting() {
        CaveLightModelV2 expectedCaveLight = new CaveLightModelV2();
        expectedCaveLight.Id = 42;
        expectedCaveLight.Name = "cave";
        expectedCaveLight.CaveType = CaveTypeEnumV2.bu;
        expectedCaveLight.TotalCapacity = 17;
        expectedCaveLight.TotalUsed = 3;

        CaveLightModelV2 caveLight = new CaveLightModelV2(expectedCaveLight);

        assertEquals(expectedCaveLight.Id, caveLight.Id);
        assertEquals(expectedCaveLight.Name, caveLight.Name);
        assertEquals(expectedCaveLight.CaveType, caveLight.CaveType);
        assertEquals(expectedCaveLight.TotalCapacity, caveLight.TotalCapacity);
        assertEquals(expectedCaveLight.TotalUsed, caveLight.TotalUsed);
    }

    @Test
    public void createCaveLightModelFromCave() {
        CaveModelV2 cave = new CaveModelV2();
        cave.Id = 42;
        cave.Name = "cave";
        cave.CaveType = CaveTypeEnumV2.bu;
        cave.CaveArrangement.TotalCapacity = 17;
        cave.CaveArrangement.TotalUsed = 3;

        CaveLightModelV2 caveLight = new CaveLightModelV2(cave);

        assertEquals(cave.Id, caveLight.Id);
        assertEquals(cave.Name, caveLight.Name);
        assertEquals(cave.CaveType, caveLight.CaveType);
        assertEquals(cave.CaveArrangement.TotalCapacity, caveLight.TotalCapacity);
        assertEquals(cave.CaveArrangement.TotalUsed, caveLight.TotalUsed);
    }

    @Test
    public void getId() {
        CaveLightModelV2 caveLight = new CaveLightModelV2();
        caveLight.Id = 42;

        assertEquals(caveLight.Id, caveLight.getId());
    }

    @Test
    public void isValid() {
        CaveLightModelV2 caveLight = new CaveLightModelV2();
        assertFalse(caveLight.isValid());

        caveLight.Name = null;
        caveLight.CaveType = CaveTypeEnumV2.bo;
        assertFalse(caveLight.isValid());

        caveLight.Name = "name";
        assertTrue(caveLight.isValid());
    }

    @Test
    public void compare() {
        CaveLightModelV2 caveLight1 = new CaveLightModelV2();
        caveLight1.CaveType = CaveTypeEnumV2.bo;
        caveLight1.Name = "name";
        CaveLightModelV2 caveLight2 = new CaveLightModelV2();
        caveLight2.CaveType = CaveTypeEnumV2.bu;
        caveLight2.Name = "name";
        CaveLightModelV2 caveLight3 = new CaveLightModelV2();
        caveLight3.CaveType = CaveTypeEnumV2.f;
        caveLight3.Name = "name";
        CaveLightModelV2 caveLight4 = new CaveLightModelV2();
        caveLight4.CaveType = CaveTypeEnumV2.bo;
        caveLight4.Name = "aname";
        CaveLightModelV2 caveLight5 = new CaveLightModelV2();
        caveLight5.CaveType = CaveTypeEnumV2.bo;
        caveLight5.Name = "zname";
        CaveLightModelV2 caveLight6 = new CaveLightModelV2();
        caveLight6.CaveType = CaveTypeEnumV2.bo;
        caveLight6.Name = "name";

        assertEquals(1, caveLight1.compareTo(caveLight2));
        assertEquals(-1, caveLight1.compareTo(caveLight3));
        assertEquals(1, caveLight1.compareTo(caveLight4));
        assertEquals(-1, caveLight1.compareTo(caveLight5));
        assertEquals(0, caveLight1.compareTo(caveLight6));
    }

    @Test
    public void trimAll() {
        CaveLightModelV2 caveLight = new CaveLightModelV2();
        caveLight.Id = 42;
        caveLight.Name = "cave ";
        caveLight.CaveType = CaveTypeEnumV2.bu;
        caveLight.TotalCapacity = 17;
        caveLight.TotalUsed = 3;

        caveLight.trimAll();

        assertEquals("cave", caveLight.Name);
    }
}