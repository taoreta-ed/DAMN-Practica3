package com.example.damn_practica3 // Corrected package name

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FileAdapter(private val files: List<File>, private val onItemClick: (File) -> Unit) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = files[position]
        holder.bind(file)
    }

    override fun getItemCount(): Int = files.size

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // These IDs will now be resolved correctly from item_file.xml
        private val fileNameTextView: TextView = itemView.findViewById(R.id.textViewFileName)
        private val fileIconImageView: ImageView = itemView.findViewById(R.id.imageViewFileIcon)

        fun bind(file: File) {
            fileNameTextView.text = file.name
            if (file.isDirectory) {
                fileIconImageView.setImageResource(R.drawable.ic_folder) // Will be resolved
            } else {
                fileIconImageView.setImageResource(R.drawable.ic_file)   // Will be resolved
            }
            itemView.setOnClickListener { onItemClick(file) }
        }
    }
}