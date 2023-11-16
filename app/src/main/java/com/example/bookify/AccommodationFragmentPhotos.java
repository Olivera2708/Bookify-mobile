package com.example.bookify;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationFragmentPhotos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationFragmentPhotos extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccommodationFragmentPhotos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccommodationFragmentPhotos.
     */
    // TODO: Rename and change types and number of parameters

    ActivityResultLauncher<Intent> launchSomeActivity;

    public static AccommodationFragmentPhotos newInstance(String param1, String param2) {
        AccommodationFragmentPhotos fragment = new AccommodationFragmentPhotos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        launchSomeActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        // do your operation from here....
                        ClipData clipData = data.getClipData();
                        if (data != null && clipData != null) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                Uri selectedImageUri = clipData.getItemAt(i).getUri();
                                setImage(selectedImageUri);
                            }
                        } else if(data != null && data.getData() != null){
                            Uri selectedImageUri = data.getData();
                            setImage(selectedImageUri);
                        }
                    }
                });
    }

    private void setImage(Uri selectedImageUri) {
        Bitmap selectedImageBitmap = null;
        try {
            selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().getContentResolver(),
                    selectedImageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LinearLayout ll = view.findViewById(R.id.photosLayout);
        ImageView imageView = new ImageView(requireActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ll.getWidth(), 500);
        imageView.setImageBitmap(selectedImageBitmap);
        imageView.setLayoutParams(params);

        ImageView deleteIcon = new ImageView(requireActivity());
        deleteIcon.setImageResource(R.drawable.clear);
        RelativeLayout.LayoutParams exitIconLayoutParams = new RelativeLayout.LayoutParams(100, 100);
        exitIconLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        exitIconLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        deleteIcon.setLayoutParams(exitIconLayoutParams);

        RelativeLayout containerLayout = new RelativeLayout(requireActivity());
        containerLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        ));

        containerLayout.addView(imageView);
        containerLayout.addView(deleteIcon);

        deleteIcon.setOnClickListener(v -> {
            ll.removeView(containerLayout);
        });

        ll.addView(containerLayout);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_accommodation_photos, container, false);
        Button upload = view.findViewById(R.id.btnUpload);

        upload.setOnClickListener(v -> {
            chooseImage();
        });

        return view;
    }

    private void chooseImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        launchSomeActivity.launch(i);
    }
}