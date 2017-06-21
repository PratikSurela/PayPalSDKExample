package com.app.easypaypal.interfaces;

import com.app.easypaypal.model.PayPalResultData;

/**
 * Created by Pratik Surela on 20/6/17.
 */

public interface PayPalResult {
    public void onPayPalResult(PayPalResultData resultData);
}