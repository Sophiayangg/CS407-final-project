package com.cs407.final_project;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listGroupTitles;  // Your group titles
    private HashMap<String, List<String>> listChildData;  // Your child data

    // New member variable for tracking checked states
    private HashMap<String, List<Boolean>> checkedStates;

    public CustomExpandableListAdapter(Context context, List<String> listGroupTitles,
                                       HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.listGroupTitles = listGroupTitles;
        this.listChildData = listChildData;
        initializeCheckedStates();
    }

    private void initializeCheckedStates() {
        checkedStates = new HashMap<>();
        for (String group : listGroupTitles) {
            List<Boolean> childCheckedStates = new ArrayList<>();
            List<String> children = listChildData.get(group);
            for (int i = 0; i < children.size(); i++) {
                childCheckedStates.add(false); // Initialize all as unchecked
            }
            checkedStates.put(group, childCheckedStates);
        }
    }

    @Override
    public int getGroupCount() {
        return this.listGroupTitles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listChildData.get(this.listGroupTitles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listGroupTitles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listChildData.get(this.listGroupTitles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String groupTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.group_item, null);
        }

        TextView tvGroupName = convertView.findViewById(R.id.tvGroupName);
        ImageView ivExpandToggle = convertView.findViewById(R.id.ivExpandToggle);

        tvGroupName.setText(groupTitle);

        // Set the state of the icon
        ivExpandToggle.setImageResource(isExpanded ? R.drawable.ic_expand_less : R.drawable.ic_expand_more);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.child_item, null);
        }

        CheckBox checkBoxChild = convertView.findViewById(R.id.checkBoxChild);
        checkBoxChild.setText(childText);
        checkBoxChild.setChecked(checkedStates.get(listGroupTitles.get(groupPosition)).get(childPosition));

        checkBoxChild.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedStates.get(listGroupTitles.get(groupPosition)).set(childPosition, isChecked);
            }
        });

        return convertView;
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void resetAllCheckBoxes() {
        for (String group : checkedStates.keySet()) {
            List<Boolean> childCheckedStates = checkedStates.get(group);
            for (int i = 0; i < childCheckedStates.size(); i++) {
                childCheckedStates.set(i, false);
            }
        }
        notifyDataSetChanged(); // Notify the adapter to refresh the views
    }

}

