package com.skeleton.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.skeleton.R;
import com.skeleton.constant.AppConstant;
import com.skeleton.retrofit.APIError;
import com.skeleton.retrofit.ApiInterface;
import com.skeleton.retrofit.MultipartParams;
import com.skeleton.retrofit.ResponseResolver;
import com.skeleton.retrofit.RestClient;
import com.skeleton.util.ValidateEditText;
import com.skeleton.util.customview.MaterialEditText;
import com.skeleton.util.imagepicker.ImageChooser;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Response;

/**
 * sign up fragment
 */

public class SignupFragment extends Fragment implements AppConstant {


    private MaterialEditText etName, etEmail, etMobileNo, etPassword, etConfirmPassword, etDob, etCountryCode;
    private Button btnSignup;
    private RadioGroup rgGender;
    private ValidateEditText validateEditText = new ValidateEditText();
    private ImageView ciProfilePic;
    private ImageChooser imageChooser;
    private File file;
    private HashMap<String, RequestBody> map;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        Log.d("debug", "button clicked");
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        initiallization(view);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.e("debug", "button clicked");
                if (isValidate()) {
                    Log.e("debug", "validation successful");

                    updateDataInHashMap();

                    ApiInterface apiInterface = RestClient.getApiInterface();
                    apiInterface.postUser(map).enqueue(new ResponseResolver<Response<JSONObject>>(getActivity(), true, true) {

                        @Override
                        public void success(final Response response) {
                            Toast.makeText(getContext(), "successful", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void failure(final APIError error) {
                            Toast.makeText(getContext(), "unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.e("debug", "validation unsuccessful");
                }
            }
        });

        ciProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                imageChooser = new ImageChooser(new ImageChooser.Builder(SignupFragment.this));
                imageChooser.selectImage(new ImageChooser.OnImageSelectListener() {
                    @Override
                    public void loadImage(final List<ChosenImage> list) {
                        file = new File(list.get(0).getOriginalPath());
                        if (file != null) {
                            Glide.with(SignupFragment.this)
                                    .load(list.get(0).getQueryUri())
                                    .into(ciProfilePic);
                            Log.e("debug", list.get(0).getQueryUri());
                        } else {
                            Log.e("debug", "Error");

                        }
                    }

                    @Override
                    public void croppedImage(final File mCroppedImage) {

                    }
                });
            }
        });
        return view;
    }

    /**
     * @param view view
     */
    public void initiallization(final View view) {
        etName = (MaterialEditText) view.findViewById(R.id.et_name);
        etEmail = (MaterialEditText) view.findViewById(R.id.et_email);
        etMobileNo = (MaterialEditText) view.findViewById(R.id.et_contactno);
        etPassword = (MaterialEditText) view.findViewById(R.id.et_password);
        etConfirmPassword = (MaterialEditText) view.findViewById(R.id.et_confirm_password);
        etDob = (MaterialEditText) view.findViewById(R.id.et_dob);
        etCountryCode = (MaterialEditText) view.findViewById(R.id.et_countrycode);
        ciProfilePic = (ImageView) view.findViewById(R.id.ci_profilepic);
        btnSignup = (Button) view.findViewById(R.id.btn_Signup);
        Log.e("debug", "initialization");
    }

    /**
     * @return result
     */
    private boolean isValidate() {
        if (validateEditText.checkName(etName, true)
                && validateEditText.checkName(etName, false)
                && validateEditText.checkPhoneNumber(etMobileNo)
                && validateEditText.checkEmail(etEmail)
                && validateEditText.checkPassword(etPassword, false)
                && validateEditText.checkPassword(etConfirmPassword, true)
                && validateEditText.comparePassword(etPassword, etConfirmPassword)) {
            return validateEditText.checkEmail(etEmail);
        } else {
            Log.e("debug", "error");
            return false;
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        imageChooser.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {
        imageChooser.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * hash map
     */
    public void updateDataInHashMap() {
        map = new MultipartParams.Builder()
                .add(KEY_FRAGMENT_FNAME, etName.getText().toString().trim())
                .add(KEY_FRAGMENT_LNAME, etName.getText().toString().trim())
                .add(KEY_FRAGMENT_DOB, etDob.getText().toString().trim())
                .add(KEY_FRAGMENT_COUNTRYCODE, etCountryCode.getText().toString().trim())
                .add(KEY_FRAGMENT_MOBILENO, etMobileNo.getText().toString().trim())
                .add(KEY_FRAGMENT_EMAIL, etEmail.getText().toString().trim())
                .add(KEY_FRAGMENT_PASSWORD, etPassword.getText().toString().trim())
                .add(KEY_FRAGMENT_LANGUAGE, LANGUAGE)
                .add(KEY_FRAGMENT_DEVICETYPE, DEVICE_TYPE)
                .add(KEY_FRAGMENT_DEVICETOKEN, DEVICE_TOKEN)
                .add(KEY_FRAGMENT_APPVERSION, APP_VERSION)
                .add(KEY_FRAGMENT_PROFILEPIC, ciProfilePic)
                .build().getMap();
    }

}
