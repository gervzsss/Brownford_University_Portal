// Handles fetching and rendering the latest 5 pending approvals for the admin dashboard
// and provides approve/decline/delete actions for each request.
document.addEventListener('DOMContentLoaded', function () {
    fetchPendingApprovals();
});

function fetchPendingApprovals() {
    fetch('/api/enrollments/pending-approvals/summary')
        .then(response => response.json())
        .then(data => renderPendingApprovals(data))
        .catch(() => renderPendingApprovals([]));
}

function renderPendingApprovals(approvals) {
    const container = document.getElementById('pending-approvals-list');
    const emptyState = document.getElementById('pending-approvals-empty');
    const viewAll = document.getElementById('pending-approvals-view-all');
    container.innerHTML = '';
    if (approvals.length === 0) {
        container.style.display = 'none';
        emptyState.style.display = 'block';
        viewAll.style.display = 'none';
        return;
    }
    container.style.display = 'block';
    emptyState.style.display = 'none';
    viewAll.style.display = 'block';
    approvals.forEach(approval => {
        // Format date
        let month = '', day = '';
        if (approval.requestDate) {
            const date = new Date(approval.requestDate);
            month = date.toLocaleString('default', { month: 'short' });
            day = date.getDate();
        }
        const title = `Enrollment Request: ${approval.studentName || ''}`;
        const description = `Program: ${approval.programName || ''} | Year: ${approval.yearLevel || ''} | Semester: ${approval.semester || ''}`;
        const requestId = approval.enrollmentId;
        const card = document.createElement('div');
        card.className = 'event-card';
        card.innerHTML = `
            <div class="event-date">
                <span class="event-month">${month}</span>
                <span class="event-day">${day}</span>
            </div>
            <div class="event-details">
                <h4 class="event-title">${title}</h4>
                <p class="event-description">${description}</p>
                <div class="event-info">
                    <span class="event-time">${requestId}</span>
                </div>
            </div>
        `;
        container.appendChild(card);
    });
}
