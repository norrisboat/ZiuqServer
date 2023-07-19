package com.norrisboat.data.models.quiz

import kotlinx.serialization.Serializable

@Serializable
data class QuizCategory(val name: String, val key: String)

enum class Difficulty {
    Easy,
    Medium,
    Hard
}

@Serializable
data class QuestionType(val name: String, val key: String)

val questionCategories = listOf(
    QuizCategory("Science", "science"),
    QuizCategory("Film And TV", "film_and_tv"),
    QuizCategory("Music", "music"),
    QuizCategory("History", "history"),
    QuizCategory("Geography", "geography"),
    QuizCategory("Art And Literature", "art_and_literature"),
    QuizCategory("Sport And Leisure", "sport_and_leisure"),
    QuizCategory("General Knowledge", "general_knowledge"),
    QuizCategory("Science", "science"),
    QuizCategory("Food And Drink", "food_and_drink"),
    QuizCategory("Random", "random"),
)

val questionTypes = listOf(
    QuestionType("Text", "text_choice"),
    QuestionType("Image", "image_choice")
)

@Serializable
data class QuizSetupData(
    val categories: List<QuizCategory>,
    val difficulties: List<Difficulty>,
    val types: List<QuestionType>
)

val defaultSetupData = QuizSetupData(questionCategories, Difficulty.values().toList(), questionTypes)