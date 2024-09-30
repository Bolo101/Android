package com.example.qcm0

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qcm0.ui.theme.Qcm0Theme

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
    var nameInput by remember { mutableStateOf("") } // Correction ici

    // Layout d'accueil
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(text = "Entrez votre nom:")
        TextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("Nom") } // Ajout d'un label pour le champ
        )
        Button(onClick = { onNameEntered(nameInput) }) {
            Text(text = "Commencer")
        }
    }
}

@Composable
fun QuestionScreen(question: Question, onAnswerSelected: (Int) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(text = question.question)
        question.options.forEachIndexed { index, option ->
            Button(onClick = { onAnswerSelected(index) }) {
                Text(text = option)
            }
        }
    }
}

@Composable
fun ResultScreen(score: Int, totalQuestions: Int, modifier: Modifier = Modifier) {
    Text(
        text = "Vous avez obtenu ${score} sur $totalQuestions!",
        modifier = modifier.padding(16.dp)
    )
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
