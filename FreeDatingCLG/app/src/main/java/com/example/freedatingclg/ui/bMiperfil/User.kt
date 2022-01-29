package com.example.freedatingclg.ui.bMiperfil

data class User (
    //correo : "clgg88@gmail.com"
    //atributos : {
    //    ,edad: 30
    //    ,estatura: 180
    //    ,peso: 80
    //    ,localizacion: "Terrassa"
    //    ,formación: "Ingeniero"
    //    ,ingresos: 15000
    //    ,drogas sociales: {
    //        tabaco: true
    //        alcohol: false
    //        alucinogenos: true
    //    }
    //    ,religion: "Ateao"
    //    ,vacunadoCovid: true
    //    ,descripcion : "Soy muy amigo de mis amigos"
    //}

    var correo: String,
    var nombreCompleto: String,
    var edad: String,
    var estatura: String,
    var peso: String,
    var localizacion: String,
    var formacion: String,
    var ingresos: String,
    var dsTabaco: String,
    var dsAlcohol: String,
    var dsAlucinogenos: String,
    var religion: String,
    var vacunadoCovid: String,
    var bio: String
    //val correo: String,
    //val edad: Int,
    //val estatura: Int,
    //val peso: Int,
    //val localizacion: String,
    //val formacion: String,
    //val ingresos: Int,
    //val dsTabaco: Boolean,
    //val dsAlcohol: Boolean,
    //val dsAlucinogenos: Boolean,
    //val religion: String,
    //val vacunadoCovid: Boolean,
    //val bio: String

    ){
        constructor() : this("","","","","","","","","","","","","", "")
    }
