package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.models.CaveArrangementModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class CaveArrangementManager {

    public static CaveArrangementManager Instance;
    private static boolean _isInitialized;

    private CaveArrangementManager() {
    }

    public static boolean IsInitialized() {
        return _isInitialized;
    }

    public static void Init() {
        Instance = new CaveArrangementManager();
        _isInitialized = true;
    }

    public Map<CoordinatesModel, CavePlaceTypeEnum> getPlaceMap(CaveArrangementModel caveArrangement) {
        Map<CoordinatesModel, CavePlaceTypeEnum> placeMap = new HashMap<>();

        CoordinatesModel originCoordinates = new CoordinatesModel(0, 0);
        if (caveArrangement.PatternMap.containsKey(originCoordinates)) {
            List<CoordinatesModel> patternsDone = new ArrayList<>();
            Map<CoordinatesModel, CoordinatesModel> patternsAbsoluteLastRowCol = new HashMap<>();
            Queue<CoordinatesModel> coordinatesToHandleQueue = new LinkedList<>();
            coordinatesToHandleQueue.add(originCoordinates);
            while (!coordinatesToHandleQueue.isEmpty()) {
                CoordinatesModel coordinates = coordinatesToHandleQueue.remove();
                int row = coordinates.Row;
                int col = coordinates.Col;
                getPlaceMap(placeMap, patternsDone, patternsAbsoluteLastRowCol, coordinatesToHandleQueue,
                        caveArrangement.PatternMap, coordinates,
                        getIndexFirstRow(row, col, patternsAbsoluteLastRowCol),
                        getIndexFirstCol(row, col, patternsAbsoluteLastRowCol),
                        getTopPatternExpendable(row, col, caveArrangement.PatternMap),
                        getLeftPatternExpendable(row, col, caveArrangement.PatternMap));
            }

            fillPattern(placeMap, patternsAbsoluteLastRowCol);
        }

        return placeMap;
    }

    private int getIndexFirstRow(int row, int col, Map<CoordinatesModel, CoordinatesModel> patternsAbsoluteLastRowCol) {
        if (row == 0) {
            return 0;
        } else {
            CoordinatesModel topCoordinates = new CoordinatesModel(row - 1, col);
            if (patternsAbsoluteLastRowCol.containsKey(topCoordinates)) {
                CoordinatesModel topCoordinatesRowCol = patternsAbsoluteLastRowCol.get(topCoordinates);
                return topCoordinatesRowCol.Row;
            } else {
                CoordinatesModel leftTopCoordinates = new CoordinatesModel(row - 1, col - 1);
                if (patternsAbsoluteLastRowCol.containsKey(leftTopCoordinates)) {
                    CoordinatesModel leftTopCoordinatesRowCol = patternsAbsoluteLastRowCol.get(leftTopCoordinates);
                    return leftTopCoordinatesRowCol.Row;
                } else {
                    // should not happen
                    return 0;
                }
            }
        }
    }

    private int getIndexFirstCol(int row, int col, Map<CoordinatesModel, CoordinatesModel> patternsAbsoluteLastRowCol) {
        if (col == 0) {
            return 0;
        } else {
            CoordinatesModel leftCoordinates = new CoordinatesModel(row, col - 1);
            if (patternsAbsoluteLastRowCol.containsKey(leftCoordinates)) {
                CoordinatesModel leftCoordinatesRowCol = patternsAbsoluteLastRowCol.get(leftCoordinates);
                return leftCoordinatesRowCol.Col;
            } else {
                // should not happen
                return 0;
            }
        }
    }

    private boolean getTopPatternExpendable(int row, int col, Map<CoordinatesModel, PatternModel> patternMap) {
        if (row == 0) {
            return false;
        } else {
            CoordinatesModel topCoordinates = new CoordinatesModel(row - 1, col);
            if (patternMap.containsKey(topCoordinates)) {
                return patternMap.get(topCoordinates).IsVerticallyExpendable;
            } else {
                // should not happen
                return false;
            }
        }
    }

    private boolean getLeftPatternExpendable(int row, int col, Map<CoordinatesModel, PatternModel> patternMap) {
        if (col == 0) {
            return false;
        } else {
            CoordinatesModel leftCoordinates = new CoordinatesModel(row, col - 1);
            if (patternMap.containsKey(leftCoordinates)) {
                return patternMap.get(leftCoordinates).IsVerticallyExpendable;
            } else {
                // should not happen
                return false;
            }
        }
    }

    private void getPlaceMap(Map<CoordinatesModel, CavePlaceTypeEnum> placeMap, List<CoordinatesModel> patternsDone,
                             Map<CoordinatesModel, CoordinatesModel> patternsAbsoluteLastRowCol, Queue<CoordinatesModel> coordinatesToHandleQueue,
                             Map<CoordinatesModel, PatternModel> patternMap, CoordinatesModel patternCoordinates,
                             int indexFirstRow, int indexFirstCol, boolean isTopPatternExpendable, boolean isLeftPatternExpendable) {

        if (patternsDone.contains(patternCoordinates)) {
            return;
        }
        PatternModel pattern = patternMap.get(patternCoordinates);

        // if isTopPatternExpendable, then indexFirstRow is the index of the last row
        // else it's the real first index
        // same thing for columns
        int indexRealFirstRow = indexFirstRow;
        if (isTopPatternExpendable && !pattern.IsVerticallyExpendable) {
            indexRealFirstRow++;
        }
        int indexRealFirstCol = indexFirstCol;
        if (isLeftPatternExpendable && !pattern.IsHorizontallyExpendable) {
            indexRealFirstCol++;
        }

        int indexLastRow = indexRealFirstRow;
        int indexLastCol = indexRealFirstCol;

        for (Map.Entry<CoordinatesModel, CavePlaceTypeEnum> placeTypeEntry : pattern.PlaceMap.entrySet()) {
            CoordinatesModel coordinates = placeTypeEntry.getKey();
            CavePlaceTypeEnum placeType = placeTypeEntry.getValue();
            int patternRow = coordinates.Row;
            int patternCol = coordinates.Col;
            int absoluteRow = indexRealFirstRow + patternRow;
            int absoluteCol = indexRealFirstCol + patternCol;

            CoordinatesModel absoluteCoordinates = new CoordinatesModel(absoluteRow, absoluteCol);
            indexLastRow = Math.max(indexLastRow, absoluteRow);
            indexLastCol = Math.max(indexLastCol, absoluteCol);
            if (patternRow != 0 && patternCol != 0) {
                placeMap.put(absoluteCoordinates, placeType);
            } else if (patternRow == 0) {
                if (!isTopPatternExpendable || !pattern.IsVerticallyExpendable) {
                    placeMap.put(absoluteCoordinates, placeType);
                } else {
                    // merge 2 complementary places if necessary
                    if (placeMap.containsKey(absoluteCoordinates)) {
                        CavePlaceTypeEnum oldPlaceType = placeMap.get(absoluteCoordinates);
                        if (oldPlaceType == CavePlaceTypeEnum.PLACE_WITH_TOP && placeType == CavePlaceTypeEnum.PLACE_WITH_BOTTOM) {
                            placeMap.put(absoluteCoordinates, CavePlaceTypeEnum.PLACE);
                        }
                    } else {
                        placeMap.put(absoluteCoordinates, placeType);
                    }
                }
            } else {
                if (!isLeftPatternExpendable || !pattern.IsHorizontallyExpendable) {
                    placeMap.put(absoluteCoordinates, placeType);
                } else {
                    // merge 2 complementary places if necessary
                    if (placeMap.containsKey(absoluteCoordinates)) {
                        CavePlaceTypeEnum oldPlaceType = placeMap.get(absoluteCoordinates);
                        if (oldPlaceType == CavePlaceTypeEnum.PLACE_WITH_RIGHT && placeType == CavePlaceTypeEnum.PLACE_WITH_LEFT) {
                            placeMap.put(absoluteCoordinates, CavePlaceTypeEnum.PLACE);
                        }
                    } else {
                        placeMap.put(absoluteCoordinates, placeType);
                    }
                }
            }
        }
        patternsDone.add(patternCoordinates);
        int indexFirstRowForBottom = pattern.IsVerticallyExpendable ? indexLastRow : indexLastRow + 1;
        int indexFirstColForRight = pattern.IsHorizontallyExpendable ? indexLastCol : indexLastCol + 1;
        patternsAbsoluteLastRowCol.put(patternCoordinates, new CoordinatesModel(indexFirstRowForBottom, indexFirstColForRight));

        CoordinatesModel rightCoordinates = new CoordinatesModel(patternCoordinates.Row, patternCoordinates.Col + 1);
        CoordinatesModel bottomCoordinates = new CoordinatesModel(patternCoordinates.Row + 1, patternCoordinates.Col);

        if (patternMap.containsKey(rightCoordinates)) {
            coordinatesToHandleQueue.add(rightCoordinates);
        }
        if (patternMap.containsKey(bottomCoordinates)) {
            coordinatesToHandleQueue.add(bottomCoordinates);
        }
    }

    private void fillPattern(Map<CoordinatesModel, CavePlaceTypeEnum> placeMap, Map<CoordinatesModel, CoordinatesModel> patternsAbsoluteLastRowCol) {
        int maxRowIndex = 0;
        int maxColIndex = 0;
        for (CoordinatesModel rowCol : patternsAbsoluteLastRowCol.values()) {
            maxRowIndex = Math.max(rowCol.Row, maxRowIndex);
            maxColIndex = Math.max(rowCol.Col, maxColIndex);
        }
        for (int row = 0; row <= maxRowIndex; row++) {
            for (int col = 0; col <= maxColIndex; col++) {
                CoordinatesModel rowCol = new CoordinatesModel(row, col);
                if (!placeMap.containsKey(rowCol)) {
                    placeMap.put(rowCol, CavePlaceTypeEnum.NO_PLACE);
                }
            }
        }
    }

    public int computeTotalCapacityWithBulk(CaveArrangementModel caveArrangement) {
        return caveArrangement.NumberBottlesBulk;
    }

    public int computeTotalCapacityWithBoxes(CaveArrangementModel caveArrangement) {
        return caveArrangement.NumberBottlesPerBox * caveArrangement.NumberBoxes;
    }

    public int computeTotalCapacityWithPattern(CaveArrangementModel caveArrangement) {
        Map<CoordinatesModel, List<Integer>> coordinatesPlacesWithTop = new HashMap<>();
        Map<CoordinatesModel, List<Integer>> coordinatesPlacesWithBottom = new HashMap<>();
        Map<CoordinatesModel, List<Integer>> coordinatesPlacesWithLeft = new HashMap<>();
        Map<CoordinatesModel, List<Integer>> coordinatesPlacesWithRight = new HashMap<>();

        int capacity = 0;

        for (Map.Entry<CoordinatesModel, PatternModel> patternEntry : caveArrangement.PatternMap.entrySet()) {
            CoordinatesModel patternCoordinates = patternEntry.getKey();
            PatternModel pattern = patternEntry.getValue();
            if (pattern.IsHorizontallyExpendable) {
                coordinatesPlacesWithLeft.put(patternCoordinates, new ArrayList<Integer>());
                coordinatesPlacesWithRight.put(patternCoordinates, new ArrayList<Integer>());
            }
            if (pattern.IsVerticallyExpendable) {
                coordinatesPlacesWithTop.put(patternCoordinates, new ArrayList<Integer>());
                coordinatesPlacesWithBottom.put(patternCoordinates, new ArrayList<Integer>());
            }

            for (Map.Entry<CoordinatesModel, CavePlaceTypeEnum> patternPlaceTypeEntry : pattern.PlaceMap.entrySet()) {
                CoordinatesModel placeCoordinates = patternPlaceTypeEntry.getKey();
                CavePlaceTypeEnum placeType = patternPlaceTypeEntry.getValue();
                switch (placeType) {
                    case PLACE:
                        capacity++;
                        break;
                    case PLACE_WITH_RIGHT:
                        if (!pattern.IsHorizontallyExpendable) continue;
                        coordinatesPlacesWithRight.get(patternCoordinates).add(placeCoordinates.Row);
                        break;
                    case PLACE_WITH_LEFT:
                        if (!pattern.IsHorizontallyExpendable) continue;
                        coordinatesPlacesWithLeft.get(patternCoordinates).add(placeCoordinates.Row);
                        break;
                    case PLACE_WITH_BOTTOM:
                        if (!pattern.IsVerticallyExpendable) continue;
                        coordinatesPlacesWithBottom.get(patternCoordinates).add(placeCoordinates.Col);
                        break;
                    case PLACE_WITH_TOP:
                        if (!pattern.IsVerticallyExpendable) continue;
                        coordinatesPlacesWithTop.get(patternCoordinates).add(placeCoordinates.Col);
                        break;
                    default:
                        break;
                }
            }
        }

        for (Map.Entry<CoordinatesModel, List<Integer>> placesWithTopEntry : coordinatesPlacesWithTop.entrySet()) {
            CoordinatesModel patternCoordinates = placesWithTopEntry.getKey();
            CoordinatesModel patternTopCoordinates = new CoordinatesModel(patternCoordinates.Row - 1, patternCoordinates.Col);
            if (!coordinatesPlacesWithBottom.containsKey(patternTopCoordinates)) continue;

            List<Integer> placesWithBottom = coordinatesPlacesWithBottom.get(patternTopCoordinates);
            for (int placeCol : placesWithTopEntry.getValue()) {
                if (placesWithBottom.contains(placeCol)) {
                    capacity++;
                }
            }
        }

        for (Map.Entry<CoordinatesModel, List<Integer>> placesWithLeftEntry : coordinatesPlacesWithLeft.entrySet()) {
            CoordinatesModel patternCoordinates = placesWithLeftEntry.getKey();
            CoordinatesModel patternLeftCoordinates = new CoordinatesModel(patternCoordinates.Row, patternCoordinates.Col - 1);
            if (!coordinatesPlacesWithRight.containsKey(patternLeftCoordinates)) continue;

            List<Integer> placesWithRight = coordinatesPlacesWithRight.get(patternLeftCoordinates);
            for (int placeRaw : placesWithLeftEntry.getValue()) {
                if (placesWithRight.contains(placeRaw)) {
                    capacity++;
                }
            }
        }
        return capacity;
    }
}
