document.addEventListener('DOMContentLoaded', function () {
    
    function safe(val) { return val && val !== '' ? val : 'N/A'; }


    fetch('/api/faculty/profile/info')
        .then(response => response.json())
        .then(data => {
            const fullName = ((data.firstName ? data.firstName : '') + ' ' + (data.middleName ? data.middleName + ' ' : '') + (data.lastName ? data.lastName : ''));
            document.getElementById('profileName').textContent = safe(fullName);
            document.getElementById('facultyId').textContent = safe(data.facultyId);
            document.getElementById('fullName').textContent = safe(fullName).toUpperCase();
            document.getElementById('email').textContent = safe(data.email);
            document.getElementById('status').textContent = safe(data.status);
            document.getElementById('lastLogin').textContent = safe(data.lastLogin);
            document.getElementById('gender').textContent = safe(data.gender);
            document.getElementById('dateOfBirth').textContent = safe(data.dateOfBirth);
            document.getElementById('position').textContent = safe(data.position);
            document.getElementById('specialization').textContent = safe(data.specialization);
            document.getElementById('highestDegree').textContent = safe(data.highestDegree);
            document.getElementById('employmentStatus').textContent = safe(data.employmentStatus);
            document.getElementById('researchInterests').textContent = safe(data.researchInterests);
            document.getElementById('publications').textContent = safe(data.publications);
            // Pre-fill modal fields (if needed)
            // ...add as needed...
        })
        .catch(() => {
            [
                'profileName', 'facultyId', 'fullName', 'email', 'status', 'lastLogin', 'gender', 'dateOfBirth',
                'position', 'specialization', 'highestDegree', 'employmentStatus', 'researchInterests', 'publications'
            ].forEach(id => {
                const el = document.getElementById(id);
                if (el) el.textContent = 'N/A';
            });
        });
    // ...existing code...
});