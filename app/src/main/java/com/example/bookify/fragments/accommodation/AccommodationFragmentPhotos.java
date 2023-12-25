package com.example.bookify.fragments.accommodation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.content.CursorLoader;

import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.bookify.R;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.fragments.MyFragment;
import com.example.bookify.model.ImageDTO;
import com.example.bookify.model.ImageMobileDTO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationFragmentPhotos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationFragmentPhotos extends MyFragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    AccommodationUpdateViewModel viewModel;

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
    List<MultipartBody.Part> imageParts = new ArrayList<>();

    List<ImageDTO> imageDTOS = new ArrayList<>();

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
                        } else if (data != null && data.getData() != null) {
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

        File file = new File(getRealPathFromURI(getActivity(), selectedImageUri));
        RequestBody requestFile = RequestBody.create(okhttp3.MultipartBody.FORM, file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("images", file.getName(), requestFile);
        imageParts.add(imagePart);

        deleteIcon.setOnClickListener(v -> {
            ll.removeView(containerLayout);
            imageParts.remove(imagePart);
            imageDTOS.removeIf(imageDTO -> imagePart.equals(imageDTO.getImagePart()));
        });

        imageDTOS.add(new ImageDTO(selectedImageBitmap, imagePart));

        ll.addView(containerLayout);
    }

//    Boolean load = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_accommodation_photos, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(AccommodationUpdateViewModel.class);

        Button upload = view.findViewById(R.id.btnUpload);

        upload.setOnClickListener(v -> {
            chooseImage();
        });

        if (viewModel.getIsEditMode().getValue()) {
            setImages();
            loadImages();
//            load = false;
        } else {
            loadImages();
        }

        return view;
    }

    private void loadImages() {
        for (ImageDTO imageDTO : imageDTOS) {

            LinearLayout ll = view.findViewById(R.id.photosLayout);
            ImageView imageView = new ImageView(requireActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(imageDTO.getBitmap());
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

            MultipartBody.Part imagePart;
            if (imageDTO.getImagePart() != null) {

                imagePart = imageDTO.getImagePart();
                if (!imageParts.contains(imagePart)) {
                    imageParts.add(imagePart);
                }
            }

            deleteIcon.setOnClickListener(v -> {
                ll.removeView(containerLayout);
                if (imageDTO.getImagePart() != null) {
                    imageParts.remove(imageDTO.getImagePart());
                } else {
                    ImageMobileDTO dto = viewModel.getStoredImages().getValue().stream().filter(obj -> imageDTO.getBitmap().equals(decodeBase64Image(obj.getData()))).findFirst().get();
                    Call<Long> call = ClientUtils.accommodationService.deleteImage(dto.getId());

                    call.enqueue(new Callback<Long>() {
                        @Override
                        public void onResponse(Call<Long> call, Response<Long> response) {

                        }

                        @Override
                        public void onFailure(Call<Long> call, Throwable t) {

                        }
                    });
                }
                imageDTOS.removeIf(image -> imageDTO.getBitmap().equals(image.getBitmap()));
            });

//            imageDTOS.add(new ImageDTO(selectedImageBitmap, imagePart));

            ll.addView(containerLayout);
        }
    }

    private Bitmap decodeBase64Image(String base64Image) {
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void setImages() {
        for (ImageMobileDTO image : viewModel.getStoredImages().getValue()) {

            Bitmap selectedImageBitmap = decodeBase64Image(image.getData());

            LinearLayout ll = view.findViewById(R.id.photosLayout);
            ImageView imageView = new ImageView(requireActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
//                imageDTOS.removeIf(imageDTO -> selectedImageBitmap.equals(imageDTO.getBitmap()));

                viewModel.getStoredImages().getValue().remove(image);

                Call<Long> call = ClientUtils.accommodationService.deleteImage(image.getId());

                call.enqueue(new Callback<Long>() {
                    @Override
                    public void onResponse(Call<Long> call, Response<Long> response) {

                    }

                    @Override
                    public void onFailure(Call<Long> call, Throwable t) {

                    }
                });
            });

//            imageDTOS.add(new ImageDTO(selectedImageBitmap, null));

            ll.addView(containerLayout);
        }
    }

    private void chooseImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        launchSomeActivity.launch(i);
    }

    @Override
    public int isValid() {
        if (viewModel.getStoredImages().getValue() != null) {
            if (imageDTOS.size() + viewModel.getStoredImages().getValue().size() == 0) {
                return 3;
            }
        }else{
            if (imageDTOS.size() == 0) {
                return 3;
            }
        }
        viewModel.setImages(imageParts);
        return 0;
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI(Context context, Uri uri) {
        String filePath = "";

        // SDK < API11
        if (Build.VERSION.SDK_INT < 11) {
            filePath = getRealPathFromURIBelowAPI11(context, uri);
        }
        // SDK >= 11 && SDK < 19
        else if (Build.VERSION.SDK_INT < 19) {
            filePath = getRealPathFromURIAPI11to18(context, uri);
        }
        // SDK >= 19 (Android 4.4)
        else {
            filePath = getRealPathFromURIAPI19(context, uri);
        }

        return filePath;
    }

    private static String getRealPathFromURIBelowAPI11(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @SuppressLint("NewApi")
    private static String getRealPathFromURIAPI11to18(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            cursor.close();
        }
        return result;
    }

    @SuppressLint("NewApi")
    private static String getRealPathFromURIAPI19(Context context, Uri uri) {
        String filePath = "";

        if (DocumentsContract.isDocumentUri(context, uri)) {
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isExternalStorageDocument(uri)) {
                String[] split = documentId.split(":");
                String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return context.getExternalFilesDir(null) + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(documentId));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                String[] split = documentId.split(":");
                String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                String selection = "_id=?";
                String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else {
            return getDataColumn(context, uri, null, null);
        }

        return filePath;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(columnIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}