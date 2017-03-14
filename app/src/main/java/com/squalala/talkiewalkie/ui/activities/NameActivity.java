/*
 * Copyright (C) 2017 Fayçal Kaddouri <powervlagos@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.squalala.talkiewalkie.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squalala.talkiewalkie.R;
import com.squalala.talkiewalkie.utils.BitmapUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

import static com.squalala.talkiewalkie.Constants.BUCKET_FIREBASE;

/**
 * Created by Fayçal KADDOURI on 11/02/17.
 */

public class NameActivity extends BaseActivity {

    @BindView(R.id.editUsername)
    EditText editUsername;

    @BindView(R.id.backdrop)
    SimpleDraweeView profileImage;

    @BindView(R.id.IndicatorView)
    AVLoadingIndicatorView progresView;

    public static final String KEY_MODIFICATION = "modification";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        ButterKnife.bind(this);

        Nammu.init(this);

        profileImage.setImageURI(session.getUrlAvatar());
        editUsername.setText(session.getUsername());


        boolean isModification = getIntent().getBooleanExtra(KEY_MODIFICATION, false);

        if (!isModification && !TextUtils.isEmpty(session.getUsername())) {
            navigateListRoomsActivity();
        }
    }

    @DebugLog
    @OnClick(R.id.backdrop)
    void selectImage() {

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {

                @DebugLog
                @Override
                public void permissionGranted() {
                    EasyImage.openGallery(NameActivity.this, 0);
                    //Nothing, this sample saves to Public gallery so it needs permission
                }

                @DebugLog
                @Override
                public void permissionRefused() {
                   // finish();
                }
            });
        }
        else {
            EasyImage.openGallery(NameActivity.this, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                e.printStackTrace();
            }

            @DebugLog
            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {

                if (imageFile != null) {

                    progresView.setVisibility(View.VISIBLE);
                    profileImage.setVisibility(View.GONE);

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReferenceFromUrl(BUCKET_FIREBASE);

                    StorageMetadata metadata = new StorageMetadata.Builder()
                            .setContentType("image/jpeg")
                            .build();

                 /*   BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);  */
                    Bitmap bitmap =  BitmapUtils.resizeBitmap(imageFile.getAbsolutePath(), 400, 400);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] data = baos.toByteArray();

                    StorageReference mountainImagesRef = storageRef.child("images/" + imageFile.getName());
                    UploadTask uploadTask = mountainImagesRef.putBytes(data);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            System.out.println("Upload is " + progress + "% done");
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            profileImage.setImageURI(downloadUrl);

                            progresView.setVisibility(View.GONE);
                            profileImage.setVisibility(View.VISIBLE);

                            session.setUrlAvatar(downloadUrl.toString());

                            Answers.getInstance().logCustom(new CustomEvent("Images"));

                            System.out.println(downloadUrl);
                        }
                    });


                }

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(NameActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        EasyImage.clearConfiguration(this);
        super.onDestroy();
    }

    private String getUsername() {
        return editUsername.getText().toString().trim();
    }

    private void navigateListRoomsActivity() {
        Intent intent = new Intent(this, ListRoomsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btnRecordUsername)
    void checkUsername() {
        if (!TextUtils.isEmpty(getUsername())) {
            session.setUsername(getUsername());
            navigateListRoomsActivity();
        }
    }
}
