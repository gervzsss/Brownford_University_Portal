// Global modal scroll lock logic
(function () {
    // Helper: get all open modals
    function anyModalOpen() {
        return Array.from(document.querySelectorAll('.modal')).some(
            modal => (modal.classList.contains('show') || modal.style.display === 'block') && getComputedStyle(modal).display !== 'none'
        );
    }

    // Lock or unlock body scroll
    function updateBodyScrollLock() {
        if (anyModalOpen()) {
            document.body.classList.add('no-scroll');
        } else {
            document.body.classList.remove('no-scroll');
        }
    }

    // Observe modal open/close
    const observer = new MutationObserver(updateBodyScrollLock);
    document.querySelectorAll('.modal').forEach(modal => {
        observer.observe(modal, { attributes: true, attributeFilter: ['style', 'class'] });
    });

    // Also listen for manual open/close via JS
    document.addEventListener('click', function () {
        setTimeout(updateBodyScrollLock, 10);
    });
    document.addEventListener('keydown', function () {
        setTimeout(updateBodyScrollLock, 10);
    });

    // On DOMContentLoaded, update scroll lock in case a modal is open by default
    document.addEventListener('DOMContentLoaded', updateBodyScrollLock);

    // In case modals are dynamically added later
    const bodyObserver = new MutationObserver(function () {
        document.querySelectorAll('.modal').forEach(modal => {
            observer.observe(modal, { attributes: true, attributeFilter: ['style', 'class'] });
        });
    });
    bodyObserver.observe(document.body, { childList: true, subtree: true });
})();

// Global scroll lock for sidebar navigation
(function () {
    function isSidebarOpen() {
        const sidebar = document.getElementById("sidebar");
        return sidebar && sidebar.classList.contains("open");
    }
    function updateSidebarScrollLock() {
        if (isSidebarOpen()) {
            document.body.classList.add("no-scroll");
        } else {
            // Only remove if no modal is open (modal-global.js handles this too)
            if (!document.querySelector('.modal.show, .modal[style*="display: block"]')) {
                document.body.classList.remove("no-scroll");
            }
        }
    }
    // Listen for sidebar open/close
    document.addEventListener("DOMContentLoaded", function () {
        const sidebar = document.getElementById("sidebar");
        if (!sidebar) return;
        const observer = new MutationObserver(updateSidebarScrollLock);
        observer.observe(sidebar, { attributes: true, attributeFilter: ["class"] });
        // Also listen for overlay
        const sidebarOverlay = document.getElementById("sidebarOverlay");
        if (sidebarOverlay) {
            sidebarOverlay.addEventListener("click", function () {
                setTimeout(updateSidebarScrollLock, 10);
            });
        }
    });
})();

// Ensure scroll lock is checked after any click or keydown (for sidebar and modals)
document.addEventListener('click', function () {
    setTimeout(function () {
        // Check both modal and sidebar state
        const anyModalOpen = Array.from(document.querySelectorAll('.modal')).some(
            modal => (modal.classList.contains('show') || modal.style.display === 'block') && getComputedStyle(modal).display !== 'none'
        );
        const sidebar = document.getElementById('sidebar');
        const sidebarOpen = sidebar && sidebar.classList.contains('open');
        if (anyModalOpen || sidebarOpen) {
            document.body.classList.add('no-scroll');
        } else {
            document.body.classList.remove('no-scroll');
        }
    }, 10);
});
document.addEventListener('keydown', function () {
    setTimeout(function () {
        const anyModalOpen = Array.from(document.querySelectorAll('.modal')).some(
            modal => (modal.classList.contains('show') || modal.style.display === 'block') && getComputedStyle(modal).display !== 'none'
        );
        const sidebar = document.getElementById('sidebar');
        const sidebarOpen = sidebar && sidebar.classList.contains('open');
        if (anyModalOpen || sidebarOpen) {
            document.body.classList.add('no-scroll');
        } else {
            document.body.classList.remove('no-scroll');
        }
    }, 10);
});
