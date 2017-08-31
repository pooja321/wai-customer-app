package customer.thewaiapp.com.payment;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import customer.thewaiapp.com.MainActivity;
import customer.thewaiapp.com.Model.Address;
import customer.thewaiapp.com.Model.CleaningOrderAmountValues;
import customer.thewaiapp.com.Model.CookingOrderAmountValues;
import customer.thewaiapp.com.Model.Order;
import customer.thewaiapp.com.Model.OrderKey;
import customer.thewaiapp.com.Model.User;
import customer.thewaiapp.com.Model.WashingOrderAmountValues;
import customer.thewaiapp.com.Order.OrderConfirmActivity;
import customer.thewaiapp.com.R;
import customer.thewaiapp.com.Realm.RealmController;
import customer.thewaiapp.com.Utility.Constants;
import customer.thewaiapp.com.Utility.Utilities;
import io.realm.Realm;
import io.realm.RealmResults;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener, ExitAlertDialogFragment.ExitOrderListener {

    Map<String, Object> OrderUpdates = new HashMap<>();
    public static final String DIALOG_ALERT = "My Alert";
    String mOrderKey, mresourceKey, UID, mOrderType, mOrderId;
    Order mOrder = new Order();
    //    OrderAmount mOrderAmount = new OrderAmount();
    Address mAddress = new Address();

    Realm realm;
    private DatabaseReference mDatabase;
    private Toolbar mtoolbar;
    RadioGroup mRadioGroupPayment;
    RadioButton mRadioButtonCOD, mRadioButtonPaytm, mRadioButtonPayu;
    Button mButtonSubmit;
    private ProgressDialog mProgressDialog;
    TextView mTextviewTotal, mmTextviewBaseamount, mTextviewServicetax, mTextviewTotalpayable, mTextviewAddressname, mTextviewHousenum, mTextviewAreaname, mTextviewLandmark, mTextviewCity, mTextviewState, mTextviewPincode;
    Long ResourceMobileNumber;
    String ResourceName, ResourceType;
    User user;
    HttpClient httpclient = new DefaultHttpClient();
    private static final String ACCOUNT_SID = "ACa5ff1aeb91ffc0f7247b6651680df2f2";
    private static final String AUTH_TOKEN = "300b72a74d60918828f99bc423b9e9db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        realm = Realm.getDefaultInstance();
        UID = Utilities.getUid();
        mOrder = (Order) getIntent().getSerializableExtra("order");
        ResourceMobileNumber = getIntent().getLongExtra("ResourceMobileNumber", 0);
        ResourceName = getIntent().getStringExtra("ResourceName");
        ResourceType = getIntent().getStringExtra("ResourceType");
        Log.v("wai", "Order id: " + mOrder.getOrderId());
        Log.v("Profile123", "Order type: " + ResourceMobileNumber);
        Log.v("Profile123", "Order type: " + ResourceName);

        mAddress = (Address) getIntent().getSerializableExtra("Address");
        Log.v("wai", "Address id : " + mAddress.getAddressId());
        mOrderType = mOrder.getOrderType();
        mOrderId = mOrder.getOrderId();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mresourceKey = mOrder.getResourceId();
        this.realm = RealmController.with(this).getRealm();
        mtoolbar = (Toolbar) findViewById(R.id.payment_toolbar);
        mtoolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mtoolbar);
        setTitle("Summary of booking");

        mRadioGroupPayment = (RadioGroup) findViewById(R.id.payment_radiogroup);
        mRadioButtonCOD = (RadioButton) findViewById(R.id.payment_rb_mode_COD);
        mRadioButtonPaytm = (RadioButton) findViewById(R.id.payment_rb_mode_paytm);
        mRadioButtonPayu = (RadioButton) findViewById(R.id.payment_rb_mode_payu);
        mButtonSubmit = (Button) findViewById(R.id.payment_bt_submit);
        mTextviewTotal = (TextView) findViewById(R.id.paymenttotal);
        mmTextviewBaseamount = (TextView) findViewById(R.id.paymentbaseamount);
        mTextviewServicetax = (TextView) findViewById(R.id.paymentservicetax);
        mTextviewTotalpayable = (TextView) findViewById(R.id.paymenttotalPayable);
        mTextviewAddressname = (TextView) findViewById(R.id.payment_addressname);
        mTextviewHousenum = (TextView) findViewById(R.id.payment_houseno);
        mTextviewAreaname = (TextView) findViewById(R.id.payment_item_areaname);
        mTextviewLandmark = (TextView) findViewById(R.id.payment_item_landmark);
        mTextviewCity = (TextView) findViewById(R.id.payment_item_cityname);
        mTextviewState = (TextView) findViewById(R.id.payment_item_statename);
        mTextviewPincode = (TextView) findViewById(R.id.payment_item_pincode);
        mButtonSubmit.setOnClickListener(this);
        String UserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        RealmResults<User> UserResults = realm.where(User.class).equalTo("Email", UserEmail).findAll();
        if (UserResults.size() > 0) {
            user = UserResults.get(0);
        }
        switch (mOrderType) {
            case Constants.ORDER_TYPE_CLEANING:
                RealmResults<CleaningOrderAmountValues> cleaningOrderAmountValues = realm.where(CleaningOrderAmountValues.class).equalTo("OrderId", mOrderId).findAll();
                if (cleaningOrderAmountValues.size() > 0) {
                    CleaningOrderAmountValues cleaningOrderAmountValues1 = cleaningOrderAmountValues.get(0);
                    if (cleaningOrderAmountValues != null) {
                        int roomsamount = cleaningOrderAmountValues1.getRoomsAmount();
                        int washroomamount = cleaningOrderAmountValues1.getWashroomsAmount();
                        int utensilamount = cleaningOrderAmountValues1.getUtensilbucketAmount();
                        double totalamount = cleaningOrderAmountValues1.getTotalAmount();
                        int baseamount = cleaningOrderAmountValues1.getBaseAmount();
                        double servicetax = cleaningOrderAmountValues1.getServiceTaxAmount();
                        mTextviewTotalpayable.setText(String.valueOf(totalamount));
                        mTextviewTotal.setText(String.valueOf(roomsamount + washroomamount + utensilamount));
                        mTextviewServicetax.setText(String.valueOf(servicetax));
                        mmTextviewBaseamount.setText(String.valueOf(baseamount));
                    }
                }
                mTextviewAddressname.setText(mAddress.getAddressName());
                mTextviewHousenum.setText(mAddress.getHouseNo());
                mTextviewAreaname.setText(mAddress.getAreaName());
                mTextviewLandmark.setText(mAddress.getLandmark());
                mTextviewCity.setText(mAddress.getCity());
                mTextviewState.setText(mAddress.getState());
                mTextviewPincode.setText(mAddress.getPincode());
                break;
            case Constants.ORDER_TYPE_COOKING:
                RealmResults<CookingOrderAmountValues> cookingOrderAmountValues = realm.where(CookingOrderAmountValues.class).equalTo("OrderId", mOrderId).findAll();
                if (cookingOrderAmountValues.size() > 0) {
                    CookingOrderAmountValues cookingOrderAmountValues1 = cookingOrderAmountValues.get(0);
                    if (cookingOrderAmountValues != null) {
                        int membersamount = cookingOrderAmountValues1.getMembersAmount();
                        int maincourseamount = cookingOrderAmountValues1.getMainCourseAmount();
                        int total = membersamount + maincourseamount;
                        double totalamount = cookingOrderAmountValues1.getTotalAmount();
                        int baseamount = cookingOrderAmountValues1.getBaseAmount();
                        double servicetax = cookingOrderAmountValues1.getServiceTaxAmount();
                        mTextviewTotalpayable.setText(String.valueOf(totalamount));
                        mTextviewTotal.setText(String.valueOf(total));
                        mTextviewServicetax.setText(String.valueOf(servicetax));
                        mmTextviewBaseamount.setText(String.valueOf(baseamount));
                    }
                }

                mTextviewAddressname.setText(mAddress.getAddressName());
                mTextviewHousenum.setText(mAddress.getHouseNo());
                mTextviewAreaname.setText(mAddress.getAreaName());
                mTextviewLandmark.setText(mAddress.getLandmark());
                mTextviewCity.setText(mAddress.getCity());
                mTextviewState.setText(mAddress.getState());
                mTextviewPincode.setText(mAddress.getPincode());
                break;
            case Constants.ORDER_TYPE_WASHING:
                RealmResults<WashingOrderAmountValues> washingOrderAmountValues = realm.where(WashingOrderAmountValues.class).equalTo("OrderId", mOrderId).findAll();
                if (washingOrderAmountValues.size() > 0) {
                    WashingOrderAmountValues washingOrderAmountValues1 = washingOrderAmountValues.get(0);
                    if (washingOrderAmountValues != null) {
                        int bucketamount = washingOrderAmountValues1.getBucketAmount();
                        double totalamount = washingOrderAmountValues1.getTotalAmount();
                        int baseamount = washingOrderAmountValues1.getBaseAmount();
                        double servicetax = washingOrderAmountValues1.getServiceTaxAmount();
                        mTextviewTotalpayable.setText(String.valueOf(totalamount));
                        mTextviewTotal.setText(String.valueOf(bucketamount));
                        mTextviewServicetax.setText(String.valueOf(servicetax));
                        mmTextviewBaseamount.setText(String.valueOf(baseamount));
                    }
                }
                mTextviewAddressname.setText(mAddress.getAddressName());
                mTextviewHousenum.setText(mAddress.getHouseNo());
                mTextviewAreaname.setText(mAddress.getAreaName());
                mTextviewLandmark.setText(mAddress.getLandmark());
                mTextviewCity.setText(mAddress.getCity());
                mTextviewState.setText(mAddress.getState());
                mTextviewPincode.setText(mAddress.getPincode());
                break;
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.payment_bt_submit:
                showProgressDialog();
                saveOrder();
                createNotif();
                sendMessageResource(ResourceMobileNumber);
                sendMessageCustomer(user.getMobileNumber());
//                SendConfirmationMessage(ResourceMobileNumber,user.getMobileNumber());
        }
    }

    private void createNotif() {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(PaymentActivity.this);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setContentTitle("New Message");
        mBuilder.setContentText("Resource booked successfully.");
        mBuilder.setTicker("New Booking Alert!");
        mBuilder.setSound(defaultSoundUri);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);

        mBuilder.setNumber(1);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        String[] events = new String[3];
        events[0] = new String("Your have succesfully booked "+ResourceName+"");
        events[1] = new String("for "+ResourceType+".");
        events[2] = new String("Contact at: "+ResourceMobileNumber+"");

        inboxStyle.setBigContentTitle("Booking Details:");

        for (int i = 0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }
        mBuilder.setStyle(inboxStyle);

        Intent resultIntent = new Intent(PaymentActivity.this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(PaymentActivity.this);
        stackBuilder.addParentStack(PaymentActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int Notifid = (int) System.currentTimeMillis();
        mNotificationManager.notify(Notifid, mBuilder.build());
    }


    private void sendMessageCustomer(long mobileNumber){
        Log.v("Msg123","Message: "+mobileNumber);
        String authkey="163975ALgKqHIMr1K595ce022";
        //Multiple mobiles numbers separated by comma
        String mobiles=String.valueOf(mobileNumber);
        //Sender ID,While using route4 sender id should be 6 characters long.
        String senderId="WaiApp";
        //Your message to send, Add URL encoding here.
        String message="Your have succesfully booked "+ResourceName+" for "+ResourceType+". Contact at: "+ResourceMobileNumber+"";
        //define route
        String route="4";
        String mainUrl="https://control.msg91.com/api/sendhttp.php?";
        URLConnection myURLConnection=null;
        URL myURL=null;
        BufferedReader reader=null;

        String encoded_message= URLEncoder.encode(message);

        StringBuilder sbPostData=new StringBuilder(mainUrl);
        sbPostData.append("authkey="+authkey);
        sbPostData.append("&mobiles="+mobiles);
        sbPostData.append("&message="+encoded_message);
        sbPostData.append("&route="+route);
        sbPostData.append("&sender="+senderId);

        mainUrl=sbPostData.toString();
        try{
        myURL=new URL(mainUrl);
        myURLConnection=myURL.openConnection();
        myURLConnection.connect();
        reader=new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
        String response;
        while((response=reader.readLine())!=null)
        System.out.println(response);

        reader.close();
        }catch(IOException e){
        e.printStackTrace();
        }
        }

private void sendMessageResource(Long resourceMobileNumber){
        Log.v("Msg123","Message: "+resourceMobileNumber);
        String authkey="163975ALgKqHIMr1K595ce022";
        //Multiple mobiles numbers separated by comma
        String mobiles=String.valueOf(resourceMobileNumber);
        //Sender ID,While using route4 sender id should be 6 characters long.
        String senderId="WaiApp";
        //Your message to send, Add URL encoding here.
        String message="You have been booked by "+String.format("%s %s",user.getFirstName(),user.getLastName())+". Contact at: "+user.getMobileNumber()+"";
        //define route
        String route="4";
        String mainUrl="https://control.msg91.com/api/sendhttp.php?";
        URLConnection myURLConnection=null;
        URL myURL=null;
        BufferedReader reader=null;

        String encoded_message=URLEncoder.encode(message);

        StringBuilder sbPostData=new StringBuilder(mainUrl);
        sbPostData.append("authkey="+authkey);
        sbPostData.append("&mobiles="+mobiles);
        sbPostData.append("&message="+encoded_message);
        sbPostData.append("&route="+route);
        sbPostData.append("&sender="+senderId);

        mainUrl=sbPostData.toString();
        try{
        myURL=new URL(mainUrl);
        myURLConnection=myURL.openConnection();
        myURLConnection.connect();
        reader=new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
        String response;
        while((response=reader.readLine())!=null)
        System.out.println(response);

        reader.close();
        }catch(IOException e){
        e.printStackTrace();
        }

        }

private void SendDetailsMessageUser(long mobileNumber){
        HttpPost httppost=new HttpPost(
        "https://api.twilio.com/2010-04-01/Accounts/ACa5ff1aeb91ffc0f7247b6651680df2f2/SMS/Messages");
        String base64EncodedCredentials="Basic "
        + Base64.encodeToString(
        (ACCOUNT_SID+":"+AUTH_TOKEN).getBytes(),
        Base64.NO_WRAP);

        httppost.setHeader("Authorization",
        base64EncodedCredentials);
        try{

        List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("From",
        "+14158552534"));
        nameValuePairs.add(new BasicNameValuePair("To",
        "+91"+mobileNumber+""));
        nameValuePairs.add(new BasicNameValuePair("Body",
        "Your have succesfully booked "+ResourceName+" for "+ResourceType+".Contact at: "+ResourceMobileNumber+""));

        httppost.setEntity(new UrlEncodedFormEntity(
        nameValuePairs));

        // Execute HTTP Post Request
        HttpResponse response=httpclient.execute(httppost);
        HttpEntity entity=response.getEntity();
        System.out.println("Entity post is: "
        + EntityUtils.toString(entity));


        }catch(ClientProtocolException e){

        }catch(IOException e){

        }
        }

private void SendConfirmationMessage(Long resourceMobileNumber,long mobileNumber){
        HttpPost httppost=new HttpPost(
        "https://api.twilio.com/2010-04-01/Accounts/ACa5ff1aeb91ffc0f7247b6651680df2f2/SMS/Messages");
        String base64EncodedCredentials="Basic "
        +Base64.encodeToString(
        (ACCOUNT_SID+":"+AUTH_TOKEN).getBytes(),
        Base64.NO_WRAP);

        httppost.setHeader("Authorization",
        base64EncodedCredentials);
        try{
        List<NameValuePair> nameValuePairs2=new ArrayList<NameValuePair>();
        nameValuePairs2.add(new BasicNameValuePair("From",
        "+14158552534"));
        nameValuePairs2.add(new BasicNameValuePair("To",
        "+91"+mobileNumber+""));
        nameValuePairs2.add(new BasicNameValuePair("Body",
        "You have succesfully booked "+ResourceName+" for "+ResourceType+".Contact at: "+ResourceMobileNumber+""));

        httppost.setEntity(new UrlEncodedFormEntity(
        nameValuePairs2));


        List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("From",
        "+14158552534"));
        nameValuePairs.add(new BasicNameValuePair("To",
        "+91"+resourceMobileNumber+""));
        nameValuePairs.add(new BasicNameValuePair("Body",
        "You have been booked by "+String.format("%s %s",user.getFirstName(),user.getLastName())+".Contact at: "+user.getMobileNumber()+""));

        httppost.setEntity(new UrlEncodedFormEntity(
        nameValuePairs));

        // Execute HTTP Post Request
        HttpResponse response=httpclient.execute(httppost);
        HttpEntity entity=response.getEntity();
        System.out.println("Entity post is: "
        +EntityUtils.toString(entity));


        }catch(ClientProtocolException e){

        }catch(IOException e){

        }
        }

private void saveOrder(){
        int id=mRadioGroupPayment.getCheckedRadioButtonId();
        String _paymentMode=null;
        switch(id){
        case R.id.payment_rb_mode_COD:
        Toast.makeText(PaymentActivity.this,"You selected Cash on delivery",Toast.LENGTH_SHORT).show();
        _paymentMode="COD";
        break;
        case R.id.payment_rb_mode_paytm:
        Toast.makeText(PaymentActivity.this,"You selected paytm",Toast.LENGTH_SHORT).show();
        _paymentMode="PAYTM";
        break;
        case R.id.payment_rb_mode_payu:
        Toast.makeText(PaymentActivity.this,"You selected paytu",Toast.LENGTH_SHORT).show();
        _paymentMode="PAYU";
        break;
        }
        mOrder.setPaymentMode(_paymentMode);
        mOrder.setOrderStatus(Constants.ORDER_STATUS_ORDERED);
        mOrder.setOrderProgressStatus(Constants.ORDER_PROGRESS_STATUS_RESOURCE_CONFIRMATION_AWAITED);
        HashMap<String, Object> orderbookingTime=new HashMap<>();
        orderbookingTime.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        mOrder.setOrderbookingTime(orderbookingTime);

        mOrderKey=mDatabase.child(Constants.FIREBASE_CHILD_USER_ORDERS).push().getKey();

        mDatabase.child(Constants.FIREBASE_CHILD_USER_ORDERS).child(UID).child(mOrderKey).setValue(mOrder).addOnCompleteListener(new OnCompleteListener<Void>(){
@Override
public void onComplete(@NonNull Task<Void> task){
        if(!task.isSuccessful()){
        Toast.makeText(PaymentActivity.this,"Order saving failed",Toast.LENGTH_SHORT).show();
        }else{
        Toast.makeText(PaymentActivity.this,"Order Saved successfully",Toast.LENGTH_SHORT).show();
        updateUserOrderHistory();
        updateResourceOrderHistory();
final OrderKey orderKey=new OrderKey();
        orderKey.setId(UID);
        orderKey.setOrderkey(mOrderKey);
        realm.executeTransaction(new Realm.Transaction(){
@Override
public void execute(Realm realm){
        realm.copyToRealmOrUpdate(orderKey);
        }
        });
        startActivity(new Intent(PaymentActivity.this,OrderConfirmActivity.class).putExtra("orderKey",mOrderKey).putExtra("orderId",mOrder.getOrderId()));
        closeProgressDialog();
        }
        }
        });

        }

private void updateResourceOrderHistory(){
        mDatabase.child(Constants.FIREBASE_CHILD_RESOURCE_ORDERS).child(mresourceKey).child(mOrderKey).setValue(mOrder);
        }

private void updateUserOrderHistory(){
        mDatabase.child(Constants.FIREBASE_CHILD_ORDER_DETAILS).child(mOrderKey).child(Constants.FIREBASE_CHILD_ORDER).setValue(mOrder);
//        mDatabase.child(Constants.FIREBASE_CHILD_ORDER_DETAILS).child(mOrderKey).child(Constants.FIREBASE_CHILD_ORDER_AMOUNT).setValue(mOrderAmount);
        mDatabase.child(Constants.FIREBASE_CHILD_ORDER_DETAILS).child(mOrderKey).child(Constants.FIREBASE_CHILD_ADDRESS).setValue(mAddress);
        switch(mOrderType){
        case Constants.ORDER_TYPE_CLEANING:
        CleaningOrderAmountValues cleaningOrderAmountValues=realm.where(CleaningOrderAmountValues.class).equalTo("OrderId",mOrderId).findFirst();
        mDatabase.child(Constants.FIREBASE_CHILD_ORDER_DETAILS).child(mOrderKey).child(Constants.FIREBASE_CHILD_ORDER_AMOUNT).setValue(cleaningOrderAmountValues);
        break;
        case Constants.ORDER_TYPE_COOKING:
        CookingOrderAmountValues cookingOrderAmountValues=realm.where(CookingOrderAmountValues.class).equalTo("OrderId",mOrderId).findFirst();
        mDatabase.child(Constants.FIREBASE_CHILD_ORDER_DETAILS).child(mOrderKey).child(Constants.FIREBASE_CHILD_ORDER_AMOUNT).setValue(cookingOrderAmountValues);
        break;
        case Constants.ORDER_TYPE_WASHING:
        WashingOrderAmountValues washingOrderAmountValues=realm.where(WashingOrderAmountValues.class).equalTo("OrderId",mOrderId).findFirst();
        mDatabase.child(Constants.FIREBASE_CHILD_ORDER_DETAILS).child(mOrderKey).child(Constants.FIREBASE_CHILD_ORDER_AMOUNT).setValue(washingOrderAmountValues);
        break;
        }

        }

@Override
public void onBackPressed(){
        ExitAlertDialogFragment exitAlertDialogFragment=new ExitAlertDialogFragment();
        exitAlertDialogFragment.show(getSupportFragmentManager(),DIALOG_ALERT);
        }

@Override
public void ExitOrder(Boolean exit){
        if(exit){
        startActivity(new Intent(PaymentActivity.this,MainActivity.class));
        }
        }

        void showProgressDialog(){
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("Saving...");
        mProgressDialog.show();
        }

        void closeProgressDialog(){
        if(mProgressDialog.isShowing()){
        mProgressDialog.dismiss();
        }
        }
        }