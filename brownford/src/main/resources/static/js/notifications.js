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
      if (notificationDropdown.classList.contains("show")) {
        fetchNotifications()
      }
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

  // --- Unified Notification Dropdown Logic for Student and Faculty ---
  const POLL_INTERVAL = 30000 // 30 seconds
  let notifications = []

  // Always use unified endpoint for both students and faculty
  const apiBase = "/api/notifications"

  function fetchNotifications() {
    fetch(apiBase)
      .then((res) => res.json())
      .then((data) => {
        notifications = data
        renderNotifications()
      })
      .catch(() => { })
  }

  function renderNotifications() {
    const list = notificationDropdown.querySelector(".notification-list")
    const badge = document.querySelector(".notification-badge")
    if (!list) return

    if (!notifications || notifications.length === 0) {
      list.innerHTML = "<p class='no-notifications'>No notifications available.</p>"
      if (badge) badge.style.display = "none"
      return
    }

    list.innerHTML = notifications
      .map((n) => {
        return `<div class="notification-item${n.read ? "" : " unread"
          }" data-id="${n.id}">
      <span class="notification-message">${n.message}</span>
      <span class="notification-date">${new Date(n.createdAt).toLocaleString()}</span>
      <button class="notification-delete-btn" title="Delete notification">&times;</button>
    </div>`
      })
      .join("")

    // Add click event to redirect and mark as read
    list.querySelectorAll(".notification-item").forEach((item) => {
      item.addEventListener("click", function (e) {
        // Prevent click if delete button is clicked
        if (e.target.classList.contains("notification-delete-btn")) return;
        const id = this.getAttribute("data-id")
        window.location.href = `/api/notifications/mark-read-and-redirect/${id}`
      })
    })

    // Add delete event listeners
    list.querySelectorAll(".notification-delete-btn").forEach((btn) => {
      btn.addEventListener("click", function (e) {
        e.stopPropagation()
        const item = btn.closest(".notification-item")
        const id = item.getAttribute("data-id")
        if (id) {
          fetch(apiBase + "/" + id, { method: "DELETE" })
            .then(() => fetchNotifications())
        }
      })
    })

    // Update bell icon with unread count
    const unreadCount = notifications.filter((n) => !n.read).length
    updateBadge(unreadCount)
  }

  function updateBadge(unreadCount) {
    let bell = document.getElementById("notificationToggle")
    if (bell) {
      let badge = bell.querySelector(".notification-badge")
      if (!badge && unreadCount > 0) {
        badge = document.createElement("span")
        badge.className = "notification-badge"
        bell.appendChild(badge)
      }
      if (badge) {
        badge.textContent = unreadCount > 0 ? unreadCount : ""
        badge.style.display = unreadCount > 0 ? "inline-block" : "none"
      }
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
      const unread = notifications.filter((n) => !n.read)
      Promise.all(
        unread.map((n) =>
          fetch(apiBase + "/read/" + n.id, {
            method: "POST",
          })
        )
      ).then(() => fetchNotifications())
    })
  }

  // Remove all notifications
  const removeAllBtn = document.querySelector(".remove-all")
  if (removeAllBtn) {
    removeAllBtn.addEventListener("click", (e) => {
      e.preventDefault()
      if (notifications.length === 0) return
      if (!confirm("Are you sure you want to remove all notifications?")) return
      Promise.all(
        notifications.map((n) =>
          fetch(apiBase + "/" + n.id, { method: "DELETE" })
        )
      ).then(() => fetchNotifications())
    })
  }
})

