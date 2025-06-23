document.addEventListener('DOMContentLoaded', function () {
    // Highlight current day
    const today = new Date().getDay(); // 0 is Sunday, 1 is Monday, etc.
    if (today > 0 && today < 7) { // Only highlight weekdays (Monday to Saturday)
        const dayIndex = today === 6 ? 6 : today; // Adjust for Sunday
        const rows = document.querySelectorAll('.schedule-table tbody tr');
        rows.forEach(row => {
            const cell = row.querySelector(`td:nth-child(${dayIndex + 1})`); // +1 because first column is time
            if (cell && cell.textContent.trim() !== '') {
                cell.style.backgroundColor = '#f0e6dd';
            }
        });
    }
    // Optionally, add a loading spinner if needed in the future

    // Dynamic filter population for faculty schedule
    const schoolYearSelect = document.getElementById('schoolYear');
    const semesterSelect = document.getElementById('semester');
    const filterForm = document.querySelector('.filter-section');

    // --- localStorage for remembering last filter ---
    const LS_SCHOOL_YEAR = 'facultyScheduleSchoolYear';
    const LS_SEMESTER = 'facultyScheduleSemester';

    fetch('/api/faculty/schedule-filters')
        .then(response => response.json())
        .then(data => {
            // Populate school years (sort descending for latest first)
            if (data.schoolYears) {
                data.schoolYears.sort((a, b) => b.localeCompare(a));
                schoolYearSelect.innerHTML = '';
                data.schoolYears.forEach((sy, idx) => {
                    const option = document.createElement('option');
                    option.value = sy;
                    option.textContent = sy;
                    schoolYearSelect.appendChild(option);
                });
            }
            // Populate semesters (only 'First' and 'Second')
            if (data.semesters) {
                const filteredSems = data.semesters.filter(sem => sem === 'First' || sem === 'Second');
                filteredSems.sort((a, b) => (a === 'First' ? -1 : 1));
                semesterSelect.innerHTML = '';
                data.semesters.forEach(sem => {
                    const option = document.createElement('option');
                    option.value = sem;
                    option.textContent = sem;
                    semesterSelect.appendChild(option);
                });
            }
            // Restore last selected filter if available
            const savedYear = localStorage.getItem(LS_SCHOOL_YEAR);
            const savedSem = localStorage.getItem(LS_SEMESTER);
            if (savedYear && Array.from(schoolYearSelect.options).some(opt => opt.value === savedYear)) {
                schoolYearSelect.value = savedYear;
            } else if (schoolYearSelect.options.length > 0) {
                schoolYearSelect.selectedIndex = 0;
            }
            if (savedSem && Array.from(semesterSelect.options).some(opt => opt.value === savedSem)) {
                semesterSelect.value = savedSem;
            } else if (semesterSelect.options.length > 0) {
                semesterSelect.selectedIndex = 0;
            }
            // After filters are populated, load the schedule table with the latest values
            updateScheduleTable();
        });

    // --- AJAX-based schedule table update ---
    function renderScheduleTable(days, timeSlots, weeklySchedule) {
        const scheduleTableContainer = document.getElementById('scheduleTable');
        scheduleTableContainer.innerHTML = '';
        if (!timeSlots || timeSlots.length === 0) {
            scheduleTableContainer.innerHTML = '<div class="no-schedule-message"><p>No classes scheduled for the selected period.</p></div>';
            return;
        }
        let tableHtml = '<table class="schedule-table"><thead><tr><th>Time</th>';
        days.forEach(day => {
            tableHtml += `<th>${day}</th>`;
        });
        tableHtml += '</tr></thead><tbody>';
        timeSlots.forEach(slot => {
            tableHtml += `<tr><td>${slot}</td>`;
            days.forEach(day => {
                let cellHtml = '';
                if (weeklySchedule[day] && weeklySchedule[day][slot] && weeklySchedule[day][slot].length > 0) {
                    weeklySchedule[day][slot].forEach(entry => {
                        cellHtml += `<div class="schedule-entry">
                            <span>${entry.course}</span> - <span>${entry.title}</span><br/>
                            <span>${entry.section}</span> | <span>${entry.room}</span><br/>
                            <span>${entry.time}</span>
                        </div>`;
                    });
                }
                tableHtml += `<td>${cellHtml}</td>`;
            });
            tableHtml += '</tr>';
        });
        tableHtml += '</tbody></table>';
        scheduleTableContainer.innerHTML = tableHtml;
    }

    function updateScheduleTable() {
        const schoolYear = schoolYearSelect.value;
        const semester = semesterSelect.value;
        const scheduleTableContainer = document.getElementById('scheduleTable');
        scheduleTableContainer.innerHTML = '<div style="text-align:center;">Loading schedule...</div>';
        fetch(`/api/faculty/schedule-data?schoolYear=${encodeURIComponent(schoolYear)}&semester=${encodeURIComponent(semester)}`)
            .then(response => response.json())
            .then(data => {
                renderScheduleTable(data.days, data.timeSlots, data.weeklySchedule);
            })
            .catch(() => {
                scheduleTableContainer.innerHTML = '<div class="no-schedule-message">Error loading schedule.</div>';
            });
    }

    // Remove form auto-submit and use AJAX instead
    schoolYearSelect.removeEventListener('change', function () { filterForm.submit(); });
    semesterSelect.removeEventListener('change', function () { filterForm.submit(); });
    schoolYearSelect.addEventListener('change', updateScheduleTable);
    semesterSelect.addEventListener('change', updateScheduleTable);

    // Save filter changes to localStorage
    schoolYearSelect.addEventListener('change', function () {
        localStorage.setItem(LS_SCHOOL_YEAR, schoolYearSelect.value);
        updateScheduleTable();
    });
    semesterSelect.addEventListener('change', function () {
        localStorage.setItem(LS_SEMESTER, semesterSelect.value);
        updateScheduleTable();
    });

    // Initial load
    updateScheduleTable();

    // Optionally, remove the view button from the DOM
    const viewButton = document.getElementById('viewButton');
    if (viewButton) {
        viewButton.style.display = 'none';
    }
});
