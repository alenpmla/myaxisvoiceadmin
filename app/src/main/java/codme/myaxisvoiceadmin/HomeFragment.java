package codme.myaxisvoiceadmin;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

/**
 * Created by Alen John Abraham on 26-06-2016.
 */
public class HomeFragment extends android.app.Fragment {

    ListView userslist;
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.fragment_home,container,false);
        Firebase.setAndroidContext(getActivity());
        mAuth = FirebaseAuth.getInstance();
        setHasOptionsMenu(true);
        userslist=(ListView)rootview.findViewById(R.id.userslist);
        Firebase firebaseref= new Firebase("https://project-3363810000149996090.firebaseio.com/users");

        final FirebaseListAdapter<UserClass> mAdapter = new FirebaseListAdapter<UserClass>(getActivity(), UserClass.class, R.layout.single_user_item, firebaseref) {

            @Override
            protected void populateView(View view, UserClass userClass, int i) {


                TextView nametv=(TextView)view.findViewById(R.id.nametv) ;
                TextView emailtv=(TextView)view.findViewById(R.id.emailtv) ;
                ImageView userimg=(ImageView)view.findViewById(R.id.userimg) ;


                nametv.setText(userClass.getDisplay_name());
                emailtv.setText(userClass.getEmail());

                try{
                    Picasso.with(getActivity().getApplicationContext()).load(userClass.getImg_url()).into(userimg);
                }catch (NullPointerException ne){
                    ne.printStackTrace();
                }



            }


        };
        userslist.setAdapter(mAdapter);


        userslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

             Firebase firebase= mAdapter.getRef(position);

                System.out.println("key is"+firebase.getKey());
                Intent intent=new Intent(getActivity(),EditUserDataActivity.class);
                intent.putExtra("uid",firebase.getKey());
                startActivity(intent);

            }
        });

        return rootview;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.homefrag, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addnew) {


            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            AddUserFragment addUserFragment = new AddUserFragment();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.container, addUserFragment);

            fragmentTransaction.commit();

            return true;
        }
        else  if(id == R.id.sendmsg)
        {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            PushNotificationFragment pushNotificationFragment = new PushNotificationFragment();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.container, pushNotificationFragment);

            fragmentTransaction.commit();



            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
