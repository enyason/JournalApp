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
import android.widget.TextView;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by enyason on 6/27/18.
 */

public class FragmentMore extends Fragment {


    LinearLayout layoutSync;

    LinearLayout layoutLogOut;

    ProgressDialog dialogSync;

    FirebaseFirestore firebaseFirestore;

    AppDataBase appDataBase;
    List<Journal> journalList;


    FirebaseAuth firebaseAuth;

    FirebaseUser user;
    FirebaseAuth.AuthStateListener authStateListener;


    ImageView imageViewUserImage;
    TextView textViewUserName;


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

        layoutSync = view.findViewById(R.id.layout_sync);
        layoutLogOut = view.findViewById(R.id.layout_log_out);
        dialogSync = new ProgressDialog(getActivity());

        imageViewUserImage = view.findViewById(R.id.profile_image);
        textViewUserName = view.findViewById(R.id.textView_user_profile_name);


        user = firebaseAuth.getCurrentUser();

        if (user != null) {
            String name = user.getDisplayName();
            Uri url = user.getPhotoUrl();

            textViewUserName.setText(name);

//            Picasso.get().load(url).fit().centerCrop().into(imageViewUserImage);


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


        layoutSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (journalList == null) {
                    return;
                }

                dialogSync.show();


                for (int i = 0; i < journalList.size(); i++) {


                    dialogSync.setMessage("Syncing With Cloud " + i + 1 + " of " + journalList.size() + 1);

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

//                                    dialogSync.dismiss();
//                                    if (task.isSuccessful()) {
//
//                                        Snackbar.make(v, "Sync Complete", Snackbar.LENGTH_LONG).show();
//                                    } else {
//
//                                        Snackbar.make(v, "Sync failed", Snackbar.LENGTH_LONG).show();
//
//
//                                    }

                                }
                            });


                }

                dialogSync.dismiss();


            }
        });

    }


}
