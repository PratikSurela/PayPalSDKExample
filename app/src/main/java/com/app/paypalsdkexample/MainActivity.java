package com.app.paypalsdkexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.easypaypal.PayPalPaymentMain;
import com.app.easypaypal.interfaces.PayPalResult;
import com.app.easypaypal.model.PayPalResultData;
import com.paypal.android.sdk.payments.PayPalConfiguration;

public class MainActivity extends AppCompatActivity implements PayPalResult {

    private Button btnBuy;
    private TextView txtProductName, txtProductPrice;
    private String strProductName = "", strProductPrice = "", TAG = "MainActivity";
    private PayPalPaymentMain payPalPaymentMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payPalPaymentMain = new PayPalPaymentMain(MainActivity.this, PayPalConfiguration.ENVIRONMENT_SANDBOX, getString(R.string.paypal_client_id));
                payPalPaymentMain.onPayPalPayment(strProductPrice, "USD", strProductName, MainActivity.this);
            }
        });
    }

    private void initViews() {

        btnBuy = (Button) findViewById(R.id.btnBuy);
        txtProductName = (TextView) findViewById(R.id.txtProductName);
        txtProductPrice = (TextView) findViewById(R.id.txtProductPrice);
        strProductName = txtProductName.getText().toString();
        strProductPrice = txtProductPrice.getText().toString();

        Log.e(TAG, "initViews: strProductName : " + strProductName);
        Log.e(TAG, "initViews: strProductPrice : " + strProductPrice);

        txtProductName.setText("Product Name : " + strProductName);
        txtProductPrice.setText("Product Price : $" + strProductPrice);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        payPalPaymentMain.setPaymentResponse(requestCode, resultCode, data);
    }

    @Override
    public void onPayPalResult(PayPalResultData resultData) {
        Log.e("MainActivity", "onPayPalResult: getPayPalAmount : " + resultData.getPayPalAmount());
        Log.e("MainActivity", "onPayPalResult: getActualTime : " + resultData.getActualTime());
        Log.e("MainActivity", "onPayPalResult: getDate : " + resultData.getDate());
        Log.e("MainActivity", "onPayPalResult: getPayPalDesc : " + resultData.getPayPalDesc());
        Log.e("MainActivity", "onPayPalResult: getPayPalTime : " + resultData.getPayPalTime());
        Log.e("MainActivity", "onPayPalResult: getTransId : " + resultData.getTransId());
    }
}