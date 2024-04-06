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

/**
 * Function to display a custom [OutlinedTextField]
 *
 * @param modifier: the modifier for the component
 * @param singleLine: whether the input must be in a single line
 * @param value: the value where store the input
 * @param visualTransformation: the [VisualTransformation] to apply to the input
 * @param onValueChange: the function to call when the [value] changes
 * @param leadingIcon: the icon to display at the start of the [OutlinedTextField]
 * @param label: the label to display
 * @param placeholder: the placeholder to display
 * @param trailingIcon: the icon to display at the end of the [OutlinedTextField]
 * @param keyboardType: the type of the input to get
 * @param imeAction: the [ImeAction] to execute
 * @param errorMessage: the error message to display in the supporting text section
 * @param isError: whether the input cannot be accepted as inserted
 */
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