package com.example.hangman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HangmanGame()
        }
    }
}

@Composable
fun HangmanGame() {
    // Uses the stored list of words in the hangwords.xml
    val words = stringArrayResource(id = R.array.hangwords)
    // Randomises the word on reset
    var word by remember { mutableStateOf(words.random()) }
    // Stores any guessed letters
    var guessedLetters by remember { mutableStateOf(listOf<Char>()) }
    // Stores how many guesses you have left, 2 more guesses than the word length
    var guessesLeft by remember { mutableStateOf(word.length + 2) }
    // Stores your inputted word, starting with an empty string
    var input by remember { mutableStateOf("") }

    // Displays an new iteration of the word, where if the letters are not in guessed letters
    // will appear as an underscore
    val display = word.map { if (it in guessedLetters) it else '_' }.joinToString("")

    // Column to house text and buttons
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // The display word rendered with any underscores
        Text("Word: $display",
            fontSize = 24.sp)
        Spacer(Modifier.padding(16.dp))
        // Displays how many guesses left
        Text("Guesses left: $guessesLeft",
            fontSize = 24.sp)
        Spacer(Modifier.padding(16.dp))
        // Displays word length
        Text("Word Length: ${word.length}",
            fontSize = 24.sp)
        Spacer(Modifier.padding(16.dp))
        // Text Box for guesses
        TextField(
            // Takes a string input
            value = input,
            // Only allows 1 character
            onValueChange = { input = it.take(1) },
            // Display text for text box
            label = { Text("Enter a letter") }
        )
        Spacer(Modifier.padding(16.dp))


        // Guess button for confirming letter
        Button(onClick = {
            // Submits the first character of the text box, with a null-safe option
            val guess = input.firstOrNull()
            // If a guessed letter is present then...
            if (guess != null) {
                // Adds the letter to the guessed letters
                guessedLetters = guessedLetters + guess
                // If the letter is not found in the word, it reduces your guesses left by 1 (--)
                if (guess !in word) {
                    guessesLeft--
                }
            }
            // Resets the text box to an empty string
            input = ""
        },
            modifier = Modifier
                // Button Size
                .defaultMinSize(minWidth = 150.dp, minHeight = 60.dp)) {
            // Display text for button
            Text("Guess", fontSize = 24.sp)
        }
        Spacer(Modifier.padding(16.dp))
        // Displays the guessed letters
            Text("Guessed letters: $guessedLetters",
                fontSize = 24.sp)
        // Displays "You Lose" if you have no guesses left
        if (guessesLeft == 0) {
            Spacer(Modifier.padding(16.dp))
            Text("You Lose", fontSize = 48.sp,
                fontWeight = FontWeight.Bold)
        }
        // Displays "You Win" if the display word matches the original word
        if (display == word) {
            Spacer(Modifier.padding(16.dp))
            Text("You Win", fontSize = 48.sp,
                fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.padding(16.dp))

        // Reset button, empties guessed letters, sets guesses to 5,
        // picks a new word at random and empties the input box
        Button(onClick = {
            guessedLetters = listOf()
            input = ""
            word = words.random()
            guessesLeft = word.length + 2
        }) {
            Text("Reset Game")
        }
    }
}
