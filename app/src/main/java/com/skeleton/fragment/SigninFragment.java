package com.skeleton.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.skeleton.R;
import com.skeleton.constant.AppConstant;
import com.skeleton.retrofit.APIError;
import com.skeleton.retrofit.ApiInterface;
import com.skeleton.retrofit.CommonParams;
import com.skeleton.retrofit.ResponseResolver;
import com.skeleton.retrofit.RestClient;
import com.skeleton.util.ValidateEditText;
import com.skeleton.util.customview.MaterialEditText;
import java.util.HashMap;

/**
 * Created by himanshu on 10/5/17.
 */

public class SigninFragment extends Fragment implements AppConstant {

    private MaterialEditText etEmail, etPassword;
    private Button btnSignin;
    private ValidateEditText validateEditText;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        init(view);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (isValidate()) {
                    HashMap<String, String> checkData = new CommonParams.Builder()
                            .add(KEY_FRAGMENT_EMAIL, etEmail)
                            .add(KEY_FRAGMENT_PASSWORD, etPassword)
                            .add(KEY_FRAGMENT_DEVICETYPE, DEVICE_TYPE)
                            .add(KEY_FRAGMENT_LANGUAGE, LANGUAGE)
                            .add(KEY_FRAGMENT_DEVICETOKEN, DEVICE_TOKEN)
                            .add(KEY_FRAGMENT_FLUSHPREVIOUSSESSION, FLUSHPREVIOUSSESSION)
                            .add(KEY_FRAGMENT_APPVERSION, APP_VERSION)
                            .build().getMap();

                    ApiInterface apiInterface = RestClient.getApiInterface();
                    apiInterface.userLogin(null, checkData).enqueue(new ResponseResolver<CommonParams>(getActivity(), true, true) {


                        @Override
                        public void success(final CommonParams commonParams) {
                            Toast.makeText(getContext(), "Login successfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void failure(final APIError error) {

                        }
                    });
                }
            }
        });


        return view;
    }

    /**
     * @param view view
     */
    private void init(final View view) {
        etEmail = (MaterialEditText) view.findViewById(R.id.et_email);
        etPassword = (MaterialEditText) view.findViewById(R.id.et_password);
        btnSignin = (Button) view.findViewById(R.id.btn_signin);
    }

    /**
     * @return true or false
     */
    private boolean isValidate() {
        return validateEditText.checkEmail(etEmail) && validateEditText.checkPassword(etPassword, false);
    }
}
