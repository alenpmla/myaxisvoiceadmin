package codme.myaxisvoiceadmin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alen John Abraham on 26-06-2016.
 */
public class AddUserFragment extends Fragment {
    private FirebaseAuth mAuth;
    EditText address,phonenumber,email,name,password,confirmpass;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_add_detail,container,false);
        mAuth = FirebaseAuth.getInstance();
        setHasOptionsMenu(true);
        address=(EditText)rootView.findViewById(R.id.address);
         phonenumber=(EditText)rootView.findViewById(R.id.phonenumber);
         email=(EditText)rootView.findViewById(R.id.email);
         name=(EditText)rootView.findViewById(R.id.name);
         password=(EditText)rootView.findViewById(R.id.password);
         confirmpass=(EditText)rootView.findViewById(R.id.confirmpass);





     return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.adduserfrag, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.adduser) {

            createuser(email.getText().toString(),password.getText().toString(),name.getText().toString(),phonenumber.getText().toString(),
                    address.getText().toString());



            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createuser(String email,String password,String name,String phonenumber,String address) {

        final String namesr=name;
        final String emailsr=email;
        final String phonenumbersr=phonenumber;
        final String addresssr=address;




        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("signupactivity", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Failed to create user",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "User Created",
                                    Toast.LENGTH_SHORT).show();




                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users");

                            Map<String,Object> map=new HashMap<String, Object>();
                            map.put("address",addresssr);
                            map.put("display_name",namesr);
                            map.put("email",emailsr);
                            map.put("img_url","img_url");
                            map.put("phone_number",phonenumbersr);
                            map.put("scroll_message","scroll_message");

                            myRef.child( task.getResult().getUser().getUid()).updateChildren(map);
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            HomeFragment homeFragment = new HomeFragment();
                            fragmentTransaction.replace(R.id.container, homeFragment);
                            fragmentTransaction.commit();








                        }

                        // ...
                    }
                });
    }
}
