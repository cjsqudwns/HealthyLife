package com.example.healthylife

import java.io.Serializable

class ExerciseInfoData (var day: String, var startTime: String, var exercise_area: String,
                        var minute: Int, var memo: String, var check: Boolean) : Serializable{
    constructor():this("noinfo", "noinfo", "noinfo",
    0, "noinfo", false)
}