class MyHeader extends HTMLElement {
    connectedCallback() {
        const path = window.location.pathname;
        this.innerHTML = `<!-- Header -->
        <header class="header">
            <div class="logo-container">
                <div class="logo">
                    <div class="logo-inner">
                        <img src="/images/logo.png" alt="Brown Ford University Logo" class="university-logo">
                    </div>
                </div>
                <h1 class="university-name">Brown Ford University</h1>
            </div>
            <div class="header-icons">
                <!-- Notification Icon -->
                <div class="notification-icon">
                    <a href="#" class="notification-link" id="notificationToggle">
                        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none"
                            stroke="#8B4513" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path>
                            <path d="M13.73 21a2 2 0 0 1-3.46 0"></path>
                        </svg>
                    </a>
                    <!-- Notification Dropdown -->
                    <div class="notification-dropdown" id="notificationDropdown">
                        <div class="notification-header">
                            <h3>Notifications</h3>
                            <a href="#" class="mark-all-read">Mark all as read</a>
                        </div>
                        <div class="notification-list">
                            <p>No notifications available.</p>
                        </div>
                        <div class="notification-footer">
                            <a href="#" class="view-all">View All Notifications</a>
                        </div>
                    </div>
                </div>

                <!-- Profile Icon -->
                <div class="profile-icon">
                    <a href="/student-profile" class="profile-link">
                        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none"
                            stroke="#8B4513" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                            <circle cx="12" cy="7" r="4"></circle>
                        </svg>
                    </a>
                </div>

                <!-- Interactive Logout Icon as an anchor tag -->
                <div class="logout-icon">
                    <a href="/logout" class="logout-link">
                        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none"
                            stroke="#8B4513" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path stroke-linecap="round" stroke-linejoin="round"
                                d="M15.75 9V5.25A2.25 2.25 0 0 0 13.5 3h-6a2.25 2.25 0 0 0-2.25 2.25v13.5A2.25 2.25 0 0 0 7.5 21h6a2.25 2.25 0 0 0 2.25-2.25V15m3 0 3-3m0 0-3-3m3 3H9" />
                        </svg>
                    </a>
                </div>
            </div>
        </header>

        <!-- Main Navigation -->
        <nav class="main-nav">
            <ul>
                <li><a href="/student-home" class="${path.includes('student-home') ? 'active' : ''}">Home</a></li>
                <li><a href="/student-schedule" class="${path.includes('student-schedule') ? 'active' : ''}">Schedule</a>
                </li>
                <li><a href="/student-grades" class="${path.includes('student-grades') ? 'active' : ''}">Grades</a></li>
                <li><a href="/student-enrollment" class="${path.includes('student-enrollment') ? 'active' : ''}">Enrollment</a>
                </li>
            </ul>
        </nav>`;
    }
}

class MyFooter extends HTMLElement {
    connectedCallback() {
        this.innerHTML = `
        <footer class="university-footer">
        <div class="footer-container">
            <div class="footer-logo">
                <img src="/images/logo.png" alt="Brown Ford University Logo" class="footer-logo-img">
                <h3 class="footer-university-name">Brown Ford University</h3>
            </div>
            <div class="footer-contact">
                <div class="footer-contact-item">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none"
                        stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
                        <circle cx="12" cy="10" r="3"></circle>
                    </svg>
                    <span>123 University Ave, City, Country</span>
                </div>
                <div class="footer-contact-item">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none"
                        stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path
                            d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z">
                        </path>
                    </svg>
                    <span><a href="tel:+11234567890">(123) 456-7890</a></span>
                </div>
                <div class="footer-contact-item">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none"
                        stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
                        <polyline points="22,6 12,13 2,6"></polyline>
                    </svg>
                    <span><a href="mailto:info@brownford.edu">info@brownford.edu</a></span>
                </div>
            </div>
            <div class="footer-social">
                <a href="#" class="social-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none"
                        stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M18 2h-3a5 5 0 0 0-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 0 1 1-1h3z"></path>
                    </svg>
                </a>
                <a href="#" class="social-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none"
                        stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path
                            d="M23 3a10.9 10.9 0 0 1-3.14 1.53 4.48 4.48 0 0 0-7.86 3v1A10.66 10.66 0 0 1 3 4s-4 9 5 13a11.64 11.64 0 0 1-7 2c9 5 20 0 20-11.5a4.5 4.5 0 0 0-.08-.83A7.72 7.72 0 0 0 23 3z">
                        </path>
                    </svg>
                </a>
                <a href="#" class="social-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none"
                        stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <rect x="2" y="2" width="20" height="20" rx="5" ry="5"></rect>
                        <path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z"></path>
                        <line x1="17.5" y1="6.5" x2="17.51" y2="6.5"></line>
                    </svg>
                </a>
            </div>
        </div>
        <div class="footer-bottom">
            <p>&copy; 2025 Brown Ford University. All rights reserved.</p>
        </div>
    </footer>`;
    }
}

customElements.define('header-header', MyHeader);
customElements.define('footer-footer', MyFooter);
