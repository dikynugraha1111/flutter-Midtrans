package com.example.try_midtrans

import android.widget.Toast
import androidx.annotation.NonNull
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity(), TransactionFinishedCallback {
    companion object{
        const val CHANNEL = "com.midtrans.flutter"
        const val KEY_NATIVE = "showPaymentMidtrans"
    }

    override
    fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            if(call.method == KEY_NATIVE){
//                val nama =(""+call.argument("nama"))
//                val qty = (""+call.argument("qty")).toInt()
//                val price = (""+call.argument("price")).toInt()
//                val total = qty * price.toDouble()
                val price2 = 10000.0
                val qty2 = 2
                val total2 = 20000.0
                initMidTrans()

                //Create Transaction Request
                val transactionRequest = TransactionRequest("Diky"+System.currentTimeMillis().toString()+"",total2)

                //Create Item Detail's
                val item = ItemDetails("NamaItemId",price2,qty2,"KOTLIN")
                val itemDetails = ArrayList<ItemDetails>()
                itemDetails.add(item)
                transactionRequest.itemDetails = itemDetails

                //Create Customer Detail Object
                uiKitsDetails(transactionRequest)
                MidtransSDK.getInstance().transactionRequest = transactionRequest
                MidtransSDK.getInstance().startPaymentUiFlow(this)
            }
            else{
                result.notImplemented()
            }

        }
    }
    //  Deklarasi UiKitsDetails
    fun uiKitsDetails(transactionRequest: TransactionRequest){
        //Masih menggunakan metode statis value, kalau sudah masuk deploy program gunakan
        //metode dinamis, dengan memasukan value ke dalam parameter function
        val customerDetails = CustomerDetails()
        customerDetails.customerIdentifier = "DikyPro"
        customerDetails.phone = "08977901651"
        customerDetails.firstName = "Diky"
        customerDetails.lastName = "Nugraha"
        customerDetails.email = "dikynugraha1111@gmail.com"

        val shippingAddress = ShippingAddress()
        shippingAddress.address = "Magetan,Jawa Timur"
        shippingAddress.city = "Magetan"
        shippingAddress.postalCode = "63319"
        customerDetails.shippingAddress = shippingAddress

        val billingAddress = BillingAddress()
        billingAddress.address = "Magetan,Jawa Timur"
        billingAddress.city = "Magetan"
        billingAddress.postalCode = "63319"
        customerDetails.billingAddress = billingAddress

        transactionRequest.customerDetails = customerDetails
    }

    //    Deklarasi awal Midtrans
    fun initMidTrans(){
        SdkUIFlowBuilder.init()
                .setClientKey(BuildConfig.MERCHANT_CLIENT_KEY)
                .setContext(applicationContext)
                .setTransactionFinishedCallback(this)
                .setMerchantBaseUrl(BuildConfig.MERCHANT_BASE_URL) //Isi dengan file PHP (fork di git) dan telah di hosting di web. Cantumkan URL disini
                .enableLog(true)
                .setColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
                .buildSDK()
    }
    override fun onTransactionFinished(result: TransactionResult) {
        if (result.response != null) {
            when(result.status) {
                TransactionResult.STATUS_SUCCESS -> Toast.makeText(this, "Transaksi Selesai Id: ${result.response.transactionId}", Toast.LENGTH_SHORT).show()
                TransactionResult.STATUS_PENDING-> Toast.makeText(this, "Transaksi Menunggu Id: ${result.response.transactionId}", Toast.LENGTH_SHORT).show()
                TransactionResult.STATUS_FAILED -> Toast.makeText(this, "Transaksi Gagal Id: ${result.response.transactionId}", Toast.LENGTH_SHORT).show()
            }
            result.response.validationMessages
        } else if (result.isTransactionCanceled){
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_SHORT).show()
        } else if (result.status.equals(TransactionResult.STATUS_INVALID, ignoreCase = true)){
            Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this, "Transaction Finished with failure", Toast.LENGTH_SHORT).show()
        }
    }
}
