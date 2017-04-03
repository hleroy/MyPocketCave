package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.enums.CaveTypeEnum;
import com.myadridev.mypocketcave.models.v1.CaveLightModel;
import com.myadridev.mypocketcave.models.v1.CaveModel;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CaveLightModelTest {

    @Test
    public void createVoidCaveLightModel() {
        CaveLightModel caveLight = new CaveLightModel();

        assertEquals(0, caveLight.Id);
        assertNull(caveLight.Name);
        assertNull(caveLight.CaveType);
        assertEquals(0, caveLight.TotalCapacity);
        assertEquals(0, caveLight.TotalUsed);
    }

    @Test
    public void createCaveLightModelFromExisting() {
        CaveLightModel expectedCaveLight = new CaveLightModel();
        expectedCaveLight.Id = 42;
        expectedCaveLight.Name = "cave";
        expectedCaveLight.CaveType = CaveTypeEnum.BULK;
        expectedCaveLight.TotalCapacity = 17;
        expectedCaveLight.TotalUsed = 3;

        CaveLightModel caveLight = new CaveLightModel(expectedCaveLight);

        assertEquals(expectedCaveLight.Id, caveLight.Id);
        assertEquals(expectedCaveLight.Name, caveLight.Name);
        assertEquals(expectedCaveLight.CaveType, caveLight.CaveType);
        assertEquals(expectedCaveLight.TotalCapacity, caveLight.TotalCapacity);
        assertEquals(expectedCaveLight.TotalUsed, caveLight.TotalUsed);
    }

    @Test
    public void createCaveLightModelFromCave() {
        CaveModel cave = new CaveModel();
        cave.Id = 42;
        cave.Name = "cave";
        cave.CaveType = CaveTypeEnum.BULK;
        cave.CaveArrangement.TotalCapacity = 17;
        cave.CaveArrangement.TotalUsed = 3;

        CaveLightModel caveLight = new CaveLightModel(cave);

        assertEquals(cave.Id, caveLight.Id);
        assertEquals(cave.Name, caveLight.Name);
        assertEquals(cave.CaveType, caveLight.CaveType);
        assertEquals(cave.CaveArrangement.TotalCapacity, caveLight.TotalCapacity);
        assertEquals(cave.CaveArrangement.TotalUsed, caveLight.TotalUsed);
    }

    @Test
    public void getId() {
        CaveLightModel caveLight = new CaveLightModel();
        caveLight.Id = 42;

        assertEquals(caveLight.Id, caveLight.getId());
    }

    @Test
    public void isValid() {
        CaveLightModel caveLight = new CaveLightModel();
        assertFalse(caveLight.isValid());

        caveLight.CaveType = CaveTypeEnum.BOX;
        assertFalse(caveLight.isValid());

        caveLight.Name = "name";
        assertTrue(caveLight.isValid());
    }

    @Test
    public void compare() {
        CaveLightModel caveLight1 = new CaveLightModel();
        caveLight1.CaveType = CaveTypeEnum.BOX;
        caveLight1.Name = "name";
        CaveLightModel caveLight2 = new CaveLightModel();
        caveLight2.CaveType = CaveTypeEnum.BULK;
        caveLight2.Name = "name";
        CaveLightModel caveLight3 = new CaveLightModel();
        caveLight3.CaveType = CaveTypeEnum.FRIDGE;
        caveLight3.Name = "name";
        CaveLightModel caveLight4 = new CaveLightModel();
        caveLight4.CaveType = CaveTypeEnum.BOX;
        caveLight4.Name = "aname";
        CaveLightModel caveLight5 = new CaveLightModel();
        caveLight5.CaveType = CaveTypeEnum.BOX;
        caveLight5.Name = "zname";
        CaveLightModel caveLight6 = new CaveLightModel();
        caveLight6.CaveType = CaveTypeEnum.BOX;
        caveLight6.Name = "name";

        assertEquals(1, caveLight1.compareTo(caveLight2));
        assertEquals(-1, caveLight1.compareTo(caveLight3));
        assertEquals(1, caveLight1.compareTo(caveLight4));
        assertEquals(-1, caveLight1.compareTo(caveLight5));
        assertEquals(0, caveLight1.compareTo(caveLight6));
    }
}