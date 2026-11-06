// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.settings

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import helium314.keyboard.latin.R
import helium314.keyboard.latin.utils.Theme
import helium314.keyboard.latin.utils.UncachedInputMethodManagerUtils
import helium314.keyboard.latin.utils.previewDark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private val doneGreen = Color(0xFF2E9E5B)

/**
 * Simple setup: 1) enable the keyboard in system settings, 2) pick it as the active keyboard, 3) try it out.
 * Android does not allow an app to enable its own keyboard, so the user must do steps 1 and 2 (one tap each);
 * progress is detected automatically.
 */
@Composable
fun WelcomeWizard(
    close: () -> Unit,
    finish: () -> Unit
) {
    val ctx = LocalContext.current
    val imm = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val appName = stringResource(ctx.applicationInfo.labelRes)
    var enabled by remember { mutableStateOf(UncachedInputMethodManagerUtils.isThisImeEnabled(ctx, imm)) }
    var current by remember { mutableStateOf(UncachedInputMethodManagerUtils.isThisImeCurrent(ctx, imm)) }
    var autoPickerShown by rememberSaveable { mutableStateOf(false) }
    var testText by rememberSaveable { mutableStateOf("") }

    // detect progress automatically (also when coming back from system settings or the picker)
    LaunchedEffect(Unit) {
        while (true) {
            withContext(Dispatchers.IO) {
                enabled = UncachedInputMethodManagerUtils.isThisImeEnabled(ctx, imm)
                current = UncachedInputMethodManagerUtils.isThisImeCurrent(ctx, imm)
            }
            delay(300)
        }
    }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        // back from system settings: if the keyboard is now enabled, directly offer the picker
        scheduleAutoPicker(ctx, imm) { autoPickerShown }.let { shouldShow ->
            if (shouldShow) autoPickerShown = true
        }
    }
    val step = when {
        !enabled -> 1
        !current -> 2
        else -> 3
    }

    Surface(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(painterResource(R.drawable.ic_logo), null, Modifier.size(88.dp))
            Text(
                stringResource(R.string.uz_setup_headline),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Text(
                stringResource(R.string.uz_setup_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(4.dp))

            StepCard(
                number = 1, done = step > 1, active = step == 1,
                title = stringResource(R.string.uz_setup_step1_title),
                hint = stringResource(R.string.uz_setup_step1_hint, appName),
            ) {
                Button(
                    onClick = {
                        val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS).addCategory(Intent.CATEGORY_DEFAULT)
                        launcher.launch(intent)
                    },
                    Modifier.fillMaxWidth().height(52.dp)
                ) { Text(stringResource(R.string.uz_setup_step1_button), style = MaterialTheme.typography.titleMedium) }
            }
            StepCard(
                number = 2, done = step > 2, active = step == 2,
                title = stringResource(R.string.uz_setup_step2_title),
                hint = stringResource(R.string.uz_setup_step2_hint, appName),
            ) {
                Button(
                    onClick = { imm.showInputMethodPicker() },
                    Modifier.fillMaxWidth().height(52.dp)
                ) { Text(stringResource(R.string.uz_setup_step2_button), style = MaterialTheme.typography.titleMedium) }
            }
            StepCard(
                number = 3, done = false, active = step == 3,
                title = stringResource(R.string.uz_setup_step3_title),
                hint = stringResource(R.string.uz_setup_step3_hint),
            ) {
                OutlinedTextField(
                    value = testText,
                    onValueChange = { testText = it },
                    label = { Text(stringResource(R.string.uz_setup_test_label)) },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
                Button(onClick = close, Modifier.fillMaxWidth().height(52.dp)) {
                    Text(stringResource(R.string.uz_setup_continue), style = MaterialTheme.typography.titleMedium)
                }
                OutlinedButton(onClick = finish, Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.uz_setup_close))
                }
            }
        }
    }
}

/** Offers the picker once right after returning from system settings, if the keyboard has been enabled there. */
private fun scheduleAutoPicker(ctx: Context, imm: InputMethodManager, alreadyShown: () -> Boolean): Boolean {
    if (alreadyShown()) return false
    if (!UncachedInputMethodManagerUtils.isThisImeEnabled(ctx, imm)) return false
    if (UncachedInputMethodManagerUtils.isThisImeCurrent(ctx, imm)) return false
    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({ imm.showInputMethodPicker() }, 400)
    return true
}

@Composable
private fun StepCard(
    number: Int,
    done: Boolean,
    active: Boolean,
    title: String,
    hint: String,
    content: @Composable () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(if (active) scheme.primaryContainer else scheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                Modifier.size(32.dp).clip(CircleShape)
                    .background(if (done) doneGreen else if (active) scheme.primary else scheme.outline),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (done) "✓" else number.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
            }
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = if (active) scheme.onPrimaryContainer else scheme.onSurface,
            )
        }
        if (active) {
            Text(hint, style = MaterialTheme.typography.bodyLarge, color = scheme.onPrimaryContainer)
            content()
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Theme(previewDark) {
        Surface {
            WelcomeWizard({}) { }
        }
    }
}
