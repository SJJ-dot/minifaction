package com.sjj.fiction.source.remote

import com.sjj.fiction.model.Book
import com.sjj.fiction.model.WeChatLogin
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

@Repository
class HttpSource {
    private val retrofit = Retrofit.Builder()
            .baseUrl("http://118.24.120.20/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val api = retrofit.create(Api::class.java)


    fun loginWeChat(code: String, secret: String): Mono<WeChatLogin> {
        return Mono.create<WeChatLogin> {
            it.success(api.wechat(code, secret).execute().body())
        }.subscribeOn(Schedulers.elastic())
    }


    interface Api {
        @GET("https://api.weixin.qq.com/sns/jscode2session?appid=wx5f1ce778e0840039&grant_type=authorization_code")
        fun wechat(@Query("js_code") code: String, @Query("secret") secret: String): Call<WeChatLogin>
    }
}

