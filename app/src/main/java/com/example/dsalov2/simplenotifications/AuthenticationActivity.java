package com.example.dsalov2.simplenotifications;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dsalov2 on 4/30/2017.
 *
 * Variables and methods required for setting up a new or existing user with a log-in screen.
 */
public class AuthenticationActivity extends AppCompatActivity implements View.OnClickListener {
    private User user;  //The user that is signing in; current user.

    private FirebaseAuth authenticator;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        signInButton = (Button) findViewById(R.id.signInButton);
        createAccountButton = (Button) findViewById(R.id.createAccountButton);
        signInButton.setOnClickListener(this);
        createAccountButton.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        authenticator = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    user = new User(emailEditText.getText().toString());
                    Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            }
        };

        //Requires a log-in each time the app opens.
        authenticator.signOut();

        //The password input type changes the font to monospace. This brings it back to normal.
        passwordEditText.setTypeface(emailEditText.getTypeface());
    }

    /**
     * Creates a new user account for them to sign in with in the future.
     *
     * @param email
     * @param password
     */
    private void createAccount(final String email, String password) {
        authenticator.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //Adds user to Firebase database.
                            String userEmail = emailEditText.getText().toString();
                            databaseReference = database.getReference("users");
                            databaseReference.push().child("email").setValue(userEmail);

                            Toast.makeText(AuthenticationActivity.this, "User account created.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(AuthenticationActivity.this,
                                    "Email is invalid or in use.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Signs in to an already-existing user account.
     *
     * @param email
     * @param password
     */
    private void signInAccount(String email, String password) {
        authenticator.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AuthenticationActivity.this, "User signed in.",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(AuthenticationActivity.this,
                                "The email or password is invalid.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    /**
     * Determines what happens when buttons are clicked.
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        //Button for signing in to an already-existing user account.
        if (view == signInButton) {
            signInAccount(email, password);
        }

        //Button for creating a new user account.
        if (view == createAccountButton) {
            createAccount(email, password);
        }
    }

    /**
     * Attaches the listener to the authenticator.
     */
    @Override
    public void onStart() {
        super.onStart();
        authenticator.addAuthStateListener(authStateListener);
    }

    /**
     * Removes the listener from the authenticator.
     */
    @Override
    public void onStop() {
        super.onStop();

        if (authStateListener != null) {
            authenticator.removeAuthStateListener(authStateListener);
        }
    }
}
