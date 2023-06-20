package com.example.healthylife.data

data class ExerciseInfoData (var day: String, var startTime: String, var exercise_area: String,
                        var minute: Int, var memo: String, var check: Boolean = false)