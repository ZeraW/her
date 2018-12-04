package com.digitalsigma.hobrasul.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.digitalsigma.hobrasul.Other.Constant;

import net.alhazmy13.catcho.library.Catcho;

/**
 * Created by AhmedAbouElFadle on 12/13/2016.
 */
public class DialActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Catcho.Builder(this)
                .activity(ContactActivity.class)
                .build();

        if (Constant.networkName.equals("vod"))
        {
            String s = "";
            String code="*055*"+Constant.ussd+"#";

            callToneOperator(code);

        }else if (Constant.networkName.equals("or"))
        {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + 9999));
            intent.putExtra("sms_body", Constant.ussd);
            startActivity(intent);



        }
        else if (Constant.networkName.equals("et"))
        {
            String code="15*"+Constant.ussd+"#";
            callToneOperator(code);
        }



    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public void callToneOperator(String ussd)

    {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);




            callIntent.setData(ussdToCallableUri(ussd));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(callIntent);
        }catch (Exception e)

        {
            Toast.makeText(DialActivity.this, "error", Toast.LENGTH_SHORT).show();
        }

    }


    //*15*--------#
    //vodafone  *055*------#
    private Uri ussdToCallableUri(String ussd) {

        String uriString = "";

        if(!ussd.startsWith("tel:"))
            uriString += "tel:";

        for(char c : ussd.toCharArray()) {

            if(c == '#')
                uriString += Uri.encode("#");
            else
                uriString += c;
        }

        return Uri.parse(uriString);
    }


    public static void sms()
    {

    }
}
