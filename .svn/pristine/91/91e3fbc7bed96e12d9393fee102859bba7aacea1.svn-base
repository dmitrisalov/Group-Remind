package com.example.dsalov2.simplenotifications;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReminderDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private int selectedReminderPosition;   //Position of reminder in the list.
    private Group group;
    private Reminder reminder;
    private DatabaseReference remindersReference;

    private TextView detailParagraphTextView;   //Details of the reminder.
    private TextView dateNumTextView;           //Date of reminder.
    private TextView timeNumTextView;           //Time of reminder.
    private Button deleteReminderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_details);

        deleteReminderButton = (Button) findViewById(R.id.deleteReminderButton);
        detailParagraphTextView = (TextView) findViewById(R.id.detailParagraphTextView);
        dateNumTextView = (TextView) findViewById(R.id.dateNumTextView);
        timeNumTextView = (TextView) findViewById(R.id.timeNumTextView);
        deleteReminderButton.setOnClickListener(this);

        //Creates a new Reminder object out of what was clicked on in the list in
        //GroupDetailsActivity. Also creates the group object.
        Intent intent = getIntent();
        group = (Group) intent.getSerializableExtra("group");
        reminder = (Reminder) intent.getSerializableExtra("selectedReminder");
        selectedReminderPosition = intent.getIntExtra("selectedReminderPosition", 0);

        remindersReference = FirebaseDatabase.getInstance().getReference().child("groups")
                .child(group.getFirebaseKey()).child("reminders");

        if (reminder.hasNotification()) {
            dateNumTextView.setText(reminder.getFormattedDate());
            timeNumTextView.setText(reminder.getFormattedTime());
        }
        else {
            dateNumTextView.setText("Notification not set.");
            timeNumTextView.setText("Notification not set.");
        }

        detailParagraphTextView.setText(reminder.getDetails());
    }

    @Override
    public void onClick(View view) {

        //Button for deleting the reminder.
        if (view == deleteReminderButton) {
            GroupDetailsActivity.removeFromRemindersList(selectedReminderPosition);

            //Removes the reminder from the database.
            remindersReference.child(reminder.getFirebaseKey()).removeValue();

            Toast.makeText(this, "Reminder deleted.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
