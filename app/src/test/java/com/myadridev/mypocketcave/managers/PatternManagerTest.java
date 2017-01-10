package com.myadridev.mypocketcave.managers;

import android.content.Context;

import com.myadridev.mypocketcave.managers.storage.interfaces.IPatternsStorageManager;
import com.myadridev.mypocketcave.models.PatternModel;

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
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PatternManagerTest {

    @Mock
    IPatternsStorageManager mockPatternsStorageManager;

    @Mock
    private Context context;

    private List<PatternModel> patterns;

    @Before
    public void before() {
        patterns = new ArrayList<>(3);
        PatternModel pattern1 = new PatternModel();
        pattern1.Id = 1;
        pattern1.Order = 1;
        PatternModel pattern2 = new PatternModel();
        pattern2.Id = 2;
        pattern2.Order = 2;
        PatternModel pattern3 = new PatternModel();
        pattern3.Id = 3;
        pattern3.Order = 3;
        patterns.add(pattern1);
        patterns.add(pattern2);
        patterns.add(pattern3);

        DependencyManager.init();

        when(mockPatternsStorageManager.getPatterns()).thenAnswer(new Answer<List<PatternModel>>() {
            @Override
            public List<PatternModel> answer(InvocationOnMock invocation) {
                return patterns;
            }
        });
        when(mockPatternsStorageManager.getPattern(anyInt())).thenAnswer(new Answer<PatternModel>() {
            @Override
            public PatternModel answer(InvocationOnMock invocation) {
                int id = (int) invocation.getArguments()[0];
                PatternModel pattern = new PatternModel();
                pattern.Id = id;
                pattern.Order = id;
                return pattern;
            }
        });
        when(mockPatternsStorageManager.getExistingPatternId(any(PatternModel.class))).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) {
                int id = ((PatternModel) invocation.getArguments()[0]).Id;
                return id == 42 ? 42 : -1;
            }
        });
        when(mockPatternsStorageManager.insertPattern(any(Context.class), any(PatternModel.class))).thenReturn(17);
        DependencyManager.registerSingleton(IPatternsStorageManager.class, mockPatternsStorageManager, true);
    }

    @After
    public void afterClass() {
        DependencyManager.cleanUp();
    }

    @Test
    public void getPatterns() {
        List<PatternModel> allPatterns = PatternManager.getPatterns();
        assertEquals(patterns.size(), allPatterns.size());
        for (PatternModel pattern : patterns) {
            assertTrue(allPatterns.contains(pattern));
        }
    }

    @Test
    public void getPattern() {
        PatternModel pattern = PatternManager.getPattern(42);
        assertEquals(42, pattern.Id);
    }

    @Test
    public void addExistingPattern() {
        PatternModel pattern42 = new PatternModel();
        pattern42.Id = 42;
        int res = PatternManager.addPattern(context, pattern42);
        assertEquals(42, res);
    }

    @Test
    public void addNonExistingPattern() {
        PatternModel pattern2 = new PatternModel();
        int res2 = PatternManager.addPattern(context, pattern2);
        assertEquals(17, res2);
    }

    @Test
    public void setLastUsedPattern() {
        PatternManager.setLastUsedPattern(context, 2);
        for (PatternModel pattern : patterns) {
            switch (pattern.Id) {
                case 1:
                    assertEquals(2, pattern.Order);
                    break;
                case 2:
                    assertEquals(1, pattern.Order);
                    break;
                case 3:
                    assertEquals(3, pattern.Order);
                    break;
                default:
                    fail();
            }
        }
    }
}