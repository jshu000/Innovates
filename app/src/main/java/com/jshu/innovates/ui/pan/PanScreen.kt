package com.jshu.innovates.ui.pan

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jshu.innovates.ui.theme.InnovatesRed

@Composable
fun PanScreen(
    viewModel: PanViewModel = hiltViewModel(),
    onFinish: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "S.",
            color = InnovatesRed,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = "First of the few steps to set you up with a Bank Account",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text = "PAN NUMBER",
            fontSize = 12.sp,
            color = if (uiState.isPanValid) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = uiState.panNumber,
            onValueChange = viewModel::onPanChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 24.dp),
            placeholder = { Text("ABCDE1234F") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
            singleLine = true,
            isError = !uiState.isPanValid,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.LightGray
            )
        )

        Text(
            text = "BIRTHDATE",
            fontSize = 12.sp,
            color = if (uiState.isDayValid && uiState.isMonthValid && uiState.isYearValid) 
                MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = uiState.day,
                onValueChange = viewModel::onDayChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("DD") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = !uiState.isDayValid,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray
                )
            )
            OutlinedTextField(
                value = uiState.month,
                onValueChange = viewModel::onMonthChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("MM") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = !uiState.isMonthValid,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray
                )
            )
            OutlinedTextField(
                value = uiState.year,
                onValueChange = viewModel::onYearChange,
                modifier = Modifier.weight(1.5f),
                placeholder = { Text("YYYY") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = !uiState.isYearValid,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray
                )
            )
        }

        Spacer(modifier = Modifier.height(64.dp))

        val infoText = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                append("Providing PAN & Date of Birth helps us find and fetch your KYC from a central registry by the Government of India. ")
            }
            pushStringAnnotation(tag = "URL", annotation = "https://www.incometax.gov.in/")
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)) {
                append("Learn more")
            }
            pop()
        }
        
        ClickableText(
            text = infoText,
            style = LocalTextStyle.current.copy(fontSize = 12.sp, lineHeight = 16.sp)
        ) { offset ->
            infoText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                    context.startActivity(intent)
                }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                Toast.makeText(context, "Details submitted successfully", Toast.LENGTH_SHORT).show()
                onFinish()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = uiState.isNextEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = Color.LightGray
            ),
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Text("NEXT", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Text(
            text = "I don't have a PAN",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp)
                .clickable { onFinish() },
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
