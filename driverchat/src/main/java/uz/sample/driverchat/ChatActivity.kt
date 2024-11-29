package uz.sample.driverchat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.sample.featuredeliveryapp.ui.theme.FeatureDeliveryAppTheme

@SuppressLint("RestrictedApi")
class ChatActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FeatureDeliveryAppTheme {
                RenderImages {
                    finish()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RenderImages(backClicked: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        stickyHeader {
            SupportTopBar { backClicked() }
        }
        items(
            listOf(
                R.drawable.ic_driver_banner,
                R.drawable.and_logo_1,
                R.drawable.and_logo_2,
                R.drawable.and_logo_3,
                R.drawable.and_logo_4,
                R.drawable.and_logo_5,
                R.drawable.and_logo_6,
                R.drawable.and_pic_7,
                R.drawable.and_pic_8,
                R.drawable.and_pic_9,
                R.drawable.and_pic_10,
                R.drawable.and_pic_11,
            )
        ) { resId: Int ->
            Spacer(modifier = Modifier.size(16.dp))
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = painterResource(id = resId),
                contentScale = ContentScale.Crop,
                contentDescription = "",
            )
        }
    }
}

@Composable
internal fun SupportTopBar(
    backClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onSurface)
            .statusBarsPadding()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 4.dp,
                bottom = 8.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.onBackground)
                .size(40.dp)
                .clickable {
                    backClicked()
                },
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "",
            contentScale = ContentScale.Inside,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            text = stringResource(id = R.string.chat_module_title),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Modifier.size(48.dp)
    }
}