console.log("SW file parsed");

self.addEventListener('install', event => {
    console.log("SW installed");
    self.skipWaiting();
});

self.addEventListener('activate', event => {
    console.log("SW activated");
});

self.addEventListener('push', function(event) {

    console.log("Push event received");

    let title = "Default Title";
    let message = "Default Message";

    if (event.data) {
        const data = event.data.json();
        console.log("Push payload:", data);

        if (data.notification) {
            title = data.notification.title;
            message = data.notification.body;
        } else {
            title = data.title;
            message = data.message;
        }
    }

    event.waitUntil(
        self.registration.showNotification(title, {
            body: message || "No message",
            icon: "https://via.placeholder.com/128"
        })
    );
});
