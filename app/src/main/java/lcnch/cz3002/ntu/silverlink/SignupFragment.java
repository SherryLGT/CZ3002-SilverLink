package lcnch.cz3002.ntu.silverlink;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


/**
 *
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 17/02/2017
 */

public class SignupFragment extends Fragment {

    private View rootView;
    private EditText et_name, et_pwd, et_pwd2;
    private CheckBox cb_role;
    private Button btn_cancel, btn_signup;

    /**
     * Default constructor for SignupFragment
     */
    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_signup, container, false);

        et_name = (EditText) rootView.findViewById(R.id.et_name);
        et_pwd = (EditText) rootView.findViewById(R.id.et_pwd);
        et_pwd2 = (EditText) rootView.findViewById(R.id.et_pwd2);
        cb_role = (CheckBox) rootView.findViewById(R.id.cb_role);
        btn_cancel = (Button) rootView.findViewById(R.id.btn_cancel);
        btn_signup = (Button) rootView.findViewById(R.id.btn_signup);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new LoginFragment()).commit();
            }
        });

        return rootView;
    }

}
