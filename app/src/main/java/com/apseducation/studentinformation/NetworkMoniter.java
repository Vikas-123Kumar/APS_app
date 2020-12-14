package com.apseducation.studentinformation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.apseducation.studentinformation.api.API_URL;
import com.apseducation.studentinformation.bean.EventBean;
import com.apseducation.studentinformation.bean.Registration_Bean;
import com.apseducation.studentinformation.bean.Survey_bean;
import com.apseducation.studentinformation.connection.NetworkConnection;
import com.apseducation.studentinformation.profile.ui.Student_Registration;
import com.apseducation.studentinformation.profile.ui.sqlite.DataBaseHelper;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NetworkMoniter extends BroadcastReceiver {
    private Context context;
    Bitmap bitmap, bitmapProfileRegister, bitmapTenth, bitmapTwelve, bitmapGrad, bitmapIdentity;
    DataBaseHelper dataBaseHelper;
    String imageTenthToServer, imageIdentityToServer, imageGradToServer, imageTwelveToServer, imageToServer, imageToSurveyServer;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    List<Survey_bean> survey_list = new ArrayList();
    List<Registration_Bean> registration_list = new ArrayList();
    List<EventBean> event_list = new ArrayList<>();
    String isTenthPdf = "false", isTwelvePdf = "false", isGraduationPdf = "false", isIdentityPdf = "false";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        dataBaseHelper = new DataBaseHelper(context);
        if (NetworkConnection.isConnected(context)) {
            try {
                survey_list = dataBaseHelper.getNewSurvey();
                Log.e("survey list", survey_list.toString());
                for (Survey_bean survey_bean : survey_list) {
                    saveSurvey(survey_bean);
                }
            } catch (Exception ex) {
                Log.e("Exception", ex.getMessage());
            }
            try {
                registration_list = dataBaseHelper.getNewRegistration();
                Log.e("registration", registration_list.toString());
                for (Registration_Bean registration_bean : registration_list) {
                    saveRegistration(registration_bean);
                }
            } catch (Exception ex) {
                Log.e("Exception Register", ex.getMessage());
            }
            try {
                event_list = dataBaseHelper.getEvent();
                for (EventBean eventBean : event_list) {
                    saveEvent(eventBean);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    //send survey data to server if data exist in sqlite data base.
    public void saveSurvey(final Survey_bean survey_bean) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String name = survey_bean.getFirst_name();
        String email = survey_bean.getEmail();
        String mobile = survey_bean.getMobile_no();
        String father = survey_bean.getFather_name();
        String address = survey_bean.getAddress();
        String blood = survey_bean.getBlood();
        String height = survey_bean.getHeight();
        String state = survey_bean.getState();
        String dist = survey_bean.getDist();
        String coureses = survey_bean.getDepartment();
        String tehsil = survey_bean.getTehsil();
        String force = survey_bean.getForce();
        String other = survey_bean.getOther();
        String service = survey_bean.getService();
        String weight = survey_bean.getWeight();
        String specialize = survey_bean.getSpecialize();
        Date date = survey_bean.getDate();
        String college = survey_bean.getCollege();
        String pin = survey_bean.getPin_code();
        String boardTen = survey_bean.getBoardTen();
        String yearTen = survey_bean.getYearTen();
        String boardTwelve = survey_bean.getBoardTwelve();
        String yearTwelve = survey_bean.getYearTwelve();
        String boardGraduation = survey_bean.getBoardGraduation();
        String yearGraduation = survey_bean.getYeargraduation();
        String courseGraduation = survey_bean.getSubjectGraduation();

        String image = survey_bean.getImage();
        if (image != null) {
            bitmap = BitmapFactory.decodeFile(image);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            imageToSurveyServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } else {
            imageToSurveyServer = "";
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("courses", coureses);
            jsonObject.put("college", college);
            jsonObject.put("name", name);
            jsonObject.put("fatherName", father);
            jsonObject.put("dob", format.format(date));
            jsonObject.put("email", email);
            jsonObject.put("address", address);
            jsonObject.put("state", state);
            jsonObject.put("district", dist);
            jsonObject.put("tehsil", tehsil);
            jsonObject.put("pincode", pin);
            jsonObject.put("contact", mobile);
            jsonObject.put("height", height);
            jsonObject.put("weight", weight);
            jsonObject.put("bloodGroup", blood);
            jsonObject.put("board10", boardTen);
            jsonObject.put("year10", yearTen);
            jsonObject.put("board12", boardTwelve);
            jsonObject.put("year12", yearTwelve);
            jsonObject.put("graduationUniversity", boardGraduation);
            jsonObject.put("graduationYear", yearGraduation);
            jsonObject.put("graduationCourse", courseGraduation);
            jsonObject.put("forceMember", force);
            jsonObject.put("other", other);
            jsonObject.put("governmentMember", service);
            jsonObject.put("coaching", specialize);
            jsonObject.put("studentPhotoUrl", imageToSurveyServer);
            Log.e("error", jsonObject + "");
            // Request a string response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, API_URL.survey_url, jsonObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e("value of json survey", response + "");
                                String responseStatus = response.getString("message");
                                if (responseStatus.equals("Survey created")) {
                                    Log.e("value of if", "" + responseStatus);
                                    dataBaseHelper.deleteSurvey(survey_bean);
                                    context.sendBroadcast(new Intent(Student_Registration.DATA_SAVED_BROADCAST));
                                } else {
                                    Log.e("value of else", responseStatus + "");
                                }

                            } catch (Exception e) {
                                Log.e("error", e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.getMessage() == null) {
                                dataBaseHelper.deleteSurvey(survey_bean);
                            }
                        }
                    });
            // Add the request to the RequestQueue.
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //send registration data to server if data exist in sqlite data base.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveRegistration(final Registration_Bean registration_bean) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String name = registration_bean.getFirst_name();
        String email = registration_bean.getEmail();
        String fathername = registration_bean.getFather_name();
        String village = registration_bean.getAddress();
        String height = registration_bean.getHeight_register();
        String state = registration_bean.getState_register();
        Date date = registration_bean.getDate_of_birth();
        String blood = registration_bean.getBlood();
//        String paid = registration_bean.getPaymentStatus();
        String dist = registration_bean.getDist_register();
        String occupation = registration_bean.getFather_occupation();
        String mobile = registration_bean.getMobile_no();
        String whatsapp = registration_bean.getWhatsappNo();
        String weight = registration_bean.getWeight();
        String pin = registration_bean.getPin_code();
        String defence = registration_bean.getDefence();
        String category = registration_bean.getSpinnerCategory();
        String marital = registration_bean.getSpinnerMarital();
        String boardTen = registration_bean.getBoardTen();
        String boardTwelve = registration_bean.getBoardTwelve();
        String boardGrag = registration_bean.getBoardGraduation();
        String boardAspiring = registration_bean.getBoardAspiring();
        String subjectTen = registration_bean.getSubjectTen();
        String subjectTwelve = registration_bean.getSubjectTwelve();
        String college = registration_bean.getCollege();
        String subjectGrad = registration_bean.getSubjectGraduation();
        String subjectAspiring = registration_bean.getSubjectAspiring();
        String percentTen = registration_bean.getPercentageTen();
        String percentTwelve = registration_bean.getPercentageTwelve();
        String percentGrad = registration_bean.getPercentageGraduation();
        String yearTen = registration_bean.getYearTen();
//        String coursePrice = registration_bean.getPrice();
        String yearTwelve = registration_bean.getYearTwelve();
        String yearGrad = registration_bean.getYeargraduation();
        String yearAspiring = registration_bean.getYearAspiring();
        String identification = registration_bean.getIdentification();
        String profileImage = registration_bean.getImage();
        if (profileImage != null) {
            bitmapProfileRegister = BitmapFactory.decodeFile(profileImage);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapProfileRegister.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            imageToServer = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } else {
            imageToServer = "";
        }
        String tenthImage = registration_bean.getImageTenth();
        if (tenthImage != null && !tenthImage.contains(".pdf")) {
            bitmapTenth = BitmapFactory.decodeFile(tenthImage);
            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            bitmapTenth.compress(Bitmap.CompressFormat.JPEG, 100, baos1);
            byte[] imagetenth = baos1.toByteArray();
            imageTenthToServer = Base64.encodeToString(imagetenth, Base64.DEFAULT);
        } else if (tenthImage != null && tenthImage.contains(".pdf")) {
            InputStream iStream = null;
            byte[] imageIdentity;
            try {
                iStream = Files.newInputStream(Paths.get(tenthImage));
                imageIdentity = getBytes(iStream);
                imageTenthToServer = Base64.encodeToString(imageIdentity, Base64.DEFAULT);
                isTenthPdf = "true";
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            imageTenthToServer = "";
        }
        String twelveImage = registration_bean.getImageTwelve();
        if (twelveImage != null && !twelveImage.contains(".pdf")) {
            bitmapTwelve = BitmapFactory.decodeFile(twelveImage);
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            bitmapTwelve.compress(Bitmap.CompressFormat.JPEG, 100, baos2);
            byte[] imageTwelve = baos2.toByteArray();
            imageTwelveToServer = Base64.encodeToString(imageTwelve, Base64.DEFAULT);
        } else if (twelveImage != null && twelveImage.contains(".pdf")) {
            InputStream iStream = null;
            byte[] imageIdentity;
            try {
                iStream = Files.newInputStream(Paths.get(twelveImage));
                imageIdentity = getBytes(iStream);
                imageTwelveToServer = Base64.encodeToString(imageIdentity, Base64.DEFAULT);
                isTwelvePdf = "true";
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            imageTwelveToServer = "";
        }
        String gradImage = registration_bean.getImageGrad();
        if (gradImage != null && !gradImage.contains(".pdf")) {
            bitmapGrad = BitmapFactory.decodeFile(gradImage);
            ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
            bitmapGrad.compress(Bitmap.CompressFormat.JPEG, 100, baos3);
            byte[] imageGrad = baos3.toByteArray();
            imageGradToServer = Base64.encodeToString(imageGrad, Base64.DEFAULT);
        } else if (gradImage != null && gradImage.contains(".pdf")) {
            InputStream iStream = null;
            byte[] imageIdentity;
            try {
                iStream = Files.newInputStream(Paths.get(gradImage));
                imageIdentity = getBytes(iStream);
                imageGradToServer = Base64.encodeToString(imageIdentity, Base64.DEFAULT);
                isGraduationPdf = "true";
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            imageGradToServer = "";
        }
        String identityImage = registration_bean.getImageIdentity();
        if (identityImage != null && !identityImage.contains(".pdf")) {
            bitmapIdentity = BitmapFactory.decodeFile(identityImage);
            ByteArrayOutputStream baos4 = new ByteArrayOutputStream();
            bitmapIdentity.compress(Bitmap.CompressFormat.JPEG, 100, baos4);
            byte[] imageIdentity = baos4.toByteArray();
            imageIdentityToServer = Base64.encodeToString(imageIdentity, Base64.DEFAULT);
        } else if (identityImage != null && identityImage.contains(".pdf")) {
            InputStream iStream = null;
            byte[] imageIdentity;
            try {
                iStream = Files.newInputStream(Paths.get(identityImage));
                imageIdentity = getBytes(iStream);
                imageIdentityToServer = Base64.encodeToString(imageIdentity, Base64.DEFAULT);
                isIdentityPdf = "true";
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            imageIdentityToServer = "";
        }
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("courses", defence);
            jsonObject.put("college", college);
            jsonObject.put("name", name);
            jsonObject.put("fatherName", fathername);
            jsonObject.put("fatherOccupation", occupation);
            jsonObject.put("dob", format.format(date));
            jsonObject.put("category", category);
            jsonObject.put("email", email);
            jsonObject.put("address", village);
            jsonObject.put("state", state);
            jsonObject.put("district", dist);
            jsonObject.put("pincode", pin);
            jsonObject.put("contact", mobile);
            jsonObject.put("whatsapp", whatsapp);
            jsonObject.put("maritalStatus", marital);
            jsonObject.put("height", height);
            jsonObject.put("weight", weight);
//            jsonObject.put("paid", paid);
            jsonObject.put("bloodGroup", blood);
            jsonObject.put("tenthBoard", boardTen);
            jsonObject.put("tenthYear", yearTen);
            jsonObject.put("tenthSubject", subjectTen);
            jsonObject.put("tenthPercentage", percentTen);
            jsonObject.put("twelveBoard", boardTwelve);
            jsonObject.put("twelveYear", yearTwelve);
            jsonObject.put("twelveSubject", subjectTwelve);
            jsonObject.put("twelvePercentage", percentTwelve);
            jsonObject.put("aspiringBoard", boardAspiring);
            jsonObject.put("aspiringYear", yearAspiring);
            jsonObject.put("aspiringSubject", subjectAspiring);
            jsonObject.put("graduationUniversity", boardGrag);
            jsonObject.put("graduationYear", yearGrad);
            jsonObject.put("graduationPercentage", percentGrad);
            jsonObject.put("graduationSubject", subjectGrad);
            jsonObject.put("idType", identification);
            jsonObject.put("studentPhotoUrl", imageToServer);
//            jsonObject.put("coursePrice", coursePrice);
            jsonObject.put("tenthMarksheetUrl", imageTenthToServer);
            jsonObject.put("isPdf10", isTenthPdf);
            jsonObject.put("twelveMarksheetUrl", imageTwelveToServer);
            jsonObject.put("isPdf12", isTwelvePdf);
            jsonObject.put("universityDocumentUrl", imageGradToServer);
            jsonObject.put("isPdfUniversity", isGraduationPdf);
            jsonObject.put("idProofUrl", imageIdentityToServer);
            jsonObject.put("isPdfId", isIdentityPdf);

            //request, as I am using my own localhost for this tutorial
            // Request a string response from the provided URL.
            Log.e("json value register", jsonObject.toString() + "");
            // Request a string response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, API_URL.Registration_url, jsonObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e("json in register", response + "");
                                String responseStatus = response.getString("message");
                                if (responseStatus.equals("user created")) {
                                    dataBaseHelper.deleteRegistration(registration_bean);
                                    Log.e("value of if register", "" + responseStatus);
                                } else {
                                    Log.e("value of else register", responseStatus + "");
                                }
                            } catch (Exception e) {
                                Log.e("error", e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.getMessage() == null) {
                                dataBaseHelper.deleteRegistration(registration_bean);
                            }
                        }
                    });
            // Add the request to the RequestQueue.
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            Log.e("error jsonexception", e.getMessage());
        }
    }

    //change inputStream to byte array.
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void saveEvent(final EventBean eventBean) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String name = eventBean.getNameE();
        String contact = eventBean.getContactE();
        String address = eventBean.getAddressE();
        String eventId = eventBean.getEventId();
        String fatherName = eventBean.getFatherName();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("address", address);
            jsonObject.put("mobile", contact);
            jsonObject.put("fatherName", fatherName);
            jsonObject.put("id", eventId);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API_URL.event_url_create, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String responseStatus = response.getString("msg");
                        if (responseStatus.equals("Success")) {
                            Log.e("value of if register", "" + responseStatus);
                            dataBaseHelper.deleteEvent(eventBean);
                        } else {
                            Log.e("value of else register", responseStatus + "");
                        }
                    } catch (Exception e) {
                        Log.e("error", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.getMessage() == null) {
                        dataBaseHelper.deleteEvent(eventBean);
                    }
                }
            });
            requestQueue.add(jsonObjectRequest);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}

