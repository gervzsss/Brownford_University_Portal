// Handles enrollment button click and error display for student self-enrollment

document.addEventListener('DOMContentLoaded', function () {
    // Remove per-course enroll buttons logic
    const enrollAllBtn = document.getElementById('enrollAllBtn');
    if (enrollAllBtn) {
        enrollAllBtn.addEventListener('click', async function (e) {
            e.preventDefault();
            // Collect all course IDs from the available courses table
            const courseRows = document.querySelectorAll('.courses-table tbody tr[data-course-id]');
            const courseIds = Array.from(courseRows).map(row => parseInt(row.getAttribute('data-course-id')));
            if (courseIds.length === 0) {
                showEnrollmentError('No courses found to enroll.');
                return;
            }
            const studentId = document.getElementById('studentId')?.value || window.studentId;
            const semester = document.getElementById('semester')?.value || window.semester;
            const yearLevel = document.getElementById('yearLevel')?.value || window.yearLevel;
            const sectionId = document.getElementById('sectionId')?.value || null;
            if (!studentId || !semester || !yearLevel) {
                showEnrollmentError('Missing enrollment information.');
                return;
            }
            const payload = {
                studentId: studentId,
                courses: courseIds,
                semester: semester,
                yearLevel: yearLevel,
                sectionId: sectionId ? parseInt(sectionId) : null
            };
            try {
                const response = await fetch('/api/enrollments', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });
                if (!response.ok) {
                    const errorText = await response.text();
                    showEnrollmentError(errorText || 'Enrollment failed.');
                    return;
                }
                window.location.reload();
            } catch (err) {
                showEnrollmentError(err.message || 'Enrollment failed.');
            }
        });
    }
});

function showEnrollmentError(msg) {
    let errBox = document.getElementById('enrollmentErrorBox');
    if (!errBox) {
        errBox = document.createElement('div');
        errBox.id = 'enrollmentErrorBox';
        errBox.style.color = 'red';
        errBox.style.margin = '1em 0';
        const main = document.querySelector('.enrollment-content');
        main.insertBefore(errBox, main.firstChild);
    }
    errBox.textContent = msg;
}
