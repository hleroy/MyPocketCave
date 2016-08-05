package com.myadridev.mypocketcave.models;

import com.myadridev.mypocketcave.enums.FoodToEatWithEnum;
import com.myadridev.mypocketcave.enums.WineColorEnum;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BottleModelTest {
    @Test
    public void createVoidBottleModel() throws Exception {
        BottleModel bottle = new BottleModel();
        assertEquals(0, bottle.Id);
        assertNull(bottle.Name);
        assertNull(bottle.Domain);
        assertEquals(0, bottle.Millesime);
        assertNull(bottle.Comments);
        assertNull(bottle.PersonToShareWith);
        assertNull(bottle.WineColor);
        assertEquals(0, bottle.FoodToEatWithList.size());
        assertEquals(0, bottle.Stock);
    }

    @Test
    public void createBottleModelFromExisting() throws Exception {
        BottleModel expectedBottle = new BottleModel();
        expectedBottle.Id = 4;
        expectedBottle.Name = "Name";
        expectedBottle.Domain = "Domain";
        expectedBottle.Millesime = 2016;
        expectedBottle.Comments = "Comments";
        expectedBottle.PersonToShareWith = "Person";
        expectedBottle.WineColor = WineColorEnum.ROSE;
        expectedBottle.FoodToEatWithList.add(FoodToEatWithEnum.Aperitif);
        expectedBottle.FoodToEatWithList.add(FoodToEatWithEnum.Game);
        expectedBottle.FoodToEatWithList.add(FoodToEatWithEnum.Dessert);
        expectedBottle.FoodToEatWithList.add(FoodToEatWithEnum.Starter);
        expectedBottle.FoodToEatWithList.add(FoodToEatWithEnum.Soup);
        expectedBottle.Stock = 42;

        BottleModel bottle = new BottleModel(expectedBottle);
        assertEquals(expectedBottle.Id, bottle.Id);
        assertEquals(expectedBottle.Name, bottle.Name);
        assertEquals(expectedBottle.Domain, bottle.Domain);
        assertEquals(expectedBottle.Millesime, bottle.Millesime);
        assertEquals(expectedBottle.Comments, bottle.Comments);
        assertEquals(expectedBottle.PersonToShareWith, bottle.PersonToShareWith);
        assertEquals(expectedBottle.WineColor, bottle.WineColor);
        assertEquals(expectedBottle.FoodToEatWithList.size(), bottle.FoodToEatWithList.size());
        for (FoodToEatWithEnum food : expectedBottle.FoodToEatWithList) {
            assertTrue(bottle.FoodToEatWithList.indexOf(food) > -1);
        }
        assertEquals(expectedBottle.Stock, bottle.Stock);
    }

    @Test
    public void compareEqualBottleModel() throws Exception {
        BottleModel bottle1 = new BottleModel();
        bottle1.Id = 4;
        bottle1.Name = "Name";
        bottle1.Domain = "Domain";
        bottle1.Millesime = 2016;
        bottle1.Comments = "Comments";
        bottle1.PersonToShareWith = "Person";
        bottle1.WineColor = WineColorEnum.ROSE;
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Aperitif);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Game);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Dessert);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Starter);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Soup);
        bottle1.Stock = 42;

        BottleModel bottle2 = new BottleModel(bottle1);

        assertEquals(0, bottle2.compareTo(bottle1));
    }

    @Test
    public void compareBottleModelDifferentWineColor() throws Exception {
        BottleModel bottle1 = new BottleModel();
        bottle1.Id = 4;
        bottle1.Name = "Name";
        bottle1.Domain = "Domain";
        bottle1.Millesime = 2016;
        bottle1.Comments = "Comments";
        bottle1.PersonToShareWith = "Person";
        bottle1.WineColor = WineColorEnum.ROSE;
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Aperitif);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Game);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Dessert);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Starter);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Soup);
        bottle1.Stock = 42;

        BottleModel bottle2 = new BottleModel(bottle1);
        bottle2.WineColor = WineColorEnum.WHITE;

        assertEquals(-1, bottle2.compareTo(bottle1));

        bottle2.WineColor = WineColorEnum.CHAMPAGNE;

        assertEquals(1, bottle2.compareTo(bottle1));
    }

    @Test
    public void compareBottleModelDifferentName() throws Exception {
        BottleModel bottle1 = new BottleModel();
        bottle1.Id = 4;
        bottle1.Name = "Name";
        bottle1.Domain = "Domain";
        bottle1.Millesime = 2016;
        bottle1.Comments = "Comments";
        bottle1.PersonToShareWith = "Person";
        bottle1.WineColor = WineColorEnum.ROSE;
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Aperitif);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Game);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Dessert);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Starter);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Soup);
        bottle1.Stock = 42;

        BottleModel bottle2 = new BottleModel(bottle1);
        bottle2.Name = "New name";

        assertEquals(1, bottle2.compareTo(bottle1));

        bottle2.Name = "Another new name";

        assertEquals(-1, bottle2.compareTo(bottle1));
    }

    @Test
    public void compareBottleModelDifferentDomain() throws Exception {
        BottleModel bottle1 = new BottleModel();
        bottle1.Id = 4;
        bottle1.Name = "Name";
        bottle1.Domain = "Domain";
        bottle1.Millesime = 2016;
        bottle1.Comments = "Comments";
        bottle1.PersonToShareWith = "Person";
        bottle1.WineColor = WineColorEnum.ROSE;
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Aperitif);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Game);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Dessert);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Starter);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Soup);
        bottle1.Stock = 42;

        BottleModel bottle2 = new BottleModel(bottle1);
        bottle2.Domain = "New domain";

        assertEquals(1, bottle2.compareTo(bottle1));

        bottle2.Domain = "Another domain";

        assertEquals(-1, bottle2.compareTo(bottle1));
    }

    @Test
    public void compareBottleModelDifferentMillesime() throws Exception {
        BottleModel bottle1 = new BottleModel();
        bottle1.Id = 4;
        bottle1.Name = "Name";
        bottle1.Domain = "Domain";
        bottle1.Millesime = 2016;
        bottle1.Comments = "Comments";
        bottle1.PersonToShareWith = "Person";
        bottle1.WineColor = WineColorEnum.ROSE;
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Aperitif);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Game);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Dessert);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Starter);
        bottle1.FoodToEatWithList.add(FoodToEatWithEnum.Soup);
        bottle1.Stock = 42;

        BottleModel bottle2 = new BottleModel(bottle1);
        bottle2.Millesime = 2017;

        assertEquals(1, bottle2.compareTo(bottle1));

        bottle2.Millesime = 2015;

        assertEquals(-1, bottle2.compareTo(bottle1));
    }
}