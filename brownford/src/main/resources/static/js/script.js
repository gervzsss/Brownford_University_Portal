document.addEventListener("DOMContentLoaded", function () {
    console.log("JavaScript loaded successfully!");

    function togglePopup(popupId) {
        const popup = document.getElementById(popupId);
        if (popup) {
            popup.classList.toggle("hidden");
        }
    }

    // Notification Icon Click Event
    document.querySelector(".notification-icon").addEventListener("click", function (event) {
        event.stopPropagation(); // Prevents event bubbling
        togglePopup("notification-popup");
    });

    // Profile Icon Click Event
    document.querySelector(".profile-icon").addEventListener("click", function (event) {
        event.stopPropagation(); // Prevents event bubbling
        togglePopup("profile-menu");
    });

    // Close popups when clicking outside
    document.addEventListener("click", function (event) {
        const notificationPopup = document.getElementById("notification-popup");
        const profilePopup = document.getElementById("profile-menu");

        if (!event.target.closest(".notification-icon") && notificationPopup) {
            notificationPopup.classList.add("hidden");
        }
        if (!event.target.closest(".profile-icon") && profilePopup) {
            profilePopup.classList.add("hidden");
        }
    });
});
