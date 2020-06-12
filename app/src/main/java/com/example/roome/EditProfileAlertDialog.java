package com.example.roome;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This is a dialog themed activity for alerting in the EditProfileActivity when some fields are not
 * filled correctly.
 */
public class EditProfileAlertDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_edit_profile_alert_dialog);
    }

    /**
     * Closes the window when clicked ok or outside the window
     * @param okButton - The ok button in the dialog frame.
     */
    public void onClickOk(View okButton){
        finish();
    }
}
