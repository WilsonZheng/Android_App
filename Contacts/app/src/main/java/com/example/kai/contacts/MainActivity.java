package com.example.kai.contacts;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private View dialogView;
    private Button btnAdd;
    private EditText edName;
    private EditText edPhone;
    private ListView conlistview;

    // Request code for READ_CONTACTS & WRITE_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PERMISSIONS_REQUEST_WRITE_CONTACTS = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                LayoutInflater inflater = MainActivity.this.getLayoutInflater();

                builder.setTitle(R.string.addContact);

                dialogView = inflater.inflate(R.layout.dialog_addcontract, null);
                builder.setView(dialogView);

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        addContact();

                        displayContacts();
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        displayContacts();
    }



    public void displayContacts() {

        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            Uri URI = Uri.parse("content://com.android.contacts/contacts");

            ContentResolver resolver = getContentResolver();

            Cursor cursor = resolver.query(URI, null, null, null, null);

            conlistview = (ListView) findViewById(R.id.list);

            ArrayList<ContactItem> contactItems = new ArrayList<>();

            while (cursor.moveToNext()) {

                ContactItem contactItem = new ContactItem();

                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contactItem.setName(name);

                Cursor cNumber = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);


                while (cNumber.moveToNext()) {

                    String number = cNumber.getString(cNumber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    if (!TextUtils.isEmpty(number)) {
                        contactItem.setPhoneNumer(number);
                        break;
                    }

                }

                contactItems.add(contactItem);

                if (cNumber != null) {

                    cNumber.close();
                }
            }

            ContactAdapter contactAdapter = new ContactAdapter(this, contactItems);

            conlistview.setAdapter(contactAdapter);

            conlistview.setOnItemClickListener(ListViewItemClickLister);

            if (cursor != null) {

                cursor.close();
            }
        }
    }

    private void addContact() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, PERMISSIONS_REQUEST_WRITE_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            edName = (EditText) dialogView.findViewById(R.id.edName);
            edPhone = (EditText) dialogView.findViewById(R.id.edPhone);

            String strEdName = edName.getText().toString();
            String strEdPhone = edPhone.getText().toString();

            if (TextUtils.isEmpty(strEdName)) {

                Toast.makeText(MainActivity.this,R.string.inputName, Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(strEdPhone)) {

                Toast.makeText(MainActivity.this,R.string.inputPhoneNumber, Toast.LENGTH_SHORT).show();
                return;
            }


            Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
            ContentResolver resolver = getContentResolver();

            ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
            ContentProviderOperation op1 = ContentProviderOperation.newInsert(uri)
                    .withValue("account_name", null)
                    .build();
            operations.add(op1);

            uri = Uri.parse("content://com.android.contacts/data");

            ContentProviderOperation opName = ContentProviderOperation.newInsert(uri)
                    .withValueBackReference("raw_contact_id", 0)
                    .withValue("mimetype", "vnd.android.cursor.item/name")
                    .withValue("data1", strEdName)
                    .build();
            operations.add(opName);

            ContentProviderOperation opPhone = ContentProviderOperation.newInsert(uri)
                    .withValueBackReference("raw_contact_id", 0)
                    .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                    .withValue("data1", strEdPhone)
                    .build();
            operations.add(opPhone);

            try {
                resolver.applyBatch("com.android.contacts", operations);

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                displayContacts();
            } else {
                Toast.makeText(this, "Read_Contacts permission not granted.", Toast.LENGTH_SHORT).show();
            }
        }


        if (requestCode == PERMISSIONS_REQUEST_WRITE_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //addContact();
            } else {
                Toast.makeText(this, "Write_Contacts permission not granted.", Toast.LENGTH_SHORT).show();
            }
        }



    }

    private AdapterView.OnItemClickListener ListViewItemClickLister =
            new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final ContactItem contactItem = (ContactItem)parent.getAdapter().getItem(position);
                    final String phoneNumer = contactItem.getPhoneNumer();

                    String[] choices = getResources().getStringArray(R.array.choices_array);

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle(R.string.choice)
                            .setItems(choices, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {

                                        Intent intent = new Intent();
                                        intent.setAction(intent.ACTION_DIAL);
                                        intent.setData(Uri.parse("tel:" + phoneNumer));
                                        startActivity(intent);
                                    } else {

                                        Intent smsIntent = new Intent();
                                        smsIntent.setAction(smsIntent.ACTION_SENDTO);
                                        smsIntent.setData(Uri.parse("smsto:" + phoneNumer));
                                        smsIntent.putExtra("sms_body", R.string.deflautMessage);
                                        startActivity(smsIntent);
                                    }
                                }
                            });

                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            };

}