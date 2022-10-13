package sid.com.quotely.models.data

import sid.com.quotely.utils.AuthUtils

data class RequestsMeta(
    val title: String = "",
    val comment: String = "",
    val type: String = "",
    val userId: String = AuthUtils.getUserMeta().uid ?: "",
    val userName: String = AuthUtils.getUserMeta().displayName ?: "",
    val userEmail: String = AuthUtils.getUserMeta().email ?: ""
)