package jeniavrilia.polbeng.ac.id.sqliteexample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import jeniavrilia.polbeng.ac.id.sqliteexample.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_STUDENT = "extra_student"
    }

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var studentDBHelper: StudentDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get the passed student data
        val studentData = intent.getParcelableExtra<StudentModel>(EXTRA_STUDENT) as StudentModel

        // Populate fields with student data
        binding.etNIM.isEnabled = false
        binding.etNIM.setText(studentData.nim)
        binding.etNama.setText(studentData.name)
        binding.etUmur.setText(studentData.age)

        studentDBHelper = StudentDBHelper(this)

        // Update button click listener
        binding.btnUpdate.setOnClickListener {
            val nim = binding.etNIM.text.toString()
            val name = binding.etNama.text.toString()
            val age = binding.etUmur.text.toString()

            if (nim.isEmpty() || name.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "Silahkan masukkan data NIM, Nama, dan Umur!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedStudent = StudentModel(nim = nim, name = name, age = age)
            val updateCount = studentDBHelper.updateStudent(updatedStudent)

            if (updateCount > 0) {
                Toast.makeText(this, "Mahasiswa yang terupdate: $updateCount", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Tidak ada data mahasiswa yang diupdate, silahkan coba lagi!", Toast.LENGTH_SHORT).show()
            }
        }

        // Delete button click listener
        binding.btnHapus.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Konfirmasi Hapus Data")
                .setMessage("Apakah anda yakin?")
                .setPositiveButton("Ya") { _, _ ->
                    val nim = binding.etNIM.text.toString()
                    val deleteCount = studentDBHelper.deleteStudent(nim)

                    if (deleteCount > 0) {
                        Toast.makeText(this, "Mahasiswa yang terhapus: $deleteCount", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Tidak ada data mahasiswa yang dihapus, silahkan coba lagi!", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Tidak") { _, _ ->
                    // User cancelled the dialog
                }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    override fun onDestroy() {
        studentDBHelper.close()
        super.onDestroy()
    }

    private fun setUpdateState(state: Boolean) {
        binding.etNIM.isEnabled = !state
        binding.etNama.isEnabled = state
        binding.etUmur.isEnabled = state
        binding.btnUpdate.isEnabled = state
        binding.btnHapus.isEnabled = state
    }
}
