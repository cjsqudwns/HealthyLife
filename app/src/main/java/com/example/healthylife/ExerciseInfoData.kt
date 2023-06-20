package com.example.healthylife

class ExerciseInfoData (var day: String, var startTime: String, var exercise_area: String,
                        var minute: Int, var memo: String, var check: Boolean){
    constructor():this("noinfo", "noinfo", "noinfo",
    0, "noinfo", false)
}