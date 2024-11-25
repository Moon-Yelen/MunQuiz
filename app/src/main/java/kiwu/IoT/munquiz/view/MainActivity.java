package kiwu.IoT.munquiz.view;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import kiwu.IoT.munquiz.R;

public class MainActivity extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private LoginFragment loginFrag;
    private SeatingFragment seatFrag;
    private WarningFragment warnFrag;

    private BluetoothSocket socket = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    // 블루투스 활성화 요청을 위한 ActivityResultLauncher
    private final ActivityResultLauncher<Intent> enableBluetoothLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // 블루투스가 성공적으로 활성화됨
                    selectBluetoothDevice();
                } else {
                    // 블루투스 활성화 실패
                    Toast.makeText(getApplicationContext(), "Bluetooth 활성화가 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            });

    // 권한 요청을 위한 ActivityResultLauncher
    private final ActivityResultLauncher<String[]> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                // 권한 요청 결과 확인
                if (result.getOrDefault(Manifest.permission.BLUETOOTH_CONNECT, false)) {
                    // 권한이 승인되었을 때 블루투스 활성화 확인
                    checkAndEnableBluetooth();
                } else {
                    // 권한이 거부되었을 때 처리할 내용
                    Toast.makeText(getApplicationContext(), "Bluetooth 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeFragment(LOGIN_FRAGMENT); // 기본 화면을 LOGIN_FRAGMENT로 설정

        // 블루투스 권한 체크 및 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없으면 요청
            permissionLauncher.launch(new String[]{Manifest.permission.BLUETOOTH_CONNECT});
        } else {
            // 권한이 있으면 블루투스 활성화 확인
            checkAndEnableBluetooth();
        }
    }

    private void checkAndEnableBluetooth() {
        if (bluetoothAdapter == null) { // 기기가 블루투스를 지원하지 않을 때
            Toast.makeText(getApplicationContext(), "Bluetooth 미지원 기기입니다.", Toast.LENGTH_SHORT).show();
        } else {
            if (bluetoothAdapter.isEnabled()) { // 기기의 블루투스 기능이 켜져 있을 경우
                selectBluetoothDevice();
            } else { // 기기의 블루투스 기능이 꺼져 있을 경우
                // 블루투스 활성화를 위한 대화상자 출력
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                enableBluetoothLauncher.launch(intent);
            }
        }
    }

    // 블루투스 디바이스를 선택하는 함수
    private void selectBluetoothDevice() {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            } // PermissionCheck
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                ArrayList<BluetoothDevice> deviceList = new ArrayList<>(pairedDevices);

                // 사용자에게 선택 UI 보여주기
                showDeviceSelectionDialog(deviceList);

            } else {
                Toast.makeText(getApplicationContext(), "페어링된 Bluetooth 디바이스가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 페어링된 디바이스 목록을 보여주는 Dialog
    private void showDeviceSelectionDialog(ArrayList<BluetoothDevice> deviceList) {
        String[] deviceNames = new String[deviceList.size()];
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        } // PermissionCheck
        for (int i = 0; i < deviceList.size(); i++) {
            deviceNames[i] = deviceList.get(i).getName() + " (" + deviceList.get(i).getAddress() + ")";
        }

        new AlertDialog.Builder(this)
                .setTitle("페어링된 디바이스 선택")
                .setItems(deviceNames, (dialog, which) -> {
                    BluetoothDevice selectedDevice = deviceList.get(which);
                    Toast.makeText(getApplicationContext(),
                            "선택된 디바이스: " + selectedDevice.getName(),
                            Toast.LENGTH_SHORT).show();
                    connectToDevice(selectedDevice); // 선택된 디바이스와 연결 (추가 작업)
                })
                .setNegativeButton("취소", null)
                .create()
                .show();
    }

    // 선택한 디바이스와 연결하는 로직
    private void connectToDevice(BluetoothDevice device) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            } // PermissionCheck
            // BluetoothSocket을 생성하고 UUID를 설정합니다 (일반적으로 BluetoothSerialPort 서비스의 UUID 사용)
            UUID uuid = device.getUuids()[0].getUuid(); // 디바이스의 UUID를 사용
            socket = device.createRfcommSocketToServiceRecord(uuid);

            // 연결 시도
            socket.connect();
            Log.d("Bluetooth", "Connected to device: " + device.getName());

            // 연결 성공 후 데이터 송수신을 위한 스트림을 설정할 수 있습니다
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            // 데이터 수신 작업을 시작
            new Thread(new Runnable() {
                @Override
                public void run() {
                    receiveData();
                }
            }).start();

        } catch (IOException e) {
            Log.e("Bluetooth", "Connection failed: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "디바이스 연결 실패", Toast.LENGTH_SHORT).show();
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e1) {
                Log.e("Bluetooth", "Failed to close socket: " + e1.getMessage());
            }
        }
    }

    // 데이터를 수신하고 처리하는 함수
    private void receiveData() {
        byte[] buffer = new byte[1024];
        int bytesRead;

        while (true) {
            try {
                // 데이터를 InputStream에서 읽음
                bytesRead = inputStream.read(buffer);
                String data = new String(buffer, 0, bytesRead);
                Log.d("Bluetooth", "Received Data: " + data);

                // 여기서 데이터를 처리하는 추가 작업
            } catch (IOException e) {
                Log.e("Bluetooth", "Error reading data: " + e.getMessage());
                break; // 오류 발생 시 루프 종료
            }
        }
    }

    public static final int LOGIN_FRAGMENT = 0;
    public static final int SEATING_FRAGMENT = 1;
    public static final int WARNING_FRAGMENT = 2;

    // 화면 전환 함수
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
            case WARNING_FRAGMENT:
                ft = ft.replace(R.id.frmMain, warnFrag);
                ft.commitNow();
                break;
        } // end switch
    } // end changeFragment


}