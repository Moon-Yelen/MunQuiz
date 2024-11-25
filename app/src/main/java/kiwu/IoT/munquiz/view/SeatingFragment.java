package kiwu.IoT.munquiz.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kiwu.IoT.munquiz.R;
import kiwu.IoT.munquiz.model.SensorDataModel;


public class SeatingFragment extends Fragment {

    private SensorDataModel sensorData;

    public SeatingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SensorDataModel 객체 생성 또는 전달받은 데이터 설정
        sensorData = new SensorDataModel("value1", "value2", "value3");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seating, container, false);

        // 센서값 활용
        String sensor1 = sensorData.getSensorValue1();
        String sensor2 = sensorData.getSensorValue2();
        String sensor3 = sensorData.getSensorValue3();

        // 예: TextView에 값 설정
//        TextView textView = view.findViewById(R.id.textViewSensor);
//        textView.setText("Sensor 1: " + sensor1 + "\nSensor 2: " + sensor2 + "\nSensor 3: " + sensor3);


        return view; // 수정된 View 반환
    }

    // 외부에서 센서값을 업데이트
    public void updateSensorData(String value1, String value2, String value3) {
        if (sensorData != null) {
            sensorData.updateSensorValues(value1, value2, value3);
            // UI 갱신 등 필요한 작업 수행
        }
    }
}