package com.serabutinn.serabutinnn.data.api

class UserModel (
    val email: String,
    val id: String,
    val roleid:String,
    val token: String,
    val isLogin: Boolean = false,
    val name: String?,
    val customer_id: String?,
)


