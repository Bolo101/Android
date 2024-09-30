package com.example.qcm0

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qcm0.ui.theme.Qcm0Theme
import androidx.compose.ui.Alignment


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Qcm0Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val userName = remember { mutableStateOf("") }
    val currentQuestionIndex = remember { mutableStateOf(0) }
    val questionList = getQuestionList()
    val score = remember { mutableStateOf(0) }
    val isFinished = remember { mutableStateOf(false) }

    if (isFinished.value) {
        // Afficher les résultats
        ResultScreen(score.value, questionList.size, modifier)
    } else {
        if (userName.value.isEmpty()) {
            // Écran d'accueil pour saisir le nom
            WelcomeScreen { name ->
                userName.value = name
            }
        } else {
            // Afficher la question
            val question = questionList[currentQuestionIndex.value]
            QuestionScreen(question, { selectedAnswer ->
                if (selectedAnswer == question.correctAnswerIndex) {
                    score.value++
                }
                if (currentQuestionIndex.value < questionList.size - 1) {
                    currentQuestionIndex.value++
                } else {
                    isFinished.value = true
                }
            }, modifier)
        }
    }
}

@Composable
fun WelcomeScreen(onNameEntered: (String) -> Unit) {
    var nameInput by remember { mutableStateOf("") }

    // Layout d'accueil
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Entrez votre nom:")
        TextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("Nom") }
        )
        Button(
            onClick = { onNameEntered(nameInput) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6200EE), // Couleur de fond du bouton
                contentColor = Color.White // Couleur du texte du bouton
            )
        ) {
            Text(text = "Commencer")
        }
    }
}

@Composable
fun QuestionScreen(question: Question, onAnswerSelected: (Int) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = question.question)
        question.options.forEachIndexed { index, option ->
            Button(
                onClick = { onAnswerSelected(index) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF03DAC5), // Couleur de fond du bouton
                    contentColor = Color.White // Couleur du texte du bouton
                )
            ) {
                Text(text = option)
            }
        }
    }
}

@Composable
fun ResultScreen(score: Int, totalQuestions: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(), // Remplit tout l'espace disponible
        verticalArrangement = Arrangement.Center, // Centre verticalement
        horizontalAlignment = Alignment.CenterHorizontally // Centre horizontalement
    ) {
        Text(
            text = "Vous avez obtenu ${score} sur $totalQuestions!",
            modifier = Modifier.padding(16.dp) // Ajoute un espacement autour du texte
        )
    }
}


// Modèle de question
data class Question(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

// Liste de questions
fun getQuestionList(): List<Question> {
    return listOf(
        Question("Quelle est la variété de pomme de terre utilisée pour faire des frites?",
            listOf("Bintje", "Golden", "Charlotte"), 0),
        Question("Quel nutriment est particulièrement riche dans les pommes de terre?",
            listOf("Protéines", "Glucides", "Lipides"), 1),
        Question("Quelle est la principale région de culture de pommes de terre en France?",
            listOf("Bretagne", "Provence", "Normandie"), 0)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    Qcm0Theme {
        MainScreen()
    }
}
