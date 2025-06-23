// faculty-profile.js
// Handles dynamic loading and updating of faculty profile info

document.addEventListener('DOMContentLoaded', function () {
    // Helper for safe display
    function safe(val) { return val && val !== '' ? val : 'N/A'; }

    // Fetch and display profile info
    fetch('/api/faculty/profile/info')
        .then(response => response.json())
        .then(data => {
            document.getElementById('facultyName').textContent = safe((data.firstName || '') + ' ' + (data.lastName || ''));
            document.getElementById('facultyId').textContent = safe(data.facultyId);
            document.getElementById('fullName').textContent = safe((data.firstName || '') + ' ' + (data.lastName || ''));
            document.getElementById('email').textContent = safe(data.email);
            document.getElementById('status').textContent = safe(data.status);
            document.getElementById('lastLogin').textContent = safe(data.lastLogin);
            document.getElementById('gender').textContent = safe(data.gender);
            document.getElementById('dateOfBirth').textContent = safe(data.dateOfBirth);
            document.getElementById('mobileNumber').textContent = safe(data.mobileNumber);
            document.getElementById('address').textContent = safe(data.address);
            document.getElementById('specialization').textContent = safe(data.specialization);
            document.getElementById('position').textContent = safe(data.position);
            document.getElementById('highestDegree').textContent = safe(data.highestDegree);
            document.getElementById('employmentStatus').textContent = safe(data.employmentStatus);
            document.getElementById('officeLocation').textContent = safe(data.officeLocation);
            document.getElementById('officeHours').textContent = safe(data.officeHours);
            document.getElementById('emergencyContact').textContent = safe(data.emergencyContact);
            document.getElementById('researchInterests').textContent = safe(data.researchInterests);
            document.getElementById('publications').textContent = safe(data.publications);
            // Pre-fill modal fields if you add modals for editing
        })
        .catch(() => {
            [
                'facultyName', 'facultyId', 'fullName', 'email', 'status', 'lastLogin', 'gender', 'dateOfBirth', 'mobileNumber', 'address',
                'specialization', 'position', 'highestDegree', 'employmentStatus', 'officeLocation', 'officeHours', 'emergencyContact', 'researchInterests', 'publications'
            ].forEach(id => {
                const el = document.getElementById(id);
                if (el) el.textContent = 'N/A';
            });
        });

    // Modal logic
    const contactModal = document.getElementById('contactModal');
    const updateContactBtn = document.querySelector('.profile-actions .profile-button');
    const closeContactModal = document.getElementById('closeContactModal');
    updateContactBtn.onclick = () => { contactModal.style.display = 'block'; };
    closeContactModal.onclick = () => { contactModal.style.display = 'none'; };
    window.onclick = function (event) {
        if (event.target === contactModal) contactModal.style.display = 'none';
    };
    // Pre-fill modal fields on open
    updateContactBtn.addEventListener('click', function () {
        document.getElementById('editGender').value = document.getElementById('gender').textContent !== 'N/A' ? document.getElementById('gender').textContent : '';
        document.getElementById('editDateOfBirth').value = document.getElementById('dateOfBirth').textContent !== 'N/A' ? document.getElementById('dateOfBirth').textContent : '';
        document.getElementById('editMobileNumber').value = document.getElementById('mobileNumber').textContent !== 'N/A' ? document.getElementById('mobileNumber').textContent : '';
        document.getElementById('editAddress').value = document.getElementById('address').textContent !== 'N/A' ? document.getElementById('address').textContent : '';
        document.getElementById('editSpecialization').value = document.getElementById('specialization').textContent !== 'N/A' ? document.getElementById('specialization').textContent : '';
        document.getElementById('editPosition').value = document.getElementById('position').textContent !== 'N/A' ? document.getElementById('position').textContent : '';
        document.getElementById('editHighestDegree').value = document.getElementById('highestDegree').textContent !== 'N/A' ? document.getElementById('highestDegree').textContent : '';
        document.getElementById('editEmploymentStatus').value = document.getElementById('employmentStatus').textContent !== 'N/A' ? document.getElementById('employmentStatus').textContent : '';
        document.getElementById('editOfficeLocation').value = document.getElementById('officeLocation').textContent !== 'N/A' ? document.getElementById('officeLocation').textContent : '';
        document.getElementById('editOfficeHours').value = document.getElementById('officeHours').textContent !== 'N/A' ? document.getElementById('officeHours').textContent : '';
        document.getElementById('editEmergencyContact').value = document.getElementById('emergencyContact').textContent !== 'N/A' ? document.getElementById('emergencyContact').textContent : '';
        document.getElementById('editResearchInterests').value = document.getElementById('researchInterests').textContent !== 'N/A' ? document.getElementById('researchInterests').textContent : '';
        document.getElementById('editPublications').value = document.getElementById('publications').textContent !== 'N/A' ? document.getElementById('publications').textContent : '';
    });
    // Contact form submit
    document.getElementById('contactForm').onsubmit = function (e) {
        e.preventDefault();
        const payload = {
            gender: document.getElementById('editGender').value,
            dateOfBirth: document.getElementById('editDateOfBirth').value,
            mobileNumber: document.getElementById('editMobileNumber').value,
            address: document.getElementById('editAddress').value,
            specialization: document.getElementById('editSpecialization').value,
            position: document.getElementById('editPosition').value,
            highestDegree: document.getElementById('editHighestDegree').value,
            employmentStatus: document.getElementById('editEmploymentStatus').value,
            officeLocation: document.getElementById('editOfficeLocation').value,
            officeHours: document.getElementById('editOfficeHours').value,
            emergencyContact: document.getElementById('editEmergencyContact').value,
            researchInterests: document.getElementById('editResearchInterests').value,
            publications: document.getElementById('editPublications').value
        };
        fetch('/api/faculty/profile/info', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        })
            .then(res => res.json())
            .then(resp => {
                document.getElementById('contactFormMsg').textContent = 'Profile information updated!';
                // Optionally update profile display
                document.getElementById('gender').textContent = payload.gender;
                document.getElementById('dateOfBirth').textContent = payload.dateOfBirth;
                document.getElementById('mobileNumber').textContent = payload.mobileNumber;
                document.getElementById('address').textContent = payload.address;
                document.getElementById('specialization').textContent = payload.specialization;
                document.getElementById('position').textContent = payload.position;
                document.getElementById('highestDegree').textContent = payload.highestDegree;
                document.getElementById('employmentStatus').textContent = payload.employmentStatus;
                document.getElementById('officeLocation').textContent = payload.officeLocation;
                document.getElementById('officeHours').textContent = payload.officeHours;
                document.getElementById('emergencyContact').textContent = payload.emergencyContact;
                document.getElementById('researchInterests').textContent = payload.researchInterests;
                document.getElementById('publications').textContent = payload.publications;
            })
            .catch(() => {
                document.getElementById('contactFormMsg').textContent = 'Failed to update. Please try again.';
            });
    };

    // Password modal logic
    const passwordModal = document.getElementById('passwordModal');
    const changePasswordBtn = document.querySelectorAll('.profile-actions .profile-button')[1];
    const closePasswordModal = document.getElementById('closePasswordModal');
    changePasswordBtn.onclick = () => { passwordModal.style.display = 'block'; };
    closePasswordModal.onclick = () => { passwordModal.style.display = 'none'; };
    window.onclick = function (event) {
        if (event.target === contactModal) contactModal.style.display = 'none';
        if (event.target === passwordModal) passwordModal.style.display = 'none';
    };
    // Password form submit
    document.getElementById('passwordForm').onsubmit = function (e) {
        e.preventDefault();
        const currentPassword = document.getElementById('currentPassword').value;
        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        if (newPassword !== confirmPassword) {
            document.getElementById('passwordFormMsg').textContent = 'New passwords do not match!';
            return;
        }
        fetch('/api/faculty/profile/change-password', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ currentPassword, newPassword })
        })
            .then(res => res.json())
            .then(resp => {
                if (resp.success) {
                    document.getElementById('passwordFormMsg').textContent = 'Password changed successfully!';
                } else {
                    document.getElementById('passwordFormMsg').textContent = resp.message || 'Failed to change password.';
                }
            })
            .catch(() => {
                document.getElementById('passwordFormMsg').textContent = 'Failed to change password. Please try again.';
            });
    };

    // TODO: Handle other profile features or modals
});
