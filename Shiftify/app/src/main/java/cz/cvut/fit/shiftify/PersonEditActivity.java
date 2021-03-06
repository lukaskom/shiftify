package cz.cvut.fit.shiftify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import cz.cvut.fit.shiftify.data.managers.UserManager;
import cz.cvut.fit.shiftify.data.models.User;
import cz.cvut.fit.shiftify.helpers.Validator;

public class PersonEditActivity extends AppCompatActivity {
    private static final String LOG_TAG = "shiftify.personEditAct";

    private EditText firstname;
    private EditText surname;
    private EditText nickname;
    private EditText phone;
    private EditText email;
    private ImageView image;
    private User u;
    private UserManager userManager;

    // put this somewhere else
    String mCurrentPhotoPath;

    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //allows for back arrow in toolbar to be created
        setSupportActionBar(toolbar);

        // creates back arrow in toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        firstname = (EditText) findViewById(R.id.edit_text_firstname);
        surname = (EditText) findViewById(R.id.edit_text_surname);
        nickname = (EditText) findViewById(R.id.edit_text_nickname);
        phone = (EditText) findViewById(R.id.edit_text_phone);
        email = (EditText) findViewById(R.id.edit_text_email);
        image = (ImageView) findViewById(R.id.person_edit_image);


        userManager = new UserManager();

        long userId = getIntent().getLongExtra(PersonDetailActivity.USER_ID,-1);

        if(userId == -1){
            Log.e(LOG_TAG, "Nepodarilo se nacist UserId v PersonEditActivity.");
            this.finish();
        }

        try {
            u = userManager.user(userId);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Nepodarilo se nacist UserId v PersonEditActivity.");
            this.finish();
        }

        firstname.setText( u.getFirstName() );
        surname.setText( u.getSurname() );
        nickname.setText( u.getNickname() );
        phone.setText( u.getPhoneNumber() );
        email.setText( u.getEmail() );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done_item:
                personEditSave( this.findViewById(android.R.id.content) );
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    // checks whether user changed any data, if not, no write is performed
    private  boolean dataChanged(){
        return  !Objects.equals(u.getFirstName(), firstname.getText()) ||
                !Objects.equals(u.getSurname(), surname.getText()) ||
                !Objects.equals(u.getNickname(), nickname.getText()) ||
                !Objects.equals(u.getPhoneNumber(), phone.getText()) ||
                !Objects.equals(u.getEmail(), email.getText());
    }

    public void personEditSave(View view){

        // save data to DB

        if (!dataChanged()){
            this.finish();
        }

        u.setFirstName( firstname.getText().toString() );
        u.setSurname( surname.getText().toString() );
        u.setNickname( nickname.getText().toString() );

        if(phone.getText().toString().isEmpty())
            u.setPhoneNumber(null);
        else
            u.setPhoneNumber( phone.getText().toString() );

        if(email.getText().toString().isEmpty())
            u.setEmail(null);
        else
            u. setEmail( email.getText().toString() );

        if (!Validator.validateUserData(u, view, LOG_TAG, email, phone)) {
            return;
        }




        try {
            userManager.edit(u);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.finish();
    }

    public void personImageSelect(View view){

        // call methods for image selection

        Snackbar snack;
        snack = Snackbar.make(view, "Image selection feature coming soon!", Snackbar.LENGTH_SHORT);
        View snackBarView = snack.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#DD3A83FF"));
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(null, Typeface.BOLD);
        snack.show();


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
