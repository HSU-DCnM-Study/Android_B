package com.example.data.repository

import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.model.FollowDto
import com.example.data.model.LikeDto
import com.example.data.model.PostDto
import com.example.data.model.UserDto
import com.example.data.source.PostPagingSource
import com.example.data.util.timeAgoString
import com.example.domain.model.Post
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.*

object PostRepository {

    const val PAGE_SIZE = 20


    private val currentUserId = Firebase.auth.currentUser!!.uid

    suspend fun getMapFeeds(): List<Post> {
        try {
            val currentUser = Firebase.auth.currentUser
            require(currentUser != null)

            val likeCollection = Firebase.firestore.collection("likes")
            val userCollection = Firebase.firestore.collection("users")
            val postQuerySnapshot = Firebase.firestore.collection("posts")
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .get()
                .await()
            val postDtos = postQuerySnapshot.toObjects(PostDto::class.java)
            val posts = postDtos
                .map { postDto ->
                    val likes = likeCollection.whereEqualTo("postUuid", postDto.uuid).get().await()
                        .toObjects(LikeDto::class.java)
                    val writer = userCollection.document(postDto.writerUuid).get().await()
                        .toObject(UserDto::class.java)
                    val meLiked = likes.any { like -> like.userUuid == currentUserId }

                    Post(
                        uuid = postDto.uuid,
                        writerUuid = writer!!.uuid,
                        writerName = writer.name,
                        writerProfileImageUrl = writer.profileImageUrl,
                        content = postDto.content,
                        imageUrl = postDto.imageUrl,
                        likeCount = likes.size,
                        meLiked = meLiked,
                        isMine = postDto.writerUuid == currentUserId,
                        timeAgo = postDto.dateTime.timeAgoString(),
                        latitude = postDto.latitude,
                        longitude = postDto.longitude
                    )
                }
            return posts
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }


    /**
     * 내가 팔로우한 유저와 나의 게시물 목록을 불러온다.
     */

    suspend fun getHomeFeeds(): Flow<PagingData<Post>> {
        try {
            val currentUser = Firebase.auth.currentUser
            require(currentUser != null)

            return Pager(PagingConfig(pageSize = PAGE_SIZE)) {
                PostPagingSource(getWriterUuids = {
                    val result = UserRepository.getFollowingList()
                    if (result.isSuccess) {
                        result.getOrNull()!!.map { it.followeeUuid }.toMutableList().apply {
                            add(currentUser.uid)
                        }
                    } else {
                        throw IllegalStateException("회원 정보 얻기 실패")
                    }
                })
            }.flow
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    fun getPostDetailsByUser(uuid: String): Flow<PagingData<Post>> {
        try {
            val currentUser = Firebase.auth.currentUser
            require(currentUser != null)

            return Pager(PagingConfig(pageSize = PAGE_SIZE)) {
                PostPagingSource(getWriterUuids = { listOf(uuid) })
            }.flow
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun toggleLike(postUuid: String): Result<Unit> {
        val currentUser = Firebase.auth.currentUser
        require(currentUser != null)
        val db = Firebase.firestore
        val likeCollection = db.collection("likes")

        return try {
            val likes = likeCollection
                .whereEqualTo("userUuid", currentUser.uid)
                .whereEqualTo("postUuid", postUuid)
                .get().await().toObjects<LikeDto>()

            if (likes.isEmpty()) {
                val likeUuid = UUID.randomUUID().toString()
                val likeDto = LikeDto(
                    uuid = likeUuid,
                    userUuid = currentUser.uid,
                    postUuid = postUuid
                )
                likeCollection.document(likeUuid).set(likeDto).await()
            } else {
                likeCollection.document(likes.first().uuid).delete().await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePost(postUuid: String): Result<Unit> {
        val db = Firebase.firestore

        return try {
            db.collection("posts").document(postUuid).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun editPost(
        uuid: String,
        content: String,
        imageUri: Uri
    ): Result<Unit> {
        val currentUser = Firebase.auth.currentUser
        require(currentUser != null)
        val db = Firebase.firestore
        val storageRef = Firebase.storage.reference
        val postCollection = db.collection("posts")
        val imageFileName: String = UUID.randomUUID().toString() + ".png"
        val imageRef = storageRef.child(imageFileName)
        val map = mutableMapOf<String, Any>()

        map["content"] = content
        map["imageUrl"] = imageFileName

        try {
            imageRef.putFile(imageUri).await()
        } catch (e: Exception) {
            return Result.failure(e)
        }

        return try {
            postCollection.document(uuid).update(map).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun editPostOnlyContent(
        uuid: String,
        content: String,
    ): Result<Unit> {
        val currentUser = Firebase.auth.currentUser
        require(currentUser != null)
        val db = Firebase.firestore
        val postCollection = db.collection("posts")
        val map = mutableMapOf<String, Any>()

        map["content"] = content

        return try {
            postCollection.document(uuid).update(map).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadPost(
        content: String,
        imageUri: Uri,
        latitude: Double,
        longitude: Double
    ): Result<Unit> {
        val currentUser = Firebase.auth.currentUser
        require(currentUser != null)
        val db = Firebase.firestore
        val storageRef = Firebase.storage.reference
        val postCollection = db.collection("posts")
        val imageFileName: String = UUID.randomUUID().toString() + ".png"
        val imageRef = storageRef.child(imageFileName)
        val postUuid = UUID.randomUUID().toString()

        try {
            imageRef.putFile(imageUri).await()
        } catch (e: Exception) {
            return Result.failure(e)
        }

        return try {
            val postDto = PostDto(
                uuid = postUuid,
                writerUuid = currentUser.uid,
                content = content,
                imageUrl = imageFileName,
                dateTime = Date(),
                longitude = longitude,
                latitude = latitude
            )
            postCollection.document(postUuid).set(postDto).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}