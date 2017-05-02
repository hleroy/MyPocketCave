package com.myadridev.mypocketcave.managers;

import android.content.Context;

import com.myadridev.mypocketcave.managers.storage.interfaces.v2.IPatternsStorageManagerV2;
import com.myadridev.mypocketcave.models.v2.PatternModelV2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PatternManagerTest {

    @Mock
    IPatternsStorageManagerV2 mockPatternsStorageManager;

    @Mock
    private Context context;

    private List<PatternModelV2> patterns;

    @Before
    public void before() {
        patterns = new ArrayList<>(3);
        PatternModelV2 pattern1 = new PatternModelV2();
        pattern1.Id = 1;
        pattern1.Order = 0;
        PatternModelV2 pattern2 = new PatternModelV2();
        pattern2.Id = 2;
        pattern2.Order = 1;
        PatternModelV2 pattern3 = new PatternModelV2();
        pattern3.Id = 3;
        pattern3.Order = 2;
        patterns.add(pattern1);
        patterns.add(pattern2);
        patterns.add(pattern3);

        DependencyManager.init();

        when(mockPatternsStorageManager.getPatterns()).thenAnswer(new Answer<List<PatternModelV2>>() {
            @Override
            public List<PatternModelV2> answer(InvocationOnMock invocation) {
                return patterns;
            }
        });
        when(mockPatternsStorageManager.getPattern(anyInt())).thenAnswer(new Answer<PatternModelV2>() {
            @Override
            public PatternModelV2 answer(InvocationOnMock invocation) {
                int id = (int) invocation.getArguments()[0];
                return patterns.get(id - 1);
            }
        });
        when(mockPatternsStorageManager.getExistingPatternId(any(PatternModelV2.class))).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) {
                int id = ((PatternModelV2) invocation.getArguments()[0]).Id;
                return id == 42 ? 42 : -1;
            }
        });
        when(mockPatternsStorageManager.insertPattern(any(Context.class), any(PatternModelV2.class), anyBoolean())).thenReturn(17);
        DependencyManager.registerSingleton(IPatternsStorageManagerV2.class, mockPatternsStorageManager, true);
    }

    @After
    public void afterClass() {
        DependencyManager.cleanUp();
    }

    @Test
    public void getPatterns() {
        List<PatternModelV2> allPatterns = PatternManager.getPatterns();
        assertEquals(patterns.size(), allPatterns.size());
        for (PatternModelV2 pattern : patterns) {
            assertTrue(allPatterns.contains(pattern));
        }
    }

    @Test
    public void getPattern() {
        PatternModelV2 pattern = PatternManager.getPattern(2);
        assertEquals(2, pattern.Id);
    }

    @Test
    public void addExistingPattern() {
        PatternModelV2 pattern42 = new PatternModelV2();
        pattern42.Id = 42;
        int res = PatternManager.addPattern(context, pattern42);
        assertEquals(42, res);
    }

    @Test
    public void addNonExistingPattern() {
        PatternModelV2 pattern2 = new PatternModelV2();
        int res2 = PatternManager.addPattern(context, pattern2);
        assertEquals(17, res2);
    }

    @Test
    public void setLastUsedPattern() {
        PatternManager.setLastUsedPattern(context, 2);
        for (PatternModelV2 pattern : patterns) {
            switch (pattern.Id) {
                case 1:
                    assertEquals(1, pattern.Order);
                    break;
                case 2:
                    assertEquals(0, pattern.Order);
                    break;
                case 3:
                    assertEquals(2, pattern.Order);
                    break;
                default:
                    fail();
            }
        }
    }
}