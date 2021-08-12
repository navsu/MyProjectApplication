package com.memandis.appservice.main.domain.entities.signin

import android.net.Uri
//import com.memandis.appservice.main.domain.entities.signin.AuthenticatedUserInfo
//import com.memandis.appservice.main.domain.entities.signin.AuthenticatedUserInfoBasic
import com.memandis.appservice.main.domain.entities.user.UserEntity

/**
 * Delegates [AuthenticatedUserInfo] calls to a [FirebaseUser] to be used in production.
 */
class RegisteredUserInfoEntity(
    private val basicUserInfo: AuthenticatedUserInfoBasic?,
    private val isRegistered: Boolean?
    ) : AuthenticatedUserInfo {
    override fun isSignedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getEmail(): String? {
        TODO("Not yet implemented")
    }

    override fun getProviderData(): MutableList<out UserEntity>? {
        TODO("Not yet implemented")
    }

    override fun getLastSignInTimestamp(): Long? {
        TODO("Not yet implemented")
    }

    override fun getCreationTimestamp(): Long? {
        TODO("Not yet implemented")
    }

    override fun isAnonymous(): Boolean? {
        TODO("Not yet implemented")
    }

    override fun getPhoneNumber(): String? {
        TODO("Not yet implemented")
    }

    override fun getUid(): String? {
        TODO("Not yet implemented")
    }

    override fun isEmailVerified(): Boolean? {
        TODO("Not yet implemented")
    }

    override fun getDisplayName(): String? {
        TODO("Not yet implemented")
    }

    override fun getPhotoUrl(): Uri? {
        TODO("Not yet implemented")
    }

    override fun getProviderId(): String? {
        TODO("Not yet implemented")
    }

    override fun isRegistered(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isRegistrationDataReady(): Boolean {
        TODO("Not yet implemented")
    }

}
