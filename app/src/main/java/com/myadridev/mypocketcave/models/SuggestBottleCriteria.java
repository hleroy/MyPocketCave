package com.myadridev.mypocketcave.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.FoodToEatWithEnum;
import com.myadridev.mypocketcave.enums.MillesimeEnum;
import com.myadridev.mypocketcave.enums.WineColorEnum;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(as = SuggestBottleCriteria.class)
public class SuggestBottleCriteria {

    public static int NumberOfCriteria = 6;
    public final List<FoodToEatWithEnum> FoodToEatWithList;
    public WineColorEnum WineColor;
    public boolean IsWineColorRequired;
    public String Domain;
    public boolean IsDomainRequired;
    public MillesimeEnum Millesime;
    public boolean IsMillesimeRequired;
    public boolean IsFoodRequired;
    public String PersonToShareWith;
    public boolean IsPersonRequired;
    public CaveLightModel Cave;
    public boolean IsCaveRequired;

    public SuggestBottleCriteria() {
        WineColor = WineColorEnum.ANY;
        IsWineColorRequired = false;
        Domain = "";
        IsDomainRequired = false;
        Millesime = MillesimeEnum.ANY;
        IsMillesimeRequired = false;
        FoodToEatWithList = new ArrayList<>();
        IsFoodRequired = false;
        PersonToShareWith = "";
        IsPersonRequired = false;
        Cave = null;
        IsWineColorRequired = false;
    }
}
