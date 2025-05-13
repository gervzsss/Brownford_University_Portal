document.addEventListener("DOMContentLoaded", () => {
    // Sidebar toggle functionality
    const sidebarToggle = document.getElementById("sidebarToggle")
    const sidebar = document.getElementById("sidebar")
    const sidebarClose = document.getElementById("sidebarClose")
    const contentWrapper = document.getElementById("contentWrapper")
    const sidebarOverlay = document.getElementById("sidebarOverlay")

    if (sidebarToggle && sidebar && sidebarClose) {
        // Toggle sidebar when menu button is clicked
        sidebarToggle.addEventListener("click", () => {
            sidebar.classList.toggle("open")
            if (contentWrapper) contentWrapper.classList.toggle("sidebar-open")
            if (sidebarOverlay) sidebarOverlay.classList.toggle("active")
        })

        // Close sidebar when close button is clicked
        sidebarClose.addEventListener("click", () => {
            sidebar.classList.remove("open")
            if (contentWrapper) contentWrapper.classList.remove("sidebar-open")
            if (sidebarOverlay) sidebarOverlay.classList.remove("active")
        })

        // Close sidebar when clicking outside of it (on the overlay)
        if (sidebarOverlay) {
            sidebarOverlay.addEventListener("click", () => {
                sidebar.classList.remove("open")
                if (contentWrapper) contentWrapper.classList.remove("sidebar-open")
                sidebarOverlay.classList.remove("active")
            })
        }
    }

    // Dropdown functionality
    const dropdownToggles = document.querySelectorAll(".sidebar-dropdown-toggle")

    dropdownToggles.forEach((toggle) => {
        toggle.addEventListener("click", function () {
            const dropdown = this.parentElement
            dropdown.classList.toggle("open")
        })
    })

    // Set active link based on current URL
    const currentPath = window.location.pathname
    const navLinks = document.querySelectorAll(".sidebar-nav-link, .sidebar-dropdown-item")

    navLinks.forEach((link) => {
        const href = link.getAttribute("href")
        if (href && currentPath.includes(href)) {
            link.classList.add("active")

            // If it's a dropdown item, open the parent dropdown
            if (link.classList.contains("sidebar-dropdown-item")) {
                const dropdown = link.closest(".sidebar-dropdown")
                if (dropdown) {
                    dropdown.classList.add("open")
                }
            }
        }
    })
})
