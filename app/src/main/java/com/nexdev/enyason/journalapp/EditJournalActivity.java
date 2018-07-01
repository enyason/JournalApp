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

import com.bumptech.glide.Glide;

import java.util.Date;

import static android.content.Intent.ACTION_PICK;
import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

public class EditJournalActivity extends AppCompatActivity {


    private static final int GALLERY_REQUEST = 9391;
    private static final String KEY_IMAGE = "com.example.picasso:image";

    private String image = "";

    int isFav;



    ImageView imageViewOpenGallery,imageViewDisplay,imageViewFav;


    EditText editTextTitle,editTextDescription;

    ImageView imageView;

    Button buttonUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journal);

        editTextTitle = findViewById(R.id.et_title_edit);
        editTextDescription = findViewById(R.id.et_description_edit);
        imageView = findViewById(R.id.image_view_display_edit);

        buttonUpdate = findViewById(R.id.btn_update_journal);

        imageViewOpenGallery = findViewById(R.id.image_view_select_image);
        imageViewDisplay = findViewById(R.id.image_view_display);
        imageView = findViewById(R.id.image_view_fav);


        Intent intent = getIntent();
         Journal journal = (Journal) intent.getSerializableExtra("journal");




        editTextTitle.setText(journal.getTitle());
        editTextDescription.setText(journal.getDescription());

        Glide.with(this).load(journal.getImage()).dontAnimate().into(imageView);


        if (journal.isFavourite() == 1) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_red));
            isFav = 1;
        } else {
            isFav = 0;
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFav == 0) {
                    isFav = 1;
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_red));
                } else if (isFav == 1) {
                    isFav = 0;
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));

                }


            }
        });


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {



                Date date = new Date();

               String title = editTextTitle.getText().toString().trim();
               String description = editTextDescription.getText().toString().trim();

               final Journal journalUpdate = new Journal(title,description,image,isFav,date);

                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {


                        AppDataBase.getsInstance(EditJournalActivity.this)
                                .taskDao()
                                .updateJournal(journalUpdate);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(v,"Update Successful ",Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });


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
