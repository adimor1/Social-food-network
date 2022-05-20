package com.example.andro2client.model

    public class Recipe{
         var time:String = ""
             get() = field
        var level:String = ""
            get() = field


        constructor(time:String,level:String) {
            this.time = time
            this.level = level
        }



        constructor()
    }
