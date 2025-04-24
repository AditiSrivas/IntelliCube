package com.example.disproject

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val questions = remember {
        listOf(
            QuizQuestion(
                question = "What is the primary function of an agent in an interactive system?",
                options = listOf(
                    "A) To store user data",
                    "B) To act upon its environment based on sensory inputs",
                    "C) To monitor internet usage",
                    "D) To display advertisements"
                ),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                question = "Which of the following is not a type of agent mentioned in the lecture?",
                options = listOf(
                    "A) Reminder agent",
                    "B) Guide agent",
                    "C) Shopping agent",
                    "D) Surrogate agent"
                ),
                correctAnswerIndex = 2
            ),
            QuizQuestion(
                question = "Which decade popularized the use of artificial agents at the interface in HCI?",
                options = listOf(
                    "A) 1980s",
                    "B) 1990s",
                    "C) 2000s",
                    "D) 2010s"
                ),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                question = "What is the main limitation of computers in interpreting human behavior?",
                options = listOf(
                    "A) Slow processing",
                    "B) Limited battery life",
                    "C) Limited view of user actions",
                    "D) Memory overflow"
                ),
                correctAnswerIndex = 2
            ),
            QuizQuestion(
                question = "What type of agent is tailored to an individual's preferences and habits?",
                options = listOf(
                    "A) Domain agent",
                    "B) Personal agent",
                    "C) Collaborative agent",
                    "D) Predictive agent"
                ),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                question = "What do adaptive systems require to interact with another system?",
                options = listOf(
                    "A) Access to internet",
                    "B) Pre-written commands",
                    "C) A representation or model of the other system",
                    "D) Robotic hardware"
                ),
                correctAnswerIndex = 2
            ),
            QuizQuestion(
                question = "Which of the following best describes a thermostat in terms of adaptive systems?",
                options = listOf(
                    "A) Complex learning agent",
                    "B) Predictive system",
                    "C) Rule-based reactive system",
                    "D) Cognitive companion"
                ),
                correctAnswerIndex = 2
            ),
            QuizQuestion(
                question = "What is the purpose of the person model in agent architecture?",
                options = listOf(
                    "A) To define colors and layouts",
                    "B) To store domain knowledge",
                    "C) To model user behavior and goals",
                    "D) To store dialogue sequences"
                ),
                correctAnswerIndex = 2
            ),
            QuizQuestion(
                question = "The interaction model in agents includes which of the following?",
                options = listOf(
                    "A) Physical interfaces only",
                    "B) Dialogue record and knowledge base",
                    "C) Only voice inputs",
                    "D) GUI design principles"
                ),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                question = "What aspect of the agent models eye movement and speech characteristics?",
                options = listOf(
                    "A) Person model",
                    "B) Domain model",
                    "C) Interaction model",
                    "D) Agent shell"
                ),
                correctAnswerIndex = 2
            ),
            QuizQuestion(
                question = "What is the goal of embodied conversational agents (ECAs)?",
                options = listOf(
                    "A) Increase system speed",
                    "B) Perform complex calculations",
                    "C) Develop emotional engagement and support",
                    "D) Translate text to speech"
                ),
                correctAnswerIndex = 2
            ),
            QuizQuestion(
                question = "Ananova is an example of what kind of system?",
                options = listOf(
                    "A) Intelligent tutoring system",
                    "B) Search engine",
                    "C) Conversational agent",
                    "D) Mobile robot"
                ),
                correctAnswerIndex = 2
            ),
            QuizQuestion(
                question = "What differentiates a cat from a care assistant in the context of companions?",
                options = listOf(
                    "A) Lifespan",
                    "B) Utility and specific function",
                    "C) Appearance",
                    "D) Sound and motion capabilities"
                ),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                question = "Why is emotional design important in companions?",
                options = listOf(
                    "A) Increases device memory",
                    "B) Enhances data transfer speed",
                    "C) Fosters self-expression and stable interaction",
                    "D) Reduces battery consumption"
                ),
                correctAnswerIndex = 2
            ),
            QuizQuestion(
                question = "According to Reeves and Nass, people prefer systems that:",
                options = listOf(
                    "A) Have no personality",
                    "B) Match their own personalities",
                    "C) Avoid social interactions",
                    "D) Provide raw data only"
                ),
                correctAnswerIndex = 1
            )
        )
    }

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var showAnswer by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LinearProgressIndicator(
                progress = (currentQuestionIndex + 1).toFloat() / questions.size,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Question ${currentQuestionIndex + 1} of ${questions.size}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            val currentQuestion = questions[currentQuestionIndex]

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = currentQuestion.question,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    currentQuestion.options.forEachIndexed { index, option ->
                        val isCorrectAnswer = index == currentQuestion.correctAnswerIndex
                        val backgroundColor = if (showAnswer && isCorrectAnswer) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        }

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = backgroundColor
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = option,
                                    modifier = Modifier.weight(1f)
                                )

                                if (showAnswer && isCorrectAnswer) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "Correct Answer",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (showAnswer) {
                Text(
                    text = "The correct answer is: ${currentQuestion.options[currentQuestion.correctAnswerIndex]}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        if (currentQuestionIndex > 0) {
                            currentQuestionIndex--
                            showAnswer = false
                        }
                    },
                    enabled = currentQuestionIndex > 0,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Previous")
                }

                Button(
                    onClick = {
                        if (!showAnswer) {
                            showAnswer = true
                        } else if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                            showAnswer = false
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (!showAnswer) "Show Answer" else if (currentQuestionIndex < questions.size - 1) "Next" else "Finish")
                }
            }

            if (showAnswer && currentQuestionIndex == questions.size - 1) {
                Button(
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Return to Documents")
                }
            }
        }
    }
}