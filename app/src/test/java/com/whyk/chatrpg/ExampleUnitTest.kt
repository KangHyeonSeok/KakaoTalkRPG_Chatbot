package com.whyk.chatrpg

import android.util.Log
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun sheetDataRequest() {
        var signal : CountDownLatch = CountDownLatch(1)
        var isSuccess : Boolean = false

        var apiInterface = APIClient.client.create(ApiInterface::class.java)
        val call = apiInterface.getPlace()
        call.enqueue(object : Callback<SheetReturn> {
            override fun onResponse(call: Call<SheetReturn>, response: Response<SheetReturn>) {
                System.out.println("Success!" + response.toString())
                var text = response.body()
                var bookList = text?.vaules
                if (bookList != null) {
                    for (b in bookList) {
                        System.out.println(b.Name)
                    }
                }
                isSuccess = true
                signal.countDown()
            }

            override fun onFailure(call: Call<SheetReturn>, t: Throwable) {
                System.out.println("Failed Query :(" + t.toString())
                signal.countDown()
            }
        })

        signal.await(10, TimeUnit.SECONDS)
        assert(isSuccess)
    }
}