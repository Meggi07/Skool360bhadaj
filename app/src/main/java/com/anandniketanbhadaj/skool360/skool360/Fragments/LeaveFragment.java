package com.anandniketanbhadaj.skool360.skool360.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anandniketanbhadaj.skool360.R;
import com.anandniketanbhadaj.skool360.skool360.Activities.DashBoardActivity;
import com.anandniketanbhadaj.skool360.skool360.Activities.Server_Error;
import com.anandniketanbhadaj.skool360.skool360.AsyncTasks.InsertStudentLeaveAsyncTask;
import com.anandniketanbhadaj.skool360.skool360.Models.ExamSyllabus.CreateLeaveModel;
import com.anandniketanbhadaj.skool360.skool360.Utility.AppConfiguration;
import com.anandniketanbhadaj.skool360.skool360.Utility.Utility;

import java.util.Calendar;
import java.util.HashMap;

public class LeaveFragment extends Fragment {
    private static TextView txtDate, txtendDate;
    private static String dateFinal;
    private static boolean isFromDate = false;
    CreateLeaveModel leaveResponse;
    String startDate, endDate, purpose, requestfor, description;
    Fragment fragment;
    FragmentManager fragmentManager;
    LinearLayout linearBack;
    private View rootView;
    private Context mContext;
    private EditText edtPurpose, edtDescription;
    private Button btnSave, btnCancel, btnMenu, btnBack;
    private ProgressDialog progressDialog = null;
    private InsertStudentLeaveAsyncTask insertStudentLeaveAsyncTask = null;

    public LeaveFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_leave, container, false);
        mContext = getActivity();

        AppConfiguration.position = 12;

        initViews();
        setListners();

        return rootView;
    }

    public void initViews() {
        txtDate = rootView.findViewById(R.id.txtstartDate);
        edtPurpose = rootView.findViewById(R.id.edtPurpose);
        edtDescription = rootView.findViewById(R.id.edtDescription);
        btnSave = rootView.findViewById(R.id.btnSave);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        btnMenu = rootView.findViewById(R.id.btnMenu);
        btnBack = rootView.findViewById(R.id.btnBack);
        linearBack = rootView.findViewById(R.id.linearBack);
        txtendDate = rootView.findViewById(R.id.txtendDate);


        //load today's data first
        txtDate.setText("DD/MM/YYYY");
        txtendDate.setText("DD/MM/YYYY");
    }

    public void setListners() {
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DashBoardActivity.onLeft();
            }
        });
        linearBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new ShowLeaveFragment();
                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.frame_container, fragment).commit();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new ShowLeaveFragment();
                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.frame_container, fragment).commit();
            }
        });
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromDate = true;
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        txtendDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = false;

                if (!txtDate.getText().toString().equalsIgnoreCase("DD/MM/YYYY")) {

                    DialogFragment newFragment = new SelectDateFragment();
                    newFragment.show(getFragmentManager(), "DatePicker");
                } else {
                    Utility.ping(getContext(), "Plase select from date");
                }

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getsendAppoimentData();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDate.setText("DD/MM/YYYY");
                txtendDate.setText("DD/MM/YYYY");
                edtPurpose.setText("");
                edtDescription.setText("");
            }
        });
    }

    public void getsendAppoimentData() {
        startDate = txtDate.getText().toString();
        endDate = txtendDate.getText().toString();
        purpose = edtPurpose.getText().toString();
        description = edtDescription.getText().toString();

        if (Utility.isNetworkConnected(mContext)) {
            if (!startDate.equalsIgnoreCase("DD/MM/YYYY") &&
                    !endDate.equalsIgnoreCase("DD/MM/YYYY") &&
                    !purpose.equalsIgnoreCase("")) {
                if (Utility.CheckDates(startDate, endDate) == true) {
                    progressDialog = new ProgressDialog(mContext);
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HashMap<String, String> params = new HashMap<>();
                                params.put("FromDate", startDate);
                                params.put("ToDate", endDate);
                                params.put("studentId", Utility.getPref(mContext, "studid"));
                                params.put("Reason", purpose);
                                params.put("Comment", description);

                                insertStudentLeaveAsyncTask = new InsertStudentLeaveAsyncTask(params);
                                leaveResponse = insertStudentLeaveAsyncTask.execute().get();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        if (leaveResponse != null) {
                                            if (leaveResponse.getSuccess().equalsIgnoreCase("True")) {
                                                txtDate.setText("DD/MM/YYYY");
                                                txtendDate.setText("DD/MM/YYYY");
                                                edtPurpose.setText("");
                                                edtDescription.setText("");
                                                Utility.ping(mContext, leaveResponse.getFinalArray().get(0).getMessage());
                                                fragment = new ShowLeaveFragment();
                                                fragmentManager = getFragmentManager();
                                                fragmentManager.beginTransaction()
                                                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                                                        .replace(R.id.frame_container, fragment).commit();
                                            } else {
                                                txtDate.setText("DD/MM/YYYY");
                                                txtendDate.setText("DD/MM/YYYY");
                                                edtPurpose.setText("");
                                                edtDescription.setText("");
                                                Utility.ping(mContext, leaveResponse.getFinalArray().get(0).getMessage());
                                                progressDialog.dismiss();

                                            }
                                        } else {
                                            Intent serverintent = new Intent(mContext, Server_Error.class);
                                            startActivity(serverintent);
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    Utility.pong(mContext, "Please select proper date.");
                }
            } else {
                Utility.ping(mContext, "Blank field not allowed.");
            }
        } else {
            Utility.ping(mContext, "Network not available");
        }
    }

    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            dialog.getDatePicker().setMaxDate(Utility.StringToDate(Utility.getPref(getActivity(), "TODATE")));
            if (!isFromDate) {
                dialog.getDatePicker().setMinDate(Utility.StringToDate(txtDate.getText().toString()));
            } else {
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

//                Utility.ping(getContext(), "Plase select from date");
            }

            return dialog;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }

        public void populateSetDate(int year, int month, int day) {
            String d, m, y;
            d = Integer.toString(day);
            m = Integer.toString(month);
            y = Integer.toString(year);

            if (day < 10) {
                d = "0" + d;
            }
            if (month < 10) {
                m = "0" + m;
            }
            dateFinal = d + "/" + m + "/" + y;

            if (isFromDate) {
                txtDate.setText(dateFinal);
            } else {
                txtendDate.setText(dateFinal);
            }

        }
    }

}
