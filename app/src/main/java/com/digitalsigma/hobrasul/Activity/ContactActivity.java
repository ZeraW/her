package com.digitalsigma.hobrasul.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.digitalsigma.hobrasul.R;

import net.alhazmy13.catcho.library.Catcho;
import net.alhazmy13.catcho.library.error.CatchoError;


/**
 * Created by ahmed on 3/13/2017.
 */

public class ContactActivity extends AppCompatActivity {

    Button buttonSend;
    EditText textTo;
    EditText textSubject;
    EditText textMessage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_us);


        getSupportActionBar().setTitle("اتصل بنا");
        final CatchoError error = (CatchoError) getIntent().getSerializableExtra(Catcho.ERROR);



        buttonSend = (Button) findViewById(R.id.buttonSend);
        //textTo = (EditText) findViewById(R.id.editTextTo);
       // textSubject = (EditText) findViewById(R.id.editTextSubject);
        textMessage = (EditText) findViewById(R.id.editTextMessage);

        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               // String subject = textSubject.getText().toString();
                String message = error.toString();

                Intent email = new Intent(Intent.ACTION_SEND);

                //--------------------------------------------

                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"m.fathy@gmsproduction.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Elmoled Crashes");
                email.putExtra(Intent.EXTRA_TEXT, message);
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                //-----------------------------------------------------------


            }
        });
    }
}