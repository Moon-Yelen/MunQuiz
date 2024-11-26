package kiwu.IoT.munquiz.view;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.StringDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import kiwu.IoT.munquiz.R;
import kiwu.IoT.munquiz.model.SensorDataModel;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private LoginFragment loginFrag;
    private SeatingFragment seatFrag;
    private WarningFragment warnFrag;

    // 블루투스 활성화 요청을 위한 ActivityResultLauncher
    private final ActivityResultLauncher<Intent> enableBluetoothLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Toast.makeText(this, "Bluetooth 활성화됨", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Bluetooth 활성화가 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            });

    // 권한 요청을 위한 ActivityResultLauncher
    private final ActivityResultLauncher<String[]> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                if (result.getOrDefault(Manifest.permission.BLUETOOTH_CONNECT, false)) {
                    checkAndEnableBluetooth();
                } else {
                    Toast.makeText(this, "Bluetooth 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeFragment(LOGIN_FRAGMENT); // 기본 화면을 LOGIN_FRAGMENT로 설정

        // 블루투스 권한 체크 및 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(new String[]{Manifest.permission.BLUETOOTH_CONNECT});
        } else {
            checkAndEnableBluetooth();
        }
    }

    private void checkAndEnableBluetooth() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth 미지원 기기", Toast.LENGTH_SHORT).show();
        } else if (!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBluetoothLauncher.launch(intent);
        }
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public static final int LOGIN_FRAGMENT = 0;
    public static final int SEATING_FRAGMENT = 1;
    public static final int WARNING_FRAGMENT = 2;

    // 화면 전환
    public void changeFragment(int fragmentNum) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        switch (fragmentNum) {
            case LOGIN_FRAGMENT:
                if (loginFrag == null) {
                    loginFrag = new LoginFragment();
                }
                ft.replace(R.id.frmMain, loginFrag);
                break;
            case SEATING_FRAGMENT:
                if (seatFrag == null) {
                    seatFrag = new SeatingFragment();
                }
                ft.replace(R.id.frmMain, seatFrag);
                break;
            case WARNING_FRAGMENT:
                if (warnFrag == null) {
                    warnFrag = new WarningFragment();
                }
                ft.replace(R.id.frmMain, warnFrag);
                break;
        }
        ft.commitNow();
    }

    SensorDataModel sensor = new SensorDataModel();

    public boolean isEngineOn = false;
    public boolean isSit1 = false;
    public boolean isSit2 = false;

    // SeatingFragment 제어
    public void EngineOnOff() {
        Button btnCar = (Button)findViewById(R.id.btnCar);
        if (isEngineOn == false) {
            isSit1 = isSitting(1);
            isSit2 = isSitting(2);

            btnCar.setText(getString(R.string.engine_off));
            isEngineOn = true;

        }
        else {
            isSit1 = false;
            isSit2 = false;

            btnCar.setText(getString(R.string.engine_on));
            isEngineOn = false;
        }
    }

    // 센서 값을 받아 좌석 탑승 여부를 저장
    // TODO: if문 수정
    public boolean isSitting(int num){
        if (num == 1) {
            return false;
//            if (sensor.getSensorValue1().equals("T")) {
//                return true;
//            } else if (sensor.getSensorValue1().equals("F")) {
//                return false;
//            }
        } else if (num == 2) {
            return true;
//            if (sensor.getSensorValue2().equals("T")) {
//                return true;
//            } else if (sensor.getSensorValue2().equals("F")) {
//                return false;
//            }
        }
        return false;
    }
}
