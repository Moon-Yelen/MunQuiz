package kiwu.IoT.munquiz.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Set;

import kiwu.IoT.munquiz.R;
import kiwu.IoT.munquiz.model.SensorDataModel;

public class SeatingFragment extends Fragment {

    private BluetoothAdapter bluetoothAdapter;


    public SeatingFragment() {
        // Required empty public constructor
    }

    TextView seat1;
    TextView seat2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seating, container, false);

        MainActivity activity = (MainActivity) requireActivity();
        bluetoothAdapter = activity.getBluetoothAdapter();

        // 블루투스 다이얼로그 호출
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            selectBluetoothDevice();
        } else {
            Toast.makeText(activity, "Bluetooth 활성화 필요", Toast.LENGTH_SHORT).show();
        }

        seat1 = view.findViewById(R.id.Seat1);
        seat2 = view.findViewById(R.id.Seat2);

        ButtonHandler handler = new ButtonHandler();
        ButtonTestHandler testHandler = new ButtonTestHandler();

        Button btnCar = (Button)view.findViewById(R.id.btnCar);
        btnCar.setOnClickListener(handler);

        return view;
    }

    MainActivity activity = (MainActivity)getActivity();


    private class ButtonHandler implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            activity.EngineOnOff();
        }
    }

    private class ButtonTestHandler implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            seatStateChange();
        }
    }

    // 좌석 상태 표시 변경
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    private void seatStateChange() {
        if (activity.isSit1 == true) {
            if (activity.isSitting(1) == true) {
                seat1.setText("좌석 1\n\n"+R.string.seat_yes);
                seat1.setBackgroundColor(R.color.seat_yes);
            } else {
                seat1.setText("좌석 1\n\n"+R.string.seat_chk);
                seat1.setBackgroundColor(R.color.seat_chk);
            }
        } else {
            seat1.setText("좌석 1\n\n"+R.string.seat_no);
            seat1.setBackgroundColor(R.color.seat_no);
        }

        if (activity.isSit2 == true) {
            if (activity.isSitting(2) == true) {
                seat2.setText("좌석 2\n\n"+R.string.seat_yes);
                seat2.setBackgroundColor(R.color.seat_yes);
            } else {
                seat2.setText("좌석 2\n\n"+R.string.seat_chk);
                seat2.setBackgroundColor(R.color.seat_chk);
            }
        } else {
            seat2.setText("좌석 2\n\n"+R.string.seat_no);
            seat2.setBackgroundColor(R.color.seat_no);
        }
    }

    private void selectBluetoothDevice() {
        // 권한 확인
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Bluetooth 권한 필요", Toast.LENGTH_SHORT).show();
            return;
        }

        // 페어링된 블루투스 디바이스 목록 가져오기
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.isEmpty()) {
            Toast.makeText(getContext(), "페어링된 디바이스 없음", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<BluetoothDevice> deviceList = new ArrayList<>(pairedDevices);
            showDeviceSelectionDialog(deviceList);
        }
    }

    private void showDeviceSelectionDialog(ArrayList<BluetoothDevice> deviceList) {
        String[] deviceNames = new String[deviceList.size()];
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        for (int i = 0; i < deviceList.size(); i++) {
            deviceNames[i] = deviceList.get(i).getName() + " (" + deviceList.get(i).getAddress() + ")";
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("페어링된 디바이스 선택")
                .setItems(deviceNames, (dialog, which) -> {
                    BluetoothDevice selectedDevice = deviceList.get(which);
                    Toast.makeText(getContext(), "선택된 디바이스: " + selectedDevice.getName(), Toast.LENGTH_SHORT).show();
                    // 선택한 디바이스 처리 로직 추가 가능
                })
                .setNegativeButton("취소", null)
                .create()
                .show();
    }
}
