package com.example.freedatingclg.ui.dMegusta

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freedatingclg.R
import com.example.freedatingclg.databinding.FragmentMegustaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File


class MegustaFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbref: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri
    private lateinit var correo: String
    private var correoo: String = ""

    // Dataclases recuperadas de Firebase
    private lateinit var userMg: UserMegusta
    private lateinit var unCorreo: Correo

    // RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var lista: MutableList<UserMegusta>

    private lateinit var megustaViewModel: MegustaViewModel
    private var _binding: FragmentMegustaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get correo del usuario logado:
        // Firebase auth, db y storage
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        correo = auth.currentUser?.email!!
        correoo = formatCorreoForPaths(correo) // Firebase Database paths must not contain '.', '#', '$', '[', or ']'

        // ViewModel
        megustaViewModel = ViewModelProvider(this).get(MegustaViewModel::class.java)

        // DataBinding
        _binding = FragmentMegustaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // RecyclerView -- Source: https://www.youtube.com/watch?v=wG2l0DdTEAk
        lista = getMegustaUsersFromDatabase() // para recuperar los Usuarios que te gustan en función del correo
        recyclerView = root.findViewById(R.id.recyclerView)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = MegustaAdapter(lista,R.layout.row_layout)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun formatCorreoForPaths(correo: String): String{
        return correo.replace('.','*',false)
    }


    /**
     * Devuelve una lista de usuarios que te gustan
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun getMegustaUsersFromDatabase(): MutableList<UserMegusta>{

        lista = mutableListOf<UserMegusta>()

        dbref = FirebaseDatabase.getInstance().getReference("Likes").child("LikeOut").child(correoo) // Importante que sea el correoo (formateado)

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                    for (correoSnap in snapshot.children){
                        unCorreo = snapshot.getValue(Correo::class.java)!!
                        // Tengo el correo: unCorreo.mail

                        dbref = FirebaseDatabase.getInstance().getReference("Usuarios").child(unCorreo.mail)
                        dbref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()){
                                    for (unTegusta in snapshot.children){
                                        userMg = snapshot.getValue(UserMegusta::class.java)!!
                                        lista.add(userMg)
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                println("A la mierda")
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "No te gusta nadie", Toast.LENGTH_SHORT).show()
            }
        })

        return lista
        // Esto actualiza el recyclerView
        recyclerView.adapter?.notifyDataSetChanged()
    }


    /**
     * Adapter
     * Source: https://www.youtube.com/watch?v=__gxd4IKVvk
     */
    inner class MegustaAdapter (val users: MutableList<UserMegusta>, val itemLayout : Int) : RecyclerView.Adapter<MegustaFragment.MegustaViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MegustaViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
            return MegustaViewHolder(view)
        }


        override fun getItemCount(): Int {
            return users.size
        }

        override fun onBindViewHolder(holder: MegustaViewHolder, position: Int) {
            val user = users.get(position)
            holder.updateUserMegusta(user)
        }

    }

    /**
     * ViewHolder
     * Source: https://www.youtube.com/watch?v=__gxd4IKVvk
     */
    inner class MegustaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //private var imagen : ImageView = itemView.findViewById(R.id.imagen)
        private var nombre : TextView = itemView.findViewById(R.id.tvNombre)
        private var edad : TextView = itemView.findViewById(R.id.tvEdad)
        //private var correo
        private var localizacion : TextView = itemView.findViewById(R.id.tvLocalizacion)
        private var textMessage: Button = itemView.findViewById(R.id.bChat)

        /**
         * La llamamos por cada item de la lista
         */
        fun updateUserMegusta(user : UserMegusta){

            // Mapeamos el User con los controles de la vista
            nombre.text = user.nombre
            edad.text = user.edad
            localizacion.text = user.localizacion
            correoo = user.correo // Nos descargamos la imagen con el correo --> gs://freedatingclg-2022.appspot.com/Usuarios/carlos@caca*com

            // Descargamos la imagen
            storageReference = FirebaseStorage.getInstance().getReference("Usuarios/$correoo")
            val localFile = File.createTempFile("tempImage","png")
            storageReference.getFile(localFile).addOnSuccessListener {
                val bitmap: Bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                var imagen : ImageView = itemView.findViewById(R.id.imagen)
                //binding.imFoto.setImageBitmap(bitmap)
                imagen.setImageBitmap(bitmap)
            }.addOnFailureListener{
                Toast.makeText(activity,"Error downloading profile image", Toast.LENGTH_LONG).show()
            }

        }
    }

}