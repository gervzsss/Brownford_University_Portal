document.addEventListener("DOMContentLoaded", function () {
    // Auto-focus on username field when page loads
    const usernameInput = document.getElementById("username");
    if (usernameInput) {
        usernameInput.focus();
    }

    // Form elements
    const loginForm = document.querySelector("form");
    const passwordInput = document.getElementById("password");
    const passwordToggle = document.querySelector(".password-toggle");
    const submitButton = document.querySelector(".sign-in-button");
    const formGroups = document.querySelectorAll(".form-group");

    // Track if user has started interacting with the form
    let formInteractionStarted = false;

    // Toggle password visibility
    if (passwordToggle && passwordInput) {
        passwordToggle.addEventListener("click", function () {
            const type = passwordInput.getAttribute("type") === "password" ? "text" : "password";
            passwordInput.setAttribute("type", type);

            // Change the eye icon based on visibility
            const eyeIcon = passwordToggle.querySelector("svg");
            if (type === "text") {
                eyeIcon.innerHTML = `
                <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
                <line x1="1" y1="1" x2="23" y2="23"></line>
            `;
            } else {
                eyeIcon.innerHTML = `
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                <circle cx="12" cy="12" r="3"></circle>
            `;
            }
        });
    }

    // Real-time form validation
    formGroups.forEach(group => {
        const input = group.querySelector("input");
        const errorDiv = document.createElement("div");
        errorDiv.className = "input-error";
        errorDiv.style.color = "#dc3545";
        errorDiv.style.fontSize = "12px";
        errorDiv.style.marginTop = "5px";
        errorDiv.style.textAlign = "left";
        errorDiv.style.display = "none";
        errorDiv.style.height = "0";
        errorDiv.style.overflow = "hidden";
        errorDiv.style.transition = "all 0.3s ease";

        group.appendChild(errorDiv);

        // Only start validation after user has interacted with the form
        input.addEventListener("focus", () => {
            formInteractionStarted = true;
        });

        input.addEventListener("blur", () => {
            // Only validate if user has started interacting with the form
            if (formInteractionStarted && input.value.trim() === "") {
                errorDiv.textContent = `${input.placeholder} is required`;
                errorDiv.style.display = "block";
                errorDiv.style.height = "20px";
                input.style.borderColor = "#dc3545";
            } else {
                errorDiv.style.display = "none";
                errorDiv.style.height = "0";
                input.style.borderColor = "";
            }
        });

        input.addEventListener("input", () => {
            if (input.value.trim() !== "") {
                errorDiv.style.display = "none";
                errorDiv.style.height = "0";
                input.style.borderColor = "";
            }
        });
    });

    // Form submission with loading state
    if (loginForm) {
        loginForm.addEventListener("submit", function (e) {
            // Basic validation
            let isValid = true;
            formInteractionStarted = true; // Set to true on submit attempt

            formGroups.forEach(group => {
                const input = group.querySelector("input");
                const errorDiv = group.querySelector(".input-error");

                if (input.value.trim() === "") {
                    errorDiv.textContent = `${input.placeholder} is required`;
                    errorDiv.style.display = "block";
                    errorDiv.style.height = "20px";
                    input.style.borderColor = "#dc3545";
                    isValid = false;
                }
            });

            if (!isValid) {
                e.preventDefault();
                return;
            }

            // Show loading state
            submitButton.innerHTML = `
            <span class="loading-spinner" style="display: inline-block; width: 16px; height: 16px; border: 2px solid currentColor; border-radius: 50%; border-right-color: transparent; animation: spin 0.75s linear infinite; margin-right: 8px;"></span>
            SIGNING IN...
        `;
            submitButton.disabled = true;

            // Add loading spinner animation
            const style = document.createElement('style');
            style.textContent = `
            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
        `;
            document.head.appendChild(style);

            // Let the form submit normally
        });
    }

    // Add subtle animation to the login form container
    const loginFormContainer = document.querySelector(".login-form-container");
    if (loginFormContainer) {
        loginFormContainer.style.opacity = "0";
        loginFormContainer.style.transform = "translateY(20px)";
        loginFormContainer.style.transition = "opacity 0.5s ease, transform 0.5s ease";

        setTimeout(() => {
            loginFormContainer.style.opacity = "1";
            loginFormContainer.style.transform = "translateY(0)";
        }, 100);
    }

    // Add input animation effects
    const inputs = document.querySelectorAll(".form-control");
    inputs.forEach(input => {
        input.addEventListener("focus", () => {
            const parent = input.closest(".input-with-icon");
            parent.style.transition = "box-shadow 0.3s ease";
            parent.style.boxShadow = "0 0 0 3px rgba(139, 69, 19, 0.25)";
        });

        input.addEventListener("blur", () => {
            const parent = input.closest(".input-with-icon");
            parent.style.boxShadow = "";
        });
    });

    // Remember me functionality
    const rememberMeCheckbox = document.getElementById("remember-me");
    if (localStorage.getItem("rememberedUsername")) {
        usernameInput.value = localStorage.getItem("rememberedUsername");
        if (rememberMeCheckbox) rememberMeCheckbox.checked = true;
    }
    if (loginForm && rememberMeCheckbox) {
        loginForm.addEventListener("submit", function () {
            if (rememberMeCheckbox.checked) {
                localStorage.setItem("rememberedUsername", usernameInput.value);
            } else {
                localStorage.removeItem("rememberedUsername");
            }
        });
    }
});