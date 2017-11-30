package com.example.dsalov2.simplenotifications;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by dsalov2 on 4/10/2017.
 *
 * Methods and variables for displaying information about a group.
 */
public class GroupDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private final int REQUEST_CODE = 0;

    private static ArrayList<Reminder> remindersList = new ArrayList<>();

    private int selectedGroupPosition;
    private int selectedReminderPosition;
    private DatabaseReference reference;
    private Reminder newReminder;
    private Reminder selectedReminder;
    private Group group;

    private Button newReminderButton;
    private Button deleteGroupButton;
    private TextView groupNameTextView;
    private ListView remindersListView;
    private ArrayAdapter<Reminder> remindersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        groupNameTextView = (TextView) findViewById(R.id.groupNameTextView);
        newReminderButton = (Button) findViewById(R.id.newReminderButton);
        deleteGroupButton = (Button) findViewById(R.id.deleteGroupButton);
        newReminderButton.setOnClickListener(this);
        deleteGroupButton.setOnClickListener(this);

        //Creates a ListView and adapter, and links the adapter to the ListView.
        remindersListView = (ListView) findViewById(R.id.remindersListView);
        remindersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                remindersList);
        remindersListView.setAdapter(remindersAdapter);

        //When a list item is clicked, the details for the reminder are displayed.
        remindersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedReminderPosition = position;
                selectedReminder = remindersAdapter.getItem(position);

                Intent intent = new Intent(GroupDetailsActivity.this,
                        ReminderDetailsActivity.class);
                intent.putExtra("selectedReminder", selectedReminder);
                intent.putExtra("selectedReminderPosition", selectedReminderPosition);
                intent.putExtra("group", group);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        //Creates a new Group object out of what was clicked on in the list in MainActivity.
        Intent intent = getIntent();
        group = (Group) intent.getSerializableExtra("selectedGroup");
        selectedGroupPosition = intent.getIntExtra("selectedGroupPosition", 0);

        reference = FirebaseDatabase.getInstance().getReference();

        //Displays group information.
        groupNameTextView.setText(group.getName());

        //Adds any reminders that already exist.
        copyRemindersFromGroup();
    }

    /**
     * Removes the reminder from the list at the selected position.
     *
     * @param position
     */
    public static void removeFromRemindersList(int position) {
        remindersList.remove(position);
    }

    @Override
    public void onClick(View view) {

        //Button to create a new reminder.
        if (view == newReminderButton) {
            Intent intent = new Intent(GroupDetailsActivity.this, NewReminderActivity.class);
            intent.putExtra("group", group);
            startActivityForResult(intent, REQUEST_CODE);
        }

        //Button to delete the group.
        if (view == deleteGroupButton) {

            //Removes the group from the ListView in MainActivity.
            MainActivity.removeFromGroupsList(selectedGroupPosition);
            selectedGroupPosition--;

            //Fixes an issue that occurred when the current group was the last group in the list.
            if (MainActivity.getNumGroups() == 0) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
            }

            //Removes the group from the database.
            reference.child("groups").child(group.getFirebaseKey()).removeValue();
            Toast.makeText(this, "Group deleted.", Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    /**
     * Clears remindersList and adds each reminder from the group into it.
     */
    private void copyRemindersFromGroup() {
        remindersList.clear();

        for (Reminder reminder : group.getRemindersList()) {
            remindersList.add(reminder);
        }
    }

    /**
     * When a new reminder is created, it is added to the list and associated with the group.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            newReminder = (Reminder) data.getSerializableExtra("newReminder");
            remindersList.add(0, newReminder);
            group.addReminder(newReminder);

            //New intent to send the updated group back to MainActivity.
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedGroup", group);
            setResult(Activity.RESULT_OK, resultIntent);
        }
    }

    /**
     * Updates the remindersList when returning to GroupDetailsActivity.
     */
    @Override
    protected void onResume() {
        super.onResume();
        remindersAdapter.notifyDataSetChanged();
    }
}
