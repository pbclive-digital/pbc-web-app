package com.kavi.pbc.web.users.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.email.EmailGroup
import com.kavi.pbc.web.data.email.EmailGroupHeading
import com.kavi.pbc.web.data.email.EmailItem
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.ResultWrapper
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readBytes
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class EmailGroupRepository {

    val emailGroupApi = Network.shared.ktorfitClient().createEmailGroupApi()

    suspend fun createEmailGroup(emailGroup: EmailGroup): ResultWrapper<BaseResponse<String>> {
        return Network.shared.invokeApiCall { emailGroupApi.createEmailGroup(emailGroup = emailGroup) }
    }

    suspend fun createEmailGroupFromFile(groupName: String, emailListFile: PlatformFile): ResultWrapper<BaseResponse<String>> {
        val byteArray = emailListFile.readBytes()

        val multipartBody = MultiPartFormDataContent(
            formData {
                append("emailFile", byteArray, Headers.build {
                    append(HttpHeaders.ContentType, "text/csv")
                    append(HttpHeaders.ContentDisposition, "filename=\"$groupName\"")
                })
            }
        )

        return Network.shared.invokeApiCall { emailGroupApi.createEmailGroupFromFile(groupName, multipartBody) }
    }

    suspend fun getEmailGroupHeadings(): ResultWrapper<BaseResponse<List<EmailGroupHeading>>> {
        return Network.shared.invokeApiCall { emailGroupApi.getEmailGroupHeadings() }
    }

    suspend fun getEmailGroup(groupId: String): ResultWrapper<BaseResponse<EmailGroup>> {
        return Network.shared.invokeApiCall { emailGroupApi.getEmailGroup(groupId = groupId) }
    }

    suspend fun addEmailToEmailGroup(groupId: String,
                                     emailItemList: List<EmailItem>): ResultWrapper<BaseResponse<EmailGroup>> {
        return Network.shared.invokeApiCall {
            emailGroupApi.addEmailToEmailGroup(groupId = groupId, emailItemList = emailItemList)
        }
    }

    suspend fun removeEmailFromEmailGroup(groupId: String,
                                     emailItemList: List<EmailItem>): ResultWrapper<BaseResponse<EmailGroup>> {
        return Network.shared.invokeApiCall {
            emailGroupApi.removeEmailFromEmailGroup(groupId = groupId, emailItemList = emailItemList)
        }
    }

    suspend fun deleteEmailGroup(groupId: String): ResultWrapper<BaseResponse<String>> {
        return Network.shared.invokeApiCall { emailGroupApi.deleteEmailGroup(groupId = groupId) }
    }
}