package com.anandniketanbhadaj.skool360.skool360.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.anandniketanbhadaj.skool360.R;
import com.anandniketanbhadaj.skool360.skool360.Activities.DashBoardActivity;
import com.anandniketanbhadaj.skool360.skool360.AsyncTasks.InsertStudentLeaveAsyncTask;
import com.anandniketanbhadaj.skool360.skool360.AsyncTasks.PTMTeacherStudentInsertDetailAsyncTask;
import com.anandniketanbhadaj.skool360.skool360.Models.ExamSyllabus.CreateLeaveModel;
import com.anandniketanbhadaj.skool360.skool360.Models.ExamSyllabus.ExamModel;
import com.anandniketanbhadaj.skool360.skool360.Models.MainPtmSentMessageResponse;
import com.anandniketanbhadaj.skool360.skool360.Models.PTMTeacherResponse.PTMStudentWiseTeacher;
import com.anandniketanbhadaj.skool360.skool360.Utility.Utility;

import java.util.Calendar;
import java.util.HashMap;


public class LeaveFragment extends Fragment {
    private View rootView;
    private Context mContext;
    private static TextView txtDate,txtendDate;
    private EditText edtPurpose, edtDescription;
    private Button btnSave, btnCancel,btnMenu,btnBack;
    private ProgressDialog progressDialog = null;
    private InsertStudentLeaveAsyncTask insertStudentLeaveAsyncTask = null;
    CreateLeaveModel leaveResponse;
    private static String dateFinal;
    String startDate,endDate, purpose, requestfor, description;
    private static boolean isFromDate = false;

    Fragment fragment;
    FragmentManager fragmentManager;

    public LeaveFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_leave, container, false);
        mContext = getActivity();

        initViews();
        setListners();

        return rootView;
    }

    public void initViews() {
        txtDate = (TextView) rootView.findViewById(R.id.txtstartDate);
        edtPurpose = (EditText) rootView.findViewById(R.id.edtPurpose);
        edtDescription = (EditText) rootView.findViewById(R.id.edtDescription);
        btnSave = (Button) rootView.findViewById(R.id.btnSave);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnMenu=(Button)rootView.findViewById(R.id.btnMenu);
        btnBack=(Button)rootView.findViewById(R.id.btnBack);
        txtendDate=(TextView)rootView.findViewById(R.id.txtendDate);


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
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
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
                edtPurpose.setText("");
                edtDescription.setText("");
            }
        });
    }

    public void getsendAppoimentData() {
        startDate =txtDate.getText().toString();
        endDate=txtendDate.getText().toString();
        purpose = edtPurpose.getText().toString();
        description = edtDescription.getText().toString();

        if (Utility.isNetworkConnected(mContext)) {
            if (!startDate.equalsIgnoreCase("DD/MM/YYYY") &&
                    !endDate.equalsIgnoreCase("DD/MM/YYYY") &&
                    !purpose.equalsIgnoreCase("")) {
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put("FromDate",startDate);
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
                                    if (leaveResponse.getSuccess().equalsIgnoreCase("True")) {
                                        txtDate.setText("DD/MM/YYYY");
                                        txtendDate.setText("DD/MM/YYYY");
                                        edtPurpose.setText("");
                                        edtDescription.setText("");
                                        Utility.ping(mContext, "Leave Request send Successfully.");
                                        fragment = new ShowLeaveFragment();
                                        fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                                                .replace(R.id.frame_container, fragment).commit();
                                    } else {
                                        progressDialog.dismiss();

                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
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
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

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
