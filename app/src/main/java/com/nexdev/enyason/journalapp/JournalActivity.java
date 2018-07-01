package com.nexdev.enyason.journalapp;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ViewAnimator;

import com.bumptech.glide.Glide;

import java.util.Date;

import static android.content.Intent.ACTION_PICK;
import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

public class JournalActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_REQUEST = 9391;
    private static final String KEY_IMAGE = "com.example.picasso:image";

    EditText editTextTitle, editTextDescription;

    Button btnSaveJournal;

    ImageView imageViewOpenGallery;
    ImageView imageViewDisplay;

    ViewAnimator animator;
    private String image = "";


    AppDataBase appDataBase;


    boolean isAdded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);


        appDataBase = AppDataBase.getsInstance(this);

        btnSaveJournal = findViewById(R.id.btn_save_journal);

        btnSaveJournal.setOnClickListener(this);

        editTextTitle = findViewById(R.id.et_title);
        editTextDescription = findViewById(R.id.et_description);

        imageViewOpenGallery = findViewById(R.id.image_view_select_image);
        imageViewDisplay = findViewById(R.id.image_view_display);

        imageViewOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent(ACTION_PICK, EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST);
            }
        });


        if (savedInstanceState != null) {
            image = savedInstanceState.getString(KEY_IMAGE);
            if (image != null) {
                loadImage();
            }
        }

    }

    private void loadImage() {

        // Index 1 is the progress bar. Show it while we're loading the image.
//        animator.setDisplayedChild(1);

        Glide.with(this).load(image).centerCrop().into(imageViewDisplay);


    }



    @Override
    public void onClick(final View v) {

        String title, description;


        Date date = new Date();

        title = editTextTitle.getText().toString().trim();
        description = editTextDescription.getText().toString().trim();


        if (isAdded) {
            Snackbar.make(v, "Journal Already Added!", Snackbar.LENGTH_LONG).show();
            return;
        }



//        if (TextUtils.isEmpty(title)) {
//
//            Snackbar.make(v, "Journal needs a tittle", Snackbar.LENGTH_LONG).show();
//            return;
//
//        } else if (TextUtils.isEmpty(description)) {
//            Snackbar.make(v, "Journal needs a description", Snackbar.LENGTH_LONG).show();
//            return;
//
//
//        } else {
//            Snackbar.make(v, "Journal can not be empty", Snackbar.LENGTH_LONG).show();
//            return;
//        }

        final Journal journal = new Journal(title, description, image,0, date);


        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                appDataBase.taskDao().insertJournal(journal);
                isAdded = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(v, "New Journal Added", Snackbar.LENGTH_LONG).show();
                    }
                });

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            // Always cancel the request here, this is safe to call even if the image has been loaded.
            // This ensures that the anonymous callback we have does not prevent the activity from
            // being garbage collected. It also prevents our callback from getting invoked even after the
            // activity has finished.
            Glide.clear(imageViewDisplay);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_IMAGE, image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            image = data.getData().toString();

            Log.i("Image", image);
            loadImage();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
