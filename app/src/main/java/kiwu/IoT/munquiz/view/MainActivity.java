package kiwu.IoT.munquiz.view;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;

import kiwu.IoT.munquiz.R;

public class MainActivity extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private static final int REQUEST_ENABLE_BT = 10;

    private LoginFragment loginFrag;
    private SeatingFragment seatFrag;

//    https://blog.naver.com/rhrkdfus/221397524547

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        try {
            if (bluetoothAdapter.isEnabled()) {
//                블루투스 디바이스 선택 함수(추후 작성) 호출
            } else {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivityForResult(intent, REQUEST_ENABLE_BT);
            }
        } catch (Exception e) {

        }

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