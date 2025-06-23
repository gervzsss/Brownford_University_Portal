document.addEventListener("DOMContentLoaded", () => {
  // Notification dropdown toggle
  const notificationToggle = document.getElementById("notificationToggle")
  const notificationDropdown = document.getElementById("notificationDropdown")
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
  if (notificationBadge) {
    notificationBadge.style.display = "none"
  }

  // --- Notification Polling and Rendering ---
  const POLL_INTERVAL = 30000 // 30 seconds

  function fetchNotifications() {
    fetch("/api/notifications")
      .then((res) => res.json())
      .then((data) => renderNotifications(data))
      .catch(() => {})
  }

  function renderNotifications(notifications) {
    const dropdown = document.getElementById("notificationDropdown")
    const list = dropdown ? dropdown.querySelector(".notification-list") : null
    const badge = document.querySelector(".notification-badge")
    if (!list) return

    if (!notifications || notifications.length === 0) {
      list.innerHTML = "<p>No notifications available.</p>"
      if (badge) badge.style.display = "none"
      return
    }

    let unreadCount = 0
    list.innerHTML = notifications
      .map((n) => {
        if (!n.read) unreadCount++
        return `<div class="notification-item${
          n.read ? "" : " unread"
        }" data-id="${n.id}">
      <span class="notification-message">${n.message}</span>
      <span class="notification-date">${new Date(n.createdAt).toLocaleString()}</span>
      <button class="notification-delete-btn" title="Delete notification">&times;</button>
    </div>`
      })
      .join("")

    // Add delete event listeners
    list.querySelectorAll(".notification-delete-btn").forEach((btn) => {
      btn.addEventListener("click", function (e) {
        e.stopPropagation()
        const item = btn.closest(".notification-item")
        const id = item.getAttribute("data-id")
        if (id) {
          fetch(`/api/notifications/${id}`, { method: "DELETE" })
            .then(() => fetchNotifications())
        }
      })
    })

    if (badge) {
      badge.textContent = unreadCount
      badge.style.display = unreadCount > 0 ? "inline-block" : "none"
    }
  }

  // Mark all as read
  function markAllNotificationsRead() {
    fetch("/api/notifications")
      .then((res) => res.json())
      .then((data) => {
        const unread = data.filter((n) => !n.read)
        unread.forEach((n) => {
          fetch(`/api/notifications/read/${n.id}`, { method: "POST" })
        })
        setTimeout(fetchNotifications, 500) // Refresh after marking
      })
  }

  // Add badge if not present
  let badge = document.querySelector(".notification-badge")
  if (!badge) {
    const icon = document.querySelector(".notification-icon")
    if (icon) {
      badge = document.createElement("span")
      badge.className = "notification-badge"
      badge.style.display = "none"
      icon.appendChild(badge)
    }
  }
  // Poll notifications
  fetchNotifications()
  setInterval(fetchNotifications, POLL_INTERVAL)
  // Mark all as read
  const markAllReadBtn = document.querySelector(".mark-all-read")
  if (markAllReadBtn) {
    markAllReadBtn.addEventListener("click", (e) => {
      e.preventDefault()
      markAllNotificationsRead()
    })
  }
})

