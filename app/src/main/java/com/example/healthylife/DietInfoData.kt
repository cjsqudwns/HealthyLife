package com.example.healthylife

import java.io.Serializable

class DietInfoData(var day:String, var dietPart:String, var startTime: String, var calorie:Int,
                   var memoDiet:String, var carbs:Int, var protein:Int, var fat:Int, var check:Boolean) : Serializable