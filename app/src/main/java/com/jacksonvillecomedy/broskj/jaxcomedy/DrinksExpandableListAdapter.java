package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Kyle on 2/18/2015.
 */
public class DrinksExpandableListAdapter extends BaseExpandableListAdapter{

    private Context _context;
    private List<FoodAndDrinkMenuItem> _listDataHeader;
    private HashMap<FoodAndDrinkMenuItem, List<String>> _listDataChild;

    public DrinksExpandableListAdapter(Context context, List<FoodAndDrinkMenuItem> listDataHeader,HashMap<FoodAndDrinkMenuItem, List<String>> listDataChild){
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listDataChild;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if(convertView == null){
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.drinks_elv_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.elvDrinksGroupText);
        txtListChild.setText(childText);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        FoodAndDrinkMenuItem item = (FoodAndDrinkMenuItem) getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.drinks_elv_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.elvDrinksGroupTitle);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(item.getHeader());
        TextView lblListSubheader = (TextView) convertView.findViewById(R.id.elvDrinksGroupSubtitle);
        lblListSubheader.setText(item.getSubheader());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
