package com.example.learnapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listTitle;
    private HashMap<String, String> listDetails;

    public ExpandableListAdapter(Context context, List<String> listTitle, HashMap<String, String> listDetails) {
        this.context = context;
        this.listTitle = listTitle;
        this.listDetails = listDetails;
    }


    @Override
    public int getGroupCount() {
        return this.listTitle.size();
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.listTitle.get(listPosition);
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.listDetails.get(listTitle.get(listPosition));
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);

        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_def_layout, null);
        }

        TextView defText = (TextView) convertView.findViewById(R.id.defText);
        defText.setText(listTitle);

        return convertView;
    }

    @Override
    public View getChildView(int listPosition, int expandedListPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String desc = (String) getChild(listPosition, expandedListPosition);

        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_desc_layout, null);
        }

        TextView descText = (TextView) convertView.findViewById(R.id.descText);
        descText.setText(desc);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int i1) {
        return true;
    }
}
