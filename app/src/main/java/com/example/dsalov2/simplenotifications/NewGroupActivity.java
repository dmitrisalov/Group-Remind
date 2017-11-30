package com.example.dsalov2.simplenotifications;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * TODO Implement finding users/checking if they're valid.
 *
 * Created by dsalov2 on 04/30/2017.
 *
 * Variables and methods for creating a new group using a form.
 */
public class NewGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private static ArrayList<String> usersList = new ArrayList<>();

    private String nameOfGroup;
    private Group group;

    private Button addUserButton;
    private Button createGroupButton;
    private EditText userEditText;
    private EditText nameEditText;
    private ListView usersListView;
    private ArrayAdapter<String> usersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        addUserButton = (Button) findViewById(R.id.addUserButton);
        createGroupButton = (Button) findViewById(R.id.createGroupButton);
        userEditText = (EditText) findViewById(R.id.userEditText);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        addUserButton.setOnClickListener(this);
        createGroupButton.setOnClickListener(this);

        //Creates ListView for users and sets its adapter to groupsList.
        usersListView = (ListView) findViewById(R.id.usersListView);
        usersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usersList);
        usersListView.setAdapter(usersAdapter);
    }

    /**
     * Contains the methods to handle clicking on views.
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view == createGroupButton) {
            if (hasValidName()) {
                addGroupToListAndDatabase();
                finish();
            }
        }

        //Adds user to the list in MainActivity.
        if (view == addUserButton && !userEditText.getText().toString().isEmpty()) {
            addToUsersList(userEditText.getText().toString());
            usersAdapter.notifyDataSetChanged();
            userEditText.setText("");
        }
    }

    /**
     * Adds the name of the group to the list of groups in MainActivity.
     */
    private void addGroupToListAndDatabase() {
        Toast.makeText(this, "Group created.", Toast.LENGTH_SHORT).show();

        //Adds group to Firebase database.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("groups").push();

        nameOfGroup = nameEditText.getText().toString();
        group = new Group(nameOfGroup, databaseReference.getKey(), usersList);

        //Adds the group to the list in MainActivity.
        MainActivity.addToGroupsList(group);

        databaseReference.child("groupName").setValue(group.getName());

        //Adds each user to the database child of the group.
        for (String user : group.getUsersList()) {
            databaseReference.child("usersInGroup").push().child("email").setValue(user);
        }

        //Changes reference to point to the users tree.
        databaseReference = database.getReference("users");

        //Adds the group to each user in the database.
        for (String user : usersList) {
            databaseReference.orderByChild("email").equalTo(user)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference("users");

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                String userKey = childSnapshot.getKey();

                                //Adds the groupname to the user's tree.
                                reference.child(userKey).child("groups")
                                        .child(group.getFirebaseKey()).child("groupName")
                                        .setValue(group.getName());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(NewGroupActivity.this, "An error occurred.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /**
     * Checks to see if there is something entered as a group name.
     *
     * @return boolean.
     */
    private boolean hasValidName() {
        if (nameEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "You must provide a group name.", Toast.LENGTH_SHORT).show();

            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Adds a user to the top of the users list.
     *
     * @param user
     */
    private static void addToUsersList(String user) {
        usersList.add(0, user);
    }
}
