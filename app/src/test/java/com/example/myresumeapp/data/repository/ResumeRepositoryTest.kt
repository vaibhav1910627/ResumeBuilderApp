package com.example.myresumeapp.data.repository

import androidx.work.WorkManager
import com.example.myresumeapp.data.local.ResumeDao
import com.example.myresumeapp.data.local.ResumeEntity
import com.example.myresumeapp.data.model.ResumeData
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import net.bytebuddy.matcher.ElementMatchers.any
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ResumeRepositoryTest {
    private lateinit var resumeDao: ResumeDao
    private lateinit var workManager: WorkManager
    private lateinit var repository: ResumeRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        resumeDao = mockk(relaxed = true)
        workManager = mockk(relaxed = true)
        repository = ResumeRepository(resumeDao, workManager)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getLocalResume return ResumeData when entity exists in dao`() = runTest {

        //Arrange
        val resumeData = ResumeData(id = "123")
        val json = Gson().toJson(resumeData)
        val entity = ResumeEntity(id = "123", resumeJson = json)

        coEvery { resumeDao.getResume() } returns entity

        //Act
        val result = repository.getLocalResume()

        //Assert
        assertNotNull(result)
        assertEquals("123", result?.id)
        coVerify(exactly = 1) { resumeDao.getResume() }
    }

    @Test
    fun `saveResumeLocally inserts into dao anda enqueues work manager sync`() = runTest {
        //Arrange
        val resumeData = ResumeData(id = "456")
        coEvery { resumeDao.insertResume(any()) } just runs
        every {workManager.enqueue(any<androidx.work.WorkRequest>()) } returns mockk()

        //Act
        repository.saveResumeLocally(resumeData, null)

        //assert
        coVerify(exactly = 1) { resumeDao.insertResume(any()) }
        verify(exactly = 1) {workManager.enqueue(any<androidx.work.WorkRequest>()) }
    }
}