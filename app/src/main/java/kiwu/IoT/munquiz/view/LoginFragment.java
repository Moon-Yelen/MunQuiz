package kiwu.IoT.munquiz.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kiwu.IoT.munquiz.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private EditText etID;
    private EditText etPwd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        etID = (EditText)view.findViewById(R.id.etID);
        etPwd = (EditText)view.findViewById(R.id.etPwd);

        ButtonHandler handler = new ButtonHandler();

        Button btnLogin = (Button)view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(handler);

        return view;
    } // end onCreateView

    @Override
    public void onStart() {
        super.onStart();

        etPwd.setText(null);
    } // end onStart

    private class ButtonHandler implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            MainActivity activity = (MainActivity)getActivity();

            String id = "munquiz1234";
            String pwd = "test1234";

            if (id != etID.getText().toString()) {
                Toast.makeText(activity, "아이디 혹은 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            } else if (pwd != etPwd.getText().toString()) {
                Toast.makeText(activity, "아이디 혹은 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            activity.changeFragment(MainActivity.SEATING_FRAGMENT);
        }
    }
}