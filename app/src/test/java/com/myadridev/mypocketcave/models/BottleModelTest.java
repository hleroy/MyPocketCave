package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.enums.v2.FoodToEatWithEnumV2;
import com.myadridev.mypocketcave.enums.v2.WineColorEnumV2;
import com.myadridev.mypocketcave.models.v2.BottleModelV2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BottleModelTest {

    @Test
    public void createVoidBottleModel() {
        BottleModelV2 bottle = new BottleModelV2();
        assertEquals(0, bottle.Id);
        assertEquals("", bottle.Name);
        assertEquals(0, bottle.Millesime);
        assertEquals("", bottle.Domain);
        assertEquals("", bottle.Comments);
        assertEquals("", bottle.PersonToShareWith);
        assertNull(bottle.WineColor);
        assertEquals(0, bottle.FoodToEatWithList.size());
        assertEquals(0, bottle.Stock);
        assertEquals(0, bottle.NumberPlaced);
        assertEquals(0, bottle.Rating);
        assertEquals(0, bottle.PriceRating);
    }

    @Test
    public void createBottleModelFromExisting() {
        BottleModelV2 expectedBottle = new BottleModelV2();
        expectedBottle.Id = 4;
        expectedBottle.Name = "Name";
        expectedBottle.Domain = "Domain";
        expectedBottle.Millesime = 2016;
        expectedBottle.Comments = "Comments";
        expectedBottle.PersonToShareWith = "Person";
        expectedBottle.WineColor = WineColorEnumV2.ro;
        expectedBottle.FoodToEatWithList.add(FoodToEatWithEnumV2.a);
        expectedBottle.FoodToEatWithList.add(FoodToEatWithEnumV2.ga);
        expectedBottle.FoodToEatWithList.add(FoodToEatWithEnumV2.de);
        expectedBottle.FoodToEatWithList.add(FoodToEatWithEnumV2.st);
        expectedBottle.FoodToEatWithList.add(FoodToEatWithEnumV2.so);
        expectedBottle.Stock = 42;

        BottleModelV2 bottle = new BottleModelV2(expectedBottle);
        assertEquals(expectedBottle.Id, bottle.Id);
        assertEquals(expectedBottle.Name, bottle.Name);
        assertEquals(expectedBottle.Domain, bottle.Domain);
        assertEquals(expectedBottle.Millesime, bottle.Millesime);
        assertEquals(expectedBottle.Comments, bottle.Comments);
        assertEquals(expectedBottle.PersonToShareWith, bottle.PersonToShareWith);
        assertEquals(expectedBottle.WineColor, bottle.WineColor);
        assertEquals(expectedBottle.FoodToEatWithList.size(), bottle.FoodToEatWithList.size());
        for (FoodToEatWithEnumV2 food : expectedBottle.FoodToEatWithList) {
            assertTrue(bottle.FoodToEatWithList.indexOf(food) > -1);
        }
        assertEquals(expectedBottle.Stock, bottle.Stock);
    }

    @Test
    public void compareEqualBottleModel() {
        BottleModelV2 bottle1 = new BottleModelV2();
        bottle1.Id = 4;
        bottle1.Name = "Name";
        bottle1.Domain = "Domain";
        bottle1.Millesime = 2016;
        bottle1.Comments = "Comments";
        bottle1.PersonToShareWith = "Person";
        bottle1.WineColor = WineColorEnumV2.ro;
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.a);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.ga);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.de);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.st);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.so);
        bottle1.Stock = 42;

        BottleModelV2 bottle2 = new BottleModelV2(bottle1);

        assertEquals(0, bottle2.compareTo(bottle1));
    }

    @Test
    public void compareBottleModelDifferentWineColor() {
        BottleModelV2 bottle1 = new BottleModelV2();
        bottle1.Id = 4;
        bottle1.Name = "Name";
        bottle1.Domain = "Domain";
        bottle1.Millesime = 2016;
        bottle1.Comments = "Comments";
        bottle1.PersonToShareWith = "Person";
        bottle1.WineColor = WineColorEnumV2.ro;
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.a);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.ga);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.de);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.st);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.so);
        bottle1.Stock = 42;

        BottleModelV2 bottle2 = new BottleModelV2(bottle1);
        bottle2.WineColor = WineColorEnumV2.w;

        assertEquals(-1, bottle2.compareTo(bottle1));

        bottle2.WineColor = WineColorEnumV2.c;

        assertEquals(1, bottle2.compareTo(bottle1));
    }

    @Test
    public void compareBottleModelDifferentName() {
        BottleModelV2 bottle1 = new BottleModelV2();
        bottle1.Id = 4;
        bottle1.Name = "Name";
        bottle1.Domain = "Domain";
        bottle1.Millesime = 2016;
        bottle1.Comments = "Comments";
        bottle1.PersonToShareWith = "Person";
        bottle1.WineColor = WineColorEnumV2.ro;
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.a);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.ga);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.de);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.st);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.so);
        bottle1.Stock = 42;

        BottleModelV2 bottle2 = new BottleModelV2(bottle1);
        bottle2.Name = "New name";

        assertEquals(1, bottle2.compareTo(bottle1));

        bottle2.Name = "Another new name";

        assertEquals(-1, bottle2.compareTo(bottle1));
    }

    @Test
    public void compareBottleModelDifferentDomain() {
        BottleModelV2 bottle1 = new BottleModelV2();
        bottle1.Id = 4;
        bottle1.Name = "Name";
        bottle1.Domain = "Domain";
        bottle1.Millesime = 2016;
        bottle1.Comments = "Comments";
        bottle1.PersonToShareWith = "Person";
        bottle1.WineColor = WineColorEnumV2.ro;
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.a);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.ga);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.de);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.st);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.so);
        bottle1.Stock = 42;

        BottleModelV2 bottle2 = new BottleModelV2(bottle1);
        bottle2.Domain = "New domain";

        assertEquals(1, bottle2.compareTo(bottle1));

        bottle2.Domain = "Another domain";

        assertEquals(-1, bottle2.compareTo(bottle1));
    }

    @Test
    public void compareBottleModelDifferentMillesime() {
        BottleModelV2 bottle1 = new BottleModelV2();
        bottle1.Id = 4;
        bottle1.Name = "Name";
        bottle1.Domain = "Domain";
        bottle1.Millesime = 2016;
        bottle1.Comments = "Comments";
        bottle1.PersonToShareWith = "Person";
        bottle1.WineColor = WineColorEnumV2.ro;
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.a);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.ga);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.de);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.st);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnumV2.so);
        bottle1.Stock = 42;

        BottleModelV2 bottle2 = new BottleModelV2(bottle1);
        bottle2.Millesime = 2017;

        assertEquals(1, bottle2.compareTo(bottle1));

        bottle2.Millesime = 2015;

        assertEquals(-1, bottle2.compareTo(bottle1));
    }

    @Test
    public void getId() {
        BottleModelV2 bottle = new BottleModelV2();
        bottle.Id = 4;
        bottle.Name = "Name";
        bottle.Domain = "Domain";
        bottle.Millesime = 2016;
        bottle.Comments = "Comments";
        bottle.PersonToShareWith = "Person";
        bottle.WineColor = WineColorEnumV2.ro;
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.a);
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.ga);
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.de);
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.st);
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.so);
        bottle.Stock = 42;

        assertEquals(4, bottle.getId());
    }

    @Test
    public void getIsValidWhenBottleIsValid() {
        BottleModelV2 bottle = new BottleModelV2();
        bottle.Id = 4;
        bottle.Name = "Name";
        bottle.Domain = "";
        bottle.Millesime = 2016;
        bottle.Comments = "Comments";
        bottle.PersonToShareWith = "Person";
        bottle.WineColor = WineColorEnumV2.ro;
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.a);
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.ga);
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.de);
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.st);
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.so);
        bottle.Stock = 42;

        assertTrue(bottle.isValid());
    }

    @Test
    public void getIsValidWhenBottleNoWineColor() {
        BottleModelV2 bottle = new BottleModelV2();
        bottle.Id = 4;
        bottle.Name = "Name";
        bottle.Domain = "Domain";
        bottle.Millesime = 2016;
        bottle.Comments = "Comments";
        bottle.PersonToShareWith = "Person";
//        bottle.WineColor = WineColorEnumV2.ro;
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.a);
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.ga);
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.de);
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.st);
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.so);
        bottle.Stock = 42;

        assertFalse(bottle.isValid());
    }

    @Test
    public void trimAll() {
        BottleModelV2 bottle = new BottleModelV2();
        bottle.Id = 4;
        bottle.Name = "Name ";
        bottle.Domain = "  Domain";
        bottle.Millesime = 2016;
        bottle.Comments = "Comments";
        bottle.PersonToShareWith = " Person  ";
        bottle.WineColor = WineColorEnumV2.ro;
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.a);
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.ga);
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.de);
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.st);
        bottle.FoodToEatWithList.add(FoodToEatWithEnumV2.so);
        bottle.Stock = 42;

        bottle.trimAll();

        assertEquals("Name", bottle.Name);
        assertEquals("Domain", bottle.Domain);
        assertEquals("Comments", bottle.Comments);
        assertEquals("Person", bottle.PersonToShareWith);
    }
}