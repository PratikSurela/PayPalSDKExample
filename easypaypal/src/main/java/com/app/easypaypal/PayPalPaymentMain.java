package com.app.easypaypal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.app.easypaypal.interfaces.PayPalResult;
import com.app.easypaypal.model.PayPalResultData;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by Pratik Surela on 19/6/17.
 */

public class PayPalPaymentMain extends Activity {

    private Context context;
    private String price, paymentType, paymentTitle;

    //Paypal code Starts
    //private static String CONFIG_ENVIRONMENT = ENVIRONMENT_SANDBOX;
    //private static String CONFIG_CLIENT_ID = "ATVRJ1pOjGxdmJURLoexa7MtQ1f-UbEAOLngR5wMhvNX43ndns0vn-W_fLtyy2FOr7762DwLaAxtmpye";

    private static final String TAG = "PayPalPaymentMain";
    private static String CONFIG_ENVIRONMENT = "";
    private static String CONFIG_CLIENT_ID = "";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private static PayPalConfiguration config;

    private String payPalAmount = "", payPalDesc = "", transId = "", payPalTime = "";
    private PayPalResult payPalResult;
    //Paypal code Ends

    public PayPalPaymentMain(Context context, String payPalConfiguration, String clientId) {
        this.context = context;
        this.CONFIG_ENVIRONMENT = payPalConfiguration;
        this.CONFIG_CLIENT_ID = clientId;

        Log.e(TAG, "PayPalPaymentMain: CONFIG_CLIENT_ID : " + this.CONFIG_CLIENT_ID);
        Log.e(TAG, "PayPalPaymentMain: CONFIG_ENVIRONMENT : " + this.CONFIG_ENVIRONMENT);
        initPayPal();
    }

    private void initPayPal() {

        config = new PayPalConfiguration()
                .environment(CONFIG_ENVIRONMENT)
                .clientId(CONFIG_CLIENT_ID)
                // The following are only used in PayPalFuturePaymentActivity.
                .merchantName("Example Merchant")
                .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
                .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

        Intent intent = new Intent(context, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        context.startService(intent);
        this.payPalResult = (PayPalResult) context;
    }

    public void onPayPalPayment(String price, String paymentType, String paymentTitle, PayPalResult payPalResult) {
        this.price = price;
        this.paymentType = paymentType;
        this.paymentTitle = paymentTitle;
        this.payPalResult = payPalResult;
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(context, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal(price), paymentType, paymentTitle, paymentIntent);
    }

    public void setPaymentResponse(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));

                        JSONObject amountJson = confirm.getPayment().toJSONObject();

                        payPalAmount = amountJson.getString("amount");
                        payPalDesc = amountJson.getString("short_description");

                        JSONObject payment = confirm.toJSONObject();
                        if (payment != null) {
                            JSONObject objResponse = payment.getJSONObject("response");
                            if (objResponse != null) {
                                if (!objResponse.isNull("state")) {
                                    String state = objResponse.getString("state");
                                    if (state.equals("approved")) {
                                        if (!objResponse.isNull("id")) {
                                            transId = objResponse.getString("id");
                                            payPalTime = objResponse.getString("create_time");

                                            String date = payPalTime.substring(0, payPalTime.indexOf('T'));
                                            String dummyTime = payPalTime.substring(payPalTime.indexOf('T'), payPalTime.indexOf('Z'));
                                            String[] actualTime = dummyTime.split("T");
                                            payPalTime = date + " " + actualTime[1];

                                            /*Log.e(TAG, "onActivityResult: Paypal Amount :" + payPalAmount); //30
                                            Log.e(TAG, "onActivityResult: Paypal Desc :" + payPalDesc); //Pen
                                            Log.e(TAG, "onActivityResult: trans_id : " + transId); //PAY-9NP13038H2744115CLFEPERA
                                            Log.e(TAG, "onActivityResult: create_time : " + payPalTime); //2017-06-20 10:00:51
                                            Log.e(TAG, "onActivityResult: Date :" + date); //2017-06-20
                                            Log.e(TAG, "onActivityResult: Time :" + actualTime[1]); //10:00:51*/

                                            PayPalResultData payPalResultData = new PayPalResultData();
                                            payPalResultData.setPayPalAmount(payPalAmount);
                                            payPalResultData.setPayPalDesc(payPalDesc);
                                            payPalResultData.setTransId(transId);
                                            payPalResultData.setPayPalTime(payPalTime);
                                            payPalResultData.setDate(date);
                                            payPalResultData.setActualTime(actualTime[1]);

                                            payPalResult.onPayPalResult(payPalResultData);
                                        }
                                    }
                                }
                            }
                        }

                        Log.e(TAG, "PaymentConfirmation info received from PayPal ");

                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Log.e(TAG, "Future Payment code received from PayPal");

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("ProfileSharingExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Log.e(TAG, "Profile Sharing code received from PayPal");

                    } catch (JSONException e) {
                        Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("ProfileSharingExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "ProfileSharingExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

        /**
         * TODO: Send the authorization response to your server, where it can
         * exchange the authorization code for OAuth access and refresh tokens.
         *
         * Your server must then store these tokens, so that your server code
         * can execute payments for this user in the future.
         *
         * A more complete example that includes the required app-server to
         * PayPal-server integration is available from
         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
         */

    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}