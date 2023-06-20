package com.example.healthylife

class ExerciseInfoData (var day: String, var startTime: String, var exercise_area: String,
                        var minute: Int, var memo: String, var check: Boolean, var did:String){
    constructor():this("noinfo", "noinfo", "noinfo",
    0, "noinfo", false,"0")
}