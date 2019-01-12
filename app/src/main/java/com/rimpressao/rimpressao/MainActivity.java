package com.rimpressao.rimpressao;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

import cielo.orders.domain.Credentials;
import cielo.orders.domain.Order;
import cielo.orders.domain.PrinterAttributes;
import cielo.sdk.order.OrderManager;
import cielo.sdk.order.PrinterListener;
import cielo.sdk.order.ServiceBindListener;
import cielo.sdk.order.payment.PaymentError;
import cielo.sdk.order.payment.PaymentListener;
import cielo.sdk.printer.PrinterManager;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private ImageView imageView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //   simulaVenda();

        editText = findViewById(R.id.editText);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imprimeQrCode(editText.getText().toString());
            }
        });


    }

    private void simulaVenda() {
        Credentials credentials = new Credentials("A1HeErU9Dvhkl79O2LOATOF0vrX78a0Svc6rCEZwTVQRrHN9sV",
                "7tHL8pQRsh67BY4fq6BKx7bkOUiPYjNUBUX1EqTrErZbIreixc");
        final OrderManager orderManager = new OrderManager(credentials, getApplicationContext());
        ServiceBindListener serviceBindListener = new ServiceBindListener() {

            @Override
            public void onServiceBoundError(Throwable throwable) {
                Toast.makeText(getApplicationContext(),
                        "Ocorreu um erro ao tentar se conectar com o serviço OrderManager: "
                                + throwable.getLocalizedMessage()
                        , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onServiceBound() {
                Order order = orderManager.createDraftOrder("REFERÊNCIA_DO_PEDIDO");
// Identificação do produto (Stock Keeping Unit)
                String sku = "2891820317391823";
                String name = "Coca-cola lata";

// Preço unitário em centavos
                int unitPrice = 550;
                int quantity = 3;

// Unidade de medida do produto String
                String unityOfMeasure = "UNIDADE";

                order.addItem(sku, name, unitPrice, quantity, unityOfMeasure);
                orderManager.placeOrder(order);
                PaymentListener paymentListener = new PaymentListener() {
                    @Override
                    public void onStart() {
                        Log.d("SDKClient", "O pagamento começou.");
                    }

                    @Override
                    public void onPayment(@NotNull Order order) {
                        Log.d("SDKClient", "Um pagamento foi realizado.");
                    }

                    @Override
                    public void onCancel() {
                        Log.d("SDKClient", "A operação foi cancelada.");
                    }

                    @Override
                    public void onError(@NotNull PaymentError paymentError) {
                        Log.d("SDKClient", "Houve um erro no pagamento.");
                    }
                };


                orderManager.checkoutOrder(order.getId(), 200, paymentListener);

            }

            @Override
            public void onServiceUnbound() {

                Toast.makeText(getApplicationContext(), " O serviço foi desvinculado", Toast.LENGTH_LONG).show();

            }
        };
        orderManager.bind(MainActivity.this, serviceBindListener);
    }

    private void imprimeQrCode(String text) {
        PrinterManager printerManager = new PrinterManager(getApplicationContext());
        PrinterListener printerListener = new PrinterListener() {
            @Override
            public void onPrintSuccess() {
                Toast.makeText(getApplicationContext(), "Sucesso", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(@Nullable Throwable throwable) {
                if (throwable != null)
                    Toast.makeText(getApplicationContext(), "Erro: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onWithoutPaper() {
                Toast.makeText(getApplicationContext(), "Sem papel", Toast.LENGTH_LONG).show();
            }
        };
        Bitmap qrCode = QrCodeGenerator.generate(text, 200, 200);
        imageView.setImageBitmap(qrCode);
        printerManager.printImage(qrCode, style(), printerListener);
    }

    private HashMap<String, Integer> style() {
        PrinterAttributes printerAttributes = new PrinterAttributes();
        HashMap<String, Integer> alignCenter = new HashMap<>();
        alignCenter.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_CENTER);
        alignCenter.put(PrinterAttributes.KEY_TYPEFACE, 1);
        alignCenter.put(PrinterAttributes.KEY_TEXT_SIZE, 20);
        return alignCenter;
    }
}
