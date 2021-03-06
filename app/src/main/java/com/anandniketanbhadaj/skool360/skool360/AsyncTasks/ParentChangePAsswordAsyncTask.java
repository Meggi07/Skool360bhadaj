package com.anandniketanbhadaj.skool360.skool360.AsyncTasks;

import android.os.AsyncTask;

import com.anandniketanbhadaj.skool360.skool360.Utility.AppConfiguration;
import com.anandniketanbhadaj.skool360.skool360.Utility.ParseJSON;
import com.anandniketanbhadaj.skool360.skool360.WebServicesCall.WebServicesCall;

import java.util.HashMap;

public class ParentChangePAsswordAsyncTask extends AsyncTask<Void, Void, Boolean> {
    HashMap<String, String> param = new HashMap<String, String>();

    public ParentChangePAsswordAsyncTask(HashMap<String, String> param) {
        this.param = param;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String responseString = null;
        Boolean result = null;
        try {
            responseString = WebServicesCall.RunScript(AppConfiguration.getUrl(AppConfiguration.parentChagePassword), param);
            result = ParseJSON.parseChangePwdJson(responseString);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
    }
}
