package uz.sample.instantapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.instantapps.InstantApps

@SuppressLint("RestrictedApi")
class InstantActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            uz.sample.featuredeliveryapp.ui.theme.FeatureDeliveryAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                        Text(
                            "This is  Instant App",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                        )
                        Button(onClick = {
                            showInstallPrompt()
                        }) {
                            Text(
                                "Download",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp),
                                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                            )
                        }
                    }
                }
            }
        }
    }


    private fun showInstallPrompt() {
        val postInstall = Intent(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_DEFAULT)
        //.setPackage(BuildConfig.APPLICATION_ID)

        // The request code is passed to startActivityForResult().
        InstantApps.showInstallPrompt(this, postInstall, REQUEST_CODE, /* referrer= */ null)
    }

    companion object {
        const val REQUEST_CODE = 1
    }
}
