package com.dji.sdk.vizzbee.demo.battery;

import android.content.Context;
import com.dji.sdk.vizzbee.R;
import com.dji.sdk.vizzbee.internal.controller.DJISampleApplication;
import com.dji.sdk.vizzbee.internal.view.BasePushDataView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dji.common.battery.BatteryState;

/**
 * Class for getting the battery information.
 */
public class PushBatteryDataView extends BasePushDataView {
    public PushBatteryDataView(Context context) {
        super(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        try {
            DJISampleApplication.getProductInstance().getBattery().setStateCallback(new BatteryState.Callback() {
                @Override
                public void onUpdate(BatteryState djiBatteryState) {
                    stringBuffer.delete(0, stringBuffer.length());

                    stringBuffer.append("BatteryEnergyRemainingPercent: ").
                        append(djiBatteryState.getChargeRemainingInPercent()).
                                    append("%\n");
                    stringBuffer.append("CurrentVoltage: ").
                        append(djiBatteryState.getVoltage()).append("mV\n");
                    stringBuffer.append("CurrentCurrent: ").
                        append(djiBatteryState.getCurrent()).append("mA\n");
                    FirebaseDatabase bat = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = bat.getReference("user/per");
                    DatabaseReference dbref2 = bat.getReference("user/vol");
                    DatabaseReference dbref3 = bat.getReference("user/cur");


                    dbref.setValue(djiBatteryState.getChargeRemainingInPercent());
                    dbref2.setValue(djiBatteryState.getVoltage());
                    dbref3.setValue(djiBatteryState.getCurrent());

                    showStringBufferResult();
                }
            });
        } catch (Exception ignored) {
                System.out.println(ignored);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        try {
            DJISampleApplication.getProductInstance().getBattery().setStateCallback(null);
        } catch (Exception ignored) {

        }
    }
    @Override
    public int getDescription() {
        return R.string.battery_listview_push_info;
    }
    public void OnCreate()
    {

        FirebaseDatabase bat = FirebaseDatabase.getInstance();
        DatabaseReference dbref = bat.getReference("user/per");
        dbref.setValue(4);
        DatabaseReference dbref2 = bat.getReference("user/vol");
        dbref2.setValue(5);
        DatabaseReference dbref3 = bat.getReference("user/cur");
        dbref3.setValue(6);
    }
}