package com.example.freedatingclg.ui.bMiperfil

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.freedatingclg.R
import com.example.freedatingclg.databinding.FragmentMiperfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MiPerfilFragment : Fragment() {

    private lateinit var galleryViewModel: MiPerfilViewModel
    private var _binding: FragmentMiperfilBinding? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProvider(this).get(MiPerfilViewModel::class.java)

        _binding = FragmentMiperfilBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Firebase auth, db y storage
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios")

        binding.bActualizar.setOnClickListener {

            //Instanciamos un nuevo usuario
            val correo: String = ""
            val nombreCompleto: String = binding.etNombre.text.toString()
            val edad: String = binding.etEdad.text.toString()
            val estatura: String = binding.etEstatura.text.toString()
            val peso: String = binding.etPeso.text.toString()
            val localizacion: String = binding.etLocalizacion.text.toString()
            //val formacion: String = binding.etFormacion.text.toString()
            //val ingresos: String = binding.etIngresos.text.toString()
            //val dsTabaco: String = binding.etDsTabaco.text.toString()
            //val dsAlcohol: String = binding.etDsAlcohol.text.toString()
            //val dsAlucinogenos: String = binding.etDsAlucinogenos.text.toString()
            //val religion: String = ""
            //val vacunadoCovid: String = ""
            val bio: String = binding.etBio.text.toString()

            val user = User(correo,nombreCompleto,edad,estatura,peso,localizacion,"formacion","ingresos","dsTabaco","dsAlcohol","dsAlucinogenos","religion","vacunadoCovid",bio)
            println("0. Usuario creado ")

            if (uid != null){
                println("1. El user no es null")
                databaseReference.child(uid).setValue(user).addOnCompleteListener{
                    println("2. Ha conseguido escribir")
                    if (it.isSuccessful){
                        uploadFoto()
                    }else{
                        Toast.makeText(activity,"Failed to update profile",Toast.LENGTH_SHORT).show()
                    }
                }


            }

        }

        // Usuario logado
        val user: String? = FirebaseAuth.getInstance().currentUser?.displayName
        if(!user.isNullOrEmpty()){
            binding.tvCorreo.text = user
        }else{
            binding.tvCorreo.text = "Usuario desconocido"
        }

        return root
    }

    private fun uploadFoto() {
        //imageUri = Uri.parse("android.resource://$packagename/${R.drawable.foto2.PNG}")
        imageUri = Uri.parse("android.resource://com.example.freedatingclg/${R.drawable.foto2}")
        storageReference = FirebaseStorage.getInstance().getReference("Usuarios/"+auth.currentUser?.uid)
        storageReference.putFile(imageUri).addOnSuccessListener {
            println("3. Ha subido bien la foto")
            Toast.makeText(activity,"Subida correctamente",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            println("4. No ha subido bien la foto")
            Toast.makeText(activity,"No se ha podido subir la foto",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}