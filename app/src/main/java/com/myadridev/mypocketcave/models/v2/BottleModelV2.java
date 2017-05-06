package com.myadridev.mypocketcave.models.v2;

import android.graphics.Region;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.myadridev.mypocketcave.enums.v2.FoodToEatWithEnumV2;
import com.myadridev.mypocketcave.enums.v2.WineColorEnumV2;
import com.myadridev.mypocketcave.models.inferfaces.IBottleModel;
import com.myadridev.mypocketcave.models.inferfaces.IStorableModel;

import java.util.ArrayList;
import java.util.List;

public class BottleModelV2 implements IStorableModel, Comparable<BottleModelV2>, IBottleModel {

    @SerializedName("ftewl")
    public final List<FoodToEatWithEnumV2> FoodToEatWithList;
    @SerializedName("i")
    public int Id;
    @SerializedName("n")
    public String Name = "";
    @SerializedName("m")
    public int Millesime;
    @SerializedName("d")
    public String Domain = "";
    @SerializedName("c")
    public String Comments = "";
    @SerializedName("ptsw")
    public String PersonToShareWith = "";
    @SerializedName("wc")
    public WineColorEnumV2 WineColor;
    @SerializedName("s")
    public int Stock;
    @SerializedName("np")
    public int NumberPlaced;
    @SerializedName("r")
    public int Rating;
    @SerializedName("pr")
    public int PriceRating;
    @SerializedName("o")
    public boolean Organic = false;


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
        Organic = bottle.Organic;
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

    public int getId() {
        return Id;
    }

    public boolean isValid() {
        return WineColor != null && Domain != null && Name != null;
    }

    public void trimAll() {
        Name = Name.trim();
        Domain = Domain.trim();
        Comments = Comments.trim();
        PersonToShareWith = PersonToShareWith.trim();
    }
}
