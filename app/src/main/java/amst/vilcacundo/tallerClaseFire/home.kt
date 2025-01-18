package amst.vilcacundo.tallerClaseFire

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class home : AppCompatActivity() {

    private lateinit var edtValor: EditText
    private lateinit var btnEnviar: Button
    private lateinit var txtMostrar: TextView
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        edtValor = findViewById(R.id.edt_valor)
        btnEnviar = findViewById(R.id.btn_enviar)
        txtMostrar = findViewById(R.id.txt_mostrar)

        mDatabase = FirebaseDatabase.getInstance().reference

        btnEnviar.setOnClickListener {
            val valor = edtValor.text.toString().trim()

            // Verifica que el valor no esté vacío
            if (valor.isNotEmpty()) {
                // Crea una referencia en Firebase y guarda el valor
                mDatabase.child("valor").setValue(valor)

                // Limpia el campo de texto
                edtValor.text.clear()
            }
        }

        mDatabase.child("valor").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                // Obtiene el valor actual y actualiza el TextView
                val valor = snapshot.getValue(String::class.java) ?: "No hay valor"
                txtMostrar.text = valor
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                // Maneja el error si ocurre
                txtMostrar.text = "Error al leer datos."
            }
        })
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}