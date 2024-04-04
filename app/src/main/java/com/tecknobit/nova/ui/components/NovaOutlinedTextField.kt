package com.tecknobit.nova.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun NovaTextField(
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    value: MutableState<String>,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit,
    leadingIcon:  @Composable (() -> Unit)? = null,
    label: Int,
    placeholder:  @Composable (() -> Unit)? = null,
    trailingIcon:  @Composable (() -> Unit)? = {
        IconButton(
            onClick = { value.value = "" }
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null
            )
        }
    },
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    errorMessage: MutableState<String>,
    isError: MutableState<Boolean>
) {
    OutlinedTextField(
        modifier = modifier,
        singleLine = singleLine,
        value = value.value,
        visualTransformation = visualTransformation,
        onValueChange = onValueChange,
        leadingIcon = leadingIcon,
        label = {
            Text(
                text = stringResource(label)
            )
        },
        placeholder = placeholder,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        supportingText = {
            if(errorMessage.value.isNotEmpty()) {
                Text(
                    text = errorMessage.value
                )
            }
        },
        isError = isError.value
    )
}