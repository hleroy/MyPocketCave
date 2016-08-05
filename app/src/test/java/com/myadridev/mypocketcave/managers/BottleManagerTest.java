package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.enums.WineColorEnum;
import com.myadridev.mypocketcave.models.SuggestBottleCriteria;
import com.myadridev.mypocketcave.models.SuggestBottleResultModel;
import com.myadridev.mypocketcave.testHelpers.BottleTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class BottleManagerTest {

    private List<Integer> indexesBottlesCreated;

    @Before
    public void before() {
        if (!BottleTestHelper.IsInitialized()) {
            BottleTestHelper.Init();
        }
        indexesBottlesCreated = BottleTestHelper.Instance.createSearchBottleSet();
    }

    @Test
    public void searchBottleNoCriteria() throws Exception {
        SuggestBottleCriteria criteria = new SuggestBottleCriteria();
        List<SuggestBottleResultModel> suggestedBottles = BottleManager.Instance.getSuggestBottles(criteria);
        assertEquals(indexesBottlesCreated.size(), suggestedBottles.size());
        for (SuggestBottleResultModel suggestBottle : suggestedBottles) {
            assertEquals(SuggestBottleCriteria.NumberOfCriteria, suggestBottle.Score);
        }
    }

    @Test
    public void searchBottleWineColorNotRequired() throws Exception {
        SuggestBottleCriteria criteria = new SuggestBottleCriteria();
        criteria.WineColor = WineColorEnum.RED;
        List<SuggestBottleResultModel> suggestedBottles = BottleManager.Instance.getSuggestBottles(criteria);
        assertEquals(indexesBottlesCreated.size(), suggestedBottles.size());
        for (SuggestBottleResultModel suggestBottle : suggestedBottles) {
            if (suggestBottle.Bottle.WineColor == WineColorEnum.RED) {
                assertEquals(SuggestBottleCriteria.NumberOfCriteria, suggestBottle.Score);
            } else {
                assertEquals(SuggestBottleCriteria.NumberOfCriteria - 1, suggestBottle.Score);
            }
        }
    }

    @After
    public void after() {
        BottleTestHelper.Instance.deleteBottleSet(indexesBottlesCreated);
    }
}