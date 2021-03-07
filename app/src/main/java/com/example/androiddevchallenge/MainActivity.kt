/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.util.isNumber

class MainActivity : AppCompatActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                Surface(color = MaterialTheme.colors.background) {
                    AppState()
                }
            }
        }
    }
}

private lateinit var timer: CountDownTimer

// Start building your app here!
@ExperimentalAnimationApi
@Composable
fun MyApp(text: MutableState<String>) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val error = rememberSaveable { mutableStateOf(false) }
        val timerText = rememberSaveable { mutableStateOf("") }
        val timerSet = rememberSaveable { mutableStateOf(false) }

        AnimatedVisibility(visible = !timerSet.value) {
            Text(
                text = stringResource(id = R.string.txt_set_timer),
                style = MaterialTheme.typography.h1
            )
        }
        AnimatedVisibility(visible = !timerSet.value) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(16.dp),
                    value = text.value,
                    onValueChange = {
                        text.value = it
                        error.value = (!it.isNumber() && !it.equals(""))
                    },
                    label = { Text(stringResource(R.string.txt_hint)) },
                    isError = error.value
                )
                Text(
                    text = if (text.value.isNotEmpty()) stringResource(id = R.string.txt_seconds)
                    else stringResource(
                        id = R.string.txt_second
                    ),
                )
            }
        }
        AnimatedVisibility(visible = !timerSet.value) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val timerIsSet = timerSet.value
                        if (timerIsSet) {
                            timer.cancel()
                            timerText.value = ""
                            timerSet.value = false
                        } else {
                            startTimer(
                                timerText = timerText,
                                count = text.value.toLong() * 1000,
                                timerSet = timerSet
                            )
                            timer.start()
                            timerSet.value = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    enabled = !error.value
                ) {
                    val timerIsSet = timerSet.value
                    Text(
                        text = stringResource(id = R.string.btn_go),
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
        AnimatedVisibility(visible = timerSet.value) {
            Text(
                text = timerText.value,
                modifier = Modifier
                    .padding(16.dp),
                style = MaterialTheme.typography.h1
            )
        }

        AnimatedVisibility(visible = timerSet.value) {
            Button(
                onClick = {
                    timer.cancel()
                    timerSet.value = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.btn_cancel),
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

private fun startTimer(
    timerText: MutableState<String>,
    count: Long,
    timerSet: MutableState<Boolean>
) {
    timer = object : CountDownTimer(count, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            timerText.value = (millisUntilFinished / 1000L).toString()
        }

        override fun onFinish() {
            timerText.value = ""
            timerSet.value = false
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun AppState() {
    val text = rememberSaveable { mutableStateOf("") }
    MyApp(text = text)
}

@ExperimentalAnimationApi
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        AppState()
    }
}

@ExperimentalAnimationApi
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        AppState()
    }
}
