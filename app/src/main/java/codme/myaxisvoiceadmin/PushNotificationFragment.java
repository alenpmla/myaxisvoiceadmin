package codme.myaxisvoiceadmin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Alen on 27-Jun-16.
 */
public class PushNotificationFragment extends Fragment {
    EditText pushmsg;
    Button sendbtn;
    int msgId=2125454;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

  View rootview=  inflater.inflate(R.layout.fragment_pushnotify,container,false);
setHasOptionsMenu(true);
         pushmsg=(EditText)rootview.findViewById(R.id.pushmsg);
         sendbtn=(Button) rootview.findViewById(R.id.sendbtn);

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

System.out.println("sending msg");
                FirebaseMessaging fm = FirebaseMessaging.getInstance();
                fm.send(new RemoteMessage.Builder("567658990459" + "@gcm.googleapis.com")
                        .setMessageId(Integer.toString(msgId++))
                        .addData("my_message", "Hello World")
                        .addData("my_action","SAY_HELLO")
                        .build());

            }
        });


        return  rootview;



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pushnfrag, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.backpush) {


            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            HomeFragment homeFragment = new HomeFragment();
            fragmentTransaction.replace(R.id.container, homeFragment);
            fragmentTransaction.commit();

            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
