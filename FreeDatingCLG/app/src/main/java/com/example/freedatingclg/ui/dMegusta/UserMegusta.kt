package com.example.freedatingclg.ui.dMegusta

data class UserMegusta (
    /**
     *
     * private var imagen : ImageView = itemView.findViewById(R.id.imagen)
     * private var nombre : TextView = itemView.findViewById(R.id.tvNombre)
     * private var edad : TextView = itemView.findViewById(R.id.tvEdad)
     * //private var correo
     * private var localizacion : TextView = itemView.findViewById(R.id.tvLocalizacion)
     */
        var imagen: String
        ,var nombre: String
        ,var edad : String
        ,var correo: String
        ,var localizacion: String

        ){
constructor()  : this("","","","","")
}
