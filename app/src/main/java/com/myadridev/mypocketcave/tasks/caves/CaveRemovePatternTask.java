package com.myadridev.mypocketcave.tasks.caves;

import android.os.AsyncTask;

import com.myadridev.mypocketcave.activities.AbstractCaveEditActivity;
import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.enums.PatternTypeEnum;
import com.myadridev.mypocketcave.helpers.CollectionsHelper;
import com.myadridev.mypocketcave.managers.CoordinatesManager;
import com.myadridev.mypocketcave.models.CaveModel;
import com.myadridev.mypocketcave.models.CavePlaceModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModelWithBottles;

import java.util.Map;

public class CaveRemovePatternTask extends AsyncTask<CoordinatesModel, Void, Void> {

    private final AbstractCaveEditActivity editActivity;

    public CaveRemovePatternTask(AbstractCaveEditActivity editActivity) {
        this.editActivity = editActivity;
    }

    @Override
    protected Void doInBackground(CoordinatesModel... params) {
        CoordinatesModel coordinates = params[0];

        doRemovePattern(editActivity.cave, coordinates);
        // for oldCave too to keep the old patterns for eventual modifications
        doRemovePattern(editActivity.oldCave, coordinates);
        return null;
    }

    private void doRemovePattern(CaveModel cave, CoordinatesModel coordinates) {
        if (cave == null) return;
        // remove pattern
        PatternModelWithBottles oldPattern = CollectionsHelper.getValueOrDefault(cave.CaveArrangement.PatternMap, coordinates, null);
        if (oldPattern == null) return;
        cave.CaveArrangement.PatternMap.remove(coordinates);

        if (oldPattern.Type == PatternTypeEnum.STAGGERED_ROWS) {
            removeMainPattern(cave, coordinates, oldPattern);
        }

        // patterns on top go down
        int row = coordinates.Row + 1;
        int col = coordinates.Col;
        CoordinatesModel otherPatternCoordinates = new CoordinatesModel(row, col);
        PatternModelWithBottles pattern = CollectionsHelper.getValueOrDefault(cave.CaveArrangement.PatternMap, otherPatternCoordinates, null);
        while (pattern != null) {
            movePatternDown(cave, coordinates, row, col, otherPatternCoordinates, pattern);
            row++;
            otherPatternCoordinates = new CoordinatesModel(row, col);
            pattern = CollectionsHelper.getValueOrDefault(cave.CaveArrangement.PatternMap, otherPatternCoordinates, null);
        }

        // if column is empty, move right side of the cave left
        if (row == 1) {
            movePatternsToLeft(cave.CaveArrangement.PatternMap, col);
        }
    }

    private void movePatternDown(CaveModel cave, CoordinatesModel coordinates, int row, int col, CoordinatesModel otherPatternCoordinates, PatternModelWithBottles pattern) {
        cave.CaveArrangement.PatternMap.remove(otherPatternCoordinates);
        if (pattern.Type == PatternTypeEnum.STAGGERED_ROWS) {
            if (pattern.IsHorizontallyExpendable || (pattern.IsVerticallyExpendable && row == coordinates.Row + 1)) {
                removeHalfBottles(pattern,
                        pattern.IsHorizontallyExpendable,
                        pattern.IsHorizontallyExpendable,
                        pattern.IsVerticallyExpendable && row == coordinates.Row + 1,
                        false);
            }
            if (pattern.IsHorizontallyExpendable) {
                // remove half bottles on left/right patterns for the entire column
                PatternModelWithBottles rightPattern = CollectionsHelper.getValueOrDefault(cave.CaveArrangement.PatternMap, new CoordinatesModel(row, col + 1), null);
                removeHalfBottles(rightPattern, true, false, false, false);
                PatternModelWithBottles leftPattern = CollectionsHelper.getValueOrDefault(cave.CaveArrangement.PatternMap, new CoordinatesModel(row, col - 1), null);
                removeHalfBottles(leftPattern, false, true, false, false);
            }
        }
        cave.CaveArrangement.PatternMap.put(new CoordinatesModel(row - 1, col), pattern);
    }

    private void removeMainPattern(CaveModel cave, CoordinatesModel coordinates, PatternModelWithBottles oldPattern) {
        if (oldPattern.IsHorizontallyExpendable || oldPattern.IsVerticallyExpendable) {
            removeHalfBottles(oldPattern, true, true, true, true);
        }
        int row = coordinates.Row;
        int col = coordinates.Col;
        if (oldPattern.IsHorizontallyExpendable) {
            // remove half bottles on left/right patterns for the entire column
            PatternModelWithBottles rightPattern = CollectionsHelper.getValueOrDefault(cave.CaveArrangement.PatternMap, new CoordinatesModel(row, col + 1), null);
            removeHalfBottles(rightPattern, true, false, false, false);
            PatternModelWithBottles leftPattern = CollectionsHelper.getValueOrDefault(cave.CaveArrangement.PatternMap, new CoordinatesModel(row, col - 1), null);
            removeHalfBottles(leftPattern, false, true, false, false);
        }
        if (oldPattern.IsVerticallyExpendable) {
            PatternModelWithBottles bottomPattern = CollectionsHelper.getValueOrDefault(cave.CaveArrangement.PatternMap, new CoordinatesModel(row - 1, col), null);
            removeHalfBottles(bottomPattern, false, false, false, true);
        }
    }

    private void movePatternsToLeft(Map<CoordinatesModel, PatternModelWithBottles> patternMap, int column) {
        int col = column + 1;
        int row = 0;
        CoordinatesModel coordinates = new CoordinatesModel(row, col);
        PatternModelWithBottles pattern = CollectionsHelper.getValueOrDefault(patternMap, coordinates, null);
        while (pattern != null) {
            patternMap.remove(coordinates);
            patternMap.put(new CoordinatesModel(row, col - 1), pattern);
            row++;
            coordinates = new CoordinatesModel(row, col);
            pattern = CollectionsHelper.getValueOrDefault(patternMap, coordinates, null);
        }
        if (row > 0) {
            movePatternsToLeft(patternMap, col);
        }
    }

    private void removeHalfBottles(PatternModelWithBottles pattern, boolean removeOnFirstColumn, boolean removeOnLastColumn, boolean removeOnFirstLine, boolean removeOnLastLine) {
        if (pattern == null) return;
        // remove half bottles on left/right for the entire column
        CoordinatesModel maxRowCol = CoordinatesManager.getMaxRowCol(pattern.PlaceMapWithBottles.keySet());
        int maxRow = maxRowCol.Row;
        int maxCol = maxRowCol.Col;
        for (int r = 0; r <= maxRow; r++) {
            if (removeOnFirstColumn) {
                removeBottleIfNeeded(pattern, new CoordinatesModel(r, 0));
            }
            if (removeOnLastColumn) {
                removeBottleIfNeeded(pattern, new CoordinatesModel(r, maxCol));
            }
        }
        for (int c = 0; c <= maxCol; c++) {
            if (removeOnFirstLine) {
                removeBottleIfNeeded(pattern, new CoordinatesModel(0, c));
            }
            if (removeOnLastLine) {
                removeBottleIfNeeded(pattern, new CoordinatesModel(maxRow, c));
            }
        }
    }

    private void removeBottleIfNeeded(PatternModelWithBottles pattern, CoordinatesModel coordinates) {
        CavePlaceModel cavePlace = pattern.PlaceMapWithBottles.get(coordinates);
        if (cavePlace.BottleId != -1) {
            pattern.FloatNumberPlacedBottlesByIdMap.put(cavePlace.BottleId, pattern.FloatNumberPlacedBottlesByIdMap.get(cavePlace.BottleId) - 0.25f);
            cavePlace.BottleId = -1;
            cavePlace.PlaceType = CavePlaceTypeEnum.getEmptyByPosition(cavePlace.PlaceType.Position);
        }
    }

    @Override
    protected void onPostExecute(Void value) {
        editActivity.createCaveArrangementAdapter();
    }
}
