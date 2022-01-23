package com.example.freedatingclg.ui.bMiperfil

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class MiPerfilFragment : Fragment() {

    private lateinit var galleryViewModel: MiPerfilViewModel
    private var _binding: FragmentMiperfilBinding? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri
    private lateinit var correo: String
    private var correoo: String = ""
    private lateinit var user: User

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
        correo = auth.currentUser?.email!!
        correoo = formatCorreoForPaths(correo) // Firebase Database paths must not contain '.', '#', '$', '[', or ']'

        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios")

        getProfileFromFirebase()

        binding.ibPhoto.setOnClickListener {
            selectPhoto()
        }

        binding.bActualizar.setOnClickListener {

            //Instanciamos un nuevo usuario
            //val correo: String = ""
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


            user = User(correoo,nombreCompleto,edad,estatura,peso,localizacion,"formacion","ingresos","dsTabaco","dsAlcohol","dsAlucinogenos","religion","vacunadoCovid",bio)
            //println("0. Usuario creado ")

            if (uid != null){
                //println("1. El user no es null")
                databaseReference.child(correoo).setValue(user).addOnCompleteListener{
                    //println("2. Ha conseguido escribir")
                    if (it.isSuccessful){
                        uploadFoto(correoo)
                    }else{
                        Toast.makeText(activity,"Failed to update profile",Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(activity,"No hay usuario logado",Toast.LENGTH_SHORT).show()
            }
        }

        // Usuario logado
        if(!uid.isNullOrEmpty()){
            binding.tvCorreo.text = correoo
        }else{
            binding.tvCorreo.text = "Usuario desconocido"
        }

        return root
    }

    private fun selectPhoto() {
        // de la galeria
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    private fun getProfileFromFirebase() {
        databaseReference.child(correoo).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!

                // Actualizamos con el nuevo user la UI
                //binding.imFoto
                binding.tvCorreo.text = user.correo.replace('*','.',false) //  correo.replace('.','*',false)
                binding.etNombre.hint = user.nombreCompleto
                binding.etLocalizacion.hint = user.localizacion
                binding.etEdad.hint = user.edad
                binding.etEstatura.hint = user.estatura
                binding.etPeso.hint = user.peso
                binding.etBio.hint = user.bio

                getImageFromStorage()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Error downloading profile data", Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun getImageFromStorage() {
        //storageReference = FirebaseStorage.getInstance().reference.child(correoo)
        storageReference = FirebaseStorage.getInstance().getReference("Usuarios/$correoo")
        val localFile = File.createTempFile("tempImage","png")
        storageReference.getFile(localFile).addOnSuccessListener {
            val bitmap: Bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.imFoto.setImageBitmap(bitmap)
        }.addOnFailureListener{
            Toast.makeText(activity,"Error downloading profile image",Toast.LENGTH_LONG).show()
        }
    }

    private fun uploadFoto(correo: String) {
        //imageUri = Uri.parse("android.resource://$packagename/${R.drawable.foto2.PNG}")
        imageUri = Uri.parse("android.resource://com.example.freedatingclg/${R.drawable.foto2}")
        storageReference = FirebaseStorage.getInstance().getReference("Usuarios/$correoo")
        storageReference.putFile(imageUri).addOnSuccessListener {
            //println("3. Ha subido bien la foto")
            Toast.makeText(activity,"Perfil actualizado y foto subida correctamente",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            //println("4. No ha subido bien la foto")
            Toast.makeText(activity,"No se ha podido subir la foto",Toast.LENGTH_SHORT).show()
        }
    }

    private fun formatCorreoForPaths(correo: String): String{
         return correo.replace('.','*',false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK){
            imageUri = data?.data!!
            binding.imFoto.setImageURI(imageUri)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}