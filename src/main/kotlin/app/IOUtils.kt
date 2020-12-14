package app
import java.io.File
import java.io.IOException
import java.nio.file.Files

fun loadFile(file:File) = String(Files.readAllBytes(file.toPath()))

fun saveFile(file:File,text:String) {
    try {
        Files.writeString(file.toPath(), text)
    }catch (e: Throwable){
        throwError(e)
    }
}

fun throwError(e: Throwable): Nothing{
    throw IOException(e)
}