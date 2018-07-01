package com.nexdev.enyason.journalapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by enyason on 6/27/18.
 */

public class FragmentMore extends Fragment {


    LinearLayout layoutUpload, layoutLogOut, layoutDelete, layoutRetrieve;


    ProgressDialog dialogSync;

    FirebaseFirestore firebaseFirestore;

    AppDataBase appDataBase;
    List<Journal> journalList;
    List<Journal> getJournalListCloud;


    FirebaseAuth firebaseAuth;

    FirebaseUser user;
    FirebaseAuth.AuthStateListener authStateListener;


    ImageView imageViewUserImage;
    TextView textViewUserName, textViewUserEmail;

    ProgressBar pbUpload, pbRetrive;


    int count = 0;
    private GoogleSignInClient mGoogleSignInClient;


    public FragmentMore() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        firebaseFirestore = FirebaseFirestore.getInstance();
        appDataBase = AppDataBase.getsInstance(getActivity());

        firebaseAuth = FirebaseAuth.getInstance();

        View view = inflater.inflate(R.layout.fragment_more, container, false);

        return view;

//        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();

//        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutUpload = view.findViewById(R.id.layout_sync);
        layoutLogOut = view.findViewById(R.id.layout_log_out);
        dialogSync = new ProgressDialog(getActivity());
        layoutDelete = view.findViewById(R.id.layout_delete);

        layoutRetrieve = view.findViewById(R.id.layout_download);

        imageViewUserImage = view.findViewById(R.id.profile_image);
        textViewUserName = view.findViewById(R.id.textView_user_profile_name);
        textViewUserEmail = view.findViewById(R.id.textView_user_email);

        pbRetrive = view.findViewById(R.id.pb_retrieve);
        pbUpload = view.findViewById(R.id.pb_download);


        user = firebaseAuth.getCurrentUser();

        if (user != null) {
            String name = user.getDisplayName();
            Uri url = user.getPhotoUrl();
            String email = user.getEmail();

            textViewUserName.setText(name);
            textViewUserEmail.setText(email);

            Glide.with(this).load(url).dontAnimate().placeholder(R.drawable.ic_person).into(imageViewUserImage);


        }


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//                if (firebaseAuth.getCurrentUser() == null) {
//                    startActivity(new Intent(getContext(),SignInActivity.class));
//                    getActivity().finish();
//                }
//            }
//        };

        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                journalList = appDataBase.taskDao().loadAlll();
                Log.i("List size", "" + journalList.size());

            }
        });


        layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                dialogSync.setMessage("Deleting Journals...");
                dialogSync.show();

                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        appDataBase.taskDao().deleteAllJournal();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialogSync.dismiss();
                                Snackbar.make(v, "All Entries Deleted", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });


        layoutLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();

                // Google sign out
                mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(),
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

//                                updateUI(null);

                                startActivity(new Intent(getContext(), SignInActivity.class));
                                getActivity().finish();

                            }
                        });

            }
        });


        layoutRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                getJournalListCloud = new ArrayList<>();

                pbRetrive.setVisibility(View.VISIBLE);


                firebaseFirestore.collection("Journal").document("users")
                        .collection("enyasonjnr@gmail.com")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {


                            for (DocumentSnapshot documentSnapshot : task.getResult()) {

                                final String title = documentSnapshot.getString("title");
                                final String description = documentSnapshot.getString("description");
                                final String imageUri = documentSnapshot.getString("image");


                                AppExecutor.getInstance().diskIO()
                                        .execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                AppDataBase.getsInstance(getActivity()).taskDao()
                                                        .insertJournal(new Journal(title, description, imageUri, 1, new Date()));

                                            }
                                        });
//                                getJournalListCloud.add());

                            }
                            pbRetrive.setVisibility(View.INVISIBLE);

                            Snackbar.make(v, "Download Complete", Snackbar.LENGTH_SHORT).show();


                        } else {


                        }


                    }
                });


            }
        });


        layoutUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (journalList == null) {
                    return;
                }

                pbUpload.setVisibility(View.VISIBLE);
//                dialogSync.show();



                    for (int i = 0; i < journalList.size(); i++) {

                        Journal journal = journalList.get(i);
                        Map<String, Object> objectMap = new HashMap<>();

                        objectMap.put("title", journal.getTitle());
                        objectMap.put("description", journal.getDescription());
                        objectMap.put("image", journal.getImage());


                        firebaseFirestore.collection("Journal").document("users")
                                .collection("enyasonjnr@gmail.com").add(objectMap)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                        count++;

                                        if (task.isSuccessful()) {


                                            if (count == journalList.size()) {
                                                pbUpload.setVisibility(View.INVISIBLE);
////
                                                Snackbar.make(v, "Upload Complete", Snackbar.LENGTH_SHORT).show();
//
                                            }
                                        }

//

                                    }
                                });


//


                    }

//



            }
        });

    }


}
