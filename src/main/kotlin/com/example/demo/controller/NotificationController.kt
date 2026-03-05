package com.example.demo.controller

import com.example.demo.model.PushSubscription
import com.example.demo.repository.PushSubscriptionRepository
import com.example.demo.service.WebPushService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/notifications")
class NotificationController(
    private val repository: PushSubscriptionRepository,
    private val webPushService: WebPushService
) {

    @PostMapping("/subscribe")
    fun subscribe(@RequestBody subscription: PushSubscription) {
        val saved = repository.save(subscription)

        println("Saved subscription: ${saved.endpoint}")
        println("Total subscriptions: ${repository.count()}")
    }

    @PostMapping("/push")
    fun pushNotification(): String {

        val subscriptions = repository.findAll()
        println("Subscribers found: ${subscriptions.size}")

        val payload = """
        {
          "title": "Pop-Up",
          "message": "Message"
        }
        """.trimIndent()

        subscriptions.forEach { subscription ->
            try {
                webPushService.sendNotification(subscription, payload)
                println("Push sent to: ${subscription.endpoint}")
            } catch (e: Exception) {
                println("Push failed for: ${subscription.endpoint}")
                e.printStackTrace()
            }
        }

        return "Push attempted for ${subscriptions.size} subscribers"
    }
}