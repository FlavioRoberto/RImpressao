package com.rimpressao.rimpressao_library.Impressao;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.rimpressao.rimpressao_library.Helpers.Dispositivo;
import com.rimpressao.rimpressao_library.QrCodeGenerator;

import java.util.List;

import cielo.sdk.order.PrinterListener;
import cielo.sdk.printer.PrinterManager;

public class CieloLio {
    private Context context;
    private Dispositivo dispositivo;

    public CieloLio(Context context) {
        this.context = context;
        dispositivo = new Dispositivo(context);
    }

    public void imprimir(List<Linha> texto) {
        if (!dispositivo.isCompativelImpressao()) {
            Toast.makeText(context, "Modelo não compatível com impressão!", Toast.LENGTH_LONG).show();
            return;
        }

        PrinterManager printerManager = new PrinterManager(context);
        PrinterListener printerListener = new PrinterListener() {
            @Override
            public void onPrintSuccess() {
                Toast.makeText(context, "Sucesso", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(@Nullable Throwable throwable) {
                if (throwable != null)
                    Toast.makeText(context, "Erro: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onWithoutPaper() {
                Toast.makeText(context, "Sem papel", Toast.LENGTH_LONG).show();
            }
        };

        for (Linha linha : texto) {
            if (linha.getTexto().length() > 0)
                printerManager.printText(linha.getTexto(), linha.getEstilo(), printerListener);
            if (linha.getImagem() != null)
                printerManager.printImage(linha.getImagem(), linha.getEstilo(), printerListener);
        }
    }


}
