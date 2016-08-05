package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.enums.CavePlaceTypeEnum;
import com.myadridev.mypocketcave.models.CaveArrangementModel;
import com.myadridev.mypocketcave.models.CoordinatesModel;
import com.myadridev.mypocketcave.models.PatternModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Map<CoordinatesModel, Integer> nbRawsByPattern = new HashMap<>(caveArrangement.PatternMap.size());
        Map<CoordinatesModel, Integer> nbColsByPattern = new HashMap<>(caveArrangement.PatternMap.size());

        for (Map.Entry<CoordinatesModel, PatternModel> patternEntry : caveArrangement.PatternMap.entrySet()) {
            CoordinatesModel coordinates = patternEntry.getKey();
            PatternModel pattern = patternEntry.getValue();
            nbRawsByPattern.put(coordinates, pattern.IsVerticallyExpandable ? pattern.NumberRaws - 1 : pattern.NumberRaws);
            nbColsByPattern.put(coordinates, pattern.IsHorizontallyExpendable ? pattern.NumberCols - 1 : pattern.NumberCols);
        }

        for (Map.Entry<CoordinatesModel, PatternModel> patternEntry : caveArrangement.PatternMap.entrySet()) {
            CoordinatesModel coordinates = patternEntry.getKey();
            PatternModel pattern = patternEntry.getValue();
            int minRawPattern = 0;
            int maxRawPattern = pattern.NumberRaws - 1;
            int minColPattern = 0;
            int maxColPattern = pattern.NumberCols - 1;

            int minRaw = getMinRaw(nbRawsByPattern, coordinates);
            int minCol = getMinCol(nbColsByPattern, coordinates);

            for (Map.Entry<CoordinatesModel, CavePlaceTypeEnum> placeTypeEntry : pattern.PlaceMap.entrySet()) {
                computeAndPutPlaceTypeInMap(placeMap, minRawPattern, maxRawPattern, minColPattern, maxColPattern, minRaw, minCol, placeTypeEntry);
            }
        }

        return placeMap;
    }

    private void computeAndPutPlaceTypeInMap(Map<CoordinatesModel, CavePlaceTypeEnum> placeMap, int minRawPattern, int maxRawPattern, int minColPattern, int maxColPattern, int minRaw, int minCol, Map.Entry<CoordinatesModel, CavePlaceTypeEnum> placeTypeEntry) {
        CoordinatesModel coordinatesInPattern = placeTypeEntry.getKey();
        CavePlaceTypeEnum placeType = placeTypeEntry.getValue();

        CoordinatesModel placeCoordinates = new CoordinatesModel(coordinatesInPattern.Raw + minRaw, coordinatesInPattern.Col + minCol);
        CavePlaceTypeEnum place = placeType;

        if (coordinatesInPattern.Raw == minRawPattern) {
            if (placeMap.containsKey(placeCoordinates)) {
                CavePlaceTypeEnum alreadyPlacedType = placeMap.get(placeCoordinates);
                if (alreadyPlacedType == CavePlaceTypeEnum.PLACE_WITH_BOTTOM && placeType == CavePlaceTypeEnum.PLACE_WITH_TOP) {
                    place = CavePlaceTypeEnum.PLACE;
                }
            }
        }
        if (coordinatesInPattern.Raw == maxRawPattern) {
            if (placeMap.containsKey(placeCoordinates)) {
                CavePlaceTypeEnum alreadyPlacedType = placeMap.get(placeCoordinates);
                if (alreadyPlacedType == CavePlaceTypeEnum.PLACE_WITH_TOP && placeType == CavePlaceTypeEnum.PLACE_WITH_BOTTOM) {
                    place = CavePlaceTypeEnum.PLACE;
                }
            }
        }
        if (coordinatesInPattern.Col == minColPattern) {
            if (placeMap.containsKey(placeCoordinates)) {
                CavePlaceTypeEnum alreadyPlacedType = placeMap.get(placeCoordinates);
                if (alreadyPlacedType == CavePlaceTypeEnum.PLACE_WITH_RIGHT && placeType == CavePlaceTypeEnum.PLACE_WITH_LEFT) {
                    place = CavePlaceTypeEnum.PLACE;
                }
            }
        }
        if (coordinatesInPattern.Col == maxColPattern) {
            if (placeMap.containsKey(placeCoordinates)) {
                CavePlaceTypeEnum alreadyPlacedType = placeMap.get(placeCoordinates);
                if (alreadyPlacedType == CavePlaceTypeEnum.PLACE_WITH_LEFT && placeType == CavePlaceTypeEnum.PLACE_WITH_RIGHT) {
                    place = CavePlaceTypeEnum.PLACE;
                }
            }
        }
        placeMap.put(placeCoordinates, place);
    }

    private int getMinCol(Map<CoordinatesModel, Integer> nbColsByPattern, CoordinatesModel coordinates) {
        int minCol = 0;
        for (Map.Entry<CoordinatesModel, Integer> nbColsEntry : nbColsByPattern.entrySet()) {
            if (nbColsEntry.getKey().Col < coordinates.Col && nbColsEntry.getKey().Raw == coordinates.Raw) {
                minCol += nbColsEntry.getValue();
            }
        }
        return minCol;
    }

    private int getMinRaw(Map<CoordinatesModel, Integer> nbRawsByPattern, CoordinatesModel coordinates) {
        int minRaw = 0;
        for (Map.Entry<CoordinatesModel, Integer> nbRawsEntry : nbRawsByPattern.entrySet()) {
            if (nbRawsEntry.getKey().Col == coordinates.Col && nbRawsEntry.getKey().Raw < coordinates.Raw) {
                minRaw += nbRawsEntry.getValue();
            }
        }
        return minRaw;
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
            if (pattern.IsVerticallyExpandable) {
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
                        coordinatesPlacesWithRight.get(patternCoordinates).add(placeCoordinates.Raw);
                        break;
                    case PLACE_WITH_LEFT:
                        if (!pattern.IsHorizontallyExpendable) continue;
                        coordinatesPlacesWithLeft.get(patternCoordinates).add(placeCoordinates.Raw);
                        break;
                    case PLACE_WITH_BOTTOM:
                        if (!pattern.IsVerticallyExpandable) continue;
                        coordinatesPlacesWithBottom.get(patternCoordinates).add(placeCoordinates.Col);
                        break;
                    case PLACE_WITH_TOP:
                        if (!pattern.IsVerticallyExpandable) continue;
                        coordinatesPlacesWithTop.get(patternCoordinates).add(placeCoordinates.Col);
                        break;
                    default:
                        break;
                }
            }
        }

        for (Map.Entry<CoordinatesModel, List<Integer>> placesWithTopEntry : coordinatesPlacesWithTop.entrySet()) {
            CoordinatesModel patternCoordinates = placesWithTopEntry.getKey();
            CoordinatesModel patternTopCoordinates = new CoordinatesModel(patternCoordinates.Raw - 1, patternCoordinates.Col);
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
            CoordinatesModel patternLeftCoordinates = new CoordinatesModel(patternCoordinates.Raw, patternCoordinates.Col - 1);
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
