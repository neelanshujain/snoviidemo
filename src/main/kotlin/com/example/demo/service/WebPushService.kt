package com.example.demo.service

import com.example.demo.model.PushSubscription
import com.example.demo.repository.PushSubscriptionRepository
import jakarta.annotation.PostConstruct
import nl.martijndwars.webpush.Notification
import nl.martijndwars.webpush.PushService
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Security

@Service
class WebPushService(
    private val pushSubscriptionRepository: PushSubscriptionRepository
) {

    @Value("\${push.vapid.public-key}")
    private lateinit var publicKey: String

    @Value("\${push.vapid.private-key}")
    private lateinit var privateKey: String

    private lateinit var pushService: PushService

    @PostConstruct
    fun init() {
        Security.addProvider(BouncyCastleProvider())

        pushService = PushService()
        pushService.setPublicKey(publicKey)
        pushService.setPrivateKey(privateKey)
        pushService.setSubject("mailto:test@example.com")

    }

    fun getPublicKey(): String {
        return publicKey
    }

    fun getAllSubscriptions(): List<PushSubscription> {
        return pushSubscriptionRepository.findAll()
    }

    fun sendNotification(subscription: PushSubscription, payload: String) {
        try {
            println("🔥 sendNotification() called")
            println("Endpoint: ${subscription.endpoint}")

            val notification = Notification(
                subscription.endpoint,
                subscription.keys.p256dh,
                subscription.keys.auth,
                payload
            )

            val response = pushService.send(notification)

            println("========== FCM RESPONSE ==========")
            println("Status Code: ${response.statusLine.statusCode}")
            println("Reason: ${response.statusLine.reasonPhrase}")
            println("==================================")
            println("p256dh: ${subscription.keys.p256dh}")
            println("auth: ${subscription.keys.auth}")

        } catch (e: Exception) {
            println("❌ ERROR SENDING PUSH")
            e.printStackTrace()
        }
    }
}