package com.gkzxhn.legalconsulting.utils.download

import com.gkzxhn.legalconsulting.common.App.Companion.mContext
import com.gkzxhn.legalconsulting.net.ApiService
import com.gkzxhn.legalconsulting.net.CacheInterceptorLogin
import com.gkzxhn.legalconsulting.utils.download.entity.DownInfo
import com.gkzxhn.legalconsulting.utils.download.entity.DownState
import com.gkzxhn.legalconsulting.utils.logE
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.util.concurrent.TimeUnit


/**
 * @classname：下载文件 管理类
 * @author：liushaoxiang
 * @date：2018/11/6 5:33 PM
 * @description：
 */
class HttpDownManager private constructor() {

    companion object {
        @Volatile
        private var mInstance: HttpDownManager? = null

        fun getInstance(): HttpDownManager {
            if (mInstance == null) {
                synchronized(HttpDownManager::class) {
                    if (mInstance == null) {
                        mInstance = HttpDownManager()
                    }
                }
            }
            return mInstance!!
        }
    }

    /*回调sub队列*/
    var subMap: HashMap<String, ProgressDownSubscriber<Any>>
    var downInfos: HashSet<DownInfo>
    val DEFAULT_TIMEOUT: Long = 60


    /**
     * 开始下载
     */
    fun startDown(info: DownInfo?) {
        /*正在下载不处理*/
        if (info == null || subMap[info.url] != null) {
//        if (info == null) {
            return
        }
        /*添加回调处理类*/
        val subscriber = ProgressDownSubscriber<Any>(info)
        /*记录回调sub*/
        subMap[info.url!!] = subscriber
        /*获取service，多次请求公用一个sercie*/
        val downloadService: ApiService?
        if (downInfos.contains(info)) {
            downloadService = info.service
        } else {
            val interceptor = DownloadInterceptor(subscriber)

            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(CacheInterceptorLogin(mContext))
                    .addNetworkInterceptor(CacheInterceptorLogin(mContext))
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build()


            val retrofit = Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(info.baseUrl)
                    .build()
            downloadService = retrofit.create<ApiService>(ApiService::class.java)

            info.service = downloadService
            if (!downInfos.contains(info)) {
                downInfos.add(info)
            }
        }
        /*得到rx对象-上一次下載的位置開始下載*/

        downloadService!!.downloadFile("bytes=" + info.readLength + "-", info.url!!)
//        downloadService!!.downloadFile(info.url!!)
                /*指定线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*读取下载写入文件*/
                .map {
                    try {
                        writeCache(it, File(info.savePath), info)
                    } catch (e: IOException) {
                        /*失败抛出异常*/
                        e.message.toString().logE(this@HttpDownManager)
                        throw e
                    }
                    info
                }
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*数据回调*/
                .subscribe(subscriber)


    }


    fun writeCache(responseBody: ResponseBody, file: File, info: DownInfo) {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs()


        val allLength: Long
        if (info.countLength == 0L) {
            allLength = responseBody.contentLength()
        } else {
            allLength = info.countLength
        }
        var channelOut: FileChannel? = null
        var randomAccessFile: RandomAccessFile? = null
        randomAccessFile = RandomAccessFile(file, "rwd")
        channelOut = randomAccessFile.channel
        val mappedBuffer = channelOut!!.map(FileChannel.MapMode.READ_WRITE,
                0, allLength)
        val buffer = ByteArray(1024 * 10)
        var len: Int = responseBody.byteStream().read(buffer)
        var record = 0
        while (len != -1) {
            mappedBuffer.put(buffer, 0, len)
            record += len
            len = responseBody.byteStream().read(buffer)
        }
        responseBody.byteStream().close()
        channelOut.close()
        randomAccessFile.close()
    }

    /**
     * 停止下载
     */
    fun stopDown(info: DownInfo?) {
        if (info == null) return
        info.state = DownState.STOP
        info.listener!!.onStop()
        if (subMap.containsKey(info.url)) {
            val subscriber = subMap[info.url]
//            subscriber?.dispose()
            subMap.remove(info.url)
        }
        /*同步数据库*/
    }

    /**
     * 暂停下载
     */
    fun pause(info: DownInfo?) {
        if (info == null) return
        info.state = DownState.PAUSE
        info.listener!!.onPuase()
        if (subMap.containsKey(info.url)) {
            val subscriber = subMap[info.url]
//            subscriber?.dispose()
            subMap.remove(info.url)
        }
        /*这里需要讲info信息写入到数据中，可自由扩展，用自己项目的数据库*/
    }

    /**
     * 停止全部下载
     */
    fun stopAllDown() {
        for (downInfo in downInfos) {
            stopDown(downInfo)
        }
        subMap.clear()
        downInfos.clear()
    }

    /**
     * 暂停全部下载
     */
    fun pauseAll() {
        for (downInfo in downInfos) {
            pause(downInfo)
        }
        subMap.clear()
        downInfos.clear()
    }

    init {
        downInfos = HashSet()
        subMap = HashMap()
    }
}