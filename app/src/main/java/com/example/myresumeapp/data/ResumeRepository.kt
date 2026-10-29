import android.graphics.Bitmap
import com.example.myresumeapp.data.ResumeData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID


class ResumeRepository {
    private val firebaseDatabase: DatabaseReference = Firebase.database.reference
    private val firebaseStorage: StorageReference = Firebase.storage.reference
    private val userID: String get() = Firebase.auth.currentUser?.uid ?: "guest"
//ye function new id ke liye location allocate krta aur existing ke liye existing use krta...
    suspend fun createOrUpdate(resume: ResumeData, localPhotoBitmap: Bitmap?): ResumeData {
        val id = resume.id.ifEmpty {
            firebaseDatabase.child("users/$userID/resumes").push().key ?: UUID.randomUUID()
                .toString()}


    //ye photo ko photourl banata
            var photoUrl = resume.personal.photoUrl
            if (localPhotoBitmap != null) {
                val bytes = ByteArrayOutputStream().use { out ->
                    localPhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 92, out)
                    out.toByteArray()
                }
                val photoPath = "users/$userID/$id/photo.jpg"
                val photoStorage = firebaseStorage.child(photoPath)
                photoStorage.putBytes(bytes).await()
                photoUrl = photoStorage.downloadUrl.await().toString()
            }



        //ye resume ke liye ki jo hmne data liya hai id and photo(which was converted to url) use resume me show kr ske...
            val updated = resume.copy(
                id = id,
                personal = resume.personal.copy(photoUrl = photoUrl)
            )
            firebaseDatabase.child("users/$userID/resumes/$id").setValue(updated)
            return updated
        }

    suspend fun fetch(id: String): ResumeData? {
        val resumeData = firebaseDatabase.child("users/$userID/resumes/$id").get().await()
        return resumeData.getValue(ResumeData::class.java)

    }

    suspend fun delete(id: String) {
        firebaseDatabase.child("users/$userID/resumes/$id").removeValue().await()
        runCatching { firebaseStorage.child("users/userID/$id/photo.jpg").delete().await() }
    }
    }
//child ka mtlb hm child directory ko dhund rhe hain firebase ke andar