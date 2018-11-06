package com.gkzxhn.legalconsulting.utils.download

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

class DownloadResponseBody(private val responseBody: ResponseBody,
                           progressListener: DownloadProgressListener)
    : ResponseBody() {
    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    private val progressListener: DownloadProgressListener? = progressListener
    private var bufferedSource: BufferedSource? = null

    override fun source(): BufferedSource? {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            internal var totalBytesRead = 0L
            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                progressListener?.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1L)
                return bytesRead
            }
        }
    }

    /**
     * 成功回调处理
     * Created by WZG on 2016/10/20.
     */
    interface DownloadProgressListener {
        /**
         * 下载进度
         * @param read
         * @param count
         * @param done
         */
        fun update(read: Long, count: Long, done: Boolean)
    }
}
