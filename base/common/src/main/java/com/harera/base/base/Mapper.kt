package com.harera.base.base

import com.google.gson.Gson
import com.harera.model.response.MessageResponse

object Mapper {

    fun messageFromText(text: String) = Gson().fromJson(text, MessageResponse::class.java)

}