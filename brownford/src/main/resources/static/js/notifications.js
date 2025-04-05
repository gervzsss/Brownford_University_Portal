document.addEventListener("DOMContentLoaded", () => {
  // Notification dropdown toggle
  const notificationToggle = document.getElementById("notificationToggle")
  const notificationDropdown = document.getElementById("notificationDropdown")
  const markAllReadBtn = document.querySelector(".mark-all-read")
  const notificationBadge = document.querySelector(".notification-badge")

  if (notificationToggle && notificationDropdown) {
    // Toggle notification dropdown
    notificationToggle.addEventListener("click", (e) => {
      e.preventDefault()
      notificationDropdown.classList.toggle("show")
    })

    // Close dropdown when clicking outside
    document.addEventListener("click", (e) => {
      if (
        notificationToggle &&
        notificationDropdown &&
        !notificationToggle.contains(e.target) &&
        !notificationDropdown.contains(e.target)
      ) {
        notificationDropdown.classList.remove("show")
      }
    })
  }

  // Mark all as read functionality
  if (markAllReadBtn && notificationBadge) {
    markAllReadBtn.addEventListener("click", (e) => {
      e.preventDefault()
      const unreadItems = document.querySelectorAll(".notification-item.unread")
      unreadItems.forEach((item) => {
        item.classList.remove("unread")
      })
      notificationBadge.style.display = "none"
    })
  }
})

