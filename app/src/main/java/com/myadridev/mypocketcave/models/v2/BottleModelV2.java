package com.myadridev.mypocketcave.models.v2;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.FoodToEatWithEnum;
import com.myadridev.mypocketcave.enums.WineColorEnum;
import com.myadridev.mypocketcave.models.IBottleModel;
import com.myadridev.mypocketcave.models.IStorableModel;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(as = BottleModelV2.class)
public class BottleModelV2 implements IStorableModel, Comparable<BottleModelV2>, IBottleModel {

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
    public int Rating;
    public int PriceRating;

    // Whisky, Rum, Liqueur : Age
    // Whisky : Type : Single Malt, Single Grain, Blended Malt, Blended Grain
    // Whisky, Rum, Beer : Pays
    // Whisky : Tourbé, Peu Tourbé, Pas Tourbé
    // Rum : Blanc, Vieux, Epicé
    // Cider : Brut, Doux
    // Beer : Type : Blanche, Blonde, Brune, Rousse, Ambrée, Stout, Weissbier
    // Remplissage de la bouteille

    // TODO : Apellation
    // TODO : conservation en fonction de couleur + millésime + apellation

    // TODO : année de consommation conseillée ou équivalent
    // TODO : photo

    public BottleModelV2() {
        FoodToEatWithList = new ArrayList<>();
    }

    public BottleModelV2(BottleModelV2 bottle) {
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
        Rating = bottle.Rating;
        PriceRating = bottle.PriceRating;
    }

    @Override
    public int compareTo(@NonNull BottleModelV2 otherBottle) {
        int compareColor = WineColor.Id - otherBottle.WineColor.Id;
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

    @JsonIgnore
    public void trimAll() {
        Name = Name.trim();
        Domain = Domain.trim();
        Comments = Comments.trim();
        PersonToShareWith = PersonToShareWith.trim();
    }
}
