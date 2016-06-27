package codme.myaxisvoiceadmin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditUserDataActivity extends AppCompatActivity {
EditText name,phonenumber,address,scrollmsg,rateurl,myofferurl;
    ImageView profileimg;
String uidfromintent;
    Button chngimgbtn;
    final int SELECT_PHOTO=202;
    Boolean imgchanged=false;
    Bitmap imgtouploadbm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_data);

        Intent intent=getIntent();
        uidfromintent=intent.getStringExtra("uid");

        name=(EditText)findViewById(R.id.name);
        phonenumber=(EditText)findViewById(R.id.phonenumber);
        address=(EditText)findViewById(R.id.address);
        scrollmsg=(EditText)findViewById(R.id.scrollmsg);
        rateurl=(EditText)findViewById(R.id.rateurl);
        myofferurl=(EditText)findViewById(R.id.myofferurl);

        profileimg=(ImageView)findViewById(R.id.profileimg);

        chngimgbtn=(Button)findViewById(R.id.chngimgbtn);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        DatabaseReference newref = myRef.child(uidfromintent);

        newref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserClass userClass=dataSnapshot.getValue(UserClass.class);
                name.setText(userClass.getDisplay_name());
                phonenumber.setText(userClass.getPhone_number());
                address.setText(userClass.getAddress());
                scrollmsg.setText(userClass.getScroll_message());
                rateurl.setText(userClass.getRateurl());
                myofferurl.setText(userClass.getMyofferurl());
                try{
                    Picasso.with(getApplicationContext()).load(userClass.getImg_url()).into(profileimg);
                }catch (NullPointerException ne){
                    ne.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        chngimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_PHOTO);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editusermenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save) {


System.out.println("imagechanged status"+imgchanged);
            if(imgchanged){
                uploadpic();
            }
            else {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users");
                DatabaseReference newref = myRef.child(uidfromintent);

                Map<String,Object>map=new HashMap<>();
                map.put("display_name",name.getText().toString());
                map.put("phone_number",phonenumber.getText().toString());
                map.put("address",address.getText().toString());
                map.put("scroll_message",scrollmsg.getText().toString());
                map.put("rateurl",rateurl.getText().toString());
                map.put("myofferurl",myofferurl.getText().toString());

                newref.updateChildren(map);

                finish();
            }









            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PHOTO)
                onSelectFromGalleryResult(data);

        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                imgchanged=true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            imgchanged=false;
        }
        profileimg.setImageBitmap(bm);
        imgtouploadbm=bm;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void uploadpic(){
        profileimg.setImageBitmap(imgtouploadbm);
        FirebaseUser usernew = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://project-3363810000149996090.appspot.com");
        StorageReference users = storageRef.child("users").child(usernew.getUid());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imgtouploadbm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataimg = baos.toByteArray();

        UploadTask uploadTask = users.putBytes(dataimg);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(getApplicationContext(),"upload success",Toast.LENGTH_SHORT).show();
                try{
                    Picasso.with(getApplicationContext().getApplicationContext()).load(downloadUrl.toString()).into(profileimg);
                }catch (NullPointerException ne){
                    ne.printStackTrace();
                }

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users");
                DatabaseReference newref = myRef.child(uidfromintent);

                Map<String,Object>map=new HashMap<>();
                map.put("display_name",name.getText().toString());
                map.put("img_url",downloadUrl.toString());
                map.put("phone_number",phonenumber.getText().toString());
                map.put("address",address.getText().toString());
                map.put("scroll_message",scrollmsg.getText().toString());
                map.put("rateurl",rateurl.getText().toString());
                map.put("myofferurl",myofferurl.getText().toString());

                newref.updateChildren(map);
                finish();
            }
        });


    }
}
