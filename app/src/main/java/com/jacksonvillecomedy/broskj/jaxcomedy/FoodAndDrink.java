package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kyle on 12/29/2014.
 */
public class FoodAndDrink extends Activity {

    int screenWidth, screenHeight;
    FoodExpandableListAdapter foodAdapter;
    DrinksExpandableListAdapter drinksAdapter;
    ExpandableListView elvFoodView, elvDrinksView;
    List<FoodAndDrinkMenuItem> listDataHeader;
    HashMap<FoodAndDrinkMenuItem, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_and_drink);
        System.out.println("food and drinks created");

        declarations();
    }

    public void declarations() {
        ActivityManager manager = new ActivityManager(this);
        manager.manageActionBar(getActionBar());
        manager.scaleBackground((LinearLayout) findViewById(R.id.llFoodAndDrink), R.drawable.background);

        elvFoodView = (ExpandableListView) findViewById(R.id.elvFood);
        elvDrinksView = (ExpandableListView) findViewById(R.id.elvDrinks);

        prepareFoodListData();
        foodAdapter = new FoodExpandableListAdapter(this, listDataHeader, listDataChild);
        elvFoodView.setAdapter(foodAdapter);

        elvFoodView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < elvFoodView.getExpandableListAdapter().getGroupCount(); i++) {
                    if (i != groupPosition)
                        elvFoodView.collapseGroup(i);
                }
            }
        });

        prepareDrinksListData();
        drinksAdapter = new DrinksExpandableListAdapter(this, listDataHeader, listDataChild);
        elvDrinksView.setAdapter(drinksAdapter);

        elvDrinksView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < elvDrinksView.getExpandableListAdapter().getGroupCount(); i++) {
                    if (i != groupPosition)
                        elvDrinksView.collapseGroup(i);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        enables home button in actionbar
         */
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    } //end onOptionsItemSelected

    private void prepareFoodListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        List<String>[] list = (ArrayList<String>[]) new ArrayList[7];
        String[] headers = {"Appetizers", "Salads", "Shtick Entrees", "Additional", "Bun Entrees",
                "Papa Murphy's Pizza", "Dessert Cakes"};
        String[] subheaders = {"", "Includes dinner roll", "Served with house salad and roll", "Served with fries (substitute onion rings $1)",
                "Served with fries (substitute onion rings $1)", "They make it, we bake it", "From Cinnoti's Bakery"};
        String[] children = {getResources().getString(R.string.appetizers),
                getResources().getString(R.string.salads),
                getResources().getString(R.string.shtick_entrees),
                getResources().getString(R.string.additional),
                getResources().getString(R.string.bun_entrees),
                getResources().getString(R.string.pizza),
                getResources().getString(R.string.cakes)};

        for (int i = 0; i < list.length; i++) {

            list[i] = new ArrayList<>();
            listDataHeader.add(new FoodAndDrinkMenuItem(headers[i], subheaders[i]));
            list[i].add(children[i]);
            listDataChild.put(listDataHeader.get(i), list[i]);
        }

    }//end prepareListData

    private void prepareDrinksListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        List<String>[] list = (ArrayList<String>[]) new ArrayList[10];
        String[] headers = {"Beer", "Wine", "Vodka", "Rum", "Gin", "Tequila", "Scotch", "Bourbon, " +
                "Blends, & Whiskey", "Cordials/Liqueurs", "Non-alcoholic"};
        String[] subheaders = {"Bottles or a bucket of 5", "Glasses or bottles", "", "", "", "", "", "", "", "No free refills"};
        String[] children = {getResources().getString(R.string.beer),
                getResources().getString(R.string.wine),
                getResources().getString(R.string.vodka),
                getResources().getString(R.string.rum),
                getResources().getString(R.string.gin),
                getResources().getString(R.string.tequila),
                getResources().getString(R.string.scotch),
                getResources().getString(R.string.bourbons_blends_whiskeys),
                getResources().getString(R.string.cordials_liqueurs),
                getResources().getString(R.string.non_alcoholic)};

        for (int i = 0; i < list.length; i++) {

            list[i] = new ArrayList<>();
            listDataHeader.add(new FoodAndDrinkMenuItem(headers[i], subheaders[i]));
            list[i].add(children[i]);
            listDataChild.put(listDataHeader.get(i), list[i]);
        }

    }//end prepareListData
}