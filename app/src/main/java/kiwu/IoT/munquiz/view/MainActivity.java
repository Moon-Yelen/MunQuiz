package kiwu.IoT.munquiz.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import kiwu.IoT.munquiz.R;

public class MainActivity extends AppCompatActivity {
    private LoginFragment loginFrag;
    private SeatingFragment seatFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        changeFragment(SEATING_FRAGMENT);
    }

    public static final int LOGIN_FRAGMENT = 0;
    public static final int SEATING_FRAGMENT = 1;

    public void changeFragment(int fragmentNum) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        switch(fragmentNum) {
            case LOGIN_FRAGMENT:
                ft = ft.replace(R.id.frmMain, loginFrag);
                ft.commitNow();
                break;
            case SEATING_FRAGMENT:
                ft = ft.replace(R.id.frmMain, seatFrag);
                ft.commitNow();
                break;
        } // end switch
    } // end changeFragment
}