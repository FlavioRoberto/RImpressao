package com.rimpressao.rimpressao_library.Helpers;

import android.content.Context;

import cielo.orders.domain.DeviceModel;
import cielo.sdk.info.InfoManager;

public class Dispositivo {
    private InfoManager infoManager;
    private Context context;

    public Dispositivo(Context context) {
        this.infoManager = new InfoManager();
        this.context = context;
    }

    public float bateria() {
        return infoManager.getBatteryLevel(context);
    }


    public boolean isCompativelImpressao(){
        if(DeviceModel.LIO_V2.equals(infoManager.getDeviceModel()))
            return true;
        return  false;
    }
}
