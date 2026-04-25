package com.kavi.pbc.web.users.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.email.EmailGroup
import com.kavi.pbc.web.data.email.EmailGroupHeading
import com.kavi.pbc.web.data.email.EmailItem
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.ResultWrapper

class EmailGroupRepository {

    val emailGroupApi = Network.shared.ktorfitClient().createEmailGroupApi()

    suspend fun createEmailGroup(emailGroup: EmailGroup): ResultWrapper<BaseResponse<String>> {
        return Network.shared.invokeApiCall { emailGroupApi.createEmailGroup(emailGroup = emailGroup) }
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
}