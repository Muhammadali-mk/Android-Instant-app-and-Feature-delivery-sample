package uz.sample.featuredeliveryapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import uz.sample.featuredeliveryapp.ui.theme.FeatureDeliveryAppTheme
import uz.sample.featuredeliveryapp.utils.DownloadModuleConfirmationDialog
import uz.sample.featuredeliveryapp.utils.DynamicDeliveryState
import uz.sample.featuredeliveryapp.utils.DynamicModuleDownloadController
import uz.sample.featuredeliveryapp.utils.LoadingDialog

class MainActivity : ComponentActivity() {

    private lateinit var downloadController: DynamicModuleDownloadController
    private var dynamicModuleDownloadState = mutableStateOf("Chat Dynamic module state:\n")
    private var showDownloadChatFeatureDialog = mutableStateOf(false)
    private var isLoading by mutableStateOf(false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        downloadController = DynamicModuleDownloadController(baseContext)
        setContent {
            FeatureDeliveryAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ContentBody(Modifier.padding(innerPadding))
                }
            }
        }
    }

    private fun downloadDynamicModule() {
        lifecycleScope.launch {
            downloadController.downloadDynamicModule(NAME_CHAT_DYNAMIC_MODULE)
                .collect { state ->
                    when (state) {
                        is DynamicDeliveryState.DownloadCompleted -> {
                            isLoading = false
                            dynamicModuleDownloadState.value = " Download completed!"
                        }

                        DynamicDeliveryState.Downloading -> {
                            isLoading = true
                            dynamicModuleDownloadState.value = " Downloading module..."
                        }

                        is DynamicDeliveryState.Failed -> {
                            isLoading = false
                            dynamicModuleDownloadState.value = " Error: ${state.errorMessage}"
                        }

                        DynamicDeliveryState.InstallSuccess -> {
                            dynamicModuleDownloadState.value = " Installed successfully!"
                            isLoading = false
                            showDriverChatScreen()
                        }
                    }
                }
        }
    }

    @Composable
    private fun ContentBody(modifier: Modifier) {
        LaunchedEffect(Unit) {
            isLoading = false
        }
        Column(modifier = modifier.fillMaxSize()) {
            Image(
                painterResource(R.drawable.feature_delivery_app),
                contentDescription = "eCommerce app banner",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillWidth
            )
            Text(
                "Sample Driver App",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            )
            IconButton(
                onClick = {
                    openDriverChatFeature()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_chat),
                        "Chat with Driver",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Support Chat",
                        style = TextStyle(color = Color.White, fontSize = 16.sp)
                    )
                }
            }
            LazyColumn {
                item {
                    Text(
                        text = dynamicModuleDownloadState.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
            }
            DownloadModuleConfirmationDialog(showDownloadChatFeatureDialog, ::downloadDynamicModule)
            LoadingDialog(isLoading = isLoading)
        }
    }

    private fun openDriverChatFeature() {
        if (downloadController.isModuleDownloaded(NAME_CHAT_DYNAMIC_MODULE)) {
            Log.wtf("DynamicModuleDownloadController", " isModuleDownloaded  YEAH ")
            showDriverChatScreen()
        } else {
            Log.wtf("DynamicModuleDownloadController", " isModuleDownloaded  NOT ")
            showDownloadChatFeatureDialog.value = true
        }
    }

    private fun showDriverChatScreen() {
        val intent = Intent().apply {
            setClassName(
                this@MainActivity.packageName,
                CLASS_NAME_DRIVER_CHAT_ACTIVITY
            )
        }
        startActivity(intent)
    }

    companion object {
        private const val NAME_CHAT_DYNAMIC_MODULE = "driverchat"
        private const val CLASS_NAME_DRIVER_CHAT_ACTIVITY = "uz.sample.driverchat.ChatActivity"
    }
}