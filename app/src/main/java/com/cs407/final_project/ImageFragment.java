package com.cs407.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class ImageFragment extends Fragment {
    private static final String ARG_IMAGE_RESOURCE = "imageResource";

    public static ImageFragment newInstance(int imageResource) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE_RESOURCE, imageResource);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image1, container, false);
        ImageView imageView = view.findViewById(R.id.autumn_pecan_pie);
        if (getArguments() != null) {
            int imageResource = getArguments().getInt(ARG_IMAGE_RESOURCE);
            imageView.setImageResource(imageResource);
        }
        return view;

        //navigate to anther page.
        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click, e.g., start another activity
                Intent intent = new Intent(getActivity(), AnotherActivity.class);
                startActivity(intent);
            }
        });*/
    }

}

