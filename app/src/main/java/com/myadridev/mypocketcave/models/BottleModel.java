package com.myadridev.mypocketcave.models;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.FoodToEatWithEnum;
import com.myadridev.mypocketcave.enums.WineColorEnum;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(as = BottleModel.class)
public class BottleModel implements IStorableModel, Comparable<BottleModel> {

    @JsonProperty("ftewl")
    public final List<FoodToEatWithEnum> FoodToEatWithList;
    public int Id;
    public String Name;
    public int Millesime;
    public String Domain;
    public String Comments;
    @JsonProperty("ptsw")
    public String PersonToShareWith;
    @JsonProperty("wc")
    public WineColorEnum WineColor;
    public int Stock;
    public int NumberPlaced;
    // TODO : Apellation
    // TODO : conservation en fonction de couleur + millésime + apellation
    // TODO : notation (prestige + gout)

    //TODO : année de consommation conseillée ou équivalent
    //TODO : photo

    public BottleModel() {
        FoodToEatWithList = new ArrayList<>();
    }

    public BottleModel(BottleModel bottle) {
        Id = bottle.Id;
        Name = bottle.Name;
        Domain = bottle.Domain;
        Millesime = bottle.Millesime;
        Comments = bottle.Comments;
        PersonToShareWith = bottle.PersonToShareWith;
        WineColor = bottle.WineColor;
        FoodToEatWithList = new ArrayList<>(bottle.FoodToEatWithList);
        Stock = bottle.Stock;
        NumberPlaced = bottle.NumberPlaced;
    }

    @Override
    public int compareTo(@NonNull BottleModel otherBottle) {
        int compareColor = WineColor.id - otherBottle.WineColor.id;
        if (compareColor > 0)
            return 1;
        else if (compareColor < 0)
            return -1;
        else {
            int compareName = Domain.compareToIgnoreCase(otherBottle.Domain);
            if (compareName < 0) {
                return -1;
            } else if (compareName > 0) {
                return 1;
            } else {
                int compareDomaine = Name.compareToIgnoreCase(otherBottle.Name);
                if (compareDomaine < 0) {
                    return -1;
                } else if (compareDomaine > 0) {
                    return 1;
                } else {

                    int compareMillesime = Millesime - otherBottle.Millesime;
                    if (compareMillesime > 0)
                        return 1;
                    else if (compareMillesime < 0)
                        return -1;
                    return 0;
                }
            }
        }
    }

    @JsonIgnore
    public int getId() {
        return Id;
    }

    @JsonIgnore
    public boolean isValid() {
        return WineColor != null && Domain != null && Name != null;
    }
}
