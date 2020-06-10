package bildverarbeitung.dotjp.qrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {
    /*  QR Code Text




    APL von Josef Prothmann und David Oliver Taube.
    Studiengang Angewandte Informatik
    6. Semester

    Testbeispiel eines QR Codes


    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void OpenGenerate(View view) {
        Intent browserIntent = new Intent(this,Generate.class);
        startActivity(browserIntent);
    }

    public void OpenScan(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("QR-Code scannen");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setCaptureActivity(CaptureActivityPortrait.class);
        integrator.setOrientationLocked(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Dialog(result.getContents());
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void Dialog(String msg) {
        String message = msg;
        if (message.startsWith("http://") || message.startsWith("https://")) {
            new AlertDialog.Builder(this)
                    .setTitle("Gescannte Daten")
                    .setMessage(message)
                    .setPositiveButton("Webseite besuchen", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String uri = "";
                                    if (!message.startsWith("http://") && !message.startsWith("https://")) {
                                        uri = "http://" + message;
                                    } else {
                                        uri = msg;
                                    }
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                    startActivity(browserIntent);
                                }
                            }
                    ).setNegativeButton("Schließen", null)
                    .show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Gescannte Daten")
                    .setMessage(msg)
                    .setNegativeButton("Schließen", null)
                    .show();
        }
    }
}
