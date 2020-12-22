package ru.ridkeim.storageexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import ru.ridkeim.storageexample.databinding.ActivityMainBinding
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var amBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        amBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(amBinding.root)
        loadFiles()
        amBinding.saveFile.setOnClickListener {
            createFile()
            loadFiles()
        }
        amBinding.fileView.setOnItemClickListener { parent, view, position, id ->
            val filename = parent.getItemAtPosition(position).toString()
            loadFile(filename)
        }

        amBinding.fileView.setOnItemLongClickListener { parent, view, position, id ->
            val filename = parent.getItemAtPosition(position).toString()
            val message = if(deleteFile(filename)){
                loadFiles()
                "Файл $filename удален"
            }else{
                "Файл $filename не удален"
            }
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
            return@setOnItemLongClickListener true
        }
    }

    private fun loadFile(filename: String) {
        val stringBuilder = StringBuilder()
        openFileInput(filename).use {
            val byteArray = ByteArray(it.available())
            while ( it.read(byteArray) != -1){
                stringBuilder.append(byteArray.decodeToString())
            }
        }
        amBinding.fileName.setText(filename)
        amBinding.fileContent.setText(stringBuilder)
    }

    private fun loadFiles(){
        amBinding.fileView.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            fileList()
        )
    }

    private fun createFile(){
        openFileOutput(amBinding.fileName.text.toString(), MODE_PRIVATE).use {
            val content = amBinding.fileContent.text.toString()
            it.write(content.encodeToByteArray())
            it.flush()
        }
    }
}