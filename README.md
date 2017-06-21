<h1>PayPal SDK Example</h1>

<p>This is a simple example of paypal integration in andorid using &quot;easypaypal&quot; module.</p>

<p><strong>Step 1 :</strong>&nbsp;import module <strong>&quot;easypaypal&quot;</strong> into your project.</p>

<p><strong>Step 2 :&nbsp;</strong>Declare and initilize <strong>&quot;PayPalPaymentMain&quot;</strong> class and than in onCreate() method</p>

<p style="margin-bottom: 0.11cm; line-height: 100%"><code>payPalPaymentMain = new PayPalPaymentMain(context, PayPalConfiguration.ENVIRONMENT_SANDBOX, getString(R.string.paypal_client_id));<br />
payPalPaymentMain.onPayPalPayment(strProductPrice, &quot;USD&quot;, strProductName, context);</code></p>

<p style="margin-bottom: 0.11cm; line-height: 100%"><strong>Step 3:</strong> override <strong>&quot;onActivityResult()&quot;</strong> and do following&nbsp;</p>

<pre class="western">
<code>payPalPaymentMain.setPaymentResponse(requestCode, resultCode, data);</code></pre>

<p style="margin-bottom: 0.11cm; line-height: 100%"><strong>Step 4 :</strong> implements <strong>&quot;PayPalResult&quot;</strong> interface into your activity/fragment and override method <strong>&quot;onPayPalResult(PayPalResultData resultData)&quot;</strong>.</p>

<p style="margin-bottom: 0.11cm; line-height: 100%"><strong>Step 5 :&nbsp;</strong>Use paypal details like</p>

<pre class="western">
<code>resultData.getPayPalAmount();</code>
</pre>

<p>Use all the data like this and enjoy....:)</p>
