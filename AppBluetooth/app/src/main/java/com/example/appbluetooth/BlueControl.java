package com.example.appbluetooth;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class BlueControl extends AppCompatActivity {

    ImageButton btnTb1, btnTb2, btnDisc;
    TextView txt1, txtMAC;

    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    Set<BluetoothDevice> pairedDevices1;

    String address = null;
    private ProgressDialog progress;

    int flaglamp1 = 0;
    int flaglamp2 = 0;

    static final UUID myUUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(MainActivity.EXTRA_ADDRESS); // receive address
        setContentView(R.layout.activity_control);

        // Ánh xạ
        btnTb1 = findViewById(R.id.btnTb1);
        btnTb2 = findViewById(R.id.btnTb2);
        txt1   = findViewById(R.id.textV1);
        txtMAC = findViewById(R.id.textViewMAC);
        btnDisc = findViewById(R.id.btnDisc);

        // Kết nối Bluetooth
        new ConnectBT().execute();

        btnTb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thietBi1();
            }
        });

        btnTb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thietBi7();
            }
        });

        btnDisc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disconnect();
            }
        });
    }

    // Thiết bị 1
    private void thietBi1() {
        if (btSocket != null) {
            try {
                if (this.flaglamp1 == 0) {
                    this.flaglamp1 = 1;
                    this.btnTb1.setBackgroundResource(R.drawable.btn1);
                    btSocket.getOutputStream().write("1".getBytes());
                    txt1.setText("Thiết bị số 1 đang bật");
                    return;
                } else {
                    if (this.flaglamp1 != 1) return;

                    this.flaglamp1 = 0;
                    this.btnTb1.setBackgroundResource(R.drawable.tb1off);
                    btSocket.getOutputStream().write("A".getBytes());
                    txt1.setText("Thiết bị số 1 đang tắt");
                    return;
                }
            } catch (IOException e) {
                msg("Lỗi");
            }
        }
    }

    // Thiết bị 7
    private void thietBi7() {
        if (btSocket != null) {
            try {
                if (this.flaglamp2 == 0) {
                    this.flaglamp2 = 1;
                    this.btnTb2.setBackgroundResource(R.drawable.tb2on);
                    btSocket.getOutputStream().write("7".getBytes());
                    txt1.setText("Thiết bị số 7 đang bật");
                    return;
                } else {
                    if (this.flaglamp2 != 1) return;

                    this.flaglamp2 = 0;
                    this.btnTb2.setBackgroundResource(R.drawable.tb2off);
                    btSocket.getOutputStream().write("G".getBytes());
                    txt1.setText("Thiết bị số 7 đang tắt");
                    return;
                }
            } catch (IOException e) {
                msg("Lỗi");
            }
        }
    }

    // Ngắt kết nối
    private void Disconnect() {
        if (btSocket != null) {   // If the btSocket is busy
            try {
                btSocket.close(); // close connection
            } catch (IOException e) {
                msg("Lỗi");
            }
        }
        finish(); // return to the first layout
    }

    // Hiển thị Toast
    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    // Danh sách thiết bị đã ghép đôi
    private void pairedDevicesList1() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
        ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        pairedDevices1 = myBluetooth.getBondedDevices();

        if (pairedDevices1.size() > 0) {
            for (BluetoothDevice bt : pairedDevices1) {
                txtMAC.setText(bt.getName() + " - " + bt.getAddress());
            }
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Không tìm thấy thiết bị kết nối.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    // AsyncTask kết nối Bluetooth
    private class ConnectBT extends AsyncTask<Void, Void, Void> {

        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(
                    BlueControl.this,
                    "Đang kết nối...",
                    "Xin vui lòng đợi!!!!"
            );
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo =
                            myBluetooth.getRemoteDevice(address);

                    if (ActivityCompat.checkSelfPermission(
                            BlueControl.this,
                            Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED) {
                        return null;
                    }

                    btSocket = dispositivo
                            .createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Kết nối thất bại! Kiểm tra thiết bị.");
                finish();
            } else {
                msg("Kết nối thành công.");
                isBtConnected = true;
                pairedDevicesList1();
            }

            if (progress != null) {
                progress.dismiss();
            }
        }
    }
}
