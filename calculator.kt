package com.example.calculette0

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculette0.ui.theme.Calculette0Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Calculette0Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    CalculatorApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CalculatorApp(modifier: Modifier = Modifier) {
    var displayText by remember { mutableStateOf("0") }
    var operand1 by remember { mutableStateOf("") }
    var operand2 by remember { mutableStateOf("") }
    var operation by remember { mutableStateOf<String?>(null) }
    var result by remember { mutableStateOf<Double?>(null) }

    fun onNumberClick(number: String) {
        if (operation == null) {
            operand1 += number
            displayText = operand1
        } else {
            operand2 += number
            displayText = operand2
        }
    }

    fun onOperationClick(op: String) {
        if (operand1.isNotEmpty()) {
            operation = op
        }
    }

    fun calculateResult() {
        if (operand1.isNotEmpty() && operand2.isNotEmpty() && operation != null) {
            val num1 = operand1.toDouble()
            val num2 = operand2.toDouble()
            result = when (operation) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "*" -> num1 * num2
                "/" -> if (num2 != 0.0) num1 / num2 else null
                else -> null
            }
            displayText = result?.toString() ?: "Error"
            operand1 = ""//remise à zéro des variables
            operand2 = ""
            operation = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bolo calculateur rénal",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = displayText,//affichage du résultat
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Green)
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))// espacement des boutons de saisie
        //Création
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            NumberButton("1", onClick = { onNumberClick("1") })
            NumberButton("2", onClick = { onNumberClick("2") })
            NumberButton("3", onClick = { onNumberClick("3") })
            OperationButton("+", onClick = { onOperationClick("+") })
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            NumberButton("4", onClick = { onNumberClick("4") })
            NumberButton("5", onClick = { onNumberClick("5") })
            NumberButton("6", onClick = { onNumberClick("6") })
            OperationButton("-", onClick = { onOperationClick("-") })
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            NumberButton("7", onClick = { onNumberClick("7") })
            NumberButton("8", onClick = { onNumberClick("8") })
            NumberButton("9", onClick = { onNumberClick("9") })
            OperationButton("*", onClick = { onOperationClick("*") })
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            NumberButton("0", onClick = { onNumberClick("0") })
            OperationButton("C", onClick = {
                operand1 = ""
                operand2 = ""
                operation = null
                displayText = "0"
            })
            OperationButton("=", onClick = { calculateResult() })
            OperationButton("/", onClick = { onOperationClick("/") })
        }
    }
}

@Composable
fun NumberButton(number: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.size(80.dp)) {
        Text(text = number, fontSize = 24.sp)
    }
}

@Composable
fun OperationButton(op: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.size(80.dp)) {
        Text(text = op, fontSize = 24.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorAppPreview() {
    Calculette0Theme {
        CalculatorApp()
    }
}
