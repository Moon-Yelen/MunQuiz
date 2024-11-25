package kiwu.IoT.munquiz.model;

public class SensorDataModel {
    // 세 개의 센서값 저장
    private String sensorValue1;
    private String sensorValue2;
    private String sensorValue3;

    // 기본 생성자
    public SensorDataModel() {}

    // 모든 센서값을 초기화하는 생성자
    public SensorDataModel(String sensorValue1, String sensorValue2, String sensorValue3) {
        this.sensorValue1 = sensorValue1;
        this.sensorValue2 = sensorValue2;
        this.sensorValue3 = sensorValue3;
    }

    // 첫 번째 센서값 getter와 setter
    public String getSensorValue1() {
        return sensorValue1;
    }

    public void setSensorValue1(String sensorValue1) {
        this.sensorValue1 = sensorValue1;
    }

    // 두 번째 센서값 getter와 setter
    public String getSensorValue2() {
        return sensorValue2;
    }

    public void setSensorValue2(String sensorValue2) {
        this.sensorValue2 = sensorValue2;
    }

    // 세 번째 센서값 getter와 setter
    public String getSensorValue3() {
        return sensorValue3;
    }

    public void setSensorValue3(String sensorValue3) {
        this.sensorValue3 = sensorValue3;
    }

    // 모든 센서값을 업데이트하는 메서드
    public void updateSensorValues(String value1, String value2, String value3) {
        this.sensorValue1 = value1;
        this.sensorValue2 = value2;
        this.sensorValue3 = value3;
    }


    // 센서값 디버그용 출력
    @Override
    public String toString() {
        return "SensorDataModel{" +
                "sensorValue1='" + sensorValue1 + '\'' +
                ", sensorValue2='" + sensorValue2 + '\'' +
                ", sensorValue3='" + sensorValue3 + '\'' +
                '}';
    }
}

