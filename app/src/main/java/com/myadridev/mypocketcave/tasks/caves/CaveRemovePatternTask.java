package com.myadridev.mypocketcave.tasks.caves;

import android.os.AsyncTask;

import com.myadridev.mypocketcave.activities.AbstractCaveEditActivity;
import com.myadridev.mypocketcave.enums.v2.CavePlaceTypeEnumV2;
import com.myadridev.mypocketcave.enums.v2.PatternTypeEnumV2;
import com.myadridev.mypocketcave.helpers.CollectionsHelper;
import com.myadridev.mypocketcave.managers.CoordinatesManager;
import com.myadridev.mypocketcave.models.v2.CaveModelV2;
import com.myadridev.mypocketcave.models.v2.CavePlaceModelV2;
import com.myadridev.mypocketcave.models.v2.CoordinatesModelV2;
import com.myadridev.mypocketcave.models.v2.PatternModelWithBottlesV2;

import java.util.Map;

public class CaveRemovePatternTask extends AsyncTask<CoordinatesModelV2, Void, Void> {

    private final AbstractCaveEditActivity editActivity;

    public CaveRemovePatternTask(AbstractCaveEditActivity editActivity) {
        this.editActivity = editActivity;
    }

    @Override
    protected Void doInBackground(CoordinatesModelV2... params) {
        CoordinatesModelV2 coordinates = params[0];

        doRemovePattern(editActivity.cave, coordinates);
        // for oldCave too to keep the old patterns for eventual modifications
        doRemovePattern(editActivity.oldCave, coordinates);
        return null;
    }

    private void doRemovePattern(CaveModelV2 cave, CoordinatesModelV2 coordinates) {
        if (cave == null) return;
        // remove pattern
        PatternModelWithBottlesV2 oldPattern = CollectionsHelper.getValueOrDefault(cave.CaveArrangement.PatternMap, coordinates, null);
        if (oldPattern == null) return;
        cave.CaveArrangement.PatternMap.remove(coordinates);

        if (oldPattern.Type == PatternTypeEnumV2.s) {
            removeMainPattern(cave, coordinates, oldPattern);
        }

        // patterns on top go down
        int row = coordinates.Row + 1;
        int col = coordinates.Col;
        CoordinatesModelV2 otherPatternCoordinates = new CoordinatesModelV2(row, col);
        PatternModelWithBottlesV2 pattern = CollectionsHelper.getValueOrDefault(cave.CaveArrangement.PatternMap, otherPatternCoordinates, null);
        while (pattern != null) {
            movePatternDown(cave, coordinates, row, col, otherPatternCoordinates, pattern);
            row++;
            otherPatternCoordinates = new CoordinatesModelV2(row, col);
            pattern = CollectionsHelper.getValueOrDefault(cave.CaveArrangement.PatternMap, otherPatternCoordinates, null);
        }

        // if column is empty, move right side of the cave left
        if (row == 1) {
            movePatternsToLeft(cave.CaveArrangement.PatternMap, col);
        }
    }

    private void movePatternDown(CaveModelV2 cave, CoordinatesModelV2 coordinates, int row, int col, CoordinatesModelV2 otherPatternCoordinates, PatternModelWithBottlesV2 pattern) {
        cave.CaveArrangement.PatternMap.remove(otherPatternCoordinates);
        if (pattern.Type == PatternTypeEnumV2.s) {
            if (pattern.IsHorizontallyExpendable || (pattern.IsVerticallyExpendable && row == coordinates.Row + 1)) {
                removeHalfBottles(pattern,
                        pattern.IsHorizontallyExpendable,
                        pattern.IsHorizontallyExpendable,
                        pattern.IsVerticallyExpendable && row == coordinates.Row + 1,
                        false);
            }
            if (pattern.IsHorizontallyExpendable) {
                // remove half bottles on left/right patterns for the entire column
                PatternModelWithBottlesV2 rightPattern = CollectionsHelper.getValueOrDefault(cave.CaveArrangement.PatternMap, new CoordinatesModelV2(row, col + 1), null);
                removeHalfBottles(rightPattern, true, false, false, false);
                PatternModelWithBottlesV2 leftPattern = CollectionsHelper.getValueOrDefault(cave.CaveArrangement.PatternMap, new CoordinatesModelV2(row, col - 1), null);
                removeHalfBottles(leftPattern, false, true, false, false);
            }
        }
        cave.CaveArrangement.PatternMap.put(new CoordinatesModelV2(row - 1, col), pattern);
    }

    private void removeMainPattern(CaveModelV2 cave, CoordinatesModelV2 coordinates, PatternModelWithBottlesV2 oldPattern) {
        if (oldPattern.IsHorizontallyExpendable || oldPattern.IsVerticallyExpendable) {
            removeHalfBottles(oldPattern, true, true, true, true);
        }
        int row = coordinates.Row;
        int col = coordinates.Col;
        if (oldPattern.IsHorizontallyExpendable) {
            // remove half bottles on left/right patterns for the entire column
            PatternModelWithBottlesV2 rightPattern = CollectionsHelper.getValueOrDefault(cave.CaveArrangement.PatternMap, new CoordinatesModelV2(row, col + 1), null);
            removeHalfBottles(rightPattern, true, false, false, false);
            PatternModelWithBottlesV2 leftPattern = CollectionsHelper.getValueOrDefault(cave.CaveArrangement.PatternMap, new CoordinatesModelV2(row, col - 1), null);
            removeHalfBottles(leftPattern, false, true, false, false);
        }
        if (oldPattern.IsVerticallyExpendable) {
            PatternModelWithBottlesV2 bottomPattern = CollectionsHelper.getValueOrDefault(cave.CaveArrangement.PatternMap, new CoordinatesModelV2(row - 1, col), null);
            removeHalfBottles(bottomPattern, false, false, false, true);
        }
    }

    private void movePatternsToLeft(Map<CoordinatesModelV2, PatternModelWithBottlesV2> patternMap, int column) {
        int col = column + 1;
        int row = 0;
        CoordinatesModelV2 coordinates = new CoordinatesModelV2(row, col);
        PatternModelWithBottlesV2 pattern = CollectionsHelper.getValueOrDefault(patternMap, coordinates, null);
        while (pattern != null) {
            patternMap.remove(coordinates);
            patternMap.put(new CoordinatesModelV2(row, col - 1), pattern);
            row++;
            coordinates = new CoordinatesModelV2(row, col);
            pattern = CollectionsHelper.getValueOrDefault(patternMap, coordinates, null);
        }
        if (row > 0) {
            movePatternsToLeft(patternMap, col);
        }
    }

    private void removeHalfBottles(PatternModelWithBottlesV2 pattern, boolean removeOnFirstColumn, boolean removeOnLastColumn, boolean removeOnFirstLine, boolean removeOnLastLine) {
        if (pattern == null) return;
        // remove half bottles on left/right for the entire column
        CoordinatesModelV2 maxRowCol = CoordinatesManager.getMaxRowCol(pattern.PlaceMapWithBottles.keySet());
        int maxRow = maxRowCol.Row;
        int maxCol = maxRowCol.Col;
        for (int r = 0; r <= maxRow; r++) {
            if (removeOnFirstColumn) {
                removeBottleIfNeeded(pattern, new CoordinatesModelV2(r, 0));
            }
            if (removeOnLastColumn) {
                removeBottleIfNeeded(pattern, new CoordinatesModelV2(r, maxCol));
            }
        }
        for (int c = 0; c <= maxCol; c++) {
            if (removeOnFirstLine) {
                removeBottleIfNeeded(pattern, new CoordinatesModelV2(0, c));
            }
            if (removeOnLastLine) {
                removeBottleIfNeeded(pattern, new CoordinatesModelV2(maxRow, c));
            }
        }
    }

    private void removeBottleIfNeeded(PatternModelWithBottlesV2 pattern, CoordinatesModelV2 coordinates) {
        CavePlaceModelV2 cavePlace = pattern.PlaceMapWithBottles.get(coordinates);
        if (cavePlace.BottleId != -1) {
            pattern.FloatNumberPlacedBottlesByIdMap.put(cavePlace.BottleId, pattern.FloatNumberPlacedBottlesByIdMap.get(cavePlace.BottleId) - 0.25f);
            cavePlace.BottleId = -1;
            cavePlace.PlaceType = CavePlaceTypeEnumV2.getEmptyByPosition(cavePlace.PlaceType.Position);
        }
    }

    @Override
    protected void onPostExecute(Void value) {
        editActivity.createCaveArrangementAdapter();
    }
}
