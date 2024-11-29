package uz.sample.featuredeliveryapp.utils

import android.content.Context
import android.util.Log
import com.google.android.play.core.splitinstall.SplitInstallException
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

sealed interface DynamicDeliveryState {
    data object Downloading : DynamicDeliveryState
    data object DownloadCompleted : DynamicDeliveryState
    data object InstallSuccess : DynamicDeliveryState
    data class Failed(val errorMessage: String) : DynamicDeliveryState
}

class DynamicModuleDownloadController(context: Context) {

    private val splitInstallManager by lazy { SplitInstallManagerFactory.create(context) }
    private var mySessionId = 0

    fun isModuleDownloaded(moduleName: String): Boolean {
        val isDownloaded = splitInstallManager.installedModules.contains(moduleName)
        Log.wtf(TAG, "isDownloaded $isDownloaded")
        return isDownloaded
    }

    fun downloadDynamicModule(moduleName: String): Flow<DynamicDeliveryState> = callbackFlow {
        val request = SplitInstallRequest.newBuilder()
            .addModule(moduleName)
            .build()

        val listener = SplitInstallStateUpdatedListener { state ->
            Log.wtf(TAG, "SplitInstallStateUpdatedListener $state")
            handleInstallStates(state, this)
        }

        splitInstallManager.registerListener(listener)

        // Start installation
        splitInstallManager.startInstall(request)
            .addOnSuccessListener { sessionId ->
                mySessionId = sessionId
                Log.d(TAG, "Install started with session ID: $sessionId")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Install failed: ${e.message}")
                val errorCode = (e as SplitInstallException).errorCode
                val errorMessage = getErrorMessage(errorCode)
                trySend(DynamicDeliveryState.Failed(errorMessage))
            }

        awaitClose {
            splitInstallManager.unregisterListener(listener)
        }
    }

    private fun handleInstallStates(
        state: SplitInstallSessionState,
        flowCollector: kotlinx.coroutines.channels.SendChannel<DynamicDeliveryState>
    ) {
        if (state.sessionId() == mySessionId) {
            when (state.status()) {
                SplitInstallSessionStatus.DOWNLOADING -> {
                    Log.wtf("", "handleInstallStates DOWNLOADING")
                    flowCollector.trySend(DynamicDeliveryState.Downloading)
                }

                SplitInstallSessionStatus.DOWNLOADED -> {
                    flowCollector.trySend(DynamicDeliveryState.DownloadCompleted)
                }

                SplitInstallSessionStatus.INSTALLED -> {
                    Log.d(TAG, "Dynamic Module downloaded")
                    flowCollector.trySend(DynamicDeliveryState.InstallSuccess)
                }

                SplitInstallSessionStatus.FAILED -> {
                    flowCollector.trySend(DynamicDeliveryState.Failed("Installation failed"))
                }

                SplitInstallSessionStatus.CANCELED -> {
                    flowCollector.trySend(DynamicDeliveryState.Failed("Installation Cancelled"))
                }
            }
        }
    }

    private fun getErrorMessage(errorCode: Int): String {
        return when (errorCode) {
            SplitInstallErrorCode.NETWORK_ERROR -> "No internet found"
            SplitInstallErrorCode.MODULE_UNAVAILABLE -> "Module unavailable"
            SplitInstallErrorCode.ACTIVE_SESSIONS_LIMIT_EXCEEDED -> "Active session limit exceeded"
            SplitInstallErrorCode.INSUFFICIENT_STORAGE -> "Insufficient storage"
            SplitInstallErrorCode.PLAY_STORE_NOT_FOUND -> "Google Play Store Not Found!"
            else -> "Something went wrong! Try again later"
        }
    }

    companion object {
        private const val TAG = "DynamicModuleDownloadController"
    }
}