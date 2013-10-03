package com.pincelancer.readcontactslist09;

//import com.pincelancer.readcontactslist06.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public TextView outputText;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        outputText = (TextView) findViewById(R.id.textView1);
        readContacts();
    }
 
    public void readContacts(){
         ContentResolver cr = getContentResolver();
         
         StringBuffer output = new StringBuffer();
         
         Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
 
         if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                	output.append("\n First Name:" + name + " ID: " + id);
                    //System.out.println("name : " + name + ", ID : " + id);
 
                    // get the phone number
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                           ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                           new String[]{id}, null);
                    while (pCur.moveToNext()) {
                          String phone = pCur.getString(
                                 pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                          //System.out.println("phone" + phone);
                          output.append("\n Phone number:" + phone);
                    }
                    pCur.close();
 
 
                    // get email and type
 
                   Cursor emailCur = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCur.moveToNext()) {
                        // This would allow you get several email addresses
                            // if the email addresses were stored in an array
                        String email = emailCur.getString(
                                      emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        String emailType = emailCur.getString(
                                      emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
 
                      //output.append("\nEmail:" + email + " Email Type : " + emailType);
                      //System.out.println("Email " + email + " Email Type : " + emailType);
                      output.append("\nEmail:" + email );
                        
                    }
                    emailCur.close();
                    
                    
                }
                output.append("\n");
            }
            outputText.setText(output);
       }
    }
 
}